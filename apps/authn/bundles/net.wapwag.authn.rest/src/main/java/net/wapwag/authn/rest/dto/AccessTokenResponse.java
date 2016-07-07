package net.wapwag.authn.rest.dto;

import javax.ws.rs.QueryParam;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

/**
 * Access token response dto.
 * Created by Lee on 2016/7/5.
 */
@XmlRootElement
public class AccessTokenResponse {

    /**
     * Access token which can be used for access authorized resource.
     */
    private String accessToken;

    /**
     * For users who have authorized scopes for the application.
     */
    private Set<String> scope;

    public AccessTokenResponse(String accessToken, Set<String> scope) {
        this.accessToken = accessToken;
        this.scope = scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Set<String> getScope() {
        return scope;
    }

}
