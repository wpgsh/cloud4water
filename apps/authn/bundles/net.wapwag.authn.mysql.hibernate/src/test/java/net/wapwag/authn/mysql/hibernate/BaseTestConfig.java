package net.wapwag.authn.mysql.hibernate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static net.wapwag.authn.mysql.hibernate.PrepareContext.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.apache.aries.jpa.template.EmFunction;
import org.apache.aries.jpa.template.JpaTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mockito;

import net.wapwag.authn.dao.TxAwareEntityManager;
import net.wapwag.authn.dao.UserDao;
import net.wapwag.authn.dao.UserDaoImpl;
/**
 * Base test configuration
 * Created by Administrator on 2016/10/28 0028.
 */
public class BaseTestConfig {

    private static EntityManagerFactory emf;

    protected EntityManager em;

    private EntityTransaction tx;

    protected UserDao userDao;

    private static class TestUserDaoImpl extends UserDaoImpl {
        TestUserDaoImpl(TxAwareEntityManager entityManager) {
            this.entityManager = entityManager;
        }
    }

    private UserDao createDao(EntityManager em) {
        return new TestUserDaoImpl(createTxAwareEntityManager(em));
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
//        if (PrepareContext.cleanDatabase()) {
//            DataInsert.initialData();
//        }
    }

    @Before
    public void beforeMethod() throws NotSupportedException, SystemException {
        em = emf.createEntityManager();
        userDao = createDao(em);
        transactionManager.begin();
    }

    @After
    public void afterMethod() throws IllegalStateException, SecurityException, SystemException {
    	transactionManager.rollback();
    }

}
