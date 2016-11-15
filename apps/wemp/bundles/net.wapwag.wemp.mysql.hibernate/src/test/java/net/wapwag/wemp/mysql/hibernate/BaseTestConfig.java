package net.wapwag.wemp.mysql.hibernate;

import net.wapwag.wemp.dao.TxAwareEntityManager;
import net.wapwag.wemp.dao.WaterEquipmentDao;
import net.wapwag.wemp.dao.WaterEquipmentDaoImpl;
import org.apache.aries.jpa.template.EmFunction;
import org.apache.aries.jpa.template.JpaTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.Status;
import javax.transaction.Transaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

/**
 * Base test configuration
 * Created by Administrator on 2016/10/28 0028.
 */
public class BaseTestConfig {

    private static EntityManagerFactory emf;

    protected EntityManager em;

    WaterEquipmentDao waterEquipmentDao;

    private static class TestWaterEquipmentDaoImpl extends WaterEquipmentDaoImpl {
        TestWaterEquipmentDaoImpl(TxAwareEntityManager entityManager) {
            this.entityManager = entityManager;
        }
    }

    private WaterEquipmentDao createDao(EntityManager em) {
        return new TestWaterEquipmentDaoImpl(createTxAwareEntityManager(em));
    }

    private static class TestH2TxAwareEntityManager extends MysqlTxAwareEntityManager {
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
            
            Transaction tx = PrepareContext.transactionManager.getTransaction();
            if (tx != null) {
            	tx = null;
            } else {
            	PrepareContext.transactionManager.begin();
            	tx = PrepareContext.transactionManager.getTransaction();
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
        if (PrepareContext.cleanDatabase()) {
            DataInsert.initialData();
        }
    }

    @Before
    public void beforeMethod() throws Exception {
        em = emf.createEntityManager();        
        waterEquipmentDao = createDao(em); 
    }

    @After
    public void afterMethod() throws Exception {
    }

}
