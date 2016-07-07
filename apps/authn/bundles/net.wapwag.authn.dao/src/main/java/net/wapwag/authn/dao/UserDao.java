package net.wapwag.authn.dao;

import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;

public interface UserDao {

	User getUser(long uid) throws UserDaoException;

	RegisteredClient getRegisteredClient(String clientId) throws UserDaoException;

	AccessToken getAccessToken(AccessToken accessToken) throws UserDaoException;

    long saveAccessToken(AccessToken accessToken) throws UserDaoException;
	
	AccessToken lookupAccessToken(String handle) throws UserDaoException;

}