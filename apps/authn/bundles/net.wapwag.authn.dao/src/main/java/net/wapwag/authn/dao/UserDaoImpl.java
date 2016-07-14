package net.wapwag.authn.dao;

import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;
import org.apache.aries.jpa.template.EmFunction;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Component
public class UserDaoImpl implements UserDao {

	private static final Logger logger = LoggerFactory
			.getLogger(UserDaoImpl.class);

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
	public AccessToken lookupAccessToken(final String handle)
			throws UserDaoException {
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
    public RegisteredClient getClientByRedirectURI(final String redirectURI) throws UserDaoException {
        try {
            return entityManager.txExpr(new EmFunction<RegisteredClient>() {
                @Override
                public RegisteredClient apply(EntityManager em) {
                    return em.createQuery("select r from RegisteredClient r where r.redirectURI = :redirectURI",
                            RegisteredClient.class)
                            .setParameter("redirectURI", redirectURI)
                            .getSingleResult();
                }
            });
        } catch (Exception e) {
            throw new UserDaoException("Cannot get client by redirect_uri", e);
        }
    }

    @Override
	public RegisteredClient getClientByClientId(final long clientId)
			throws UserDaoException {
		try {
			return entityManager.txExpr(new EmFunction<RegisteredClient>() {
				@Override
				public RegisteredClient apply(EntityManager em) {
					return em.createQuery("select r from RegisteredClient r where r.id = :clientId",
                            RegisteredClient.class)
							.setParameter("clientId", clientId)
							.getSingleResult();
				}
			});
		} catch (Exception e) {
			throw new UserDaoException("Cannot add access token", e);
		}
	}

	@Override
	public long saveAccessToken(final AccessToken accessToken)
			throws UserDaoException {
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
                            "select at from AccessToken at where at.user.id = :userId and at.registeredClient.id = :clientId",
                            AccessToken.class)
                            .setParameter("userId", accessToken.getUser().getId())
                            .setParameter("clientId", accessToken.getRegisteredClient().getId())
                            .getSingleResult();
                }
            });
        } catch (Exception e) {
            return null;
//            throw new UserDaoException("cannot find access token", e);
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

	@Override
	public User removeUser(final long uid) throws UserDaoException {
		try {
			return entityManager.txExpr(new EmFunction<User>() {
				@Override
				public User apply(EntityManager em) {
					User user = em.find(User.class, uid);
					em.remove(user);
					return user;
				}
			});
		} catch (Exception e) {
			throw new UserDaoException("Cannot add user", e);
		}
	}

	@Override
	public User updateUser(final long uid) throws UserDaoException {
		try {
			return entityManager.txExpr(new EmFunction<User>() {
				@Override
				public User apply(EntityManager em) {
					User user = new User();
					return em.merge(user);
				}
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
	public User saveUserAvatar(final User user) throws UserDaoException {
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

	@Override
	public User removeUserAvatar(final long uid) throws UserDaoException {
		try {
			return entityManager.txExpr(new EmFunction<User>() {
				@Override
				public User apply(EntityManager em) {
					User user = em.find(User.class, uid);
					em.remove(user);
					return user;
				}
			});
		} catch (Exception e) {
			throw new UserDaoException("Cannot add user", e);
		}
	}

	@Override
	public User updateUserAvatar(final long uid) throws UserDaoException {
		try {
			return entityManager.txExpr(new EmFunction<User>() {
				@Override
				public User apply(EntityManager em) {
					User user = new User();
					return em.merge(user);
				}
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
                	String queryUser = "select * from users where username ='"+userName+"' ";
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
}
