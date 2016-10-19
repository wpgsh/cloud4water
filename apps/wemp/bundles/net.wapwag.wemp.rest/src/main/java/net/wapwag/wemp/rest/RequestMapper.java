package net.wapwag.wemp.rest;

import javax.ws.rs.FormParam;
import java.util.Map;

/**
 * General request mapper
 * Created by Administrator on 2016/9/28 0028.
 */
public class RequestMapper {

    @FormParam("paraMap")
    private Map<String, Object> paraMap;

    public Map<String, Object> getParaMap() {
        return paraMap;
    }

    public void setParaMap(Map<String, Object> paraMap) {
        this.paraMap = paraMap;
    }
}
