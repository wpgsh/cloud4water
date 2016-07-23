package net.wapwag.authn.dao;

import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;

public interface UserDao {

	User getUser(long uid) throws UserDaoException;

	int saveUser(User user) throws UserDaoException;

	int removeUser(long uid) throws UserDaoException;

	User getUserAvatar(long id) throws UserDaoException;

	int saveUserAvatar(User user) throws UserDaoException;

	int removeUserAvatar(long id) throws UserDaoException;

	RegisteredClient getClientByClientId(long clientId) throws UserDaoException;

	RegisteredClient getClientByRedirectURI(String redirectURI) throws UserDaoException;

	AccessToken getAccessToken(AccessToken accessToken) throws UserDaoException;

	AccessToken getAccessTokenByCode(String code) throws UserDaoException;

    AccessToken getAccessTokenByUserIdAndClientId(long userId, long clientId) throws UserDaoException;

	long saveAccessToken(AccessToken accessToken) throws UserDaoException;

	AccessToken lookupAccessToken(String handle) throws UserDaoException;
	
	User getUserByName(String userName)throws UserDaoException;
	
	User getUserByEmail(String email)throws UserDaoException;
	
	User updateUserPwd(User user)throws UserDaoException;

}