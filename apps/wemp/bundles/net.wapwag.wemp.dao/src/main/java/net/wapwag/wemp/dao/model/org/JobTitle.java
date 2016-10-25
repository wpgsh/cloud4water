package net.wapwag.wemp.dao.model.org;

import net.wapwag.wemp.dao.model.link.UserOrg;

import javax.persistence.*;
import java.util.Set;

/**
 * Job title such as manager,technician or secretary,etc.
 * Created by Administrator on 2016/10/25 0025.
 */
@Entity
@Table(name = "job_title")
public class JobTitle {

    @Id
    private long id;

    @Column
    private String name;

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

}
