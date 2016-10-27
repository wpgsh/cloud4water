package net.wapwag.wemp.mysql.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestObjectModel {
	
	private static EntityManagerFactory emf;
	
	private EntityManager em;
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		emf = PrepareContext.createEMF();
	}

	// Disable test for now to avoid pounding on the remote DB service
	// during every build
	// @Test
	public void test() {
		em = emf.createEntityManager();
		em.close();
	}

}
