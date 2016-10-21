package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Country
 * Created by Administrator on 2016/10/18 0018.
 */
@Entity
@Table(name = "city_data")
public class City extends ObjectData {

    @ManyToOne
    private Province province;

    @OneToMany
    @JoinColumn(name = "city_id")
    private Set<County> countySet;

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Set<County> getCountySet() {
        return countySet;
    }

    public void setCountySet(Set<County> countySet) {
        this.countySet = countySet;
    }
}
