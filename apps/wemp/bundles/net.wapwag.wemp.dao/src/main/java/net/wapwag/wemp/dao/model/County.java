package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.List;

/**
 * Country
 * Created by Administrator on 2016/10/18 0018.
 */
@Entity
@Table(name = "county_data")
public class County {

    @Id
    @Column
    private String id;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "county_id")
    private List<Town> townList;

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

    public List<Town> getTownList() {
        return townList;
    }

    public void setTownList(List<Town> townList) {
        this.townList = townList;
    }
}
