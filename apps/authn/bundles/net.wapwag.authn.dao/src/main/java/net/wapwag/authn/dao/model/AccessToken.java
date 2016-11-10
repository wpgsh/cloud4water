package net.wapwag.authn.dao.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Access token data model.
 * Updated by Lee on 2016/7/5.
 */
@Entity
@Table(name="access_tokens")
public class AccessToken implements Serializable {

    @EmbeddedId
    private AccessTokenId accessTokenId;

    @Column(name="handle", unique = true)
    private String handle;

    @Column(name = "scope")
    private String scope;

    @Column(name = "authorization_code", unique = true)
    private String authrizationCode;

    @Column(name="ac_expiration")
	private long expiration;

    public AccessTokenId getAccessTokenId() {
        return accessTokenId;
    }

    public void setAccessTokenId(AccessTokenId accessTokenId) {
        this.accessTokenId = accessTokenId;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAuthrizationCode() {
        return authrizationCode;
    }

    public void setAuthrizationCode(String authrizationCode) {
        this.authrizationCode = authrizationCode;
    }

}
