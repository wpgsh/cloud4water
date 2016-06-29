package net.wapwag.authn;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import net.wapwag.authn.Ids.UserId;
import net.wapwag.authn.dao.UserDao;
import net.wapwag.authn.dao.UserDaoException;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.model.AccessToken;
import net.wapwag.authn.model.UserProfile;

@Component
public class AuthenticationServiceImpl implements AuthenticationService {
	
	@Reference
	protected UserDao userDao;

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
		if (accessToken != null) {
			return new AccessToken(
					accessToken.getUser().getId(), 
					accessToken.getExpiration(), 
					accessToken.getClientId(), 
					accessToken.getHandle());
		} else {
			return null;
		}
	}

}
