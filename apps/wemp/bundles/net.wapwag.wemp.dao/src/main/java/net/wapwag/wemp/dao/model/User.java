package net.wapwag.wemp.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User model
 * Created by Administrator on 2016/9/28 0028.
 */
@Entity
@Table(name = "user_data")
public class User {

    @Id
    @Column
    private String id;

    @Column
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