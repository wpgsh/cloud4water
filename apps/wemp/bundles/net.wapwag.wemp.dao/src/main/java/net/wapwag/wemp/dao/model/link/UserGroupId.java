package net.wapwag.wemp.dao.model.link;

import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.User;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class UserGroupId implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public UserGroupId() {
    }

    public UserGroupId(User user, Group group) {
        this.user = user;
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        UserGroupId other = (UserGroupId) obj;

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

        if (group == null) {
            if (other.group != null)
                return false;
        } else {
            if (other.group == null) {
                return false;
            } else {
                if (group.getId() != other.group.getId()) {
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
        result = prime * result + ((group == null) ? 0 : Long.hashCode(group.getId()));
        return result;
    }
}