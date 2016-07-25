package net.wapwag.authn.dao;

import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;
import org.apache.aries.jpa.template.EmFunction;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Component(scope=ServiceScope.SINGLETON)
public class UserDaoImpl implements UserDao {

	private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	@Reference(target = "(osgi.name=user)")
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
			/*return entityManager.txExpr((em -> {
				return em.find(User.class, uid);
			}));*/
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
			return entityManager.txExpr(em -> em.find(AccessToken.class, handle));
		} catch (Exception e) {
			throw new UserDaoException("Cannot get access token", e);
		}
	}

    @Override
    public RegisteredClient getClientByRedirectURI(final String redirectURI) throws UserDaoException {
        try {
            return entityManager.txExpr(em ->
                em.createQuery("select r from RegisteredClient r where r.redirectURI = :redirectURI",
                        RegisteredClient.class).setParameter("redirectURI", redirectURI)
                        .getSingleResult()
            );
        } catch (Exception e) {
            throw new UserDaoException("Cannot get client by redirect_uri", e);
        }
    }

    @Override
	public RegisteredClient getClientByClientId(final long clientId) throws UserDaoException {
		try {
			return entityManager.txExpr(em ->
                em.createQuery("select r from RegisteredClient r where r.id = :clientId",
                    RegisteredClient.class)
                    .setParameter("clientId", clientId)
                    .getSingleResult()
            );
		} catch (Exception e) {
			throw new UserDaoException("Cannot add access token", e);
		}
	}

	@Override
	public long saveAccessToken(final AccessToken accessToken) throws UserDaoException {
		try {
			return entityManager.txExpr(em -> em.merge(accessToken) != null ? 1L : 0L);
		} catch (Exception e) {
			throw new UserDaoException("Cannot add access token", e);
		}
	}

    @Override
    public AccessToken getAccessToken(final AccessToken accessToken) throws UserDaoException {
        try {
            return entityManager.txExpr(em -> em.createQuery(
                "select at from AccessToken at where at.user.id = :userId and at.registeredClient.id = :clientId",
                    AccessToken.class)
                    .setParameter("userId", accessToken.getUser().getId())
                    .setParameter("clientId", accessToken.getRegisteredClient().getId())
                    .getSingleResult()
            );
        } catch (Exception e) {
            return null;
//            throw new UserDaoException("cannot find access token", e);
        }
    }

	@Override
	public AccessToken getAccessTokenByCode(String code) throws UserDaoException {
        try {
            return entityManager.txExpr(em -> em.createQuery(
                    "select token from AccessToken token where token.authrizationCode = :code", AccessToken.class)
                    .setParameter("code", code)
                    .getSingleResult()
            );
        } catch (Exception e) {
            return null;
        }
	}

    @Override
    public AccessToken getAccessTokenByUserIdAndClientId(long userId, long clientId) throws UserDaoException {
        try {
            return entityManager.txExpr(em -> em.createQuery(
                    "select at from AccessToken at where at.user.id = :userId and at.registeredClient.id = :clientId",
                    AccessToken.class)
                    .setParameter("userId", userId)
                    .setParameter("clientId", clientId)
                    .getSingleResult()
            );
        } catch (Exception e) {
            return null;
        }
    }

    @Override
	public int saveUser(final User user) throws UserDaoException {
		try {
			return entityManager.txExpr(em -> {
					 em.merge(user);
					 return 1;
				
			});
		} catch (Exception e) {
			throw new UserDaoException("Cannot add user", e);
		}
	}

	@Override
	public int removeUser(final long uid) throws UserDaoException {
		try {
			return entityManager.txExpr(em -> {
				User user = em.find(User.class, uid);
				em.remove(user);
				return 1;
			});
		} catch (Exception e) {
			throw new UserDaoException("Cannot add user", e);
		}
	}

	

	@Override
	public User getUserAvatar(final long uid) throws UserDaoException {
		try {
			return entityManager.txExpr(new EmFunction<User>() {
				@Override
				public User apply(EntityManager em) {
					return em.find(User.class, uid);
				}
			});
		} catch (Exception e) {
			throw new UserDaoException("Cannot add user", e);
		}
	}

	@Override
	public int saveUserAvatar(final User user) throws UserDaoException {
		try {
			return entityManager.txExpr(em -> {
				em.merge(user);
				return 1;

			});
		} catch (Exception e) {
			throw new UserDaoException("Cannot add user", e);
		}
	}

	@Override
	public int removeUserAvatar(final long uid) throws UserDaoException {
		try {
			return entityManager.txExpr(em -> {
				User user = em.find(User.class, uid);
				em.remove(user);
				return 1;
			});
		} catch (Exception e) {
			throw new UserDaoException("Cannot add user", e);
		}
	}


    @Override
	public User getUserByName(final String userName) throws UserDaoException {
		try {
            return entityManager.txExpr(new EmFunction<User>() {
                @Override
                public User apply(EntityManager em) {
                	String queryUser = "select * from users where username ='"+userName+"' or email ='" + userName +"' ";
                	Query query = em.createNativeQuery(queryUser,User.class);
                	List<User> result = query.getResultList();
                	if (result.size() >= 1) {
                		return result.get(0);
					}
                	return null;
                }
            });
        } catch (Exception e) {
            throw new UserDaoException("Cannot find user by username", e);
        }
	}
    
    @Override
	public User getUserByEmail(final String email) throws UserDaoException {
		try {
            return entityManager.txExpr(new EmFunction<User>() {
                @Override
                public User apply(EntityManager em) {
                	String queryUser = "select * from users where email ='"+email+"' ";
                	Query query = em.createNativeQuery(queryUser,User.class);
                	List<User> result = query.getResultList();
                	if (result.size() >= 1) {
                		return result.get(0);
					}
                	return null;
                }
            });
        } catch (Exception e) {
            throw new UserDaoException("Cannot find user by username", e);
        }
	}

	@Override
	public User updateUserPwd(final User user) throws UserDaoException {
		try {
			final long uid = user.getId();
			return entityManager.txExpr(new EmFunction<User>() {
				@Override
				public User apply(EntityManager em) {
					User newUser =  em.find(User.class, uid);
					newUser.setPasswordHash(user.getPasswordHash());
					return newUser;
				}
			});
		} catch (Exception e) {
			throw new UserDaoException("Cannot get user entity", e);
		}
	}
}