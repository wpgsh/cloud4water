package net.wapwag.wemp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.TestCase;
import net.wapwag.wemp.dao.model.Area;
import net.wapwag.wemp.dao.model.Organization;
import net.wapwag.wemp.dao.model.Province;
import net.wapwag.wemp.dao.model.User;
import net.wapwag.wemp.dao.model.UserOrganizationRole;
import net.wapwag.wemp.dao.model.WaterManagementAuthority;

public class TestObjectModel {
	
	private static EntityManagerFactory emf;
	
	private EntityTransaction tx;
	
	@BeforeClass
	public static void initializeEntityManagerFactory() {
		emf = Persistence.createEntityManagerFactory("sample.jpa");
	}

	@Test	
	public void testModel() {
		
		EntityManager em = emf.createEntityManager();		
        tx = em.getTransaction();
        tx.begin();

		Area area = new Area();
		area.setTitle("Area");
		em.persist(area);
		
		Province province = new Province();
		province.setTitle("Province");
		
		// NOTE: it is important to set both ends of the relation
		// here:
		// 1) province.setArea will cause the database to properly persist the relation
		// 2) area.getProvinces().add(...) will ensure that the subsequent code within
		//    the same transaction will get the correct list (the list is NOT updated by
		//    hibernate on its own)
		province.setArea(area);
		area.getProvinces().add(province);
		
		em.persist(area);
		em.persist(province);
		
		em.flush();
		
		tx.commit();
		
		System.out.println("area.id="+area.getId());
		System.out.println("province.id="+province.getId());
		
		tx = em.getTransaction();
		
		tx.begin();
		
		em.refresh(area);
		em.refresh(province);
		
		System.out.println("area.provinces="+area.getProvinces());		
		System.out.println("province.area="+province.getArea());
		
		tx.commit();
		em.close();
	}
	

    @Test
    public void testManyToMany() {
    	
    	EntityManager em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();    	

        User user;
        Organization organization;

        user = new User();
        user.setExternalId("admin0");
        em.persist(user);

        organization = new WaterManagementAuthority();
        organization.setTitle("Org0");
        em.persist(organization);

        UserOrganizationRole userOrganizationRole;

        userOrganizationRole = new UserOrganizationRole();
        userOrganizationRole.setId(new UserOrganizationRole.UserOrganizationId(user, organization));
        em.persist(userOrganizationRole);
        
        tx.commit();
        em.close();
        
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();    	        
        
        user = em.
        		createQuery("select u from User u where u.externalId = :externalId", User.class).
        		setParameter("externalId", "admin0").
        		getSingleResult();
        
        // NOTE: since we have a totally new EntityManager for the remainder of the testcase, the values from the DB will be
        // refreshed
        
        System.out.println("User.externalId="+user.getExternalId());
        TestCase.assertEquals("admin0", user.getExternalId());
        
        List<Organization> organizations = em.createQuery("select uor.id.organization from UserOrganizationRole uor where uor.id.user = :user", 
        		Organization.class).setParameter("user", user).getResultList();
        
        TestCase.assertEquals(1, organizations.size());
        TestCase.assertEquals("Org0", organizations.get(0).getTitle());
        
        System.out.println("Organizations="+organizations);
        
        TestCase.assertEquals(1, user.getOrganizations().size());
        
        System.out.println("User.organizations="+user.getOrganizations());
        		
    	tx.commit();
    	em.close();

    }


}
