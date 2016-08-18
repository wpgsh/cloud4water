package net.wapwag.authn;

import com.eaio.uuid.UUID;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.wapwag.authn.Ids.UserId;
import net.wapwag.authn.dao.UserDao;
import net.wapwag.authn.dao.UserDaoException;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.model.AccessToken;
import net.wapwag.authn.model.UserProfile;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component(scope=ServiceScope.SINGLETON)
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	@Reference
	UserDao userDao;
	
	/**
	 * FOR TEST PURPOSES ONLY: override OSGi dependency injection
	 */
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public UserProfile getUserProfile(UserId uid) throws AuthenticationServiceException {
		User user;
		try {
			user = userDao.getUser(uid.id);
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException("Cannot get public user profile", e);
		}
		
		if (user != null) {
			return new UserProfile(user.getId());
		} else {
			return null;
		}
	}

	@Override
	public AccessToken lookupToken(String handle) throws AuthenticationServiceException {
		net.wapwag.authn.dao.model.AccessToken accessToken;
		try {
			accessToken = userDao.lookupAccessToken(handle);
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException("Cannot get access token", e);
		}
        return new AccessToken(
                accessToken.getUser().getId(),
                Long.MAX_VALUE,
                accessToken.getRegisteredClient().getClientId(),
                accessToken.getHandle(),
                ImmutableSet.copyOf(
                		Optional.fromNullable(accessToken.getScope()).
                		transform(String::trim).
                		transform(s -> {
                            assert s != null;
                            return s.split(" ");
                        }).
                		or(new String[0])));
	}

	@Override
	public String getAccessToken(String cliendId, String clientSecret, String code, String redirectURI)
			throws OAuthProblemException {
		return userDao.txExpr(() -> {
	        try {
	            RegisteredClient registeredClient = userDao.getClientByRedirectURI(redirectURI);

				if (registeredClient == null) {
				    throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_CLIENT, "unknow client");
                }

	            //valid clientSecret and clientId.
	            if (StringUtils.isNotBlank(cliendId)
                        && StringUtils.isNotBlank(clientSecret)
                        && clientSecret.equals(registeredClient.getClientId())
                        && clientSecret.equals(registeredClient.getClientSecret())) {

	                net.wapwag.authn.dao.model.AccessToken accessToken;
	                accessToken = userDao.getAccessTokenByCode(code);

                    //validate authorization code and if match then invalidate it.
                    if (accessToken != null
                            && StringUtils.isNotBlank(code)
                            && StringUtils.isNotBlank(accessToken.getAuthrizationCode())
                            && accessToken.getExpiration() > 0
                            && code.equals(accessToken.getAuthrizationCode())) {

                        accessToken.setExpiration(0L);
                        userDao.saveAccessToken(accessToken);
                        return accessToken.getHandle();
                    } else {
                        throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_GRANT,
                                "invalid or expired authorization code");
                    }
	            } else {
	                throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_CLIENT,
                            "error client credential");
	            }
	        } catch (UserDaoException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
	            throw OAuthProblemException.error(OAuthError.CodeResponse.SERVER_ERROR,
                        "authorization server encountered an unexpected exception");
	        }
		}, OAuthProblemException.class);
	}

	@Override
	public String getAuthorizationCode(long userId, String redirectURI, final Set<String> scope)
            throws OAuthProblemException {
        return userDao.txExpr(() -> {
            long result;
            boolean valid = false;
            Set<String> defaultScope = Sets.newHashSet();
            net.wapwag.authn.dao.model.AccessToken accessToken;

            try {

                RegisteredClient registeredClient = userDao.getClientByRedirectURI(redirectURI);

                //validate client.
                if (registeredClient != null) {

                    String code = StringUtils.replace(new UUID().toString(), "-", "");

                    //find accessToken by clientId and userId.
                    accessToken = userDao.getAccessTokenByUserIdAndClientId(userId, registeredClient.getId());

                    if ("wapwag".equals(registeredClient.getClientVendor())) {
                        //implicit scope
                        defaultScope.add("user:*");
                    } else if (scope == null || scope.size() == 0) {
                        throw OAuthProblemException.error(OAuthError.CodeResponse.INVALID_SCOPE,
                                "requested scope is invalid");
                    } else {
                            defaultScope = scope;
                        if (accessToken != null) {
                            Set<String> originalScope = new HashSet<>(Arrays.asList(accessToken.getScope().split(" ")));
                            //if no new scope need
                            if (originalScope.containsAll(defaultScope)) {
                                defaultScope = originalScope;
                                valid = true;
                            }
                        }
                    }

                    //if accessToken isn't exist or exist but need new scope,refresh accessToken
                    if (!valid) {
                        accessToken = new net.wapwag.authn.dao.model.AccessToken();
                        accessToken.setHandle(StringUtils.replace(new UUID().toString(), "-", ""));
                    }

                    accessToken.setScope(StringUtils.join(scope, " "));
                    accessToken.setUser(userDao.getUser(userId));
                    accessToken.setRegisteredClient(registeredClient);

                    //generate authorization code
                    accessToken.setAuthrizationCode(code);
                    accessToken.setExpiration(Long.MAX_VALUE);

                    //update authorization code
                    result = userDao.saveAccessToken(accessToken);
                } else {
                    throw OAuthProblemException.error(OAuthError.CodeResponse.UNAUTHORIZED_CLIENT,
                            "error client credential");
                }

                if (result > 0) {
                    return accessToken.getAuthrizationCode();
                } else {
                    throw new UserDaoException("Can't save authorization code");
                }
            } catch (UserDaoException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
                throw OAuthProblemException.error(OAuthError.CodeResponse.SERVER_ERROR,
                        "authorization server encountered an unexpected exception");
            }
        }, OAuthProblemException.class);
    }

	/**
	 * Check the client is a valid wpg client.
	 * <b>NOTE:</b>some client frameworks don't supply client_id and reply on redirect_uri
	 * as the key,so use redirect_uri as a client identifier.
	 *
	 * @param redirectURI client identifier.
	 * @return return the registered client model.
	 */
	@Override
	public RegisteredClient getClient(String redirectURI) throws AuthenticationServiceException {
		try {
            return StringUtils.isNotBlank(redirectURI) ? userDao.getClientByRedirectURI(redirectURI) : null;
        } catch (UserDaoException e) {
            throw new AuthenticationServiceException("Cannot get register client by redirect_uri", e);
        }
	}

	@Override
	public User getUser(long uid) throws AuthenticationServiceException {
		User user;
		try {
			user = userDao.getUser(uid);
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException("Cannot get user", e);
		}

		if (user != null) {
			return user;
		} else {
			return null;
		}
	}

	@Override
	public int saveUser(User user) throws AuthenticationServiceException {
		try {
			return userDao.saveUser(user);
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException("cannot add user");
		}
	}

	@Override
	public int removeUser(long uid) throws AuthenticationServiceException {
		try {
			return userDao.removeUser(uid);
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException("cannot remove user");
		}
		
	}

	@Override
	public User getUserAvatar(long uid) throws AuthenticationServiceException {
		User user;
		try {
			user = userDao.getUserAvatar(uid);
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException("Cannot get user", e);
		}

		if (user != null) {
			return new User();
		} else {
			return null;
		}
	}

	@Override
	public int saveUserAvatar(User user) throws AuthenticationServiceException {
		try {
			return  userDao.saveUserAvatar(user);
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException("cannot add user");
		}
	}

	@Override
	public int removeUserAvatar(long uid)
			throws AuthenticationServiceException {
		try {
			return userDao.removeUserAvatar(uid);
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException("cannot remove user");
		}
	}

    @Override
	public User getUserByName(String userName)
			throws AuthenticationServiceException {
		User user;
		try {
			user = userDao.getUserByName(userName);
        } catch (UserDaoException e) {
            throw new AuthenticationServiceException("cannot add user");
        }
        return user;
    }
    
    @Override  
	public User getUserByEmail(String email)
			throws AuthenticationServiceException {
		User user;
		try {
			user = userDao.getUserByEmail(email);
        } catch (UserDaoException e) {
            throw new AuthenticationServiceException("cannot add user");
        }
        return user;
    }
    @Override  
	public User updateUserPwd(User user)
			throws AuthenticationServiceException {
		try {
			user = userDao.updateUserPwd(user);
        } catch (UserDaoException e) {
            throw new AuthenticationServiceException("cannot add user");
        }
        return user;
    }

}
