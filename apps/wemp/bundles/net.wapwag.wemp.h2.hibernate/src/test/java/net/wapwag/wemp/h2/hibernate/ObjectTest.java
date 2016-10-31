package net.wapwag.wemp.h2.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.TestCase;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectType;
import net.wapwag.wemp.dao.model.link.UserObject;
import net.wapwag.wemp.dao.model.link.UserObjectId;
import net.wapwag.wemp.dao.model.permission.User;

/**
 * Test object
 * Created by Administrator on 2016/10/27 0027.
 */
public class ObjectTest {

    private static EntityManagerFactory emf;
    
    private EntityManager em;

    private EntityTransaction tx;
    
    @BeforeClass
    public static void before() throws Exception {
    	emf = PrepareContext.createEMF();
    }
    
    private long userId;
    
    private long sampleObjectId;
    
    @Before
    public void prepareObjects() {
    	em = emf.createEntityManager();    	    	
    	try {
    		tx = em.getTransaction();
    		tx.begin();
    		
    		User user = new User();
    		user.setName("user0");
    		em.persist(user);
    		userId = user.getId();

    		for (int i=0; i<13; i++) {
    			ObjectData object = new ObjectData(ObjectType.AREA);
    			em.persist(object);
    			sampleObjectId = object.getId();
    		}
    		
    		tx.commit();
    	} finally {
    		
    	}
    }

    @Test
    public void testUserObject() throws Exception {
    	em = emf.createEntityManager();
    	try {
	        tx = em.getTransaction();
	        tx.begin();
	        
	        User user = em.find(User.class, userId);
	        List<ObjectData> objSet = em.createQuery("select obj from ObjectData obj", ObjectData.class).getResultList();
	        UserObject userObject = null;
	
	        for (ObjectData obj : objSet) {
	            userObject = new UserObject();
	            userObject.setUserObjectId(new UserObjectId(user, obj));
	            userObject.setActionId("read");
	            userObject.setTransitive(0);
	            em.persist(userObject);
	        }
	        
	        tx.commit();
    	} finally {
    		em.close();
    	}
    	
    	em = emf.createEntityManager();
    	try {
	        tx = em.getTransaction();
	        tx.begin();

	        User user = em.find(User.class, userId);
            List<ObjectData> objSet = em.createQuery("select uo.userObjectId.objectData from UserObject uo where uo.userObjectId.user = :user", ObjectData.class)
                    .setParameter("user", user).getResultList();

            TestCase.assertTrue(objSet.size() == 13);
            
            tx.commit();
    	} finally {
    		em.close();
    	}

    	em = emf.createEntityManager();
    	try {
	        tx = em.getTransaction();
	        tx.begin();

	        ObjectData objectData = em.find(ObjectData.class, sampleObjectId);
	        String sql = "select uo.userObjectId.user from UserObject uo " +
	                "where uo.userObjectId.objectData = :obj and uo.actionId = :actionId";
	        List<User> userList = em.createQuery(sql, User.class)
	                .setParameter("obj", objectData)
	                .setParameter("actionId", "read").getResultList();
	        TestCase.assertTrue(userList != null && userList.size() > 0);
            
            tx.commit();
    	} finally {
    		em.close();
    	}
    }

}
