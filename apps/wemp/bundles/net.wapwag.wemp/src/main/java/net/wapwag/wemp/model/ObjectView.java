package net.wapwag.wemp.model;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectType;

/**
 * Object view class for json parse
 * Created by Administrator on 2016/10/27 0027.
 */
public class ObjectView {

    private final long id;

    private final String name;

    private final ObjectType objectType;

    private ObjectView(ObjectData objectData) {
        this.id = objectData.getId();
        this.name = objectData.getName();
        this.objectType = objectData.getType();
    }

    public static ObjectView newInstance(ObjectData objectData) {
        return new ObjectView(objectData);
    }
}
