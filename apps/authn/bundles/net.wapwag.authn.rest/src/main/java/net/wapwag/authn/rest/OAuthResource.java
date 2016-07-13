package net.wapwag.authn.rest;

import net.wapwag.authn.AuthenticationService;
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
@Component(service = OAuthResource.class)
@Path("/oauth")
public class OAuthResource {

    private static final Logger logger = LoggerFactory.getLogger(OAuthResource.class);

    /**
     * Jersey built-in request uri information model.
     */
    @Context
    private UriInfo uriInfo;

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
        String code = authnService.getAuthorizationCode(
                authorizeRequest.getClientId(),
                authorizeRequest.getRedirectURI(),
                authorizeRequest.getScope()
        );

//        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().queryParam("code", code);
        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
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
        String accessToken = authnService.getAccessToken(
                accessTokenRequest.getClientId(),
                accessTokenRequest.getClientSecret(),
                accessTokenRequest.getAuthorizationCode(),
                accessTokenRequest.getRedirectURI()
        );
        return new AccessTokenResponse(accessToken, null);
    }

}
