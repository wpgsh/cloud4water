package net.wapwag.authn.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static net.wapwag.authn.rest.MockData.*;
import static net.wapwag.authn.rest.AuthenticationResourceMock.*;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.common.OAuth;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.thingswise.appframework.jaxrs.utils.AnnotationProcessor;
import com.thingswise.appframework.jaxrs.utils.AppframeworkDynamicFeature;
import com.thingswise.appframework.jaxrs.utils.AuthorizationAnnotationProcessor;
import com.thingswise.appframework.jaxrs.utils.AuthorizationScheme;
import com.thingswise.appframework.jaxrs.utils.AuthorizationSchemes;
import com.thingswise.appframework.jaxrs.utils.OAuth2AnnotationProcessor;
import com.thingswise.appframework.jaxrs.utils.TokenHandler;
import com.thingswise.appframework.jaxrs.utils.TokenHandlers;

import net.wapwag.authn.model.AccessTokenMapper;
import net.wapwag.authn.model.UserView;
import net.wapwag.authn.rest.authz.AnyAuthenticatedUserScheme;
import net.wapwag.authn.rest.authz.AuthorizationOnlyUserId;
import net.wapwag.authn.rest.authz.AuthorizationOnlyUserIdScheme;
import net.wapwag.authn.rest.oauth2.UsersTokenHandler;

/**
 * Base resource test
 * Created by Administrator on 2016/11/7 0007.
 */
abstract class BaseResourceTest extends JerseyTest {

    private Gson gson = new Gson();

//    abstract ResourceConfig initResource();

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(MultiPartFeature.class);
    }
    
    static Invocation.Builder validToken(WebTarget target) {
        return target.request().header(OAuth.HeaderType.AUTHORIZATION, String.format("%s %s", OAuth.OAUTH_HEADER_NAME, handle));
    }

    static Invocation.Builder invalidToken(WebTarget target) {
        return target.request().header(OAuth.HeaderType.AUTHORIZATION, String.format("%s %s", OAuth.OAUTH_HEADER_NAME, ivalid_handle));
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

        AnyAuthenticatedUserScheme anyAuthz = new AnyAuthenticatedUserScheme();
        
        AuthorizationOnlyUserIdScheme userIdScheme = new AuthorizationOnlyUserIdScheme();
        userIdScheme.setAuthnService(mockService);

        TestAuthorizationSchemes schemes = new TestAuthorizationSchemes();
        schemes.bindAuthorizationScheme(anyAuthz);
        schemes.bindAuthorizationScheme(userIdScheme);

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
                                Optional.fromNullable(accessToken.getScope()).
                                        transform(String::trim).
                                        transform(s -> {
                                            assert s != null;
                                            return s.split(" ");
                                        }).
                                        or(new String[0]))));

        when(mockService.getUserInfo(eq("token2_ivalid"))).thenReturn(null);
        when(mockService.getUserInfo(eq("token2"))).thenReturn(UserView.newInstance(user));

        UsersTokenHandler tokenHandler = new UsersTokenHandler();
        tokenHandler.setService(mockService);

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
        testResourceSet.add(AuthenticationResourceMock.class);
        ResourceConfig cfg = new ResourceConfig(testResourceSet);
        
        try {
            cfg.register(createJAXRSProvider()).register(MultiPartFeature.class);
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

}
