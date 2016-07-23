package net.wapwag.authn.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Access token data model.
 * Updated by Lee on 2016/7/5.
 */
@Entity
@Table(name="access_tokens")
public class AccessToken {

    @ManyToOne
    @JoinColumn(name="user_id")
	private User user;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private RegisteredClient registeredClient;

    @Id
    @Column(name="handle")
	private String handle;

    @Column(name = "scope")
    private String scope;

    @Column(name = "authorization_code", unique = true)
    private String authrizationCode;

    @Column(name="ac_expiration")
	private long expiration;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RegisteredClient getRegisteredClient() {
        return registeredClient;
    }

    public void setRegisteredClient(RegisteredClient registeredClient) {
        this.registeredClient = registeredClient;
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
