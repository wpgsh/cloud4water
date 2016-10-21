package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Object data model
 * Created by Administrator on 2016/9/28 0028.
 */
@Entity
@Table(name = "object_data")
@Inheritance(strategy = InheritanceType.JOINED)
public class ObjectData {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
