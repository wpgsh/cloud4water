package net.wapwag.wemp.dao;

import net.wapwag.wemp.h2.hibernate.H2TxAwareEntityManager;
import org.apache.aries.jpa.template.EmFunction;
import org.apache.aries.jpa.template.JpaTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transaction;

import static net.wapwag.wemp.dao.PrepareContext.transactionManager;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

/**
 * Base test configuration
 * Created by Administrator on 2016/10/28 0028.
 */
public class BaseTestConfig {

    private static EntityManagerFactory emf;

    protected EntityManager em;

    static WaterEquipmentDao waterEquipmentDao;

    private static class TestWaterEquipmentDaoImpl extends WaterEquipmentDaoImpl {
        TestWaterEquipmentDaoImpl(TxAwareEntityManager entityManager) {
            this.entityManager = entityManager;
        }
    }

    private WaterEquipmentDao createDao(EntityManager em) {
        return new TestWaterEquipmentDaoImpl(createTxAwareEntityManager(em));
    }

    private static class TestH2TxAwareEntityManager extends H2TxAwareEntityManager {
        TestH2TxAwareEntityManager(JpaTemplate jpa) {
            this.jpa = jpa;
        }
    }

    private TxAwareEntityManager createTxAwareEntityManager(EntityManager em) {
        return new TestH2TxAwareEntityManager(createJpaTemplate(em));
    }

    @SuppressWarnings("Duplicates")
    private JpaTemplate createJpaTemplate(EntityManager em) {
        JpaTemplate jpa = mock(JpaTemplate.class);
        Mockito.when(jpa.txExpr(any())).thenAnswer(iom -> {
            EmFunction<?> code = iom.getArgument(0);
            
            // assuming the transaction has begun already
            
            Transaction tx = transactionManager.getTransaction();
            if (tx != null) {
            	tx = null;
            } else {
            	transactionManager.begin();
            	tx = transactionManager.getTransaction();
            }
            

            Object result;
            try {
                result = code.apply(em);
                if (tx != null) {
                	tx.commit();
                }
            } catch (Exception e) {
            	if (tx != null) {
            		tx.rollback();
            	}
                throw e;
            }

            return result;
        });

        return jpa;
    }

    @BeforeClass
    public static void initialData() throws Exception {
        emf = PrepareContext.createEMF();
        DataInsert.initialData();
    }

    @Before
    public void beforeMethod() throws Exception {
    	PrepareContext.newTransaction();
        em = emf.createEntityManager();        
        waterEquipmentDao = createDao(em);
        transactionManager.begin();
    }

    @After
    public void afterMethod() throws Exception {
        transactionManager.rollback();
    }

}
