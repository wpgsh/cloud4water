package net.wapwag.wemp.dao;

import net.wapwag.wemp.dao.model.Area;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
		System.out.println("初始化完成");
		em.flush();
		
		tx.commit();
		
	}
	
	@After
	public void after() {
		em.close();
	}

}
