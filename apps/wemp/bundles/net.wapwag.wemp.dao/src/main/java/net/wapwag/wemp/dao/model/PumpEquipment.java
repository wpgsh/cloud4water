package net.wapwag.wemp.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Pump equipment entity
 * Created by Administrator on 2016/10/19 0019.
 */
@Entity
@Table(name = "pump_equipment_data")
public class PumpEquipment {

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
