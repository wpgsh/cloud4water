package net.wapwag.wemp.dao.model.link;

import net.wapwag.wemp.dao.model.org.Organization;
import net.wapwag.wemp.dao.model.permission.User;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class UserOrgId implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private User user;

    @ManyToOne
    private Organization organization;

    public UserOrgId() {
    }

    public UserOrgId(User user, Organization organization) {
        this.user = user;
        this.organization = organization;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        UserOrgId other = (UserOrgId) obj;

        if (user == null) {
            if (other.user != null)
                return false;
        } else {
            if (other.user == null) {
                return false;
            } else {
                if (user.getId() != other.user.getId()) {
                    return false;
                }
            }
        }

        if (organization == null) {
            if (other.organization != null)
                return false;
        } else {
            if (other.organization == null) {
                return false;
            } else {
                if (organization.getId() != other.organization.getId()) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((user == null) ? 0 : Long.hashCode(user.getId()));
        result = prime * result + ((organization == null) ? 0 : Long.hashCode(organization.getId()));
        return result;
    }
}