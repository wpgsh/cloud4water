package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.List;

/**
 * Country
 * Created by Administrator on 2016/10/18 0018.
 */
@Entity
@Table(name = "pump_room_data")
public class PumpRoom {

    @Id
    @Column
    private String id;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "pump_room_id")
    private List<PumpEquipment> pumpEquipmentList;

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

    public List<PumpEquipment> getPumpEquipmentList() {
        return pumpEquipmentList;
    }

    public void setPumpEquipmentList(List<PumpEquipment> pumpEquipmentList) {
        this.pumpEquipmentList = pumpEquipmentList;
    }
}
