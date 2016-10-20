package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Country
 * Created by Administrator on 2016/10/18 0018.
 */
@Entity
@Table(name = "project_data")
public class Project {

    @Id
    @Column
    private String id;

    @Column
    private String name;

    @JsonIgnore
    @ManyToOne
    private County county;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private Set<PumpRoom> pumpRoomSet;

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
