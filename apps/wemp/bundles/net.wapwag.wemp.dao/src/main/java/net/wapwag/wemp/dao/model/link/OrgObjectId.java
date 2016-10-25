package net.wapwag.wemp.dao.model.link;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.org.Organization;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class OrgObjectId implements Serializable {

    @ManyToOne
    private Organization organization;

    @ManyToOne
    private ObjectData objectData;

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
}
