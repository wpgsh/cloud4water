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
@Table(name = "province_data")
public class Province extends ObjectData {

    @ManyToOne
    private Area area;

    @OneToMany
    @JoinColumn(name = "province_id")
    private Set<City> citySet;

    public Province() {
        super(ObjectType.PROVINCE);
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Set<City> getCitySet() {
        return citySet;
    }

    public void setCitySet(Set<City> citySet) {
        this.citySet = citySet;
    }
}
