package net.wapwag.authn;

import java.util.Set;
import java.util.UUID;

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

@Component
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final Logger logger = LoggerFactory
			.getLogger(AuthenticationServiceImpl.class);

	@Reference
	private UserDao userDao;

	@Override
	public UserProfile getUserProfile(UserId uid)
			throws AuthenticationServiceException {
		User user;
		try {
			user = userDao.getUser(uid.id);
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException(
					"Cannot get public user profile", e);
		}

		if (user != null) {
			return new UserProfile(user.getId());
		} else {
			return null;
		}
	}

	@Override
	public AccessToken lookupToken(String handle)
			throws AuthenticationServiceException {
		net.wapwag.authn.dao.model.AccessToken accessToken;
		try {
			logger.info(handle);
			accessToken = userDao.lookupAccessToken(handle);
			logger.info(accessToken.toString());
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException("Cannot get access token",
					e);
		}
		return new AccessToken(accessToken.getUser().getId(),
				accessToken.getExpiration(), accessToken.getRegisteredClient()
						.getClientId(), accessToken.getHandle());
	}

	@Override
	public String getAccessToken(String clientId, String clientSecret,
			String code, String redirectURI)
			throws AuthenticationServiceException {
		RegisteredClient registeredClient = null;
		net.wapwag.authn.dao.model.AccessToken accessToken = null;
		try {
			registeredClient = userDao.getRegisteredClient(clientId);

			// valid clientSecret.
			if (clientSecret != null
					&& clientSecret.equals(registeredClient.getClientSecret())) {
				accessToken = new net.wapwag.authn.dao.model.AccessToken();
				accessToken.setUser(userDao.getUser(1));
				accessToken.setRegisteredClient(registeredClient);
				accessToken = userDao.getAccessToken(accessToken);
				// valid authorization code.
				if (code.equals(accessToken.getAuthrizationCode())) {
					return accessToken.getHandle();
				}
			}
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException("Cannot get access token",
					e);
		}
		return null;
	}

	@Override
	public String getAuthorizationCode(String clientId, String redirectURI,
			Set<String> scope) throws AuthenticationServiceException {
		RegisteredClient registeredClient = null;
		net.wapwag.authn.dao.model.AccessToken accessToken = null;
		long result = 0;
		try {
			registeredClient = userDao.getRegisteredClient(clientId);
			if (registeredClient != null) {
				accessToken = new net.wapwag.authn.dao.model.AccessToken();
				accessToken.setUser(userDao.getUser(1));
				accessToken.setRegisteredClient(registeredClient);
				accessToken = userDao.getAccessToken(accessToken); // find
																	// accessToken
																	// by
																	// clientId
																	// and
																	// userId
				if (accessToken != null) {
					// generate authorization code
					accessToken.setAuthrizationCode(UUID.randomUUID()
							.toString().replaceAll("-", ""));
					accessToken.setExpiration(9223372036854775807L);
				}
				// update authorization code
				result = userDao.saveAccessToken(accessToken);
			}
			logger.info(result + "");
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException(
					"Cannot get register client", e);
		}

		if (result > 0) {
			return accessToken.getAuthrizationCode();
		}
		return null;
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
	public User saveUser(User user) throws AuthenticationServiceException {
		try {
			user = userDao.saveUser(user);
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException("cannot add user");
		}
		return user;
	}

	@Override
	public User removeUser(long uid) throws AuthenticationServiceException {
		User user;
		try {
			user = userDao.removeUser(uid);
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException("cannot remove user");
		}
		return null;
	}

	@Override
	public User updateUser(long uid) throws AuthenticationServiceException {
		User user;
		try {
			user = userDao.updateUser(uid);
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException("cannot add user");
		}
		return user;
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
	public User saveUserAvatar(User user) throws AuthenticationServiceException {
		try {
			user = userDao.saveUserAvatar(user);
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException("cannot add user");
		}
		return user;
	}

	@Override
	public User removeUserAvatar(long uid)
			throws AuthenticationServiceException {
		User user;
		try {
			user = userDao.removeUserAvatar(uid);
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException("cannot remove user");
		}
		return null;
	}

	@Override
	public User updateUserAvatar(long uid)
			throws AuthenticationServiceException {
		User user;
		try {
			user = userDao.updateUserAvatar(uid);
		} catch (UserDaoException e) {
			throw new AuthenticationServiceException("cannot add user");
		}
		return user;
	}

}
