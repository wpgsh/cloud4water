package net.wapwag.wemp.rest;

import static net.wapwag.wemp.rest.MockData.accessToken;
import static net.wapwag.wemp.rest.MockData.action;
import static net.wapwag.wemp.rest.MockData.handle;
import static net.wapwag.wemp.rest.MockData.objectDataList;
import static net.wapwag.wemp.rest.MockData.userId;
import static net.wapwag.wemp.rest.UserResourceMock.mockService;
import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.gson.reflect.TypeToken;
import com.thingswise.appframework.jaxrs.utils.AnnotationProcessor;
import com.thingswise.appframework.jaxrs.utils.AppframeworkDynamicFeature;
import com.thingswise.appframework.jaxrs.utils.AuthorizationAnnotationProcessor;
import com.thingswise.appframework.jaxrs.utils.AuthorizationScheme;
import com.thingswise.appframework.jaxrs.utils.AuthorizationSchemes;
import com.thingswise.appframework.jaxrs.utils.OAuth2AnnotationProcessor;
import com.thingswise.appframework.jaxrs.utils.TokenHandler;
import com.thingswise.appframework.jaxrs.utils.TokenHandlers;

import net.wapwag.wemp.model.AccessTokenMapper;
import net.wapwag.wemp.model.ObjectView;
import net.wapwag.wemp.rest.authz.FineGrainedAuthorizationScheme;
import net.wapwag.wemp.rest.oauth2.WempTokenHandler;

public class AuthzTest extends BaseResourceTest {
	
	static class TestAppframeworkDynamicFeature extends AppframeworkDynamicFeature {

		@Override
		public void bindAnnotationProcessor(AnnotationProcessor ap) {
			super.bindAnnotationProcessor(ap);
		}
		
	}
	
	static class TestAuthorizationAnnotationProcessor extends AuthorizationAnnotationProcessor {
		
		public void bindAuthorizationSchemes(AuthorizationSchemes schemes) {
			this.schemes = schemes;
		}
		
	}
	
	static class TestOAuth2AnnotationProcessor extends OAuth2AnnotationProcessor {
		
		public void bindTokenHandlers(TokenHandlers handlers) {
			this.handlers = handlers;
		}
	}
	
	static class TestTokenHandlers extends TokenHandlers {

		@Override
		public void bindTokenHandler(TokenHandler tokenHandler) throws Exception {
			super.bindTokenHandler(tokenHandler);
		}
		
	}
	
	static class TestAuthorizationSchemes extends AuthorizationSchemes {

		@Override
		public void bindAuthorizationScheme(AuthorizationScheme authorizationScheme) throws Exception {
			super.bindAuthorizationScheme(authorizationScheme);
		}		
		
	}
	
	DynamicFeature createJAXRSProvider() throws Exception {
				
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
			                    Optional.fromNullable(accessToken.getScope()).
			                            transform(String::trim).
			                            transform(s -> {
			                                assert s != null;
			                                return s.split(" ");
			                            }).
			                            or(new String[0]))));
		
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
	
	@Override
	ResourceConfig initResource() {
		ResourceConfig cfg = new ResourceConfig(UserResource.class);
		
		try {
			cfg.register(createJAXRSProvider());
		} catch (Exception e) {
			throw new RuntimeException("Cannot initialize JAX-RS interceptors", e);
		}
		
		return cfg;
	}
	
	String path;

	@Ignore
	@Test
    public void testGetObjectsByUser() throws Exception {
        when(mockService.getObjectsByUser(userId, "read"))
                .thenReturn(objectDataList.stream().map(ObjectView::newInstance).collect(Collectors.toSet()));

        path = String.format("/user/%s/objects", userId);

        Response response = target(path).queryParam("action", action).request().
        	header("authorization", "Bearer "+handle).get();
        
        assertEquals(OK_200, response.getStatus());
        
        Type type = new TypeToken<Set<ObjectView>>(){}.getType();
        Set<ObjectView> objectViews = getResult(response, type);

        assertNotNull(objectViews);
        assertEquals(objectDataList.size(), objectViews.size());
    }

}
