package net.wapwag.authn.dao;


import javax.persistence.EntityManager;
import javax.persistence.OrderColumn;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import javax.swing.text.StringContent;

import net.wapwag.authn.dao.model.RegisteredClient;
import org.apache.aries.jpa.template.EmFunction;
import org.apache.aries.jpa.template.JpaTemplate;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Component
public class UserDaoImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	@Reference(target="(osgi.name=user)")
	private TxAwareEntityManager entityManager;
	
	@Activate
	protected void init() throws Exception {
		entityManager.init();
	}
	
	@Deactivate
	protected void destroy() {
		
	}
	
	@Override
	public User getUser(final long uid) throws UserDaoException {
		try {
            return entityManager.txExpr(new EmFunction<User>() {
				@Override
				public User apply(EntityManager em) {
					return em.find(User.class, uid);
				}
			});
        } catch (Exception e) {
			throw new UserDaoException("Cannot get user entity", e);
		}
	}

	@Override
	public AccessToken lookupAccessToken(final String handle) throws UserDaoException {
		try {
			return entityManager.txExpr(new EmFunction<AccessToken>() {
				@Override
				public AccessToken apply(EntityManager em) {
					return em.find(AccessToken.class, handle);
				}				
			});			
		} catch (Exception e) {
			throw new UserDaoException("Cannot get access token", e);
		}
	}

	@Override
	public RegisteredClient getRegisteredClient(final String clientId) throws UserDaoException {
		try {
            return entityManager.txExpr(new EmFunction<RegisteredClient>() {
                @Override
                public RegisteredClient apply(EntityManager em) {
                    return em.createQuery("select r from RegisteredClient r where r.clientId = :clientId", RegisteredClient.class).
                    		setParameter("clientId", clientId).getSingleResult();
                }
            });
        } catch (Exception e) {
            throw new UserDaoException("Cannot add access token", e);
        }
	}

	@Override
	public long saveAccessToken(final AccessToken accessToken) throws UserDaoException {
        try {
            return entityManager.txExpr(new EmFunction<Long>() {
                @Override
                public Long apply(EntityManager em) {
                    em.merge(accessToken);
                    return 1L;
                }
            });
        } catch (Exception e) {
            throw new UserDaoException("Cannot add access token", e);
        }
	}

    @Override
    public AccessToken getAccessToken(final AccessToken accessToken) throws UserDaoException {
        try {
            return entityManager.txExpr(new EmFunction<AccessToken>() {
                @Override
                public AccessToken apply(EntityManager em) {
                    return em.createQuery(
                            "select at from AccessToken at where at.user.id = :userId and at.registeredClient.clientId = :clientId", AccessToken.class)
                            .setParameter("userId", accessToken.getUser().getId())
                            .setParameter("clientId", accessToken.getRegisteredClient().getClientId())
                            .getSingleResult();
                }
            });
        } catch (Exception e) {
            throw new UserDaoException("cannot find access token", e);
        }
    }

    @Override
    public User saveUser(final User user) throws UserDaoException {
        try {
            return entityManager.txExpr(new EmFunction<User>() {
                @Override
                public User apply(EntityManager em) {
                    return em.merge(user);
                }
            });
        } catch (Exception e) {
            throw new UserDaoException("Cannot add user", e);
        }
    }
}
