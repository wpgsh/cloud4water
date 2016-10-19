package net.wapwag.wemp;

import net.wapwag.wemp.dao.model.Area;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectDict;
import net.wapwag.wemp.model.AccessToken;

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
	
	int saveObject(ObjectData ObjectData) throws WaterEquipmentServiceException;

	int removeObject(ObjectData ObjectData) throws WaterEquipmentServiceException;

	ObjectData getObject(ObjectData ObjectData) throws WaterEquipmentServiceException;

	int saveObjectDict(ObjectDict objectDict) throws WaterEquipmentServiceException;

	int removeObjectDict(ObjectDict objectDict) throws WaterEquipmentServiceException;

	ObjectDict getObjectDict(ObjectDict objectDict) throws WaterEquipmentServiceException;

	Area getArea(Area area) throws WaterEquipmentServiceException;

}
