package net.wapwag.wemp.dao.model.org;

import net.wapwag.wemp.dao.model.ObjectType;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * WPG branch
 * Created by Administrator on 2016/10/25 0025.
 */
@Entity
@Table(name = "wpg_branch")
public class WPGBranch extends Organization {

    public WPGBranch() {
        super(ObjectType.WPG_BRANCH);
    }

}
