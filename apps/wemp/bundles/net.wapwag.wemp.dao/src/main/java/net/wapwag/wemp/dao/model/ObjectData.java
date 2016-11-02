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
    private Set<UserObject> objectUserSet;

    @OneToMany(mappedBy = "groupObjectId.objectData")
    private Set<GroupObject> objectGroupSet;

    @OneToMany(mappedBy = "orgObjectId.objectData")
    private Set<OrgObject> objectOrgSet;

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

    public Set<UserObject> getObjectUserSet() {
        return objectUserSet;
    }

    public void setObjectUserSet(Set<UserObject> objectUserSet) {
        this.objectUserSet = objectUserSet;
    }

    public Set<GroupObject> getObjectGroupSet() {
        return objectGroupSet;
    }

    public void setObjectGroupSet(Set<GroupObject> objectGroupSet) {
        this.objectGroupSet = objectGroupSet;
    }

    public Set<OrgObject> getObjectOrgSet() {
        return objectOrgSet;
    }

    public void setObjectOrgSet(Set<OrgObject> objectOrgSet) {
        this.objectOrgSet = objectOrgSet;
    }
}
