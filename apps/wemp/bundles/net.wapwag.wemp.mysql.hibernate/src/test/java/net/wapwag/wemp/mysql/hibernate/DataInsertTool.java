package net.wapwag.wemp.mysql.hibernate;

import net.wapwag.wemp.dao.model.org.Organization;
import net.wapwag.wemp.dao.model.org.WaterManageCompany;
import net.wapwag.wemp.dao.model.permission.User;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.EntityManager;

import static net.wapwag.wemp.mysql.hibernate.PrepareContext.transactionManager;

/**
 * Data insert tool,
 * <p>Use {@link org.junit.Assert} instead of .{@link junit.framework.TestCase} which suggested for the junit4</p>
 * Created by Administrator on 2016/10/31 0031.
 */
@SuppressWarnings("Duplicates")
@Ignore
public class DataInsertTool {

    private static EntityManager em;

    static {
        try {
            em = PrepareContext.createEMF().createEntityManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initialData() throws Exception {
        try {
            transactionManager.begin();

            addRemoveData();

            transactionManager.commit();
        } finally {
            em.close();
        }
    }

    private static void addRemoveData() {
        int i;
        User user;

        for (i = 10; i < 20; i++) {
            user = new User();
            user.setExternalId(i);
            user.setName("user" + i);
            em.persist(user);
        }

        Organization organization;
        for (i = 0; i < 10; i++) {
            organization = new WaterManageCompany();
            organization.setName("分公司" + i);
            em.persist(organization);
        }
    }

    @Test
    public void addData() throws Exception {
        initialData();
    }
}
