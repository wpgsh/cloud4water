package net.wapwag.wemp;

import net.wapwag.wemp.dao.WaterEquipmentDao;
import net.wapwag.wemp.dao.WaterEquipmentDaoException;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.User;
import net.wapwag.wemp.model.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component(scope=ServiceScope.SINGLETON)
public class WaterEquipmentServiceImpl implements WaterEquipmentService {

	@Reference
	private WaterEquipmentDao waterEquipmentDao;

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
    public ObjectView getObject(long objId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return new ObjectView(waterEquipmentDao.getObjectData(objId));
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
                return userList.stream().map(UserView::new).collect(Collectors.toList());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ObjectView getObjectByUser(long objId, long userId) {
        return null;
    }

    @Override
    public ResultView addObjectByUser(long objId, long userId) {
        return null;
    }

    @Override
    public ResultView removeObjectByUser(long objId, long userId, String action) {
        return null;
    }

    @Override
    public List<GroupView> getGroupsByObject(long objId, String action) {
        return null;
    }

    @Override
    public ObjectView getObjectByGroup(long objId, long groupId) {
        return null;
    }

    @Override
    public ResultView addObjectByGroup(long objId, long groupId) {
        return null;
    }

    @Override
    public ResultView removeObjectByGroup(long objId, long groupId, String action) {
        return null;
    }

    @Override
    public List<GroupView> getGroupsByOrg(long orgId) {
        return null;
    }

    @Override
    public ResultView addGroupByOrg(long orgId, Group group) {
        return null;
    }

    @Override
    public GroupView getGroupByOrg(long orgId, long groupId) {
        return null;
    }

    @Override
    public ResultView updateGroupByOrg(long orgId, long groupId, Group group) {
        return null;
    }

    @Override
    public ResultView removeGroupByOrg(long orgId, long groupId) {
        return null;
    }

    @Override
    public List<UserView> getUsersByGroup(long orgId, long groupId) {
        return null;
    }

    @Override
    public ResultView addUserByGroup(long orgId, long groupId, long userId) {
        return null;
    }

    @Override
    public ResultView removeUserByGroup(long orgId, long groupId, long userId) {
        return null;
    }

    @Override
    public List<ObjectView> getObjectsByGroup(long orgId, long groupId) {
        return null;
    }

    @Override
    public ObjectView getObjectByGroup(long objId, long groupId, String action) {
        return null;
    }

    @Override
    public List<UserView> getUsersByOrg(long orgId) {
        return null;
    }

    @Override
    public ResultView addUserByOrg(long orgId, User user) {
        return null;
    }

    @Override
    public ObjectData removeUserByOrg(long orgId, long uid) {
        return null;
    }

    @Override
    public List<ObjectView> getObjectsByOrg(long orgId) {
        return null;
    }

    @Override
    public ResultView addObjectByOrg(long orgId, ObjectData objectData) {
        return null;
    }

    @Override
    public ResultView removeObjectByOrg(long orgId, long objId) {
        return null;
    }

    @Override
    public ResultView checkPermission(long userId, ObjectData objectData) {
        return null;
    }

    @Override
    public Set<ObjectView> getObjectsByUser(long userId, String action) {
        return null;
    }
}
