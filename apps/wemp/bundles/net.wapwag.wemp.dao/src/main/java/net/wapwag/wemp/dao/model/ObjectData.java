package net.wapwag.wemp.dao.model;

import net.wapwag.wemp.dao.model.link.GroupObject;
import net.wapwag.wemp.dao.model.link.OrgObject;
import net.wapwag.wemp.dao.model.link.UserObject;

import javax.annotation.Generated;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Object data model
 * Created by Administrator on 2016/9/28 0028.
 */
@Entity
@Table(name = "object_data")
@Inheritance(strategy = InheritanceType.JOINED)
public class ObjectData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ObjectType type;

    @OneToMany(mappedBy = "userObjectId.objectData")
    private Set<UserObject> userObjectSet;

    @OneToMany(mappedBy = "groupObjectId.objectData")
    private Set<GroupObject> groupObjectSet;

    @OneToMany(mappedBy = "orgObjectId.objectData")
    private Set<OrgObject> orgObjectSet;

    public ObjectData() {

    }

    public ObjectData(ObjectType type) {
        this.type = type;
    }

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

    public ObjectType getType() {
        return type;
    }

    public void setType(ObjectType type) {
        this.type = type;
    }

    public Set<UserObject> getUserObjectSet() {
        return userObjectSet;
    }

    public void setUserObjectSet(Set<UserObject> userObjectSet) {
        this.userObjectSet = userObjectSet;
    }

    public Class<? extends ObjectData> getObjectClass() {
        return type.getObjectClass();
    }

    public Set<GroupObject> getGroupObjectSet() {
        return groupObjectSet;
    }

    public void setGroupObjectSet(Set<GroupObject> groupObjectSet) {
        this.groupObjectSet = groupObjectSet;
    }

    public Set<OrgObject> getOrgObjectSet() {
        return orgObjectSet;
    }

    public void setOrgObjectSet(Set<OrgObject> orgObjectSet) {
        this.orgObjectSet = orgObjectSet;
    }
}
