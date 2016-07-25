package net.wapwag.authn.dao.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Registered client data model.
 * Created by Lee on 2016/7/5.
 */
@Entity
@Table(name = "registered_clients")
public class RegisteredClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "redirect_uri", unique = true)
    private String redirectURI;

    @OneToMany(mappedBy = "registeredClient", fetch = FetchType.EAGER)
    private Set<AccessToken> accessTokenList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getRedirectURI() {
        return redirectURI;
    }

    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }

    public Set<AccessToken> getAccessTokenList() {
        return accessTokenList;
    }

    public void setAccessTokenList(Set<AccessToken> accessTokenList) {
        this.accessTokenList = accessTokenList;
    }

}
