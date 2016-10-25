package net.wapwag.wemp;

import net.wapwag.wemp.dao.model.*;
import net.wapwag.wemp.dao.model.geo.Area;
import net.wapwag.wemp.dao.model.geo.Country;
import net.wapwag.wemp.dao.model.project.Project;
import net.wapwag.wemp.dao.model.project.PumpEquipment;
import net.wapwag.wemp.dao.model.project.PumpRoom;
import net.wapwag.wemp.model.AccessToken;
import net.wapwag.wemp.model.CountryView;

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

    int saveCountry(Country country) throws WaterEquipmentServiceException;

	int saveObject(ObjectData ObjectData) throws WaterEquipmentServiceException;

	int removeObject(ObjectData ObjectData) throws WaterEquipmentServiceException;

	ObjectData getObject(ObjectData ObjectData) throws WaterEquipmentServiceException;

    CountryView getCountry(Country country) throws WaterEquipmentServiceException;

	Area getArea(Area area) throws WaterEquipmentServiceException;

    int saveProject(Project project) throws WaterEquipmentServiceException;

    int removeProject(Project project) throws WaterEquipmentServiceException;

    int updateProject(Project project) throws WaterEquipmentServiceException;

    Project getProject(Project project) throws WaterEquipmentServiceException;

    int savePumpRoom(PumpRoom pumpRoom) throws WaterEquipmentServiceException;

    int removePumpRoom(PumpRoom pumpRoom) throws WaterEquipmentServiceException;

    int updatePumpRoom(PumpRoom pumpRoom) throws WaterEquipmentServiceException;

    PumpRoom getPumpRoom(PumpRoom pumpRoom) throws WaterEquipmentServiceException;

    int savePumpEquipment(PumpEquipment pumpEquipment) throws WaterEquipmentServiceException;

    int removePumpEquipment(PumpEquipment pumpEquipment) throws WaterEquipmentServiceException;

    int updatePumpEquipment(PumpEquipment pumpEquipment) throws WaterEquipmentServiceException;

    PumpEquipment getPumpEquipment(PumpEquipment pumpEquipment) throws WaterEquipmentServiceException;

}
