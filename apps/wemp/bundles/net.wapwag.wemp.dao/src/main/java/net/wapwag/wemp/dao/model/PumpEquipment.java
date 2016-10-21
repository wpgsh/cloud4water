package net.wapwag.wemp.dao.model;

import javax.persistence.*;

/**
 * Pump equipment entity
 * Created by Administrator on 2016/10/19 0019.
 */
@Entity
@Table(name = "pump_equipment_data")
public class PumpEquipment extends ObjectData {

    @ManyToOne
    @JoinColumn(name = "pump_room_id")
    private PumpRoom pumpRoom;

    public PumpRoom getPumpRoom() {
        return pumpRoom;
    }

    public void setPumpRoom(PumpRoom pumpRoom) {
        this.pumpRoom = pumpRoom;
    }
}
