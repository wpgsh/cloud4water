package net.wapwag.wemp.dao.model.link;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.Group;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User group many to many linked entity
 * Created by Administrator on 2016/10/25 0025.
 */
@Entity
@Table(name = "group_object_link")
public class GroupObject {

    @EmbeddedId
    private GroupObjectId groupObjectId;

    @Column
    private String actionId;

    @Column(length = 1, columnDefinition = "char(1)", nullable = false)
    private String transitive;

    public GroupObjectId getGroupObjectId() {
        return groupObjectId;
    }

    public void setGroupObjectId(GroupObjectId groupObjectId) {
        this.groupObjectId = groupObjectId;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getTransitive() {
        return transitive;
    }

    public void setTransitive(String transitive) {
        this.transitive = transitive;
    }
}
