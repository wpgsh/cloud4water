package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.Set;

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

    @JsonIgnore
    @ManyToOne
    private Project project;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pump_room_id")
    private Set<PumpEquipment> pumpEquipmentSet;

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
