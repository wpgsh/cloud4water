package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.List;

/**
 * Country
 * Created by Administrator on 2016/10/18 0018.
 */
@Entity
@Table(name = "country_data")
public class Country {

    @Id
    @Column
    private String id;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    private List<Area> areaList;

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

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }
}
