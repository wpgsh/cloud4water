package net.wapwag.wemp.dao.model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * Organization model
 * Created by Administrator on 2016/9/28 0028.
 */
@Entity
@Table(name = "org_data")
public class Organization extends ObjectData {

    @ManyToMany
    private Set<User> userSet;

}
