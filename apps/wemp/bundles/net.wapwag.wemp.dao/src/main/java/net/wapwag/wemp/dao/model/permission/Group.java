package net.wapwag.wemp.dao.model.permission;

import net.wapwag.wemp.dao.model.link.GroupObject;
import net.wapwag.wemp.dao.model.link.UserGroup;
import net.wapwag.wemp.dao.model.org.Organization;

import javax.persistence.*;
import java.util.Set;

/**
 * User group model
 * Created by Administrator on 2016/9/28 0028.
 */
@Entity
@Table(name = "group_data")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @OneToMany
    @JoinColumn(name = "group_id")
    private Set<GroupObject> groupObjectSet;

    @OneToMany
    @JoinColumn(name = "group_id")
    private Set<UserGroup> userGroupsSet;

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

    public Set<GroupObject> getGroupObjectSet() {
        return groupObjectSet;
    }

    public void setGroupObjectSet(Set<GroupObject> groupObjectSet) {
        this.groupObjectSet = groupObjectSet;
    }

    public Set<UserGroup> getUserGroupsSet() {
        return userGroupsSet;
    }

    public void setUserGroupsSet(Set<UserGroup> userGroupsSet) {
        this.userGroupsSet = userGroupsSet;
    }
}
