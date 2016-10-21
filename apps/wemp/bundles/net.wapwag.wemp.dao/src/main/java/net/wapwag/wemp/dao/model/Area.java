package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Country
 * Created by Administrator on 2016/10/18 0018.
 */
@Entity
@Table(name = "area_data")
public class Area extends ObjectData {

    @ManyToOne
    private Country country;

    @OneToMany
    @JoinColumn(name = "area_id")
    private Set<Province> provinceSet;

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<Province> getProvinceSet() {
        return provinceSet;
    }

    public void setProvinceSet(Set<Province> provinceSet) {
        this.provinceSet = provinceSet;
    }
}
