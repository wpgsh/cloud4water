package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.List;

/**
 * Pump network entity
 * Created by Administrator on 2016/10/19 0019.
 */
@Entity
@Table(name = "pump_network_data")
public class PumpNetwork {

    @Id
    @Column
    private String id;

    @Column
    private String name;

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
}
