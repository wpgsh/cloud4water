package net.wapwag.wemp;

import com.eaio.uuid.UUID;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.wapwag.wemp.dao.WaterEquipmentDao;
import net.wapwag.wemp.dao.WaterEquipmentDaoException;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.*;
import net.wapwag.wemp.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Component(scope=ServiceScope.SINGLETON)
public class WaterEquipmentServiceImpl implements WaterEquipmentService {

    private static final Logger logger = LoggerFactory.getLogger(WaterEquipmentServiceImpl.class);
    
	@Reference
	private WaterEquipmentDao waterEquipmentDao;

    public void setWaterEquipmentDao(WaterEquipmentDao waterEquipmentDao) {
        this.waterEquipmentDao = waterEquipmentDao;
    }

	@Override
	public boolean isAuthorized(String userName, String action, String objId) throws WaterEquipmentServiceException {
        long userId = Long.valueOf(userName);
        long targetId = Long.valueOf(objId);

        return waterEquipmentDao.txExpr(() -> {
            try {
                return waterEquipmentDao.isAuthorized(userId, action, targetId);
            } catch (WaterEquipmentDaoException e) {
                return false;
            }
        }, WaterEquipmentServiceException.class);
    }

	@Override
	public AccessTokenMapper lookupToken(String handle) throws WaterEquipmentServiceException {
		return waterEquipmentDao.txExpr(() -> {
            try {
                String token = new String(Base64.getDecoder().decode(handle));

                AccessToken accessToken = waterEquipmentDao.lookupAccessToken(token);

                if (accessToken != null) {
                    AccessTokenId accessTokenId = accessToken.getAccessTokenId();
                    return new AccessTokenMapper(
                            Long.toString(accessTokenId.getUser().getId()),
                            accessToken.getExpiration(),
                            accessTokenId.getRegisteredClient().getClientId(),
                            accessToken.getHandle(),
                            ImmutableSet.copyOf(
                                    Optional.fromNullable(accessToken.getScope()).
                                            transform(String::trim).
                                            transform(s -> {
                                                assert s != null;
                                                return s.split(" ");
                                            }).
                                            or(new String[0])));
                } else {
                    return  null;
                }
            } catch (WaterEquipmentDaoException e) {
                return null;
            } catch (IllegalArgumentException e) {
                throw new WaterEquipmentServiceException("Illegal handle character");
            }
        }, WaterEquipmentServiceException.class);
	}

    /**
     * Get authorization code if the user has not been authorization
     *
     * @param userId      The user ID who has logined
     * @param cliendId    The client id registered in the resource server
     * @param redirectURI The URL in your application where users will be sent after authorization.
     * @param scope       For users who have authorized scopes for the application.
     * @return Return authorization code.
     */
    @SuppressWarnings("Duplicates")
    @Override
    public String getAuthorizationCode(long userId, String cliendId, String redirectURI, Set<String> scope) throws OAuthProblemException {
        return waterEquipmentDao.txExpr(() -> {
            long result;
            boolean valid = false;
            Set<String> defaultScope = Sets.newHashSet();
            AccessToken accessToken;

            try {

                RegisteredClient registeredClient = waterEquipmentDao.getClientByRedirectURI(redirectURI);

                //validate client.
                if (registeredClient != null
                        && StringUtils.isNotBlank(cliendId)
                        && cliendId.equals(registeredClient.getClientId())) {

                    String code = StringUtils.replace(new UUID().toString(), "-", "");

                    //find accessToken by clientId and userId.
                    accessToken = waterEquipmentDao.getAccessTokenByUserIdAndClientId(userId, registeredClient.getId());

                    if ("wapwag".equals(registeredClient.getClientVendor())) {
                        //implicit scope
                        defaultScope.add("user:*");
                    } else if (scope == null || scope.size() == 0) {
                        throw OAuthProblemException.error(OAuthError.CodeResponse.INVALID_SCOPE,
                                "requested scope is invalid");
                    } else {
                        defaultScope = scope;
                        if (accessToken != null) {
                            Set<String> originalScope = new HashSet<>(Arrays.asList(accessToken.getScope().split(" ")));
                            //if no new scope need
                            if (originalScope.containsAll(defaultScope)) {
                                defaultScope = originalScope;
                                valid = true;
                            }
                        }
                    }

                    //if accessToken isn't exist or exist but need new scope,refresh accessToken
                    if (!valid) {
                        accessToken = new AccessToken();
                        accessToken.setAccessTokenId(new AccessTokenId(waterEquipmentDao.getUser(userId), registeredClient));
                        accessToken.setHandle(StringUtils.replace(new UUID().toString(), "-", ""));
                    }

                    accessToken.setScope(StringUtils.join(defaultScope, " "));

                    //generate authorization code
                    accessToken.setAuthrizationCode(code);
                    accessToken.setExpiration(Long.MAX_VALUE);

                    //update authorization code
                    result = waterEquipmentDao.saveAccessToken(accessToken);
                } else {
                    throw OAuthProblemException.error(OAuthError.CodeResponse.UNAUTHORIZED_CLIENT,
                            "error client credential");
                }

                if (result > 0) {
                    return accessToken.getAuthrizationCode();
                } else {
                    throw new WaterEquipmentDaoException("Can't save authorization code");
                }
            } catch (WaterEquipmentDaoException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
                throw OAuthProblemException.error(OAuthError.CodeResponse.UNAUTHORIZED_CLIENT,
                        "error client credential");
            }
        }, OAuthProblemException.class);
    }

