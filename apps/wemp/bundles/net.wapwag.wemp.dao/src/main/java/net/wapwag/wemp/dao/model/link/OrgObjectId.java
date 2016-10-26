package net.wapwag.wemp.dao.model.link;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.org.Organization;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class OrgObjectId implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private Organization organization;

    @ManyToOne
    private ObjectData objectData;

    public OrgObjectId() {
    }

    public OrgObjectId(Organization organization, ObjectData objectData) {
        this.organization = organization;
        this.objectData = objectData;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
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

        OrgObjectId other = (OrgObjectId) obj;

        if (organization == null) {
            if (other.organization != null)
                return false;
        } else {
            if (other.organization == null) {
                return false;
            } else {
                if (organization.getId() != other.organization.getId()) {
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
        result = prime * result + ((organization == null) ? 0 : Long.hashCode(organization.getId()));
        result = prime * result + ((objectData == null) ? 0 : Long.hashCode(objectData.getId()));
        return result;
    }

}
