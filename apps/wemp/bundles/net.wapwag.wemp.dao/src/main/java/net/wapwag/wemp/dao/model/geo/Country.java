package net.wapwag.wemp.dao.model.geo;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectType;

import javax.persistence.*;
import java.util.Set;

/**
 * Country
 * Created by Administrator on 2016/10/18 0018.
 */
@Entity
@Table(name = "country_data")
public class Country extends ObjectData {

    @OneToMany
    @JoinColumn(name = "country_id")
    private Set<Area> areaSet;

    public Country() {
        super(ObjectType.COUNTRY);
    }

    public Set<Area> getAreaSet() {
        return areaSet;
    }

    public void setAreaSet(Set<Area> areaSet) {
        this.areaSet = areaSet;
    }
}
