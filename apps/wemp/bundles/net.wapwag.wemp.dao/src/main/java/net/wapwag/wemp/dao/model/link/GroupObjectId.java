package net.wapwag.wemp.dao.model.link;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.Group;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class GroupObjectId implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private Group group;

    @ManyToOne
    private ObjectData objectData;

    public GroupObjectId() {

    }

    public GroupObjectId(Group group, ObjectData objectData) {
        this.group = group;
        this.objectData = objectData;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        GroupObjectId other = (GroupObjectId) obj;

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
        result = prime * result + ((group == null) ? 0 : Long.hashCode(group.getId()));
        result = prime * result + ((objectData == null) ? 0 : Long.hashCode(objectData.getId()));
        return result;
    }

}