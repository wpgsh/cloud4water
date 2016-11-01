package net.wapwag.wemp.dao.model.geo;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectType;
import net.wapwag.wemp.dao.model.Parent;

import javax.persistence.*;
import java.util.Set;

/**
 * Country
 * Created by Administrator on 2016/10/18 0018.
 */
@Entity
@Table(name = "city_data")
public class City extends ObjectData {

	@Parent
    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province province;

    @OneToMany(mappedBy = "city")
    private Set<County> countySet;

    public City() {
        super(ObjectType.CITY);
    }

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
