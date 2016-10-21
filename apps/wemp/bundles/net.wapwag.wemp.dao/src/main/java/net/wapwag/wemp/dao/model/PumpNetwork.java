package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.List;

/**
 * Pump network entity
 * Created by Administrator on 2016/10/19 0019.
 */
@Entity
@Table(name = "pump_network_data")
public class PumpNetwork extends ObjectData {

    @ManyToOne
    private City city;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
