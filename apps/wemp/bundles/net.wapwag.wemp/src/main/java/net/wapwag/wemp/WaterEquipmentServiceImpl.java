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
                return ObjectView.newInstance(waterEquipmentDao.getObjectData(objId));
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
                return userList.stream().map(UserView::newInstance).collect(Collectors.toList());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ObjectView getObjectByUser(long objId, long userId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ObjectView.newInstance(waterEquipmentDao.getObjectByUser(objId, userId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView addObjectByUser(long objId, long userId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.addObjectByUser(objId, userId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView removeObjectByUser(long objId, long userId, String action) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.removeObjectByUser(objId, userId, action));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public List<GroupView> getGroupsByObject(long objId, String action) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                List<Group> groupList = waterEquipmentDao.getGroupsByObject(objId, action);
                return groupList.stream().map(GroupView::newInstance).collect(Collectors.toList());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ObjectView getObjectByGroup(long objId, long groupId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ObjectView.newInstance(waterEquipmentDao.getObjectByGroup(objId, groupId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView addObjectByGroup(long objId, long groupId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.addObjectByGroup(objId, groupId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView removeObjectByGroup(long objId, long groupId, String action) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.removeObjectByGroup(objId, groupId, action));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public List<GroupView> getGroupsByOrg(long orgId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                List<Group> groupList = waterEquipmentDao.getGroupsByOrg(orgId);
                return groupList.stream().map(GroupView::newInstance).collect(Collectors.toList());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView addGroupByOrg(long orgId, Group group) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.addGroupByOrg(orgId, group));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public GroupView getGroupByOrg(long orgId, long groupId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return GroupView.newInstance(waterEquipmentDao.getGroupByOrg(orgId, groupId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView updateGroupByOrg(long orgId, long groupId, Group group) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.updateGroupByOrg(orgId, groupId, group));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView removeGroupByOrg(long orgId, long groupId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.removeGroupByOrg(orgId, groupId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public List<UserView> getUsersByGroup(long orgId, long groupId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                List<User> userList = waterEquipmentDao.getUsersByGroup(orgId, groupId);
                return userList.stream().map(UserView::newInstance).collect(Collectors.toList());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView addUserByGroup(long orgId, long groupId, long userId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.addUserByGroup(orgId, groupId, userId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView removeUserByGroup(long orgId, long groupId, long userId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.removeUserByGroup(orgId, groupId, userId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public List<ObjectView> getObjectsByGroup(long orgId, long groupId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                List<ObjectData> objList = waterEquipmentDao.getObjectsByGroup(orgId, groupId);
                return objList.stream().map(ObjectView::newInstance).collect(Collectors.toList());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ObjectView getObjectByGroup(long orgId, long groupId, long objId, String action) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ObjectView.newInstance(waterEquipmentDao.getObjectByGroup(orgId, groupId, objId, action));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public List<UserView> getUsersByOrg(long orgId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                List<User> userList = waterEquipmentDao.getUsersByOrg(orgId);
                return userList.stream().map(UserView::newInstance).collect(Collectors.toList());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView addUserByOrg(long orgId, User user) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.addUserByOrg(orgId, user));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView removeUserByOrg(long orgId, long uid) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.removeUserByOrg(orgId, uid));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public List<ObjectView> getObjectsByOrg(long orgId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                List<ObjectData> objList = waterEquipmentDao.getObjectsByOrg(orgId);
                return objList.stream().map(ObjectView::newInstance).collect(Collectors.toList());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView addObjectByOrg(long orgId, ObjectData objectData) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.addObjectByOrg(orgId, objectData));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView removeObjectByOrg(long orgId, long objId) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.removeObjectByOrg(orgId, objId));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public ResultView checkPermission(long userId, ObjectData objectData) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                return ResultView.newInstance(waterEquipmentDao.checkPermission(userId, objectData));
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }

    @Override
    public Set<ObjectView> getObjectsByUser(long userId, String action) throws WaterEquipmentServiceException {
        return waterEquipmentDao.txExpr(() -> {
            try {
                Set<ObjectData> objSet = waterEquipmentDao.getObjectsByUser(userId, action);
                return objSet.stream().map(ObjectView::newInstance).collect(Collectors.toSet());
            } catch (WaterEquipmentDaoException e) {
                return null;
            }
        }, WaterEquipmentServiceException.class);
    }
}
