package net.wapwag.wemp.dao;

import junit.framework.TestCase;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.geo.Country;
import net.wapwag.wemp.dao.model.link.UserOrg;
import net.wapwag.wemp.dao.model.link.UserOrgId;
import net.wapwag.wemp.dao.model.org.Organization;
import net.wapwag.wemp.dao.model.org.WaterManageAuth;
import net.wapwag.wemp.dao.model.permission.User;
import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestObjectModel {

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
    public void testManyToManyInsert() {

        int i;
        User user = null;

        for (i = 0; i < 10; i++) {
            user = new User();
            user.setName("admin" + i);
            em.persist(user);
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

    }

    @Test
    public void testManyToManySelect() {
        User user = em.find(User.class, 10L);
        List<Organization> orgList = em.createQuery(
                "select userOrg.userOrgId.organization from UserOrg userOrg where userOrg.userOrgId.user = :user", Organization.class)
                .setParameter("user", user).getResultList();

        TestCase.assertTrue(orgList != null && orgList.size() > 0);
    }

    @Test
	public void testModel() {
		
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
		
	}
	
}
