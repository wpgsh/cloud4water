package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Country
 * Created by Administrator on 2016/10/18 0018.
 */
@Entity
@Table(name = "county_data")
public class County {

    @Id
    @Column
    private String id;

    @Column
    private String name;

    @OneToMany
    @JoinColumn(name = "county_id")
    private Set<Project> projectSet;

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

    public Set<Project> getTownSet() {
        return projectSet;
    }

    public void setTownSet(Set<Project> projectSet) {
        this.projectSet = projectSet;
    }
}
