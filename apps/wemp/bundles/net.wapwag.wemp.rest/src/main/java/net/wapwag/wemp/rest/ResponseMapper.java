package net.wapwag.wemp.rest;

import com.google.common.collect.Maps;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.wapwag.wemp.dao.model.JsonIgnore;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * General response mapper
 * Created by Administrator on 2016/9/28 0028.
 */
@XmlRootElement
public class ResponseMapper {

    private static final Gson serializer = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy() {

                /**
                 * @param f the field object that is under test
                 * @return true if the field should be ignored; otherwise false
                 */
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getAnnotation(JsonIgnore.class) != null;
                }

                /**
                 * @param clazz the class object that is under test
                 * @return true if the class should be ignored; otherwise false
                 */
                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            }).create();

    private Map<String, Object> resultMap = Maps.newHashMap();

    private String result;

    /**
     * JAXB won't take effect when the field don't have both getter and setter,
     * So you need to put the annotation on the getter method instead of field
     * @return
     */
    @XmlElement
    public String getResult() {
        return serializer.toJson(resultMap);
    }

    public ResponseMapper add(String key, Object value) {
        resultMap.put(key, value);
        return this;
    }

    public static ResponseMapper serialize() {
        return new ResponseMapper();
    }

}
