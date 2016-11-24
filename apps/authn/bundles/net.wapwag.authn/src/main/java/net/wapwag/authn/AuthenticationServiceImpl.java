package net.wapwag.authn;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eaio.uuid.UUID;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.wapwag.authn.Ids.UserId;
import net.wapwag.authn.dao.UserDao;
import net.wapwag.authn.dao.UserDaoException;
import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.AccessTokenId;
import net.wapwag.authn.dao.model.Image;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.model.AccessTokenMapper;
import net.wapwag.authn.model.ImageResponse;
import net.wapwag.authn.model.StringUtil;
import net.wapwag.authn.model.UserMsgResponse;
import net.wapwag.authn.model.UserProfile;
import net.wapwag.authn.model.UserView;

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
		return userDao.txExpr(() -> {
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
		}, AuthenticationServiceException.class);
	}

	@Override
	public AccessTokenMapper lookupToken(String handle) throws AuthenticationServiceException {
		return userDao.txExpr(() -> {
			AccessToken accessToken;
			try {
				accessToken = userDao.lookupAccessToken(handle);
			
				if (accessToken != null) {
			        AccessTokenId accessTokenId = accessToken.getAccessTokenId();
			        return new AccessTokenMapper(
			        		Long.toString(accessTokenId.getUser().getId()),
			                Long.MAX_VALUE,
			                accessTokenId.getRegisteredClient().getClientId(),
			                accessToken.getHandle(),
			                ImmutableSet.copyOf(
									Optional.ofNullable(accessToken.getScope())
											.map(String::trim)
											.map(s -> s.split(" "))
											.orElse(new String[0])));
				}else{
					return null;
				}
			} catch (UserDaoException e) {
				throw new AuthenticationServiceException("Cannot get access token", e);
			}
		}, AuthenticationServiceException.class);
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
                        && cliendId.equals(registeredClient.getClientId())
                        && clientSecret.equals(registeredClient.getClientSecret())) {

	                AccessToken accessToken;
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
	            throw OAuthProblemException.error(OAuthError.CodeResponse.SERVER_ERROR,
                        "authorization server encountered an unexpected exception");
	        }
		}, OAuthProblemException.class);
	}

	@SuppressWarnings("Duplicates")
	@Override
	public String getAuthorizationCode(long userId, String cliendId, String redirectURI, final Set<String> scope)
            throws OAuthProblemException {
        return userDao.txExpr(() -> {
            long result;
            boolean valid = false;
            Set<String> defaultScope = Sets.newHashSet();
            AccessToken accessToken;

            try {

                RegisteredClient registeredClient = userDao.getClientByRedirectURI(redirectURI);
                User user = userDao.getUser(userId);

                if (user == null) {
                    throw OAuthProblemException.error(OAuthError.CodeResponse.INVALID_REQUEST, "invalid user");
                } else if (!user.getEnabled()) {
                    throw OAuthProblemException.error(OAuthError.CodeResponse.INVALID_REQUEST, "user is disabled now");
                }

                //validate client.
                if (registeredClient != null
                        && StringUtils.isNotBlank(cliendId)
                        && cliendId.equals(registeredClient.getClientId())) {

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
                    }

                    if (accessToken != null) {
                        Set<String> originalScope = new HashSet<>(Arrays.asList(accessToken.getScope().split(" ")));
                        //if no new scope need
                        if (originalScope.containsAll(defaultScope)) {
							valid = true;
                        }
                    } else {
                        accessToken = new AccessToken();
                    }

                    //if accessToken isn't exist or exist but need new scope,refresh accessToken
                    if (!valid) {
                        accessToken.setAccessTokenId(new AccessTokenId(user, registeredClient));
                        accessToken.setHandle(StringUtils.replace(new UUID().toString(), "-", ""));
                    }

                    accessToken.setScope(StringUtils.join(scope, " "));

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
                throw OAuthProblemException.error(OAuthError.CodeResponse.UNAUTHORIZED_CLIENT,
                        "error client credential");
            }
        }, OAuthProblemException.class);
    }

	@Override
	public UserView getUserInfo(final String token) throws OAuthProblemException {
		return userDao.txExpr(() -> {
            try {
                if (StringUtils.isNotBlank(token)) {
                    return UserView.newInstance(userDao.getUserByAccessToken(token));
                } else {
                    return null;
                }
            } catch (UserDaoException e) {
                throw OAuthProblemException.error(OAuthError.CodeResponse.UNAUTHORIZED_CLIENT, "error client credential");
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
		return userDao.txExpr(() -> {
			try {
	            return StringUtils.isNotBlank(redirectURI) ? userDao.getClientByRedirectURI(redirectURI) : null;
	        } catch (UserDaoException e) {
	            throw new AuthenticationServiceException("Cannot get register client by redirect_uri", e);
	        }
		},AuthenticationServiceException.class);
	}

	@Override
	public User getUser(long uid) throws AuthenticationServiceException {
		return userDao.txExpr(() -> {
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
		},AuthenticationServiceException.class);
	}

	@Override
	public int saveUser(User user) throws AuthenticationServiceException {
		return userDao.txExpr(() -> {
			try {
				return userDao.saveUser(user);
			} catch (UserDaoException e) {
				throw new AuthenticationServiceException("cannot add user");
			}
		},AuthenticationServiceException.class);
	}

	@Override
	public int removeUser(long uid) throws AuthenticationServiceException {
		return userDao.txExpr(() -> {
			try {
				return userDao.removeUser(uid);
			} catch (UserDaoException e) {
				throw new AuthenticationServiceException("cannot remove user");
			}
		},AuthenticationServiceException.class);
	}

    @Override
	public User getUserByName(String userName)
			throws AuthenticationServiceException {
    	return userDao.txExpr(() -> {
			User user;
			try {
				user = userDao.getUserByName(userName);
	        } catch (UserDaoException e) {
	            throw new AuthenticationServiceException("cannot get user By Name");
	        }
	        return user;
    	},AuthenticationServiceException.class);
    }
    
    @Override  
	public User getUserByEmail(String email)
			throws AuthenticationServiceException {
    	return userDao.txExpr(() -> {
			User user;
			try {
				user = userDao.getUserByEmail(email);
	        } catch (UserDaoException e) {
	            throw new AuthenticationServiceException("cannot get user By Email");
	        }
	        return user;
    	},AuthenticationServiceException.class);
    }

    @Override
	public User updateUserPwd(User user)
			throws AuthenticationServiceException {
    	return userDao.txExpr(() -> {
	    	User user1;
			try {
				user1 = userDao.updateUserPwd(user);
	        } catch (UserDaoException e) {
	            throw new AuthenticationServiceException("cannot update user pwd");
	        }
	        return user1;
    	},AuthenticationServiceException.class);
    }

	@Override
	public int saveImg(Image image) throws AuthenticationServiceException {
		return userDao.txExpr(() -> {
			try {
				return userDao.saveImg(image);
			} catch (UserDaoException e) {
				throw new AuthenticationServiceException("cannot save Image");
			}
		},AuthenticationServiceException.class);
	}

	@Override
	public int deleteImg(String avartarId) throws AuthenticationServiceException {
		return userDao.txExpr(() -> {
			try {
				return userDao.deleteImg(avartarId);
			} catch (UserDaoException e) {
				throw new AuthenticationServiceException("cannot remove user Avatar");
			}
		},AuthenticationServiceException.class);
	}

	@Override
	public Image getAvatar(String avartarId) throws AuthenticationServiceException {
		return userDao.txExpr(() -> {
			try {
				return userDao.getAvatar(avartarId);
			} catch (UserDaoException e) {
				throw new AuthenticationServiceException("Cannot get user Avatar", e);
			}
		},AuthenticationServiceException.class);
	}

	@Override
	public UserMsgResponse updateUserProfile(User userRequest, long userId) throws AuthenticationServiceException {
		return userDao.txExpr(() -> {
			try{
				User user = userDao.getUser(userId);
		        //Get the userRequest and convert it to User so the service layer could operate it.
		        if(userRequest.getUsername() != null){
		        	user.setUsername(userRequest.getUsername());
		        }
		        if(userRequest.getPasswordHash() != null){
		        	long pwdSalt = System.currentTimeMillis();
		            //save passwordHash,rule -> SHA1(pwd + pwdSalt)
		            user.setPasswordHash(StringUtil.strSHA1(StringUtil.strMd5(userRequest.getPasswordHash()) + pwdSalt));
		            //save pwdSalt
		            user.setPasswordSalt(pwdSalt + "");
		        }
		        user.setEnabled(userRequest.getEnabled());
		        if(userRequest.getAvartarId() != null){
		        	user.setAvartarId(userRequest.getAvartarId());
		        }
		        if(userRequest.getAvatar() != null){
		        	user.setAvatar(userRequest.getAvatar());
		        }
		        if(userRequest.getEmail() != null){
		        	user.setEmail(userRequest.getEmail());
		        }
		        if(userRequest.getHomepage() != null){
		        	user.setHomepage(userRequest.getHomepage());
		        }
		        if(userRequest.getName() != null){
		        	user.setName(userRequest.getName());
		        }
		        if(userRequest.getPhone1() != null){
		        	user.setPhone1(userRequest.getPhone1());
		        }
		        if(userRequest.getPhone2() != null){
		        	user.setPhone2(userRequest.getPhone2());
		        }
		        if(userRequest.getEmail() != null){
		        	user.setEmail(userRequest.getEmail());
		        }

		        int result = userDao.saveUser(user);
		        String msg = (result == 1 ? "update success" : "update fail");
		        return new UserMsgResponse(result == 1, msg);
			}catch(UserDaoException e){
				throw new AuthenticationServiceException("Cannot updateUserProfile", e);
			}
		},AuthenticationServiceException.class);
	}

	@Override
	public UserMsgResponse createNewAvatar(long userId, InputStream inputStream) throws AuthenticationServiceException {
		return userDao.txExpr(() -> {
			try{
				User user = new User();

		    	ByteArrayOutputStream output = new ByteArrayOutputStream();
		        byte[] buf = new byte[1024];
		        int numBytesRead = 0;
		        while ((numBytesRead = inputStream.read(buf)) != -1) {
		            output.write(buf, 0, numBytesRead);
		        }
		        byte[] photo = output.toByteArray();

		    	String avartarId = StringUtil.getUUID();
		    	Image image = new Image();
		    	image.setId(avartarId);
		    	image.setImage(photo);

		        int result = userDao.saveImg(image);

		        if(result > 0){
		        	user = userDao.getUser(userId);
		        	user.setAvartarId(avartarId);
		        	userDao.saveUser(user);
		        }

		        String msg = (result == 1 ? "add success" : "add fail");
		        return new UserMsgResponse(result == 1, msg);

			}catch(Exception e){
				throw new AuthenticationServiceException("Cannot createNewAvatar", e);
			}

		},AuthenticationServiceException.class);
	}

	@Override
	public UserMsgResponse updateUserAvatar(long userId, InputStream inputStream)
			throws AuthenticationServiceException {
		return userDao.txExpr(() -> {
			try{
				User user = new User();

		    	ByteArrayOutputStream output = new ByteArrayOutputStream();
		        byte[] buf = new byte[1024];
		        int numBytesRead = 0;
		        while ((numBytesRead = inputStream.read(buf)) != -1) {
		            output.write(buf, 0, numBytesRead);
		        }
		        byte[] photo = output.toByteArray();

		    	String avartarId = StringUtil.getUUID();
		    	Image image = new Image();
		    	image.setId(avartarId);
		    	image.setImage(photo);

		        int result = userDao.saveImg(image);

		        if(result > 0){
		        	user = userDao.getUser(userId);
		        	user.setAvartarId(avartarId);
		        	userDao.saveUser(user);
		        }

		        String msg = (result == 1 ? "update success" : "update fail");
		        return new UserMsgResponse(result == 1, msg);

			}catch(Exception e){
				throw new AuthenticationServiceException("Cannot updateUserAvatar", e);
			}
		},AuthenticationServiceException.class);
	}

	@Override
	public UserMsgResponse createNewUser(User userRequest) throws AuthenticationServiceException {
		return userDao.txExpr(() -> {
			try{
		        if(userRequest.getPasswordHash() != null){
		            long pwdSalt = System.currentTimeMillis();
		            //save passwordHash,rule -> SHA1(pwd + pwdSalt)
		            userRequest.setPasswordHash(StringUtil.strSHA1(StringUtil.strMd5(userRequest.getPasswordHash()) + pwdSalt));
		            //save pwdSalt
		            userRequest.setPasswordSalt(pwdSalt + "");
		        }
		    	int result = userDao.saveUser(userRequest);
		    	String msg = (result == 1 ? "add success" : "add fail");
		        return new UserMsgResponse(result == 1, msg);
			}catch(Exception e){
				throw new AuthenticationServiceException("Cannot createNewUser", e);
			}
		},AuthenticationServiceException.class);
	}

	@Override
	public UserMsgResponse removeUserAvatar(long userId) throws AuthenticationServiceException {
		return userDao.txExpr(() -> {
			try{
		    	User user = userDao.getUser(userId);

		    	int result = userDao.deleteImg(user.getAvartarId());
		    	if(result > 0){
		    		user.setAvartarId("");
		    		userDao.saveUser(user);
		    	}
		    	String msg = (result == 1 ? "remove success" : "remove fail");
		        return new UserMsgResponse(result == 1, msg);
			}catch(Exception e){
				throw new AuthenticationServiceException("Cannot removeUserAvatar", e);
			}
		},AuthenticationServiceException.class);
	}

	@Override
	public UserMsgResponse removeUserProfile(long userId) throws AuthenticationServiceException {
		return userDao.txExpr(() -> {
			try{
				int result = userDao.removeUser(userId);
		    	String msg = (result == 1 ? "remove success" : "remove fail");
		        return new UserMsgResponse(result == 1, msg);
			}catch(Exception e){
				throw new AuthenticationServiceException("Cannot removeUserProfile", e);
			}
		},AuthenticationServiceException.class);
	}

	@Override
	public ImageResponse getUserAvatar(long userId) throws AuthenticationServiceException {
		return userDao.txExpr(() -> {
			try{
				User user = userDao.getUser(userId);
				if (user == null) {
					throw new AuthenticationServiceException("User not found: " + userId);
				}
				ImageResponse imageResponse = new ImageResponse();
				if(user.getAvartarId() != null){
					Image image = userDao.getAvatar(user.getAvartarId());
					if(image != null){
						imageResponse.setId(image.getId() + "");
						imageResponse.setImage(image.getImage());
					}else{
						throw new AuthenticationServiceException("Image not found: " + userId);
					}
				}
		        return imageResponse;
			}catch(Exception e){
				throw new AuthenticationServiceException("Cannot removeUserProfile", e);
			}
		},AuthenticationServiceException.class);
	}

}
