package net.wapwag.wemp.model;

import net.wapwag.wemp.dao.model.permission.User;

/**
 * Object view class for json parse
 * Created by Administrator on 2016/10/27 0027.
 */
public class UserView {

    public final long id;

    public final String name;

    private UserView(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }

    public static UserView newInstance(User user) {
        return new UserView(user);
    }

}