    /**
     * Get access token if the user has been authorization
     *
     * @param clientId     The client id registered in the resource server
     * @param clientSecret The client secret you received from authorization system.
     * @param code         The code you received from authorize response.
     * @param redirectURI  The URL in your application where users will be sent after
     *                     authorization.
     * @return Return accessToken.
     */
    @SuppressWarnings("Duplicates")
    @Override
    public String getAccessToken(String clientId, String clientSecret, String code, String redirectURI) throws OAuthProblemException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                RegisteredClient registeredClient = waterEquipmentDao.getClientByRedirectURI(redirectURI);

                if (registeredClient == null) {
                    throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_CLIENT, "unknow client");
                }

                //valid clientSecret and clientId.
                if (StringUtils.isNotBlank(clientId)
                        && StringUtils.isNotBlank(clientSecret)
                        && clientId.equals(registeredClient.getClientId())
                        && clientSecret.equals(registeredClient.getClientSecret())) {

                    AccessToken accessToken;
                    accessToken = waterEquipmentDao.getAccessTokenByCode(code);

                    //validate authorization code and if match then invalidate it.
                    if (accessToken != null
                            && accessToken.getExpiration() > 0) {
                        accessToken.setExpiration(0L);
                        waterEquipmentDao.saveAccessToken(accessToken);
                        return accessToken.getHandle();
                    } else {
                        throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_GRANT,
                                "invalid or expired authorization code");
                    }
                } else {
                    throw OAuthProblemException.error(OAuthError.TokenResponse.INVALID_CLIENT,
                            "error client credential");
                }
            } catch (WaterEquipmentDaoException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
                throw OAuthProblemException.error(OAuthError.CodeResponse.SERVER_ERROR,
                        "authorization server encountered an unexpected exception");
            }
        }, OAuthProblemException.class);

    }

    @Override
    public ObjectView getObject(long objId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ObjectView.newInstance(waterEquipmentDao.getObjectData(objId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public List<UserView> getUsersByObject(long objId, String action) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                List<User> userList = waterEquipmentDao.getUsersByObject(objId, action);
                return userList.stream().map(UserView::newInstance).collect(Collectors.toList());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ObjectView getObjectByUser(long objId, long userId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ObjectView.newInstance(waterEquipmentDao.getObjectByUser(objId, userId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView addObjectByUser(long objId, long userId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.addObjectByUser(objId, userId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView removeObjectByUser(long objId, long userId, String action) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.removeObjectByUser(objId, userId, action));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public List<GroupView> getGroupsByObject(long objId, String action) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                List<Group> groupList = waterEquipmentDao.getGroupsByObject(objId, action);
                return groupList.stream().map(GroupView::newInstance).collect(Collectors.toList());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ObjectView getObjectByGroup(long objId, long groupId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ObjectView.newInstance(waterEquipmentDao.getObjectByGroup(objId, groupId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView addObjectByGroup(long objId, long groupId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.addObjectByGroup(objId, groupId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView removeObjectByGroup(long objId, long groupId, String action) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.removeObjectByGroup(objId, groupId, action));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public List<GroupView> getGroupsByOrg(long orgId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                List<Group> groupList = waterEquipmentDao.getGroupsByOrg(orgId);
                return groupList.stream().map(GroupView::newInstance).collect(Collectors.toList());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView addGroupByOrg(long orgId, Group group) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.addGroupByOrg(orgId, group));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public GroupView getGroupByOrg(long orgId, long groupId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return GroupView.newInstance(waterEquipmentDao.getGroupByOrg(orgId, groupId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView updateGroupByOrg(long orgId, long groupId, Group group) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.updateGroupByOrg(orgId, groupId, group));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView removeGroupByOrg(long orgId, long groupId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.removeGroupByOrg(orgId, groupId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public List<UserView> getUsersByGroup(long orgId, long groupId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                List<User> userList = waterEquipmentDao.getUsersByGroup(orgId, groupId);
                return userList.stream().map(UserView::newInstance).collect(Collectors.toList());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView addUserByGroup(long orgId, long groupId, long userId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.addUserByGroup(orgId, groupId, userId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView removeUserByGroup(long orgId, long groupId, long userId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.removeUserByGroup(orgId, groupId, userId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public List<ObjectView> getObjectsByGroup(long orgId, long groupId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                List<ObjectData> objList = waterEquipmentDao.getObjectsByGroup(orgId, groupId);
                return objList.stream().map(ObjectView::newInstance).collect(Collectors.toList());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ObjectView getObjectByGroup(long orgId, long groupId, long objId, String action) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ObjectView.newInstance(waterEquipmentDao.getObjectByGroup(orgId, groupId, objId, action));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public List<UserView> getUsersByOrg(long orgId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                List<User> userList = waterEquipmentDao.getUsersByOrg(orgId);
                return userList.stream().map(UserView::newInstance).collect(Collectors.toList());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView addUserByOrg(long orgId, User user) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.addUserByOrg(orgId, user));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView removeUserByOrg(long orgId, long uid) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.removeUserByOrg(orgId, uid));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public List<ObjectView> getObjectsByOrg(long orgId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                List<ObjectData> objList = waterEquipmentDao.getObjectsByOrg(orgId);
                return objList.stream().map(ObjectView::newInstance).collect(Collectors.toList());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView addObjectByOrg(long orgId, ObjectData objectData) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.addObjectByOrg(orgId, objectData));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView removeObjectByOrg(long orgId, long objId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.removeObjectByOrg(orgId, objId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView checkPermission(long userId, ObjectData objectData) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.checkPermission(userId, objectData));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public Set<ObjectView> getObjectsByUser(long userId, String action) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                Set<ObjectData> objSet = waterEquipmentDao.getObjectsByUser(userId, action);
                return objSet.stream().map(ObjectView::newInstance).collect(Collectors.toSet());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }
}
