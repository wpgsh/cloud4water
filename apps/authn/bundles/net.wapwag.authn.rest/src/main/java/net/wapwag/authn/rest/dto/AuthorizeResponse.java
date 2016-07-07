package net.wapwag.authn.rest.dto;

import javax.ws.rs.QueryParam;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Authorize response dto.
 * Created by Lee on 2016/7/5.
 */
@XmlRootElement
public class AuthorizeResponse {

    @XmlElement(name = "code")
    private String authorizationCode;

    public AuthorizeResponse(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }
}
