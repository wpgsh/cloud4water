package net.wapwag.wemp.dao.model.permission;

import net.wapwag.wemp.dao.model.link.UserGroup;
import net.wapwag.wemp.dao.model.link.UserObject;
import net.wapwag.wemp.dao.model.link.UserOrg;

import javax.persistence.*;
import java.util.Set;

/**
 * User model
 * Created by Administrator on 2016/9/28 0028.
 */
@Entity
@Table(name = "user_data")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "userObjectId.user")
    private Set<UserObject> userObjectSet;

    @OneToMany(mappedBy = "userGroupId.user")
    private Set<UserGroup> userGroupsSet;

    @OneToMany(mappedBy = "userOrgId.user")
    private Set<UserOrg> userOrgSet;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserObject> getUserObjectSet() {
        return userObjectSet;
    }

    public void setUserObjectSet(Set<UserObject> userObjectSet) {
        this.userObjectSet = userObjectSet;
    }

    public Set<UserGroup> getUserGroupsSet() {
        return userGroupsSet;
    }

    public void setUserGroupsSet(Set<UserGroup> userGroupsSet) {
        this.userGroupsSet = userGroupsSet;
    }

    public Set<UserOrg> getUserOrgSet() {
        return userOrgSet;
    }

    public void setUserOrgSet(Set<UserOrg> userOrgSet) {
        this.userOrgSet = userOrgSet;
    }
}
