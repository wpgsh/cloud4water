package net.wapwag.wemp.dao.model.link;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.Group;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class GroupObjectId implements Serializable {

    @ManyToOne
    private Group group;

    @ManyToOne
    private ObjectData objectData;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public ObjectData getObjectData() {
        return objectData;
    }

    public void setObjectData(ObjectData objectData) {
        this.objectData = objectData;
    }
}