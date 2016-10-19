package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private List<PumpRoom> pumpRoomList;

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

    public List<PumpRoom> getPumpRoomList() {
        return pumpRoomList;
    }

    public void setPumpRoomList(List<PumpRoom> pumpRoomList) {
        this.pumpRoomList = pumpRoomList;
    }
}
