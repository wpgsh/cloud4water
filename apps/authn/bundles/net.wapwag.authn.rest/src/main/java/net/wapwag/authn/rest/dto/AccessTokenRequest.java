package net.wapwag.authn.rest.dto;

import javax.ws.rs.FormParam;

/**
 * Access token request dto.
 * Created by Lee on 2016/7/5.
 */
public class AccessTokenRequest {

    /**
     * <b>Required</b>.The client ID you registered from authorization system.
     */
    @FormParam("client_id")
    private String clientId;

    /**
     * <b>Required</b>.The client secret you received from authorization system.
     */
    @FormParam("client_secret")
    private String clientSecret;

    /**
     * <b>Required</b>.The code you received from authorize response.
     */
    @FormParam("code")
    private String authorizationCode;

    /**
     * The URL in your application where users will be sent after authorization.
     */
    @FormParam("redirect_uri")
    private String redirectURI;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getRedirectURI() {
        return redirectURI;
    }

    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }

    @Override
    public String toString() {
        return "RegisteredClient{" +
                "clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", authorizationCode='" + authorizationCode + '\'' +
                ", redirectURI='" + redirectURI + '\'' +
                '}';
    }
}
