package net.wapwag.wemp.dao;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.link.*;
import net.wapwag.wemp.dao.model.org.Organization;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.User;
import org.osgi.service.component.annotations.*;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashSet;
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
        final String hql = "select g from Group g where g.organization.id = :orgId";
		try {
            return entityManager.txExpr(em -> em.createQuery(hql, Group.class)
                    .setParameter("orgId", orgId).getResultList()
            );
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get groups by org", e);
		}
	}

	@Override
	public int addGroupByOrg(long orgId, Group group) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {
                Organization org = em.find(Organization.class, orgId);
                group.setOrganization(org);
                em.persist(group);

                return 1;
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't add group by org", e);
        }
	}

	@Override
	public Group getGroupByOrg(long orgId, long groupId) throws WaterEquipmentDaoException {
        final String hql = "select g from Group g where g.id = :groupId and g.organization.id = :orgId";
        try {
            return entityManager.txExpr(em -> em.createQuery(hql, Group.class)
                    .setParameter("groupId", groupId)
                    .setParameter("orgId", orgId).getSingleResult()
            );
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get object by group", e);
        }
	}

	@Override
	public int updateGroupByOrg(long orgId, long groupId, Group group) throws WaterEquipmentDaoException {
		try {
            return entityManager.txExpr(em -> {
                Organization org = em.find(Organization.class, orgId);

                group.setId(groupId);
                group.setOrganization(org);

                em.merge(group);
                return 1;
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't update group by org", e);
        }
	}

	@Override
	public int removeGroupByOrg(long orgId, long groupId) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> em.createQuery("delete from Group g where " +
                    "g.id = :groupId and g.organization.id = :orgId")
                    .setParameter("groupId", groupId)
                    .setParameter("orgId", orgId)
                    .executeUpdate());
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't remove group by org", e);
        }
	}

	@Override
	public List<User> getUsersByGroup(long orgId, long groupId) throws WaterEquipmentDaoException {
        final String hql = "select userGroup.userGroupId.user from UserGroup userGroup " +
                "where userGroup.userGroupId.group.id = :groupId " +
                "and userGroup.userGroupId.group.organization.id = :orgId";

        try {
            return entityManager.txExpr(em -> em.createQuery(hql, User.class)
                    .setParameter("orgId", orgId)
                    .setParameter("groupId", groupId)
                    .getResultList());
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get users by group", e);
        }
	}

	@Override
	public int addUserByGroup(long orgId, long groupId, long userId) throws WaterEquipmentDaoException {
        User user = new User();
        user.setId(userId);

        Group group = new Group();
        group.setId(groupId);
        Organization org = new Organization();
        org.setId(orgId);
        group.setOrganization(org);

        try {
            return entityManager.txExpr(em -> {
                UserGroup userGroup = new UserGroup();
                userGroup.setUserGroupId(new UserGroupId(user, group));
                em.persist(userGroup);

                return 1;
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't add user by group", e);
        }
	}

	@Override
	public int removeUserByGroup(long orgId, long groupId, long userId) throws WaterEquipmentDaoException {
        final String hql = "select userGroup from UserGroup userGroup " +
                "where userGroup.userGroupId.group.id = :groupId " +
                "and userGroup.userGroupId.group.organization.id = :orgId " +
                "and userGroup.userGroupId.user.id = :userId";
        try {
            return entityManager.txExpr(em -> {
                UserGroup userGroup = em.createQuery(hql, UserGroup.class)
                        .setParameter("orgId", orgId)
                        .setParameter("groupId", groupId)
                        .setParameter("userId", userId).getSingleResult();

                em.remove(userGroup);

                return 1;
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't remove user by org", e);
        }
	}

	@Override
	public List<ObjectData> getObjectsByGroup(long orgId, long groupId) throws WaterEquipmentDaoException {
        final String hql = "select groupObject.groupObjectId.objectData from GroupObject  groupObject " +
                "where groupObject.groupObjectId.group.id = :groupId " +
                "and groupObject.groupObjectId.group.organization.id = :orgId";

        try {
            return entityManager.txExpr(em -> em.createQuery(hql, ObjectData.class)
                    .setParameter("groupId", groupId)
                    .setParameter("orgId", orgId).getResultList()
            );
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get objects by group", e);
        }
	}

	@Override
	public ObjectData getObjectByGroup(long orgId, long groupId, long objId, String action) throws WaterEquipmentDaoException {
        final String hql = "select groupObject.groupObjectId.objectData from GroupObject  groupObject " +
                "where groupObject.groupObjectId.group.id = :groupId " +
                "and groupObject.groupObjectId.group.organization.id = :orgId " +
                "and groupObject.groupObjectId.objectData.id = :objId " +
                "and groupObject.actionId = :action";
        try {
            return entityManager.txExpr(em -> em.createQuery(hql, ObjectData.class)
                        .setParameter("groupId", groupId)
                        .setParameter("orgId", orgId)
                        .setParameter("objId", objId)
                        .setParameter("action", action).getSingleResult()
            );
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get object by group", e);
        }
	}

	@Override
	public List<User> getUsersByOrg(long orgId) throws WaterEquipmentDaoException {
        final String hql = "select userOrg.userOrgId.user from UserOrg userOrg where userOrg.userOrgId.organization.id = :orgId";

        try {
            return entityManager.txExpr(em -> em.createQuery(hql, User.class)
                    .setParameter("orgId", orgId).getResultList()
            );
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get users by org", e);
        }
	}

	@Override
	public int addUserByOrg(long orgId, User user) throws WaterEquipmentDaoException {
        Organization organization = new Organization();
        organization.setId(orgId);

        UserOrg userOrg = new UserOrg();
        UserOrgId userOrgId = new UserOrgId(user, organization);
        userOrg.setUserOrgId(userOrgId);

        try {
            return entityManager.txExpr(em -> {
                em.persist(user);
                em.persist(userOrg);
                return 1;
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't add user by org", e);
        }
	}

	@Override
	public int removeUserByOrg(long orgId, long uid) throws WaterEquipmentDaoException {
        final String hql = "delete from UserOrg userOrg " +
                "where userOrg.userOrgId.organization.id = :orgId " +
                "and userOrg.userOrgId.user.id = :userId";
        try {
            return entityManager.txExpr(em -> em.createQuery(hql)
                    .setParameter("orgId", orgId)
                    .setParameter("userId", uid).executeUpdate());
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't remove user by org", e);
        }
	}

	@Override
	public List<ObjectData> getObjectsByOrg(long orgId) throws WaterEquipmentDaoException {
        final String hql = "select orgObject.orgObjectId.objectData from OrgObject orgObject " +
                "where orgObject.orgObjectId.organization.id = :orgId";

        try {
            return entityManager.txExpr(em -> em.createQuery(hql, ObjectData.class)
                    .setParameter("orgId", orgId).getResultList());
        }  catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get objects by org", e);
        }
	}

	@Override
	public int addObjectByOrg(long orgId, ObjectData objectData) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {
                Organization organization = new Organization();
                organization.setId(orgId);

                em.persist(objectData);

                OrgObject orgObject = new OrgObject();

                orgObject.setOrgObjectId(new OrgObjectId(organization, objectData));
                em.persist(orgObject);

                return 1;
            });
        }  catch (Exception e) {
            throw new WaterEquipmentDaoException("can't add object by org", e);
        }
	}

	@Override
	public int removeObjectByOrg(long orgId, long objId) throws WaterEquipmentDaoException {
        final String hql = "delete from OrgObject orgObject " +
                "where orgObject.orgObjectId.organization.id = :orgId " +
                "and orgObject.orgObjectId.objectData.id = :objId";
        try {
            return entityManager.txExpr(em -> em.createQuery(hql)
                    .setParameter("orgId", orgId)
                    .setParameter("objId", objId).executeUpdate());
        }  catch (Exception e) {
            throw new WaterEquipmentDaoException("can't remove object by org", e);
        }
	}

	@Override
	public boolean checkPermission(long userId, ObjectData objectData) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {
                long objId = objectData.getId();

                long havePermission;

                String userObjectHql = "select count(userObject) from UserObject userObject " +
                        "where userObject.userObjectId.user.id = :userId " +
                        "and userObject.userObjectId.objectData.id = :objId";
                havePermission = em.createQuery(userObjectHql, Long.class)
                        .setParameter("userId", userId)
                        .setParameter("objId", objId).getSingleResult();

                if (havePermission == 0) {
                    String userGroupHql = "select count(userGroup) from UserGroup userGroup,GroupObject groupObject " +
                            "where userGroup.userGroupId.user.id = :userId " +
                            "and userGroup.userGroupId.group = groupObject.groupObjectId.group " +
                            "and groupObject.groupObjectId.objectData.id = :objId";
                    havePermission = em.createQuery(userGroupHql, Long.class)
                            .setParameter("userId", userId)
                            .setParameter("objId", objId).getSingleResult();

                    if (havePermission == 0) {
                        String userOrgHql = "select count(userOrg) from UserOrg userOrg,GroupObject groupObject " +
                                "where userOrg.userOrgId.user.id = :userId " +
                                "and userOrg.userOrgId.organization = groupObject.groupObjectId.group.organization " +
                                "and groupObject.groupObjectId.objectData.id = :objId";
                        havePermission = em.createQuery(userOrgHql, Long.class)
                                .setParameter("userId", userId)
                                .setParameter("objId", objId).getSingleResult();
                    }
                }
                return havePermission > 0;
            });
        }  catch (Exception e) {
            throw new WaterEquipmentDaoException("can't check permission for the object", e);
        }
	}

	@Override
	public Set<ObjectData> getObjectsByUser(long userId, String action) throws WaterEquipmentDaoException {
        final String userObjectHql = "select userObject.userObjectId.objectData " +
                "from UserObject userObject " +
                "where userObject.userObjectId.user.id = :userId " +
                "and userObject.actionId = :action";

        final String userGroupHql = "select groupObject.groupObjectId.objectData from UserGroup userGroup,GroupObject groupObject " +
                "where userGroup.userGroupId.user.id = :userId " +
                "and userGroup.userGroupId.group = groupObject.groupObjectId.group " +
                "and groupObject.actionId = :action";

        final String userOrgHql = "select groupObject.groupObjectId.objectData from UserOrg userOrg,GroupObject groupObject " +
                "where userOrg.userOrgId.user.id = :userId " +
                "and userOrg.userOrgId.organization = groupObject.groupObjectId.group.organization " +
                "and groupObject.actionId = :action";
		try {
            return entityManager.txExpr(em -> {
                List<ObjectData> objList = new ArrayList<>();

                objList.addAll(em.createQuery(userObjectHql, ObjectData.class)
                        .setParameter("userId", userId)
                        .setParameter("action", action).getResultList());

                objList.addAll(em.createQuery(userGroupHql, ObjectData.class)
                        .setParameter("userId", userId)
                        .setParameter("action", action).getResultList());

                objList.addAll(em.createQuery(userOrgHql, ObjectData.class)
                        .setParameter("userId", userId)
                        .setParameter("action", action).getResultList());

                return new HashSet<>(objList);
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get objects by user", e);
        }
    }

	@SuppressWarnings("Duplicates")
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
	
	@SuppressWarnings("Duplicates")
    @Override
	public <T, E extends Exception> T txExpr(ComplexActionWithResult<T, E> action, Class<E> exClass) throws E {		
		final List<T> result = new ArrayList<>();
		
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
