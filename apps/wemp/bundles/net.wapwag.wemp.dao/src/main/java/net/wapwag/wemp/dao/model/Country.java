package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Country
 * Created by Administrator on 2016/10/18 0018.
 */
@Entity
@Table(name = "country_data")
public class Country {

    @Id
    @Column
    private String id;

    @Column
    private String name;

    @OneToMany
    @JoinColumn(name = "country_id")
    private Set<Area> areaSet;

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

    public Set<Area> getAreaSet() {
        return areaSet;
    }

    public void setAreaSet(Set<Area> areaSet) {
        this.areaSet = areaSet;
    }
}
