package net.wapwag.wemp.dao.model.project;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectType;
import net.wapwag.wemp.dao.model.Parent;
import net.wapwag.wemp.dao.model.geo.County;

import javax.persistence.*;
import java.util.Set;

/**
 * Country
 * Created by Administrator on 2016/10/18 0018.
 */
@Entity
@Table(name = "project_data")
public class Project extends ObjectData {

	@Parent
    @ManyToOne
    @JoinColumn(name = "county_id")
    private County county;

    @OneToMany(mappedBy = "project")
    private Set<PumpRoom> pumpRoomSet;

    public Project() {
        super(ObjectType.PROJECT);
    }

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
