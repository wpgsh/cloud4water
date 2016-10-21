package net.wapwag.wemp.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.wapwag.wemp.dao.model.Area;
import net.wapwag.wemp.dao.model.Province;

public class TestObjectModel {
	
	private EntityManager em;

	@Before
	public void before() {				
		em = Persistence.createEntityManagerFactory("sample.jpa").createEntityManager();
	}

	@Test
	public void testModel() {
		
		EntityTransaction tx = em.getTransaction();
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
		
	}
	
	@After
	public void after() {
		em.close();
	}

}
