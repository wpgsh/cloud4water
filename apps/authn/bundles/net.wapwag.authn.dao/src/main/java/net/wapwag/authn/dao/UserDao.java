package net.wapwag.authn.dao;

import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;

public interface UserDao {

	User getUser(long uid) throws UserDaoException;

	User saveUser(User user) throws UserDaoException;

	User removeUser(long uid) throws UserDaoException;

	User updateUser(long uid) throws UserDaoException;

	User getUserAvatar(long id) throws UserDaoException;

	User saveUserAvatar(User user) throws UserDaoException;

	User removeUserAvatar(long id) throws UserDaoException;

	User updateUserAvatar(long id) throws UserDaoException;

	RegisteredClient getRegisteredClient(String clientId)
			throws UserDaoException;

	AccessToken getAccessToken(AccessToken accessToken) throws UserDaoException;

	long saveAccessToken(AccessToken accessToken) throws UserDaoException;

	AccessToken lookupAccessToken(String handle) throws UserDaoException;

}