package net.wapwag.wemp.dao.model;

import javax.persistence.*;
import java.util.Set;

/**
 * User model
 * Created by Administrator on 2016/9/28 0028.
 */
@Entity
@Table(name = "user_data")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @ManyToMany
    @JoinTable(name = "user_object",
        joinColumns = {
                @JoinColumn(name = "user_id")
        },
        inverseJoinColumns = {
                @JoinColumn(name = "object_id")
        }
    )
    private Set<ObjectData> objectDataSet;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ObjectData> getObjectDataSet() {
        return objectDataSet;
    }

    public void setObjectDataSet(Set<ObjectData> objectDataSet) {
        this.objectDataSet = objectDataSet;
    }
}
