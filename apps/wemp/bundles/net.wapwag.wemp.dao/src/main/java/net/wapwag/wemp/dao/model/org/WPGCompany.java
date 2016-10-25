package net.wapwag.wemp.dao.model.org;

import net.wapwag.wemp.dao.model.ObjectType;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * WPG head company
 * Created by Administrator on 2016/10/25 0025.
 */
@Entity
@Table(name = "wpg_company")
public class WPGCompany extends Organization {

    public WPGCompany() {
        super(ObjectType.WPG_COMPANY);
    }

}
