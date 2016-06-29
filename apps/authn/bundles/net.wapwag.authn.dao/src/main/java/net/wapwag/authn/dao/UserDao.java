package net.wapwag.authn.dao;

import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.User;

public interface UserDao {

	User getUser(long uid) throws UserDaoException;
	
	AccessToken lookupAccessToken(String handle) throws UserDaoException;

}