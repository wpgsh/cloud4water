package net.wapwag.wemp.dao.model.permission;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Access token id
 * Created by Administrator on 2016/11/2 0002.
 */
@Embeddable
public class AccessTokenId implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private RegisteredClient registeredClient;

    public AccessTokenId() {
        
    }
    
    public User getUser() {
        return user;
    }

    public AccessTokenId(User user, RegisteredClient registeredClient) {
        this.user = user;
        this.registeredClient = registeredClient;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        AccessTokenId other = (AccessTokenId) obj;

        if (user == null) {
            if (other.user != null)
                return false;
        } else {
            if (other.user == null) {
                return false;
            } else {
                if (user.getId() != other.user.getId()) {
                    return false;
                }
            }
        }

        if (registeredClient == null) {
            if (other.registeredClient != null)
                return false;
        } else {
            if (other.registeredClient == null) {
                return false;
            } else {
                if (registeredClient.getId() != other.registeredClient.getId()) {
                    return false;
                }
            }
        }


        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((user == null) ? 0 : Long.hashCode(user.getId()));
        result = prime * result + ((registeredClient == null) ? 0 : Long.hashCode(registeredClient.getId()));
        return result;
    }
}
