package net.wapwag.authn.rest;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;

import com.google.gson.Gson;

/**
 * Base resource test
 * Created by Administrator on 2016/11/7 0007.
 */
abstract class BaseResourceTest extends JerseyTest {

    private Gson gson = new Gson();

    abstract ResourceConfig initResource();

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(MultiPartFeature.class);
    }
    
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

}
