package net.wapwag.wemp.dao.model.org;

import net.wapwag.wemp.dao.model.ObjectType;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Water management authority
 * Created by Administrator on 2016/10/25 0025.
 */
@Entity
@Table(name = "water_management_auth")
public class WaterManageAuth extends Organization {

    public WaterManageAuth() {
        super(ObjectType.WATER_MANAGE_AUTH);
    }

}
