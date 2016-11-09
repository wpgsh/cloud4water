package net.wapwag.wemp.h2.hibernate;

import junit.framework.TestCase;
import net.wapwag.wemp.dao.TxAwareEntityManager;
import net.wapwag.wemp.dao.WaterEquipmentDao;
import net.wapwag.wemp.dao.WaterEquipmentDaoImpl;
import net.wapwag.wemp.dao.model.ObjectType;
import net.wapwag.wemp.dao.model.geo.Area;
import net.wapwag.wemp.dao.model.geo.Country;
import net.wapwag.wemp.dao.model.link.*;
import net.wapwag.wemp.dao.model.org.Organization;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.User;
import org.apache.aries.jpa.template.EmFunction;
import org.apache.aries.jpa.template.JpaTemplate;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;

public class DaoTest {
	
    private static EntityManagerFactory emf;
    
    private EntityManager em;

    private EntityTransaction tx;	

    @BeforeClass
    public static void before() throws Exception {
    	emf = PrepareContext.createEMF();
    }
    
    private long user0Id, user1Id, user2Id, user3Id;
    
    private long countryId, areaId;
    
    @Before
    public void prepareObjects() {
    	em = emf.createEntityManager();    	    	
    	try {
    		tx = em.getTransaction();
    		tx.begin();
    		
    		User user0 = new User();
    		user0.setName("user0");
			user0.setExternalId(0L);
    		em.persist(user0);
    		user0Id = user0.getId();
    		
    		User user1 = new User();
    		user1.setName("user1");
            user1.setExternalId(1L);
    		em.persist(user1);
    		user1Id = user1.getId();    		

    		User user2 = new User();
    		user2.setName("user2");
            user2.setExternalId(2L);
    		em.persist(user2);
    		user2Id = user2.getId();
    		
    		User user3 = new User();
    		user3.setName("user3");
            user3.setExternalId(3L);
    		em.persist(user3);
    		user3Id = user3.getId();    		
    		
    		Country country = new Country();
    		country.setName("C");
    		em.persist(country);
    		countryId = country.getId();
    		
    		Area area = new Area();
    		area.setName("A");
    		area.setCountry(country);
    		em.persist(area);
    		areaId = area.getId();
    		
    		UserObject uo0 = new UserObject();
    		uo0.setActionId("read");
    		uo0.setTransitive(1);
    		uo0.setUserObjectId(new UserObjectId(user0, country));
    		em.persist(uo0);
    		
    		UserObject uo1 = new UserObject();
    		uo1.setActionId("write");
    		uo1.setTransitive(0);
    		uo1.setUserObjectId(new UserObjectId(user1, area));
    		em.persist(uo1);
    		
    		Organization org0 = new Organization(ObjectType.WATER_MANAGEMENT_COMPANY);
    		org0.setName("COM");
    		em.persist(org0);
    		
    		Group g0 = new Group();
    		g0.setOrganization(org0);
    		em.persist(g0);
    		
    		Group g1 = new Group();
    		g1.setOrganization(org0);
    		em.persist(g1);
    		
    		UserGroup ug0 = new UserGroup();
    		ug0.setUserGroupId(new UserGroupId(user2, g0));
    		em.persist(ug0);
    		
    		UserGroup ug1 = new UserGroup();
    		ug1.setUserGroupId(new UserGroupId(user3, g1));
    		em.persist(ug1);
    		
    		GroupObject go0 = new GroupObject();
    		go0.setActionId("read");
    		go0.setTransitive(1);
    		go0.setGroupObjectId(new GroupObjectId(g0, country));
    		em.persist(go0);
    		
    		GroupObject go1 = new GroupObject();
    		go1.setActionId("write");
    		go1.setTransitive(0);
    		go1.setGroupObjectId(new GroupObjectId(g1, area));
    		em.persist(go1);
    		
    		tx.commit();
    	} finally {
    		
    	}
    }
    
    private static class TestWaterEquipmentDaoImpl extends WaterEquipmentDaoImpl {
    	public TestWaterEquipmentDaoImpl(TxAwareEntityManager entityManager) {
    		this.entityManager = entityManager;
    	}
    }
    
    protected WaterEquipmentDao createDao(EntityManager em) {
    	return new TestWaterEquipmentDaoImpl(createTxAwareEntityManager(em));
    }
    
    private static class TestH2TxAwareEntityManager extends H2TxAwareEntityManager {
    	public TestH2TxAwareEntityManager(JpaTemplate jpa) {
    		this.jpa = jpa;
    	}
    }
    
    protected TxAwareEntityManager createTxAwareEntityManager(EntityManager em) {    	
    	return new TestH2TxAwareEntityManager(createJpaTemplate(em));
    }
    
