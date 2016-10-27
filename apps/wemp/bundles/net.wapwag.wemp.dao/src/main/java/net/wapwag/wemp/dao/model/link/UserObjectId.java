package net.wapwag.wemp.dao.model.link;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.User;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class UserObjectId implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "obj_id")
    private ObjectData objectData;

    public UserObjectId() {
    }

    public UserObjectId(User user, ObjectData objectData) {
        this.user = user;
        this.objectData = objectData;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ObjectData getObjectData() {
        return objectData;
    }

    public void setObjectData(ObjectData objectData) {
        this.objectData = objectData;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        UserObjectId other = (UserObjectId) obj;

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

        if (objectData == null) {
            if (other.objectData != null)
                return false;
        } else {
            if (other.objectData == null) {
                return false;
            } else {
                if (objectData.getId() != other.objectData.getId()) {
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
        result = prime * result + ((objectData == null) ? 0 : Long.hashCode(objectData.getId()));
        return result;
    }
}