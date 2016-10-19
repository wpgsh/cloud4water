package net.wapwag.wemp;

import com.google.gson.Gson;
import net.wapwag.wemp.dao.WaterEquipmentDaoException;
import net.wapwag.wemp.dao.model.Area;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectDict;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import net.wapwag.wemp.dao.WaterEquipmentDao;
import net.wapwag.wemp.model.AccessToken;

@Component(scope=ServiceScope.SINGLETON)
public class WaterEquipmentServiceImpl implements WaterEquipmentService {

	@Reference
	WaterEquipmentDao waterEquipmentDao;

	@Override
	public boolean isAuthorized(String user, String permission, String target) {
		// TODO implement...
		throw new RuntimeException("TODO - not implemented");
	}

	@Override
	public AccessToken lookupToken(String handle) throws WaterEquipmentServiceException {
		// TODO implement...
		throw new RuntimeException("TODO - not implemented");
	}

	@Override
	public int saveObject(ObjectData ObjectData) throws WaterEquipmentServiceException {
		try {
			return waterEquipmentDao.saveObjectData(ObjectData);
		} catch (WaterEquipmentDaoException e) {
            throw new WaterEquipmentServiceException("can't add object");
        }
	}

	@Override
	public int removeObject(ObjectData ObjectData) throws WaterEquipmentServiceException {
		return 0;
	}

	@Override
	public ObjectData getObject(ObjectData ObjectData) throws WaterEquipmentServiceException {
		return null;
	}

	@Override
	public int saveObjectDict(ObjectDict objectDict) throws WaterEquipmentServiceException {
		try {
			return waterEquipmentDao.saveObjectDict(objectDict);
		} catch (WaterEquipmentDaoException e) {
			throw new WaterEquipmentServiceException("can't add object dict");
		}
	}

	@Override
	public int removeObjectDict(ObjectDict objectDict) throws WaterEquipmentServiceException {
		try {
			return waterEquipmentDao.removeObjectDict(objectDict.getDictId());
		} catch (WaterEquipmentDaoException e) {
			throw new WaterEquipmentServiceException("can't remove object dict");
		}
	}

	@Override
	public ObjectDict getObjectDict(ObjectDict objectDict) throws WaterEquipmentServiceException {
		return null;
	}

	@Override
	public Area getArea(Area area) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                Area result = waterEquipmentDao.getArea(area);
//                result.setName(new Gson().toJson(result));
                return result;
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
	}


}
