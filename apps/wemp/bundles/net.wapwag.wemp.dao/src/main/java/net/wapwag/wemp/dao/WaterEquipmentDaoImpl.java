package net.wapwag.wemp.dao;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.link.GroupObject;
import net.wapwag.wemp.dao.model.link.GroupObjectId;
import net.wapwag.wemp.dao.model.link.UserObject;
import net.wapwag.wemp.dao.model.link.UserObjectId;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.User;
import org.osgi.service.component.annotations.*;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component(scope=ServiceScope.SINGLETON)
public class WaterEquipmentDaoImpl implements WaterEquipmentDao {

	@Reference(target = "(osgi.name=waterequipment)")
	private TxAwareEntityManager entityManager;

	@Activate
	protected void init() throws Exception {
		entityManager.init();
	}

	@Deactivate
	protected void destroy() {

	}

	@Override
	public ObjectData getObjectData(long objId) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> em.find(ObjectData.class, objId));
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get object by objId", e);
        }
	}

    @Override
    public List<User> getUsersByObject(long objId, String action) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {

                ObjectData objectData = em.find(ObjectData.class, objId);
                String sql = "select uo.userObjectId.user from UserObject uo " +
                        "where uo.userObjectId.objectData = :obj and uo.actionId = :action";
                return em.createQuery(sql, User.class)
                        .setParameter("obj", objectData)
                        .setParameter("action", action).getResultList();

            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get users by object", e);
        }
    }

	@Override
	public ObjectData getObjectByUser(long objId, long userId) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {

                String hql = "select uo.userObjectId.objectData from UserObject uo " +
                        "where uo.userObjectId.objectData.id = :objId and uo.userObjectId.user.id = :userId";

                return em.createQuery(hql, ObjectData.class)
                        .setParameter("objId", objId)
                        .setParameter("userId", userId).getSingleResult();

            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get object by user", e);
        }
    }

	@Override
	public int addObjectByUser(long objId, long userId) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {

                User user = new User();
                user.setId(userId);

                ObjectData objectData = new ObjectData();
                objectData.setId(objId);

                UserObject userObject = new UserObject();
                userObject.setUserObjectId(new UserObjectId(user, objectData));

                em.persist(userObject);

                return 1;

            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't add object by user", e);
        }
	}

	@Override
	public int removeObjectByUser(long objId, long userId, String action) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {
                String hql = "delete from UserObject userObject where " +
                        "userObject.userObjectId.user.id = :userId " +
                        "and userObject.userObjectId.objectData.id = :objId";
                if (!action.isEmpty()) {
                    hql  += " userObject.actionId = :action";
                }
                
                Query query = em.createQuery(hql).setParameter("objId", objId)
                        .setParameter("userId", userId);

                if (!action.isEmpty()) {
                    query.setParameter("action", action);
                }

                return query.executeUpdate();

            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't remove object by user", e);
        }
	}

	@Override
	public List<Group> getGroupsByObject(long objId, String action) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {
                String hql = "select groupObject.groupObjectId.group from GroupObject groupObject " +
                        "where groupObject.groupObjectId.objectData.id = :id";

                return em.createQuery(hql, Group.class).setParameter("id", objId).getResultList();
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get groups by object", e);
        }
	}

	@Override
	public ObjectData getObjectByGroup(long objId, long groupId) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {
                String hql = "select groupObject.groupObjectId.objectData from GroupObject groupObject " +
                        "where groupObject.groupObjectId.objectData.id = :objId " +
                        "AND groupObject.groupObjectId.group.id = :groupId";

                return em.createQuery(hql, ObjectData.class)
                        .setParameter("objId", objId)
                        .setParameter("groupId", groupId).getSingleResult();
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get object by group", e);
        }
	}

	@Override
	public int addObjectByGroup(long objId, long groupId) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {

                Group group = new Group();
                group.setId(groupId);

                ObjectData objectData = new ObjectData();
                objectData.setId(objId);

                GroupObject groupObject = new GroupObject();
                groupObject.setGroupObjectId(new GroupObjectId(group, objectData));

                em.persist(groupObject);

                return 1;

            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't add object by user", e);
        }
	}

	@Override
	public int removeObjectByGroup(long objId, long groupId, String action) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {

                String hql = "delete from GroupObject groupObject " +
                        "where groupObject.groupObjectId.group.id = :groupId " +
                        "and groupObject.groupObjectId.objectData.id = :objId";

                if (!action.isEmpty()) {
                    hql  += " userObject.actionId = :action";
                }

                Query query = em.createQuery(hql).setParameter("objId", objId)
                        .setParameter("groupId", groupId);

                if (!action.isEmpty()) {
                    query.setParameter("action", action);
                }

                return query.executeUpdate();

            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't remove object by user", e);
        }
	}

	@Override
	public List<Group> getGroupsByOrg(long orgId) throws WaterEquipmentDaoException {
		return null;
	}

	@Override
	public int addGroupByOrg(long orgId, Group group) throws WaterEquipmentDaoException {
		return 0;
	}

	@Override
	public Group getGroupByOrg(long orgId, long groupId) throws WaterEquipmentDaoException {
		return null;
	}

	@Override
	public int updateGroupByOrg(long orgId, long groupId, Group group) throws WaterEquipmentDaoException {
		return 0;
	}

	@Override
	public int removeGroupByOrg(long orgId, long groupId) throws WaterEquipmentDaoException {
		return 0;
	}

	@Override
	public List<User> getUsersByGroup(long orgId, long groupId) throws WaterEquipmentDaoException {
		return null;
	}

	@Override
	public int addUserByGroup(long orgId, long groupId, long userId) throws WaterEquipmentDaoException {
		return 0;
	}

	@Override
	public int removeUserByGroup(long orgId, long groupId, long userId) throws WaterEquipmentDaoException {
		return 0;
	}

	@Override
	public List<ObjectData> getObjectsByGroup(long orgId, long groupId) throws WaterEquipmentDaoException {
		return null;
	}

	@Override
	public ObjectData getObjectByGroup(long objId, long groupId, String action) throws WaterEquipmentDaoException {
		return null;
	}

	@Override
	public List<User> getUsersByOrg(long orgId) throws WaterEquipmentDaoException {
		return null;
	}

	@Override
	public int addUserByOrg(long orgId, User user) throws WaterEquipmentDaoException {
		return 0;
	}

	@Override
	public ObjectData removeUserByOrg(long orgId, long uid) throws WaterEquipmentDaoException {
		return null;
	}

	@Override
	public List<ObjectData> getObjectsByOrg(long orgId) throws WaterEquipmentDaoException {
		return null;
	}

	@Override
	public int addObjectByOrg(long orgId, ObjectData objectData) throws WaterEquipmentDaoException {
		return 0;
	}

	@Override
	public int removeObjectByOrg(long orgId, long objId) throws WaterEquipmentDaoException {
		return 0;
	}

	@Override
	public int checkPermission(long userId, ObjectData objectData) throws WaterEquipmentDaoException {
		return 0;
	}

	@Override
	public Set<ObjectData> getObjectsByUser(long userId, String action) throws WaterEquipmentDaoException {
		return null;
	}

	@Override
	public <E extends Exception> void tx(ComplexAction<E> action, Class<E> exClass) throws E {
		Exception ex;
		try {
			ex = entityManager.txExpr(em -> {
				try {
					action.apply();
					return null;
				} catch (Exception e) {
					return e;
				}
			});
		} catch (Exception e) {
			throw new RuntimeException("Unexpected exception", e);
		}
		
		if (ex != null) {
			if (exClass.isAssignableFrom(ex.getClass())) {
				throw exClass.cast(ex);
			} else {
				throw new RuntimeException("Unexpected exception", ex);
			}
		} 
	}
	
	@Override
	public <T, E extends Exception> T txExpr(ComplexActionWithResult<T, E> action, Class<E> exClass) throws E {		
		final List<T> result = new ArrayList<T>();
		
		Exception ex;
		try {
			ex = entityManager.txExpr(em -> {
					try {
						result.add(action.apply());
						return null;
					} catch (Exception e) {
						return e;
					}
				});
		} catch (Exception e) {
			throw new RuntimeException("Unexpected exception", e);
		}
		
		if (ex != null) {
			if (exClass.isAssignableFrom(ex.getClass())) {
				throw exClass.cast(ex);
			} else {
				throw new RuntimeException("Unexpected exception", ex);
			}
		} 
		
		return result.get(0);		
	}

}
