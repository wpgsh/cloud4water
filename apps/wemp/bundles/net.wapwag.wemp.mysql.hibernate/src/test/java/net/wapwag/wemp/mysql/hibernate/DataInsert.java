package net.wapwag.wemp.mysql.hibernate;

import junit.framework.TestCase;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.geo.Country;
import net.wapwag.wemp.dao.model.link.UserOrg;
import net.wapwag.wemp.dao.model.link.UserOrgId;
import net.wapwag.wemp.dao.model.org.Organization;
import net.wapwag.wemp.dao.model.org.WaterManageAuth;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.User;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Data insert before other test
 * Created by Administrator on 2016/10/31 0031.
 */
public class DataInsert extends BaseTestConfig {


    @Test
    public void testAddUserOrg() {

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
    public void testAddObject() {

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

    @Test
    public void testAddGroup() {
        Group group = null;
        int addCount = 11;

        for (int i = 0; i < addCount; i++) {
            group = new Group();
            group.setName("研发组" + i);
            em.persist(group);
        }

        em.flush();

        long count = em.createQuery("select count(g.id) from Group g", Long.class).getSingleResult();

        TestCase.assertTrue(count == addCount);
    }

}
