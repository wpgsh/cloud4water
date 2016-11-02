package net.wapwag.wemp.dao;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.User;

import java.util.List;
import java.util.Set;

/**
 * Water Equipment DAO methods
 * 
 *
 */
public interface WaterEquipmentDao {

	ObjectData getObjectData(long objId) throws WaterEquipmentDaoException;

    List<User> getUsersByObject(long objId, String action) throws WaterEquipmentDaoException;

	ObjectData getObjectByUser(long objId, long userId) throws WaterEquipmentDaoException;

	int addObjectByUser(long objId, long userId) throws WaterEquipmentDaoException;

	int removeObjectByUser(long objId, long userId, String action) throws WaterEquipmentDaoException;

    List<Group> getGroupsByObject(long objId, String action) throws WaterEquipmentDaoException;

	ObjectData getObjectByGroup(long objId, long groupId) throws WaterEquipmentDaoException;

	int addObjectByGroup(long objId, long groupId) throws WaterEquipmentDaoException;

	int removeObjectByGroup(long objId, long groupId, String action) throws WaterEquipmentDaoException;

	List<Group> getGroupsByOrg(long orgId) throws WaterEquipmentDaoException;

	int addGroupByOrg(long orgId, Group group) throws WaterEquipmentDaoException;

	Group getGroupByOrg(long orgId, long groupId) throws WaterEquipmentDaoException;

	int updateGroupByOrg(long orgId, long groupId, Group group) throws WaterEquipmentDaoException;

	int removeGroupByOrg(long orgId, long groupId) throws WaterEquipmentDaoException;

	List<User> getUsersByGroup(long orgId, long groupId) throws WaterEquipmentDaoException;

	int addUserByGroup(long orgId, long groupId, long userId) throws WaterEquipmentDaoException;

	int removeUserByGroup(long orgId, long groupId, long userId) throws WaterEquipmentDaoException;

	List<ObjectData> getObjectsByGroup(long orgId, long groupId) throws WaterEquipmentDaoException;

	ObjectData getObjectByGroup(long orgId, long groupId, long objId, String action) throws WaterEquipmentDaoException;

	List<User> getUsersByOrg(long orgId) throws WaterEquipmentDaoException;

	int addUserByOrg(long orgId, User user) throws WaterEquipmentDaoException;

	int removeUserByOrg(long orgId, long uid) throws WaterEquipmentDaoException;

	List<ObjectData> getObjectsByOrg(long orgId) throws WaterEquipmentDaoException;

	int addObjectByOrg(long orgId, ObjectData objectData) throws WaterEquipmentDaoException;

	int removeObjectByOrg(long orgId, long objId) throws WaterEquipmentDaoException;

	boolean checkPermission(long userId, ObjectData objectData) throws WaterEquipmentDaoException;

	Set<ObjectData> getObjectsByUser(long userId, String action) throws WaterEquipmentDaoException;

    boolean isAuthorized(long userId, String action, long objectId) throws WaterEquipmentDaoException;

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

	
}
