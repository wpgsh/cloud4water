package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Country
 * Created by Administrator on 2016/10/18 0018.
 */
@Entity
@Table(name = "area_data")
public class Area {

    @Id
    @Column
    private String id;

    @Column
    private String name;

    @OneToMany
    @JoinColumn(name = "area_id")
    private Set<Province> provinceSet;

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

    public Set<Province> getProvinceSet() {
        return provinceSet;
    }

    public void setProvinceSet(Set<Province> provinceSet) {
        this.provinceSet = provinceSet;
    }
}
