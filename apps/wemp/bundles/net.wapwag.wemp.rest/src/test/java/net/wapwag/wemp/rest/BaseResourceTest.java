package net.wapwag.wemp.rest;

import com.google.gson.Gson;
import net.wapwag.wemp.WaterEquipmentService;
import net.wapwag.wemp.model.UserView;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Base resource test
 * Created by Administrator on 2016/11/7 0007.
 */
abstract class BaseResourceTest extends JerseyTest {

    static final long objId = 1L;

    private Gson gson = new Gson();

    abstract ResourceConfig initResource();

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return initResource();
    }

    <T> T getResult(Response response, Class<T> type) {
        String jsonResult = response.readEntity(String.class);
        return gson.fromJson(jsonResult, type);
    }

    <T> T getResult(Response response, Type type) {
        String jsonResult = response.readEntity(String.class);
        return gson.fromJson(jsonResult, type);
    }

}
