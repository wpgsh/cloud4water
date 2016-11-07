package net.wapwag.wemp.model;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectType;

/**
 * Object view class for json parse
 * Created by Administrator on 2016/10/27 0027.
 */
public class ObjectView {

    public final long id;

    public final String name;

    public final ObjectType objectType;

    private ObjectView(ObjectData objectData) {
        this.id = objectData.getId();
        this.name = objectData.getName();
        this.objectType = objectData.getType();
    }

    public static ObjectView newInstance(ObjectData objectData) {
        return new ObjectView(objectData);
    }
}
