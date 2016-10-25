package net.wapwag.wemp.dao.model.link;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.User;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class UserObjectId implements Serializable {

    @ManyToOne
    private User user;

    @ManyToOne
    private ObjectData objectData;

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
}