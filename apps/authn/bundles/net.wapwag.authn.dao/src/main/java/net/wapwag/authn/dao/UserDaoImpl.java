package net.wapwag.authn.dao;

import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.Image;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;
import org.apache.aries.jpa.template.EmFunction;
import org.osgi.service.component.annotations.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Component(scope=ServiceScope.SINGLETON)
public class UserDaoImpl implements UserDao {

	@Reference(target = "(osgi.name=user)")
	protected TxAwareEntityManager entityManager;

	@Activate
	protected void init() throws Exception {
		entityManager.init();
	}

	@Deactivate
	protected void destroy() {

	}

	@Override
	public User getUser(final long uid) throws UserDaoException {
		final String hql = "select user from User user where user.id = :userId";

		try {
			return entityManager.txExpr(em -> em.createQuery(hql, User.class)
					.setParameter("userId", uid)
					.getResultList()
					.stream()
					.findFirst()
					.orElse(null));
		} catch (Exception e) {
			throw new UserDaoException("Cannot get user entity", e);
		}
	}

	@Override
	public AccessToken lookupAccessToken(final String handle) throws UserDaoException {
        final String hql = "select token from AccessToken token " +
				"where token.handle = :handle " +
                "AND token.accessTokenId.user.enabled = :enabled";

        try {
            return entityManager.txExpr(em -> em.createQuery(hql, AccessToken.class)
                    .setParameter("handle", handle)
                    .setParameter("enabled", true)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null)
            );
        } catch (Exception e) {
            throw new UserDaoException("can't get access token by the handle", e);
        }
	}

    @Override
    public RegisteredClient getClientByRedirectURI(final String redirectURI) throws UserDaoException {
        try {
            return entityManager.txExpr(em ->
                em.createQuery("select r from RegisteredClient r where r.redirectURI = :redirectURI",
                        RegisteredClient.class).setParameter("redirectURI", redirectURI)
						.getResultList()
						.stream()
						.findFirst()
						.orElse(null)
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
					.getResultList()
					.stream()
					.findFirst()
					.orElse(null)
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
	public AccessToken getAccessTokenByCode(String code) throws UserDaoException {
	    final String hql = "select token from AccessToken token " +
                "where token.authrizationCode = :code " +
                "and token.accessTokenId.user.enabled = :enabled";

        try {
            return entityManager.txExpr(em -> em.createQuery(
                    hql, AccessToken.class)
                    .setParameter("code", code)
                    .setParameter("enabled", true)
					.getResultList()
					.stream()
					.findFirst()
					.orElse(null)
            );
        } catch (Exception e) {
            throw new UserDaoException("cannot find access token", e);
        }
	}

    @Override
    public AccessToken getAccessTokenByUserIdAndClientId(long userId, long clientId) throws UserDaoException {
		final String hql = "select token from AccessToken token " +
				"where token.accessTokenId.user.id = :userId " +
				"and token.accessTokenId.registeredClient.id = :clientId";
		try {
			return entityManager.txExpr(em -> em.createQuery(hql, AccessToken.class)
					.setParameter("userId", userId)
					.setParameter("clientId", clientId)
					.getResultList()
					.stream()
					.findFirst()
					.orElse(null)
			);
		} catch (Exception e) {
			throw new UserDaoException("cannot find access token", e);
		}
    }

	@Override
	public User getUserByAccessToken(String token) throws UserDaoException {
		final String hql = "select token.accessTokenId.user from AccessToken token " +
				"where token.handle = :token";
		try {
			return entityManager.txExpr(em -> em.createQuery(hql, User.class)
					.setParameter("token", token)
					.getResultList()
					.stream()
					.findFirst()
					.orElse(null)
			);
		} catch (Exception e) {
			throw new UserDaoException("cannot get user by token", e);
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

//	@Override
//	public User getUserAvatar(final long uid) throws UserDaoException {
//		try {
//			return entityManager.txExpr(new EmFunction<User>() {
//				@Override
//				public User apply(EntityManager em) {
//					return em.find(User.class, uid);
//				}
//			});
//		} catch (Exception e) {
//			throw new UserDaoException("Cannot add user", e);
//		}
//	}
//
//	@Override
//	public int saveUserAvatar(final User user) throws UserDaoException {
//		try {
//			return entityManager.txExpr(em -> {
//				em.merge(user);
//				return 1;
//
//			});
//		} catch (Exception e) {
//			throw new UserDaoException("Cannot add user", e);
//		}
//	}
//
//	@Override
//	public int removeUserAvatar(final long uid) throws UserDaoException {
//		try {
//			return entityManager.txExpr(em -> {
//				User user = em.find(User.class, uid);
//				em.remove(user);
//				return 1;
//			});
//		} catch (Exception e) {
//			throw new UserDaoException("Cannot add user", e);
//		}
//	}


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
					newUser.setPasswordSalt(user.getPasswordSalt());
					em.merge(newUser);
					return newUser;
				}
			});
		} catch (Exception e) {
			throw new UserDaoException("Cannot get user entity", e);
		}
	}
	
	@Override
	public <E extends Exception> void tx(ComplexAction<E> action, Class<E> exClass) throws E {
		Exception ex;
		try {
			ex = entityManager.txExpr(em -> {
				try {
					action.apply();
					return null;
				} catch (Exception e) {
					return e;
				}
			});
		} catch (Exception e) {
			throw new RuntimeException("Unexpected exception", e);
		}
		
		if (ex != null) {
			if (exClass.isAssignableFrom(ex.getClass())) {
				throw exClass.cast(ex);
			} else {
				throw new RuntimeException("Unexpected exception", ex);
			}
		} 
	}
	
	@Override
	public <T, E extends Exception> T txExpr(ComplexActionWithResult<T, E> action, Class<E> exClass) throws E {		
		final List<T> result = new ArrayList<T>();
		
		Exception ex;
		try {
			ex = entityManager.txExpr(em -> {
					try {
						result.add(action.apply());
						return null;
					} catch (Exception e) {
						return e;
					}
				});
		} catch (Exception e) {
			throw new RuntimeException("Unexpected exception", e);
		}
		
		if (ex != null) {
			if (exClass.isAssignableFrom(ex.getClass())) {
				throw exClass.cast(ex);
			} else {
				throw new RuntimeException("Unexpected exception", ex);
			}
		} 
		
		return result.get(0);		
	}

	@Override
	public int saveImg(Image image) throws UserDaoException {
		try {
			return entityManager.txExpr(em -> {
				em.merge(image);
				return 1;
			});
		} catch (Exception e) {
			throw new UserDaoException("Cannot save image", e);
		}
	}

	@Override
	public int deleteImg(String avartarId) throws UserDaoException {
		try {
			return entityManager.txExpr(em -> {
				Image image = em.find(Image.class, avartarId);
				em.remove(image);
				return 1;
			});
		} catch (Exception e) {
			throw new UserDaoException("Cannot add user", e);
		}
	}

	@Override
	public Image getAvatar(String avartarId) throws UserDaoException {
		try {
			return entityManager.txExpr(em -> {
				Image image = em.find(Image.class, avartarId);
				return image;
			});
		} catch (Exception e) {
			throw new UserDaoException("Cannot add user", e);
		}
	}

}
