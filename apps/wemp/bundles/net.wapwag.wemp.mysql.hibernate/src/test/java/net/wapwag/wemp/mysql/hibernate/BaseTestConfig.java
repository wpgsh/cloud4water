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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

/**
 * Base test configuration
 * Created by Administrator on 2016/10/28 0028.
 */
public class BaseTestConfig {

    private static EntityManagerFactory emf;

    protected EntityManager em;

    private EntityTransaction tx;

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

            EntityTransaction tx = em.getTransaction();
            boolean isActive = tx.isActive();
            if (!isActive) {
                tx.begin();
            }

            Object result;
            try {
                result = code.apply(em);

                if (!isActive) {
                    tx.commit();
                }
            } catch (Exception e) {
                if (!isActive) {
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
    public void beforeMethod() {
        em = emf.createEntityManager();
        tx = em.getTransaction();
        waterEquipmentDao = createDao(em);
        tx.begin();
    }

    @After
    public void afterMethod() {
        tx.rollback();
    }

}
