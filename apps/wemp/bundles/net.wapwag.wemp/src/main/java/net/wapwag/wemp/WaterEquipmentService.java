package net.wapwag.wemp;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.User;
import net.wapwag.wemp.model.*;

import java.util.List;
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
	 * @param user
	 * @param permission
	 * @param target
	 * @return
	 */
	boolean isAuthorized(String user, String permission, String target) throws WaterEquipmentServiceException;

	/**
	 * Lookup access token by a handle
	 * 
	 * @param handle
	 * @return
	 */
	AccessToken lookupToken(String handle) throws WaterEquipmentServiceException;

	ObjectView getObject(long objId) throws WaterEquipmentServiceException;

    List<UserView> getUsersByObject(long objId, String action) throws WaterEquipmentServiceException;

	ObjectView getObjectByUser(long objId, long userId) throws WaterEquipmentServiceException;

	ResultView addObjectByUser(long objId, long userId) throws WaterEquipmentServiceException;

	ResultView removeObjectByUser(long objId, long userId, String action) throws WaterEquipmentServiceException;

	List<GroupView> getGroupsByObject(long objId, String action) throws WaterEquipmentServiceException;

	ObjectView getObjectByGroup(long objId, long groupId) throws WaterEquipmentServiceException;

	ResultView addObjectByGroup(long objId, long groupId) throws WaterEquipmentServiceException;

	ResultView removeObjectByGroup(long objId, long groupId, String action) throws WaterEquipmentServiceException;

	List<GroupView> getGroupsByOrg(long orgId) throws WaterEquipmentServiceException;

	ResultView addGroupByOrg(long orgId, Group group) throws WaterEquipmentServiceException;

	GroupView getGroupByOrg(long orgId, long groupId) throws WaterEquipmentServiceException;

	ResultView updateGroupByOrg(long orgId, long groupId, Group group) throws WaterEquipmentServiceException;

	ResultView removeGroupByOrg(long orgId, long groupId) throws WaterEquipmentServiceException;

	List<UserView> getUsersByGroup(long orgId, long groupId) throws WaterEquipmentServiceException;

	ResultView addUserByGroup(long orgId, long groupId, long userId) throws WaterEquipmentServiceException;

	ResultView removeUserByGroup(long orgId, long groupId, long userId) throws WaterEquipmentServiceException;

	List<ObjectView> getObjectsByGroup(long orgId, long groupId) throws WaterEquipmentServiceException;

	ObjectView getObjectByGroup(long orgId, long groupId, long objId, String action) throws WaterEquipmentServiceException;

    List<UserView> getUsersByOrg(long orgId) throws WaterEquipmentServiceException;

    ResultView addUserByOrg(long orgId, User user) throws WaterEquipmentServiceException;

	ResultView removeUserByOrg(long orgId, long uid) throws WaterEquipmentServiceException;

    List<ObjectView> getObjectsByOrg(long orgId) throws WaterEquipmentServiceException;

    ResultView addObjectByOrg(long orgId, ObjectData objectData) throws WaterEquipmentServiceException;

    ResultView removeObjectByOrg(long orgId, long objId) throws WaterEquipmentServiceException;

    ResultView checkPermission(long userId, ObjectData objectData) throws WaterEquipmentServiceException;

    Set<ObjectView> getObjectsByUser(long userId, String action) throws WaterEquipmentServiceException;
}
