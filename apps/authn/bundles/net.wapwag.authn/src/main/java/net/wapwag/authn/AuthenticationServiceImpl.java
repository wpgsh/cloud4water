package net.wapwag.authn;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import net.wapwag.authn.Ids.UserId;
import net.wapwag.authn.dao.UserDao;
import net.wapwag.authn.dao.UserDaoException;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.model.AccessToken;
import net.wapwag.authn.model.UserProfile;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@Component
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	@Reference
	private UserDao userDao;

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
            logger.info(handle);
			accessToken = userDao.lookupAccessToken(handle);
            logger.info(accessToken.toString());
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException("Cannot get access token", e);
		}
        return new AccessToken(
                accessToken.getUser().getId(),
                accessToken.getExpiration(),
                accessToken.getRegisteredClient().getClientId(),
                accessToken.getHandle(),
                ImmutableSet.copyOf(
                		Optional.fromNullable(accessToken.getScope()).
                		transform(String::trim).
                		transform(s -> s.split(" ")).
                		or(new String[0])));
	}

	@Override
	public String getAccessToken(long userId, long clientId, String clientSecret, String code, String redirectURI)
			throws AuthenticationServiceException {
        try {
            RegisteredClient registeredClient = userDao.getClientByClientId(clientId);

            //valid clientSecret.
            if (StringUtils.isNotBlank(clientSecret) && clientSecret.equals(registeredClient.getClientSecret())) {
                net.wapwag.authn.dao.model.AccessToken accessToken = new net.wapwag.authn.dao.model.AccessToken();
                accessToken.setUser(userDao.getUser(userId));
                accessToken.setRegisteredClient(registeredClient);
                accessToken = userDao.getAccessToken(accessToken);

                if (accessToken.getExpiration() <= 0) {
                    throw new UserDaoException("Can't get access token with expired authorize code.");
                }

                //valid authorization code.
                if (StringUtils.isNotBlank(code) && code.equals(accessToken.getAuthrizationCode())) {
                    accessToken.setExpiration(0L);
                    userDao.saveAccessToken(accessToken);
                    return accessToken.getHandle();
                }
            }

            return null;
        } catch (UserDaoException e) {
            throw new AuthenticationServiceException("Cannot get access token", e);
        }
	}

	@Override
	public String getAuthorizationCode(long userId, long clientId, String redirectURI, Set<String> scope)
            throws AuthenticationServiceException {
        net.wapwag.authn.dao.model.AccessToken accessToken = null;
        long result = 0;
        try {
            RegisteredClient registeredClient = userDao.getClientByClientId(clientId);
            if (registeredClient != null) {
                accessToken = new net.wapwag.authn.dao.model.AccessToken();
                accessToken.setUser(userDao.getUser(userId));
                accessToken.setRegisteredClient(registeredClient);

				OAuthIssuer oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
                String code = oAuthIssuer.authorizationCode();

                //find accessToken by clientId and userId
                if (userDao.getAccessToken(accessToken) != null) {
                    accessToken = userDao.getAccessToken(accessToken);
                    //generate authorization code
                    accessToken.setAuthrizationCode(code);
                    accessToken.setExpiration(9223372036854775807L);
                } else {
                    accessToken.setHandle(oAuthIssuer.accessToken());
                    accessToken.setAuthrizationCode(code);
                    accessToken.setExpiration(9223372036854775807L);
                }
                //update authorization code
                result = userDao.saveAccessToken(accessToken);
            }

            if (result > 0) {
                return accessToken.getAuthrizationCode();
            } else {
                throw new UserDaoException("Can't save authorization code");
            }
        } catch (UserDaoException e) {
            throw new AuthenticationServiceException("Cannot get register client", e);
        } catch (OAuthSystemException e) {
            throw new AuthenticationServiceException("Cannot get authorization code", e);
        }
    }

	/**
	 * Check the client is a valid wpg client.
	 * <b>NOTE:</b>some client frameworks don't supply client_id and reply on redirect_uri
	 * as the key,so use redirect_uri as a client identifier.
	 *
	 * @param redirectURI client identifier.
	 * @return return the registered client model.
	 * @throws AuthenticationServiceException
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
			return new User();
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
			System.out.println("in service");
			user = userDao.getUserByName(userName);
        } catch (UserDaoException e) {
            throw new AuthenticationServiceException("cannot add user");
        }
        return user;
    }
}
