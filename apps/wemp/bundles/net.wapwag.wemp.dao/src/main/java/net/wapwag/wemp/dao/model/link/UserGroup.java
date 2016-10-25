package net.wapwag.wemp.dao.model.link;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User group many to many linked entity
 * Created by Administrator on 2016/10/25 0025.
 */
@Entity
@Table(name = "user_group_link")
public class UserGroup {

    @EmbeddedId
    private UserGroupId userGroupId;

    public UserGroupId getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UserGroupId userGroupId) {
        this.userGroupId = userGroupId;
    }
}
