package net.wapwag.wemp.dao;

import net.wapwag.wemp.dao.model.geo.Country;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.Set;

public class TestObjectModel {
	
	private EntityManager em;

	@Before
	public void before() {				
		em = Persistence.createEntityManagerFactory("test-wemp").createEntityManager();
	}

	@Test
	public void testModel() {
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
        User user = new User();
        user.setName("管理员");
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
		
	}
	
	@After
	public void after() {
		em.close();
	}

}
