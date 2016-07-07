package net.wapwag.authn.rest.dto;

import javax.ws.rs.QueryParam;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

/**
 * Authorize request dto.
 * Created by Lee on 2016/7/5.
 */
public class AuthorizeRequest {

    /**
     * <b>Required</b>.The client ID you registered in the authorize system.
     */
    @QueryParam("client_id")
    private String clientId;

    /**
     * The URL in your application where users will be sent after authorization.
     */
    @QueryParam("redirect_uri")
    private String redirectURI;

    /**
     * A comma delimited list of scopes.If not provided ,scope defaults to an empty
     * list for users that have not authorized any scopes for the application.
     */
    @QueryParam("scope")
    private Set<String> scope;

    public String getClientId() {
        return clientId;
    }

    public String getRedirectURI() {
        return redirectURI;
    }

    public Set<String> getScope() {
        return scope;
    }

    public void setClientId(String clientId) {

        this.clientId = clientId;
    }

    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }

    public void setScope(Set<String> scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "AuthorizeRequest{" +
                "clientId='" + clientId + '\'' +
                ", redirectURI='" + redirectURI + '\'' +
                ", scope=" + scope +
                '}';
    }
}
