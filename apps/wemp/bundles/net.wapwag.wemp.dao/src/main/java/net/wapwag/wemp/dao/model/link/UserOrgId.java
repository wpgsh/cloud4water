package net.wapwag.wemp.dao.model.link;

import net.wapwag.wemp.dao.model.org.Organization;
import net.wapwag.wemp.dao.model.permission.User;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class UserOrgId implements Serializable {

    @ManyToOne
    private User user;

    @ManyToOne
    private Organization organization;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}