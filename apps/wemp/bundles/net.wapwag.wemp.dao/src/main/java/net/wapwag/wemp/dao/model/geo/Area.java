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
@Table(name = "area_data")
public class Area extends ObjectData {

	@Parent
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "area")
    private Set<Province> provinceSet;

    public Area() {
        super(ObjectType.AREA);
    }

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
