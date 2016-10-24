package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Country
 * Created by Administrator on 2016/10/18 0018.
 */
@Entity
@Table(name = "project_data")
public class Project extends ObjectData {

    @ManyToOne
    private County county;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private Set<PumpRoom> pumpRoomSet;

    public County getCounty() {
        return county;
    }

    public void setCounty(County county) {
        this.county = county;
    }

    public Set<PumpRoom> getPumpRoomSet() {
        return pumpRoomSet;
    }

    public void setPumpRoomSet(Set<PumpRoom> pumpRoomSet) {
        this.pumpRoomSet = pumpRoomSet;
    }
}
