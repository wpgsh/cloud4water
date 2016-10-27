package net.wapwag.wemp.dao;

import junit.framework.TestCase;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.link.UserObject;
import net.wapwag.wemp.dao.model.link.UserObjectId;
import net.wapwag.wemp.dao.model.permission.User;
import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

/**
 * Test object
 * Created by Administrator on 2016/10/27 0027.
 */
public class ObjectTest {

    private static EntityManager em;

    private EntityTransaction tx;

    @BeforeClass
    public static void before() {
        em = Persistence.createEntityManagerFactory("test-wemp").createEntityManager();
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

    @Test
    public void addUserObject() {
        User user = em.find(User.class, 1L);
        List<ObjectData> objSet = em.createQuery("select obj from ObjectData obj", ObjectData.class).getResultList();
        UserObject userObject = null;

        for (ObjectData obj : objSet) {
            userObject = new UserObject();
            userObject.setUserObjectId(new UserObjectId(user, obj));
            userObject.setActionId("read");
            userObject.setTransitive("0");
            em.persist(userObject);
        }
    }

    @Test
    public void testGetUsersByObject() {
        User user = em.find(User.class, 1L);
        String sql = "select uo.userObjectId.objectData from UserObject uo where uo.userObjectId.user = :user";
        List<ObjectData> objSet = em.createQuery(sql, ObjectData.class)
                .setParameter("user", user).getResultList();

        TestCase.assertTrue(objSet.size() == 13);
    }

    @Test
    public void test() {
        ObjectData objectData = em.find(ObjectData.class, 12L);
        String sql = "select uo.userObjectId.user from UserObject uo " +
                "where uo.userObjectId.objectData = :obj and uo.actionId = :actionId";
        List<User> userList = em.createQuery(sql, User.class)
                .setParameter("obj", objectData)
                .setParameter("actionId", "read").getResultList();
        TestCase.assertTrue(userList != null && userList.size() > 0);
    }

}
