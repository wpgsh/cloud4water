package net.wapwag.wemp.dao.model.org;

import net.wapwag.wemp.dao.model.ObjectType;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Water management company
 * Created by Administrator on 2016/10/25 0025.
 */
@Entity
@Table(name = "water_management_company")
public class WaterManageCompany extends Organization {

    public WaterManageCompany() {
        super(ObjectType.WATER_MANAGEMENT_COMPANY);
    }

}
