package net.wapwag.wemp.dao.model.org;

import net.wapwag.wemp.dao.model.ObjectType;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * WPG department
 * Created by Administrator on 2016/10/25 0025.
 */
@Entity
@Table(name = "wpg_department")
public class WPGDepartment extends Organization {

    public WPGDepartment() {
        super(ObjectType.WPG_DEPARTMENT);
    }

}
