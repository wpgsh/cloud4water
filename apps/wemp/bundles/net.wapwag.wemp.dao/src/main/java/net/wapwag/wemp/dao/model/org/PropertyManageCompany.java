package net.wapwag.wemp.dao.model.org;

import net.wapwag.wemp.dao.model.ObjectType;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Property management company entity
 * Created by Administrator on 2016/10/25 0025.
 */
@Entity
@Table(name = "property_manage_company")
public class PropertyManageCompany extends Organization {

    public PropertyManageCompany() {
        super(ObjectType.PROPERTY_MANAGE_COMPANY);
    }
}
