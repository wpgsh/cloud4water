package net.wapwag.wemp.rest;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.thingswise.appframework.jaxrs.utils.*;
import net.wapwag.wemp.model.AccessTokenMapper;
import net.wapwag.wemp.rest.authz.FineGrainedAuthorizationScheme;
import net.wapwag.wemp.rest.oauth2.WempTokenHandler;
import org.apache.oltu.oauth2.common.OAuth;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static net.wapwag.wemp.rest.MockData.*;
import static net.wapwag.wemp.rest.UserResourceMock.mockService;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Base resource test
 * Created by Administrator on 2016/11/7 0007.
 */
abstract class BaseResourceTest extends JerseyTest {

    static final long objId = 1L;

    private Gson gson = new Gson();

    static Invocation.Builder validToken(WebTarget target) {
        return target.request().header(OAuth.HeaderType.AUTHORIZATION, String.format("%s %s", OAuth.OAUTH_HEADER_NAME, handle));
    }

    static Invocation.Builder invalidToken(WebTarget target) {
        return target.request().header(OAuth.HeaderType.AUTHORIZATION, String.format("%s %s", OAuth.OAUTH_HEADER_NAME, invalidHandle));
    }

    private static class TestAppframeworkDynamicFeature extends AppframeworkDynamicFeature {

        @Override
        public void bindAnnotationProcessor(AnnotationProcessor ap) {
            super.bindAnnotationProcessor(ap);
        }

    }

    private static class TestAuthorizationAnnotationProcessor extends AuthorizationAnnotationProcessor {

        public void bindAuthorizationSchemes(AuthorizationSchemes schemes) {
            this.schemes = schemes;
        }

    }

    private static class TestOAuth2AnnotationProcessor extends OAuth2AnnotationProcessor {

        public void bindTokenHandlers(TokenHandlers handlers) {
            this.handlers = handlers;
        }
    }

    private static class TestTokenHandlers extends TokenHandlers {

        @Override
        public void bindTokenHandler(TokenHandler tokenHandler) throws Exception {
            super.bindTokenHandler(tokenHandler);
        }

    }

    private static class TestAuthorizationSchemes extends AuthorizationSchemes {

        @Override
        public void bindAuthorizationScheme(AuthorizationScheme authorizationScheme) throws Exception {
            super.bindAuthorizationScheme(authorizationScheme);
        }

    }

    private DynamicFeature createJAXRSProvider() throws Exception {

        FineGrainedAuthorizationScheme fgAuthz = new FineGrainedAuthorizationScheme();
        fgAuthz.setWaterEquipmentService(mockService);

        TestAuthorizationSchemes schemes = new TestAuthorizationSchemes();
        schemes.bindAuthorizationScheme(fgAuthz);

        TestAuthorizationAnnotationProcessor authz = new TestAuthorizationAnnotationProcessor();
        authz.bindAuthorizationSchemes(schemes);

        when(mockService.lookupToken(any())).thenReturn(null);
        when(mockService.lookupToken(handle)).thenReturn(
                new AccessTokenMapper(
                        Long.toString(accessToken.getAccessTokenId().getUser().getId()),
                        accessToken.getExpiration(),
                        accessToken.getAccessTokenId().getRegisteredClient().getClientId(),
                        accessToken.getHandle(),
                        ImmutableSet.copyOf(
                                Optional.ofNullable(accessToken.getScope())
                                        .map(String::trim).map(s -> s.split(" "))
                                        .orElse(new String[0]))));

        when(mockService.isAuthorized(anyString(), anyString(), anyString())).thenReturn(false);
        when(mockService.isAuthorized(userId + "", action, objId + "")).thenReturn(true);
        when(mockService.isAuthorized(userId + "", "write", objId + "")).thenReturn(true);
        when(mockService.isAuthorized(userId + "", action, exceptionId + "")).thenReturn(true);

        WempTokenHandler tokenHandler = new WempTokenHandler();
        tokenHandler.setWaterEquipmentService(mockService);

        TestTokenHandlers tokenHandlers = new TestTokenHandlers();
        tokenHandlers.bindTokenHandler(tokenHandler);

        TestOAuth2AnnotationProcessor oauth2 = new TestOAuth2AnnotationProcessor();
        oauth2.bindTokenHandlers(tokenHandlers);

        TestAppframeworkDynamicFeature feature = new TestAppframeworkDynamicFeature();

        feature.bindAnnotationProcessor(oauth2);
        feature.bindAnnotationProcessor(authz);

        return feature;
    }

    ResourceConfig initResource() {
        Set<Class<?>> testResourceSet = new HashSet<>();
        testResourceSet.add(ObjectResourceMock.class);
        testResourceSet.add(OrgGroupResourceMock.class);
        testResourceSet.add(UserResourceMock.class);
        ResourceConfig cfg = new ResourceConfig(testResourceSet);
        
        try {
            cfg.register(createJAXRSProvider());
        } catch (Exception e) {
            throw new RuntimeException("Cannot initialize JAX-RS interceptors", e);
        }

        return cfg;
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

    <T> T getResult(Response response, Type type) {
        String jsonResult = response.readEntity(String.class);
        return gson.fromJson(jsonResult, type);
    }

}
