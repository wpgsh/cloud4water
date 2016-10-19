package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.List;

/**
 * Object data model
 * Created by Administrator on 2016/9/28 0028.
 */
@Entity
@Table(name = "object_data")
public class ObjectData {

    @Id
    @Column(name = "object_id")
    private String objectId;

    @Column(name = "object_type")
    private String objectType;

    @Column(name = "object_name")
    private String objectName;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "object_parent_id")
    private List<ObjectData> childList;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public List<ObjectData> getChildList() {
        return childList;
    }

    public void setChildList(List<ObjectData> childList) {
        this.childList = childList;
    }
}
