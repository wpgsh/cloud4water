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

	ObjectView getObjectByUser(long objId, long userId);

	ResultView addObjectByUser(long objId, long userId);

	ResultView removeObjectByUser(long objId, long userId, String action);

	List<GroupView> getGroupsByObject(long objId, String action);

	ObjectView getObjectByGroup(long objId, long groupId);

	ResultView addObjectByGroup(long objId, long groupId);

	ResultView removeObjectByGroup(long objId, long groupId, String action);

	List<GroupView> getGroupsByOrg(long orgId);

	ResultView addGroupByOrg(long orgId, Group group);

	GroupView getGroupByOrg(long orgId, long groupId);

	ResultView updateGroupByOrg(long orgId, long groupId, Group group);

	ResultView removeGroupByOrg(long orgId, long groupId);

	List<UserView> getUsersByGroup(long orgId, long groupId);

	ResultView addUserByGroup(long orgId, long groupId, long userId);

	ResultView removeUserByGroup(long orgId, long groupId, long userId);

	List<ObjectView> getObjectsByGroup(long orgId, long groupId);

	ObjectView getObjectByGroup(long objId, long groupId, String action);

    List<UserView> getUsersByOrg(long orgId);

    ResultView addUserByOrg(long orgId, User user);

    ObjectData removeUserByOrg(long orgId, long uid);

    List<ObjectView> getObjectsByOrg(long orgId);

    ResultView addObjectByOrg(long orgId, ObjectData objectData);

    ResultView removeObjectByOrg(long orgId, long objId);

    ResultView checkPermission(long userId, ObjectData objectData);

    Set<ObjectView> getObjectsByUser(long userId, String action);
}
