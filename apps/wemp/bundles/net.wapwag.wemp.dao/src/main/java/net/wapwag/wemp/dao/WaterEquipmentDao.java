package net.wapwag.wemp.dao;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.geo.*;
import net.wapwag.wemp.dao.model.permission.User;
import net.wapwag.wemp.dao.model.project.Project;
import net.wapwag.wemp.dao.model.project.PumpEquipment;
import net.wapwag.wemp.dao.model.project.PumpRoom;

import java.util.List;
import java.util.Set;

/**
 * Water Equipment DAO methods
 * 
 *
 */
public interface WaterEquipmentDao {

	ObjectData getObjectData(long objId) throws WaterEquipmentDaoException;

    List<User> getUsersByObject(long objId, String actionId) throws WaterEquipmentDaoException;

	int saveCountry(Country country) throws WaterEquipmentDaoException;

	int saveObjectData(ObjectData ObjectData) throws WaterEquipmentDaoException;

	int removeObjectData(long objectId) throws WaterEquipmentDaoException;

	Country getCountry(Country country) throws WaterEquipmentDaoException;

	Area getArea(Area area) throws WaterEquipmentDaoException;

	Province getProvince(Province province) throws WaterEquipmentDaoException;

	City getCity(City city) throws WaterEquipmentDaoException;

	County getCounty(long countyId) throws WaterEquipmentDaoException;

	int saveProject(Project project) throws WaterEquipmentDaoException;

	int removeProject(long projectId) throws WaterEquipmentDaoException;

    int updateProject(Project project) throws WaterEquipmentDaoException;

    Project getProject(Project project) throws WaterEquipmentDaoException;

    int savePumpRoom(PumpRoom pumpRoom) throws WaterEquipmentDaoException;

	int removePumpRoom(long pumpRoomId) throws WaterEquipmentDaoException;

    int updatePumpRoom(PumpRoom pumpRoom) throws WaterEquipmentDaoException;

    PumpRoom getPumpRoom(PumpRoom pumpRoom) throws WaterEquipmentDaoException;

    int savePumpEquipment(PumpEquipment pumpEquipment) throws WaterEquipmentDaoException;

	int removePumpEquipment(long pumpEquipmentId) throws WaterEquipmentDaoException;

    int updatePumpEquipment(PumpEquipment pumpEquipment) throws WaterEquipmentDaoException;

    PumpEquipment getPumpEquipment(PumpEquipment pumpEquipment) throws WaterEquipmentDaoException;
    
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
