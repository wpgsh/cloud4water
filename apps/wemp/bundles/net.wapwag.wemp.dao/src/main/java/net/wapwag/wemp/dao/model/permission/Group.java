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
    
    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    @OneToMany(mappedBy = "groupObjectId.group", cascade = CascadeType.ALL)
    private Set<GroupObject> groupObjectSet;

    @OneToMany(mappedBy = "userGroupId.group", cascade = CascadeType.ALL)
    private Set<UserGroup> groupUserSet;

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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Set<GroupObject> getGroupObjectSet() {
        return groupObjectSet;
    }

    public void setGroupObjectSet(Set<GroupObject> groupObjectSet) {
        this.groupObjectSet = groupObjectSet;
    }

    public Set<UserGroup> getGroupUserSet() {
        return groupUserSet;
    }

    public void setGroupUserSet(Set<UserGroup> groupUserSet) {
        this.groupUserSet = groupUserSet;
    }
}
