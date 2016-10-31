package net.wapwag.wemp.dao.model.link;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

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

    @Column(length = 1, columnDefinition = "tinyint(1)", nullable = false)
    private int transitive;

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

    public int getTransitive() {
        return transitive;
    }

    public void setTransitive(int transitive) {
        this.transitive = transitive;
    }

}
