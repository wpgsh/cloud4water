package net.wapwag.wemp.dao.model.link;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User group many to many linked entity
 * Created by Administrator on 2016/10/25 0025.
 */
@Entity
@Table(name = "org_object_link")
public class OrgObject {

    @EmbeddedId
    private OrgObjectId orgObjectId;

    public OrgObjectId getOrgObjectId() {
        return orgObjectId;
    }

    public void setOrgObjectId(OrgObjectId orgObjectId) {
        this.orgObjectId = orgObjectId;
    }
}
