package net.wapwag.wemp;

import net.wapwag.wemp.dao.WaterEquipmentDao;
import net.wapwag.wemp.dao.WaterEquipmentDaoException;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.geo.Area;
import net.wapwag.wemp.dao.model.geo.Country;
import net.wapwag.wemp.dao.model.permission.User;
import net.wapwag.wemp.dao.model.project.Project;
import net.wapwag.wemp.dao.model.project.PumpEquipment;
import net.wapwag.wemp.dao.model.project.PumpRoom;
import net.wapwag.wemp.model.AccessToken;
import net.wapwag.wemp.model.CountryView;
import net.wapwag.wemp.model.UserView;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.List;
import java.util.stream.Collectors;

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
    public ObjectData getObject(long objId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return waterEquipmentDao.getObjectData(objId);
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public List<UserView> getUsersByObject(long objId, String actionId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                List<User> userList = waterEquipmentDao.getUsersByObject(objId, actionId);
                return userList.stream().map(UserView::new).collect(Collectors.toList());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public int saveCountry(Country country) throws WaterEquipmentServiceException {
        try {
            return waterEquipmentDao.saveCountry(country);
        } catch (WaterEquipmentDaoException e) {
            throw new WaterEquipmentServiceException("can't add country");
        }
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
	public Area getArea(Area area) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
				return waterEquipmentDao.getArea(area);
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
	}

    @Override
    public CountryView getCountry(Country country) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                Country result = waterEquipmentDao.getCountry(country);
                CountryView countryView = new CountryView();
                countryView.setId(result.getId() + "");
                countryView.setName(result.getName());
                return countryView;
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public int saveProject(Project project) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return waterEquipmentDao.saveProject(project);
            } catch (WaterEquipmentDaoException e) {
                return 0;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public int removeProject(Project project) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return waterEquipmentDao.removeProject(project.getId());
            } catch (WaterEquipmentDaoException e) {
                return 0;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public int updateProject(Project project) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return waterEquipmentDao.saveProject(project);
            } catch (WaterEquipmentDaoException e) {
                return 0;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public Project getProject(Project project) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return waterEquipmentDao.getProject(project);
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public int savePumpRoom(PumpRoom pumpRoom) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return waterEquipmentDao.savePumpRoom(pumpRoom);
            } catch (WaterEquipmentDaoException e) {
                return 0;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public int removePumpRoom(PumpRoom pumpRoom) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return waterEquipmentDao.removePumpRoom(pumpRoom.getId());
            } catch (WaterEquipmentDaoException e) {
                return 0;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public int updatePumpRoom(PumpRoom pumpRoom) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return waterEquipmentDao.savePumpRoom(pumpRoom);
            } catch (WaterEquipmentDaoException e) {
                return 0;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public PumpRoom getPumpRoom(PumpRoom pumpRoom) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return waterEquipmentDao.getPumpRoom(pumpRoom);
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public int savePumpEquipment(PumpEquipment pumpEquipment) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return waterEquipmentDao.savePumpEquipment(pumpEquipment);
            } catch (WaterEquipmentDaoException e) {
                return 0;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public int removePumpEquipment(PumpEquipment pumpEquipment) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return waterEquipmentDao.removePumpEquipment(pumpEquipment.getId());
            } catch (WaterEquipmentDaoException e) {
                return 0;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public int updatePumpEquipment(PumpEquipment pumpEquipment) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return waterEquipmentDao.savePumpEquipment(pumpEquipment);
            } catch (WaterEquipmentDaoException e) {
                return 0;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public PumpEquipment getPumpEquipment(PumpEquipment pumpEquipment) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return waterEquipmentDao.getPumpEquipment(pumpEquipment);
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }
}
