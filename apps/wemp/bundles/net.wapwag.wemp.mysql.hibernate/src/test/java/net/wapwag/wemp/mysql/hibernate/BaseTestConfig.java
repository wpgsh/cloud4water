package net.wapwag.wemp.mysql.hibernate;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Base test configuration
 * Created by Administrator on 2016/10/28 0028.
 */
public class BaseTestConfig {

    protected static EntityManager em;

    protected EntityTransaction tx;

    @BeforeClass
    public static void before() throws Exception {
        em = PrepareContext.createEMF().createEntityManager();
    }

    @AfterClass
    public static void after() {
        em.close();
    }

    @Before
    public void beforeMethod() {
        tx = em.getTransaction();
        tx.begin();
    }

    @After
    public void afterMethod() {
        tx.commit();
    }

}
