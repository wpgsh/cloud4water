package net.wapwag.wemp.dao.model.project;

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
@Table(name = "pump_room_data")
public class PumpRoom extends ObjectData {

	@Parent
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "pumpRoom")
    private Set<PumpEquipment> pumpEquipmentSet;

    public PumpRoom() {
        super(ObjectType.PUMPROOM);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<PumpEquipment> getPumpEquipmentSet() {
        return pumpEquipmentSet;
    }

    public void setPumpEquipmentSet(Set<PumpEquipment> pumpEquipmentSet) {
        this.pumpEquipmentSet = pumpEquipmentSet;
    }
}
