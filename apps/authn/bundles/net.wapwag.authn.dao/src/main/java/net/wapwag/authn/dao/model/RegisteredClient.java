package net.wapwag.authn.dao.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Registered client data model.
 * Created by Lee on 2016/7/5.
 */
@Entity
@Table(name = "registered_client")
public class RegisteredClient {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "redirect_uri")
    private String redirectURI;

    @OneToMany
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

    @Override
    public String toString() {
        return "RegisteredClient{" +
                "id=" + id +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", redirectURI='" + redirectURI + '\'' +
                ", accessTokenList=" + accessTokenList +
                '}';
    }
}
