package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Country
 * Created by Administrator on 2016/10/18 0018.
 */
@Entity
@Table(name = "city_data")
public class City {

    @Id
    @Column
    private String id;

    @Column
    private String name;

    @OneToMany
    @JoinColumn(name = "city_id")
    private Set<County> countySet;

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

    public Set<County> getCountySet() {
        return countySet;
    }

    public void setCountySet(Set<County> countySet) {
        this.countySet = countySet;
    }
}
