package net.wapwag.authn;

import net.wapwag.authn.Ids.UserId;
import net.wapwag.authn.dao.UserDao;
import net.wapwag.authn.dao.UserDaoException;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.model.AccessToken;
import net.wapwag.authn.model.UserProfile;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.UUID;

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
                accessToken.getHandle());
	}

	@Override
	public String getAccessToken(long userId, long clientId, String clientSecret, String code, String redirectURI)
			throws AuthenticationServiceException {
        try {
            RegisteredClient registeredClient = userDao.getClientByClientId(clientId);

            //valid clientSecret.
            if (clientSecret != null && clientSecret.equals(registeredClient.getClientSecret())) {
                net.wapwag.authn.dao.model.AccessToken accessToken = new net.wapwag.authn.dao.model.AccessToken();
                accessToken.setUser(userDao.getUser(userId));
                accessToken.setRegisteredClient(registeredClient);
                accessToken = userDao.getAccessToken(accessToken);
                //valid authorization code.
                if (code.equals(accessToken.getAuthrizationCode())) {
                    return accessToken.getHandle();
                }
            }
        } catch (UserDaoException e) {
            throw new AuthenticationServiceException("Cannot get access token", e);
        }
        return null;
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

                //find accessToken by clientId and userId
                if (userDao.getAccessToken(accessToken) != null) {
                    accessToken = userDao.getAccessToken(accessToken);
                    //generate authorization code
                    accessToken.setAuthrizationCode(UUID.randomUUID().toString().replaceAll("-", ""));
                    accessToken.setExpiration(9223372036854775807L);
                } else {
                    accessToken.setHandle(UUID.randomUUID().toString().replaceAll("-", ""));
                    accessToken.setAuthrizationCode(UUID.randomUUID().toString().replaceAll("-", ""));
                    accessToken.setExpiration(9223372036854775807L);
                }
                //update authorization code
                result = userDao.saveAccessToken(accessToken);
            }
            logger.info(result + "");
        } catch (UserDaoException e) {
            throw new AuthenticationServiceException("Cannot get register client", e);
        }

        if (result > 0) {
            return accessToken.getAuthrizationCode();
        }
		return null;
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
        RegisteredClient registeredClient = null;
		try {
            return registeredClient = userDao.getClientByRedirectURI(redirectURI);
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