    protected JpaTemplate createJpaTemplate(EntityManager em) {
    	JpaTemplate jpa = mock(JpaTemplate.class);
    	Mockito.<Object>when(jpa.txExpr(any())).thenAnswer(iom -> {
    		EmFunction<?> code = iom.<EmFunction<?>>getArgument(0);
    		
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
    
    @Test
    public void testAuthorization() throws Exception {
    	em = emf.createEntityManager();
    	try {
	        
	        WaterEquipmentDao dao = createDao(em);
	        
	        TestCase.assertTrue(dao.isAuthorized(user0Id, "read", countryId));
	        TestCase.assertFalse(dao.isAuthorized(user0Id, "write", countryId));
	        TestCase.assertTrue(dao.isAuthorized(user0Id, "read", areaId));
	        TestCase.assertFalse(dao.isAuthorized(user0Id, "write", areaId));
	        
	        TestCase.assertFalse(dao.isAuthorized(user1Id, "read", countryId));
	        TestCase.assertFalse(dao.isAuthorized(user1Id, "write", countryId));
	        TestCase.assertFalse(dao.isAuthorized(user1Id, "read", areaId));
	        TestCase.assertTrue(dao.isAuthorized(user1Id, "write", areaId));
	        
	        TestCase.assertTrue(dao.isAuthorized(user2Id, "read", countryId));
	        TestCase.assertFalse(dao.isAuthorized(user2Id, "write", countryId));
	        TestCase.assertTrue(dao.isAuthorized(user2Id, "read", areaId));
	        TestCase.assertFalse(dao.isAuthorized(user2Id, "write", areaId));

	        TestCase.assertFalse(dao.isAuthorized(user3Id, "read", countryId));
	        TestCase.assertFalse(dao.isAuthorized(user3Id, "write", countryId));
	        TestCase.assertFalse(dao.isAuthorized(user3Id, "read", areaId));
	        TestCase.assertTrue(dao.isAuthorized(user3Id, "write", areaId));
    	} finally {
    		em.close();
    	}
    }
    
//    @Test
//    public void testPermissions() {
//    	em = emf.createEntityManager();
//    	try {
//	        tx = em.getTransaction();
//	        tx.begin();
//	        
//	        Query q0 = em.createQuery(
//	        		       "select o0.id, uo0.userObjectId.user.id, uo1.userObjectId.user.id from "
//	        		     + "   Area o0 left join o0.userObjectSet uo0 left join o0.country.userObjectSet uo1 "
//	        		     + " where "
//	        		     + "   o0.id = :objectId and ("
//	        		     + "   (uo0 is null or (uo0.userObjectId.user.id = :userId and uo0.actionId = :action)) or "
//	        		     + "   (uo1 is null or (uo1.userObjectId.user.id = :userId and uo1.actionId = :action and uo1.transitive = 1))"
//	        		     + "   )");
//	        
//	        Query q1 = em.createQuery(
//     		       "select o0.id, go0.groupObjectId.group.id, go1.groupObjectId.group.id from "
//		             + "   Area o0 left join o0.groupObjectSet go0 left join o0.country.groupObjectSet go1 left join go0.groupObjectId.group.userGroupSet ug0 left join go1.groupObjectId.group.userGroupSet ug1 "
//        		     + " where "
//		             + "   o0.id = :objectId and ("
//		             + "   (go0 is null or (ug0.userGroupId.user.id = :userId and go0.actionId = :action)) or "
//		             + "   (go1 is null or (ug1.userGroupId.user.id = :userId and go1.actionId = :action and go1.transitive = 1 ))"
//		             + ")");
//	        
//	        List result = q0.
//	        	setParameter("objectId", areaId).
//	        	setParameter("userId", user0Id).
//	        	setParameter("action", "read").
//	        	getResultList();
//	        
//	        // test: at least one row with any non-null uo* column
//	        
//	        Object[] row = (Object[])result.get(0);
//	        
//	        System.out.println(row[0]);
//	        System.out.println(row[1]);
//	        System.out.println(row[2]);
//	        
//	        result = q1.
//		        	setParameter("objectId", areaId).
//		        	setParameter("userId", user3Id).
//		        	setParameter("action", "write").
//		        	getResultList();
//		        
//	        // test: at least one row with any non-null uo* column
//	        
//	        row = (Object[])result.get(0);
//	        
//	        System.out.println(row[0]);
//	        System.out.println(row[1]);
//	        System.out.println(row[2]);
//	        
//	        
//	        tx.commit();
//    	} finally {
//    		em.close();
//    	}
//    }
    

}
