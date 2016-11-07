package net.wapwag.wemp.model;

import net.wapwag.wemp.dao.model.permission.Group;

/**
 * Group view class
 * Created by Administrator on 2016/10/21 0021.
 */
public class GroupView {

    public final long id;

    public final String name;

    private GroupView(Group group) {
        this.id = group.getId();
        this.name = group.getName();
    }

    public static GroupView newInstance(Group group) {
        return new GroupView(group);
    }

}
