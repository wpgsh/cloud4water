package net.wapwag.wemp.dao.model.org;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectType;
import net.wapwag.wemp.dao.model.link.OrgObject;
import net.wapwag.wemp.dao.model.link.UserOrg;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.User;

import javax.persistence.*;
import java.util.Set;

/**
 * Organization model
 * Created by Administrator on 2016/9/28 0028.
 */
@Entity
@Table(name = "org_data")
public class Organization extends ObjectData {

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private Set<Group> groupSet;

    @OneToMany(mappedBy = "orgObjectId.organization", cascade = CascadeType.ALL)
    private Set<OrgObject> orgObjectSet;

    @OneToMany(mappedBy = "userOrgId.organization", cascade = CascadeType.ALL)
    private Set<UserOrg> orgUserSet;

    public Organization() {

    }

    public Organization(ObjectType type) {
        super(type);
    }

    public Set<Group> getGroupSet() {
        return groupSet;
    }

    public void setGroupSet(Set<Group> groupSet) {
        this.groupSet = groupSet;
    }

    public Set<OrgObject> getOrgObjectSet() {
        return orgObjectSet;
    }

    public void setOrgObjectSet(Set<OrgObject> orgObjectSet) {
        this.orgObjectSet = orgObjectSet;
    }

    public Set<UserOrg> getOrgUserSet() {
        return orgUserSet;
    }

    public void setOrgUserSet(Set<UserOrg> orgUserSet) {
        this.orgUserSet = orgUserSet;
    }
}
