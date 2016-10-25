package net.wapwag.wemp.dao.model.geo;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectType;
import net.wapwag.wemp.dao.model.project.Project;

import javax.persistence.*;
import java.util.Set;

/**
 * Country
 * Created by Administrator on 2016/10/18 0018.
 */
@Entity
@Table(name = "county_data")
public class County extends ObjectData {

    @ManyToOne
    private City city;

    @OneToMany
    @JoinColumn(name = "county_id")
    private Set<Project> projectSet;

    public County() {
        super(ObjectType.COUNTY);
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Set<Project> getProjectSet() {
        return projectSet;
    }

    public void setProjectSet(Set<Project> projectSet) {
        this.projectSet = projectSet;
    }
}
