package net.wapwag.wemp.model;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectType;
import net.wapwag.wemp.dao.model.permission.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Object view class for json parse
 * Created by Administrator on 2016/10/27 0027.
 */
public class UserView {

    private final long id;

    private final String name;

    public UserView(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }

}
