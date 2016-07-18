package net.wapwag.authn.rest;

import net.wapwag.authn.AuthenticationService;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.rest.dto.AccessTokenRequest;
import net.wapwag.authn.rest.dto.AccessTokenResponse;
import net.wapwag.authn.rest.dto.AuthorizeRequest;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Authentification resource.
 * Created by Lee on 2016/7/5.
 */
@Deprecated
@Component(service = OAuthResource.class)
@Path("/oauth")
public class OAuthResource {

    private static final Logger logger = LoggerFactory.getLogger(OAuthResource.class);

    @Reference
    private AuthenticationService authnService;

    /**
     * Get authorization code for application user to fetch access token.
     * @param authorizeRequest Authorize request query param.
     * @return Return authorize response with client_id,redirect_uri,scope.
     * @throws Exception
     */
    @GET
    @Path("/authorize")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthorizationCode(@BeanParam AuthorizeRequest authorizeRequest) throws Exception {
        logger.info("/authorize ----->" + authorizeRequest.toString());
        RegisteredClient client = authnService.getClient(authorizeRequest.getRedirectURI());
        logger.info(client.getClientId());
        logger.info(client.getClientSecret());
        logger.info(client.getRedirectURI());
        String code = authnService.getAuthorizationCode(
                1L, client.getId(), authorizeRequest.getRedirectURI(),
                authorizeRequest.getScope()
        );

        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        logger.info("oAuthResource ----->" + code);
        return Response.seeOther(UriBuilder.fromUri(authorizeRequest.getRedirectURI()).queryParam("code", code).build())
                .cacheControl(cacheControl)
                .header("Pragma", "no-cache")
                .build();
    }

    /**
     * Get access token for application user to fetch authorized resource.
     * @param accessTokenRequest Access token query param.
     * @return Return accessToken response with client_id,client_secret,code,redirect_uri.
     */
    @POST
    @Path("/access_token")
    @Produces(MediaType.APPLICATION_JSON)
    public AccessTokenResponse getAccessToken(@BeanParam AccessTokenRequest accessTokenRequest) throws Exception {
        logger.info(accessTokenRequest.toString());
        RegisteredClient client = authnService.getClient(accessTokenRequest.getRedirectURI());
        String accessToken = authnService.getAccessToken(
                1L, client.getId(), accessTokenRequest.getClientSecret(),
                accessTokenRequest.getAuthorizationCode(), accessTokenRequest.getRedirectURI());
        return new AccessTokenResponse("Jersey支持中文", null);
    }

}
