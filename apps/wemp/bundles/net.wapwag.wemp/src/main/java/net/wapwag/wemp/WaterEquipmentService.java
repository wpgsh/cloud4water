package net.wapwag.wemp;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.User;
import net.wapwag.wemp.model.*;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Water Equipment Service methods
 * 
 *
 */
public interface WaterEquipmentService {

	/**
	 * Check if the given user has the provided permission for the
	 * specified target.
	 * 
	 * @param userId
	 * @param permission
	 * @param targetId
	 * @return
	 */
	boolean isAuthorized(long userId, String permission, long targetId) throws WaterEquipmentServiceException;

	/**
	 * Lookup access token by a handle
	 * 
	 * @param handle the access token need to be checked
	 * @return return the token that the user has
	 */
	AccessTokenMapper lookupToken(String handle) throws WaterEquipmentServiceException;

	/**
	 * Get authorization code if the user has not been authorized
	 *
	 * @param userId      The user ID who has logined
	 * @param cliendId The client id registered in the resource server
	 * @param redirectURI The URL in your application where users will be sent after authorization.
	 * @param scope       For users who have authorized scopes for the application.
	 * @return Return authorization code.
	 */
	String getAuthorizationCode(long userId, String cliendId, String redirectURI, Set<String> scope) throws OAuthProblemException;

	/**
	 * Get access token if the user has been authorization
	 *
	 * @param clientId     The client id registered in the resource server
	 * @param clientSecret The client secret you received from authorization system.
	 * @param code         The code you received from authorize response.
	 * @param redirectURI  The URL in your application where users will be sent after authorization.
	 * @return Return accessToken.
	 */
	String getAccessToken(String clientId, String clientSecret, String code, String redirectURI) throws OAuthProblemException;

	ObjectView getObject(long objId) throws WaterEquipmentServiceException;

    List<UserView> getUsersByObject(long objId, String action) throws WaterEquipmentServiceException;

	List<String> getUserPermissionByObject(long objId, long userId) throws WaterEquipmentServiceException;

	ResultView addObjectByUser(long objId, long userId, String action) throws WaterEquipmentServiceException;

	String removeObjectByUser(long objId, long userId, String action) throws WaterEquipmentServiceException;

	List<GroupView> getGroupsByObject(long objId, String action) throws WaterEquipmentServiceException;

	ObjectView getObjectByGroup(long objId, long groupId) throws WaterEquipmentServiceException;

	ResultView addObjectByGroup(long objId, long groupId) throws WaterEquipmentServiceException;

	String removeObjectByGroup(long objId, long groupId, String action) throws WaterEquipmentServiceException;

	List<GroupView> getGroupsByOrg(long orgId) throws WaterEquipmentServiceException;

	ResultView addGroupByOrg(long orgId, Group group) throws WaterEquipmentServiceException;

	GroupView getGroupByOrg(long orgId, long groupId) throws WaterEquipmentServiceException;

	ResultView updateGroupByOrg(long orgId, long groupId, Group group) throws WaterEquipmentServiceException;

	String removeGroupByOrg(long orgId, long groupId) throws WaterEquipmentServiceException;

	List<UserView> getUsersByGroup(long orgId, long groupId) throws WaterEquipmentServiceException;

	ResultView addUserByGroup(long orgId, long groupId, User user) throws WaterEquipmentServiceException;

	String removeUserByGroup(long orgId, long groupId, long userId) throws WaterEquipmentServiceException;

	List<ObjectView> getObjectsByGroup(long orgId, long groupId) throws WaterEquipmentServiceException;

	Map<String, Boolean> getObjectByGroup(long orgId, long groupId, long objId, String action) throws WaterEquipmentServiceException;

    List<UserView> getUsersByOrg(long orgId) throws WaterEquipmentServiceException;

    ResultView addUserByOrg(long orgId, User user) throws WaterEquipmentServiceException;

	String removeUserByOrg(long orgId, long uid) throws WaterEquipmentServiceException;

    List<ObjectView> getObjectsByOrg(long orgId) throws WaterEquipmentServiceException;

    ResultView addObjectByOrg(long orgId, ObjectData objectData) throws WaterEquipmentServiceException;

    String removeObjectByOrg(long orgId, long objId) throws WaterEquipmentServiceException;

	Map<String, Boolean> checkPermission(long userId, ObjectData objectData) throws WaterEquipmentServiceException;

    Set<Long> getObjectsByUser(long userId, String action) throws WaterEquipmentServiceException;

	int saveAuthnUser(AuthnUser authnUser) throws WaterEquipmentServiceException;
}
