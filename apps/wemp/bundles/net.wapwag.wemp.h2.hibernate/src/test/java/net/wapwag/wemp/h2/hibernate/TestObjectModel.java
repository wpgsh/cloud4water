package net.wapwag.wemp.h2.hibernate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.TestCase;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.geo.Country;
import net.wapwag.wemp.dao.model.link.UserOrg;
import net.wapwag.wemp.dao.model.link.UserOrgId;
import net.wapwag.wemp.dao.model.org.Organization;
import net.wapwag.wemp.dao.model.org.WaterManageAuth;
import net.wapwag.wemp.dao.model.permission.User;

public class TestObjectModel {

    private static EntityManagerFactory emf;

    private EntityManager em;
    
    private EntityTransaction tx;

	@BeforeClass
	public static void before() throws Exception {
		emf = PrepareContext.createEMF();
	}
    
    private long sampleUserId;

    @Test
    public void testManyToManyInsert() {
    	
    	em = emf.createEntityManager();
    	
    	try {
    		tx = em.getTransaction();
    		tx.begin();
    		
	        int i;
	        User user = null;
	
	        for (i = 0; i < 10; i++) {
	            user = new User();
	            user.setName("admin" + i);
	            user.setExternalId(i);
	            em.persist(user);
	            sampleUserId = user.getId();
	        }
	
	        Organization organization;
	        Set<Organization> orgSet = new HashSet<>();
	        for (i = 0; i < 10; i++) {
	            organization = new WaterManageAuth();
	            organization.setName("研发部" + i);
	            orgSet.add(organization);
	            em.persist(organization);
	        }
	
	        UserOrg userOrg;
	
	        for (Organization tmp : orgSet) {
	            userOrg = new UserOrg();
	            userOrg.setUserOrgId(new UserOrgId(user, tmp));
	            em.persist(userOrg);
	        }
	        
	        tx.commit();
	        
	        tx = em.getTransaction();
	        tx.begin();

	        user = em.find(User.class, sampleUserId);
	        List<Organization> orgList = em.createQuery(
	                "select userOrg.userOrgId.organization from UserOrg userOrg where userOrg.userOrgId.user = :user", Organization.class)
	                .setParameter("user", user).getResultList();

	        TestCase.assertTrue(orgList != null && orgList.size() > 0);
	        
	        tx.commit();
    	} finally {
    		em.close();
    	}
    }

    @Test
	public void testModel() {
    	
    	em = emf.createEntityManager();
    	
    	try {
    		tx = em.getTransaction();
    		tx.begin();
    		
	        User user = new User();
	        user.setName("管理员");
	        user.setExternalId(11L);
			em.persist(user);
	
			ObjectData china = new Country();
	        china.setName("中国");
	
	        em.persist(china);
	
	        ObjectData america = new Country();
	        america.setName("美国");
	
	        em.persist(america);
	
	        ObjectData japan = new Country();
	        japan.setName("日本");
	
	        em.persist(japan);
	
			em.flush();
			
			tx.commit();
    	} finally {
    		em.close();
    	}
	}
	
}
