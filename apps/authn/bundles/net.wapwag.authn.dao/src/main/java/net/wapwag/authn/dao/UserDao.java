package net.wapwag.authn.dao;

import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.Image;
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
	
	/**
	 * An action that consists of several steps to be performed
	 * within a single transaction
	 * 
	 * @author Alexander Lukichev
	 *
	 * @param <E> an exception class that can be thrown 
	 */
	@FunctionalInterface
	static interface ComplexAction<E extends Exception> {
		
		void apply() throws E;
		
	}
	
	/**
	 * Execute a complex action within single a transaction. Throws a checked
	 * exception
	 * 
	 * @param action the action to execute
	 * @param exClass the checked exception class
	 * @throws E
	 */
	<E extends Exception> void tx(ComplexAction<E> action, Class<E> exClass) throws E;
	
	/**
	 *
	 * An action that consists of several steps to be performed
	 * within a single transaction and returns some value
     *
	 * @author Alexander Lukichev
	 *
	 * @param <T> the type of the returned value
	 * @param <E> an exception class that can be thrown
	 */
	@FunctionalInterface
	static interface ComplexActionWithResult<T, E extends Exception> {
		
		T apply() throws E;
		
	}
	
	/**
	 * Execute a complex action within single a transaction. Throws a checked
	 * exception or returns a typed value
	 * 
	 * @param action the action to execute
	 * @param exClass the checked exception class
	 * @throws E
	 */
	<T, E extends Exception> T txExpr(ComplexActionWithResult<T, E> action, Class<E> exClass) throws E;

	int saveImg(Image image) throws UserDaoException;

	int deleteImg(String avartarId) throws UserDaoException;

	Image getAvatar(String avartarId) throws UserDaoException;

}