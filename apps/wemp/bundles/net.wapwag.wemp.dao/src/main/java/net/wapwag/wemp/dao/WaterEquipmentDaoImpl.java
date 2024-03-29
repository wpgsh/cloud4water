package net.wapwag.wemp.dao;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.Parent;
import net.wapwag.wemp.dao.model.link.*;
import net.wapwag.wemp.dao.model.org.Organization;
import net.wapwag.wemp.dao.model.permission.AccessToken;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.RegisteredClient;
import net.wapwag.wemp.dao.model.permission.User;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.*;

@Component(scope=ServiceScope.SINGLETON)
public class WaterEquipmentDaoImpl implements WaterEquipmentDao {

	private static final Logger logger = LoggerFactory.getLogger(WaterEquipmentDaoImpl.class);

	@Reference(target = "(osgi.name=waterequipment)")
	protected TxAwareEntityManager entityManager;

	@Activate
	protected void init() throws Exception {
		entityManager.init();
	}

	@Deactivate
	protected void destroy() {

	}

    @Override
    public boolean isAuthorized(long userId, String action, long objectId) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {
                ObjectData object = em.find(ObjectData.class, objectId);

                StringBuilder select0 = new StringBuilder();
                StringBuilder select1 = new StringBuilder();

                select0.append("select o0.id,uo0.userObjectId.user.id");
                select1.append("select o0.id,go0.groupObjectId.group.id");

                Class<?> clazz = object.getType().getObjectClass();

                StringBuilder from0 = new StringBuilder();
                StringBuilder from1 = new StringBuilder();

                from0.append("from ").append(clazz.getSimpleName()).append(" o0 left join o0.objectUserSet uo0");
                from1.append("from ").append(clazz.getSimpleName()).append(" o0 left join o0.objectGroupSet go0 left join go0.groupObjectId.group.groupUserSet ug0");

                StringBuilder where0 = new StringBuilder();
                StringBuilder where1 = new StringBuilder();

                where0.append("where o0.id = :objectId and ((uo0 is null or (uo0.userObjectId.user.id = :userId and uo0.actionId = :action))");
                where1.append("where o0.id = :objectId and ((go0 is null or (ug0.userGroupId.user.id = :userId and go0.actionId = :action))");

                String objId = "o0";
                int idx = 1;

                while (true) {
                    Field[] declaredFields = clazz.getDeclaredFields();
                    if (declaredFields != null) {
                        for (Field f : declaredFields) {
                            if (f.isAnnotationPresent(Parent.class)) {
                                clazz = f.getType();
                                objId = objId + "." + f.getName();
                                if (clazz.isAnnotationPresent(Entity.class)) {
                                    select0.append(",uo").append(idx).append(".userObjectId.user.id");
                                    from0.append(" left join ").append(objId).append(".objectUserSet uo").append(idx);
                                    where0.append(" or (uo").append(idx).append(" is null or (uo").append(idx).append(".userObjectId.user.id = :userId and uo").append(idx).append(".actionId = :action and uo").append(idx).append(".transitive = 1))");

                                    select1.append(",go").append(idx).append(".groupObjectId.group.id");
                                    from1.append(" left join ").append(objId).append(".objectGroupSet go").append(idx).append(" left join go").append(idx).append(".groupObjectId.group.groupUserSet ug").append(idx);
                                    where1.append(" or (go").append(idx).append(" is null or (ug").append(idx).append(".userGroupId.user.id = :userId and go").append(idx).append(".actionId = :action and go").append(idx).append(".transitive = 1))");

                                    idx++;
                                    continue;
                                } else {
                                    throw new RuntimeException("Invalid parent class. A JPA Entity expected: " + clazz.getName());
                                }
                            }
                        }
                    }
                    break;
                }

                where0.append(")");
                where1.append(")");

                String query0 = select0 + " " + from0 + " " + where0;
                logger.debug("isAuthorized: query0: {}. objectId={}, userId={}, action={}", query0, objectId, userId, action);

                List result0 = em.createQuery(query0).
                        setParameter("objectId", objectId).
                        setParameter("userId", userId).
                        setParameter("action", action).
                        getResultList();

                if (result0.size() > 0) {
                    for (Object[] row : (List<Object[]>) result0) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("isAuthorized: result0=" + Arrays.asList(row));
                        }
                        if (row[0] != null) {
                            for (int i = 0; i < idx; i++) {
                                if (row[i + 1] != null) {
                                    return true;
                                }
                            }
                        }
                    }
                }

                String query1 = select1 + " " + from1 + " " + where1;
                logger.debug("isAuthorized: query1: {}. objectId={}, userId={}, action={}", query1, objectId, userId, action);

                List result1 = em.createQuery(query1).
                        setParameter("objectId", objectId).
                        setParameter("userId", userId).
                        setParameter("action", action).
                        getResultList();

                if (result1.size() > 0) {
                    for (Object[] row : (List<Object[]>) result1) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("isAuthorized: result1=" + Arrays.asList(row));
                        }
                        if (row[0] != null) {
                            for (int i = 0; i < idx; i++) {
                                if (row[i + 1] != null) {
                                    return true;
                                }
                            }
                        }
                    }
                }

                return false;
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("Cannot check authorization", e);
        }
    }

    @Override
    public AccessToken lookupAccessToken(final String handle) throws WaterEquipmentDaoException {
        final String hql = "select token from AccessToken token where token.handle = :handle";

        try {
            return entityManager.txExpr(em -> em.createQuery(hql, AccessToken.class)
                    .setParameter("handle", handle)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null)
            );
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get access token by the handle", e);
        }
    }

    @Override
    public RegisteredClient getClientByRedirectURI(String redirectURI) throws WaterEquipmentDaoException {
        final String hql = "select client from RegisteredClient client where client.redirectURI = :redirectURI";
        try {
            return entityManager.txExpr(em ->
                    em.createQuery(hql, RegisteredClient.class)
                            .setParameter("redirectURI", redirectURI)
                            .getResultList()
                            .stream()
                            .findFirst()
                            .orElse(null)
            );
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("Cannot get client by redirect_uri", e);
        }
    }

    @Override
    public AccessToken getAccessTokenByUserIdAndClientId(long userId, long clientId) throws WaterEquipmentDaoException {
        final String hql = "select token from AccessToken token " +
                "where token.accessTokenId.user.id = :userId " +
                "and token.accessTokenId.registeredClient.id = :clientId";
        try {
            return entityManager.txExpr(em -> em.createQuery(hql, AccessToken.class)
                    .setParameter("userId", userId)
                    .setParameter("clientId", clientId)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null)
            );
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't find access token", e);
        }
    }

    @Override
    public AccessToken getAccessTokenByCode(String code) throws WaterEquipmentDaoException {
        final String hql = "select token from AccessToken token where token.authrizationCode = :code";

        try {
            return entityManager.txExpr(em -> em.createQuery(hql, AccessToken.class)
                    .setParameter("code", code)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null)
            );
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't find access token", e);
        }
    }

    @Override
    public long saveAccessToken(AccessToken accessToken) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> em.merge(accessToken) != null ? 1L : 0L);
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("Cannot add access token", e);
        }
    }

    @Override
    public int addUser(User user) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {
                em.persist(user);
                return 1;
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("Cannot add user", e);
        }
    }

    @Override
    public User getUser(long uid) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr((em) -> em.find(User.class, uid));
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("Cannot get user by id", e);
        }
    }

    @Override
    public User getUserByExternalId(long externalId) throws WaterEquipmentDaoException {
        final String hql = "select user from User user where user.externalId = :externalId";

        try {
            return entityManager.txExpr((em) -> em.createQuery(hql, User.class)
                    .setParameter("externalId", externalId)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null)
            );
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("Cannot get user by external id", e);
        }
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
	public List<String> getUserPermissionByObject(long objId, long userId) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {

                String hql = "select uo.actionId from UserObject uo " +
                        "where uo.userObjectId.objectData.id = :objId " +
                        "and uo.userObjectId.user.id = :userId";

                return em.createQuery(hql, String.class)
                        .setParameter("objId", objId)
                        .setParameter("userId", userId)
                        .getResultList();

            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get object by user", e);
        }
    }

	@Override
	public int addObjectByUser(long objId, long userId, String action) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {

                User user = new User();
                user.setId(userId);

                ObjectData objectData = new ObjectData();
                objectData.setId(objId);

                UserObject userObject = new UserObject();
                userObject.setUserObjectId(new UserObjectId(user, objectData));
                userObject.setActionId(action);

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
                    hql  += " and userObject.actionId = :action";
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
                        .setParameter("groupId", groupId)
                        .getResultList()
                        .stream()
                        .findFirst()
                        .orElse(null);
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
                    hql  += " and groupObject.actionId = :action";
                }

                Query query = em.createQuery(hql);

                query.setParameter("objId", objId);
                query.setParameter("groupId", groupId);

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
                    .setParameter("orgId", orgId)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null)
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

                Group groupEntity = em.find(Group.class, groupId);
                groupEntity.setName(group.getName());
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
        final String hql = "select g from Group g where g.id = :groupId and g.organization.id = :orgId";

        try {
            return entityManager.txExpr(em -> {
                Group group = em.createQuery(hql, Group.class)
                        .setParameter("groupId", groupId)
                        .setParameter("orgId", orgId)
                        .getResultList()
                        .stream()
                        .findFirst()
                        .orElse(null);

                em.remove(group);

                return 1;
            });
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
                        .setParameter("userId", userId)
                        .getResultList()
                        .stream()
                        .findFirst()
                        .orElse(null);

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
	public Long getObjectByGroup(long orgId, long groupId, long objId, String action) throws WaterEquipmentDaoException {
        final String hql = "select groupObject.groupObjectId.objectData.id from GroupObject  groupObject " +
                "where groupObject.groupObjectId.group.id = :groupId " +
                "and groupObject.groupObjectId.group.organization.id = :orgId " +
                "and groupObject.groupObjectId.objectData.id = :objId " +
                "and groupObject.actionId = :action";
        try {
            return entityManager.txExpr(em -> em.createQuery(hql, Long.class)
                        .setParameter("groupId", groupId)
                        .setParameter("orgId", orgId)
                        .setParameter("objId", objId)
                        .setParameter("action", action)
                        .getResultList()
                        .stream()
                        .findFirst()
                        .orElse(null)
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
    public int removeUsersByOrg(long orgId) throws WaterEquipmentDaoException {
        final String userOrgHql = "delete from UserOrg userOrg " +
                "where userOrg.userOrgId.organization.id = :orgId";

        final String userGroupHql = "delete from UserGroup userGroup " +
                "where userGroup.userGroupId.group.id in " +
                "(select group.id from Group group where organization.id = :orgId)";
        try {
            return entityManager.txExpr(em -> {
                int removeCount = 0;

                removeCount += em.createQuery(userOrgHql)
                        .setParameter("orgId", orgId)
                        .executeUpdate();

                removeCount += em.createQuery(userGroupHql)
                        .setParameter("orgId", orgId)
                        .executeUpdate();

                return removeCount;
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't remove users by org", e);
        }
    }

	@Override
	public int removeUserByOrg(long orgId, long uid) throws WaterEquipmentDaoException {
        final String userOrgHql = "delete from UserOrg userOrg " +
                "where userOrg.userOrgId.organization.id = :orgId " +
                "and userOrg.userOrgId.user.id = :userId";

        final String userGroupHql = "delete from UserGroup userGroup " +
                "where userGroup.userGroupId.group.id in (select group.id from Group group where organization.id = :orgId )" +
                "and userGroup.userGroupId.user.id = :userId";
        try {
            return entityManager.txExpr(em -> {
                int removeCount = 0;
                removeCount += em.createQuery(userOrgHql)
                        .setParameter("orgId", orgId)
                        .setParameter("userId", uid).executeUpdate();

                removeCount += em.createQuery(userGroupHql)
                        .setParameter("orgId", orgId)
                        .setParameter("userId", uid).executeUpdate();

                return removeCount;
            });
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
	public int addObjectByOrg(long orgId, long objId) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {
                Organization organization = new Organization();
                organization.setId(orgId);

                ObjectData objectData = new ObjectData();
                objectData.setId(objId);

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
                        .setParameter("objId", objId)
                        .getResultList()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (havePermission == 0) {
                    String userGroupHql = "select count(userGroup) from UserGroup userGroup,GroupObject groupObject " +
                            "where userGroup.userGroupId.user.id = :userId " +
                            "and userGroup.userGroupId.group = groupObject.groupObjectId.group " +
                            "and groupObject.groupObjectId.objectData.id = :objId";
                    havePermission = em.createQuery(userGroupHql, Long.class)
                            .setParameter("userId", userId)
                            .setParameter("objId", objId)
                            .getResultList()
                            .stream()
                            .findFirst()
                            .orElse(null);

                    if (havePermission == 0) {
                        String userOrgHql = "select count(userOrg) from UserOrg userOrg,GroupObject groupObject " +
                                "where userOrg.userOrgId.user.id = :userId " +
                                "and userOrg.userOrgId.organization = groupObject.groupObjectId.group.organization " +
                                "and groupObject.groupObjectId.objectData.id = :objId";
                        havePermission = em.createQuery(userOrgHql, Long.class)
                                .setParameter("userId", userId)
                                .setParameter("objId", objId)
                                .getResultList()
                                .stream()
                                .findFirst()
                                .orElse(null);
                    }
                }
                return havePermission > 0;
            });
        }  catch (Exception e) {
            throw new WaterEquipmentDaoException("can't check permission for the object", e);
        }
	}

	@Override
	public Set<Long> getObjectsByUser(long userId, String action) throws WaterEquipmentDaoException {
        final String userObjectHql = "select userObject.userObjectId.objectData.id " +
                "from UserObject userObject " +
                "where userObject.userObjectId.user.id = :userId " +
                "and userObject.actionId = :action";

        final String userGroupHql = "select groupObject.groupObjectId.objectData.id from UserGroup userGroup,GroupObject groupObject " +
                "where userGroup.userGroupId.user.id = :userId " +
                "and userGroup.userGroupId.group = groupObject.groupObjectId.group " +
                "and groupObject.actionId = :action";

        final String userOrgHql = "select groupObject.groupObjectId.objectData.id from UserOrg userOrg,GroupObject groupObject " +
                "where userOrg.userOrgId.user.id = :userId " +
                "and userOrg.userOrgId.organization = groupObject.groupObjectId.group.organization " +
                "and groupObject.actionId = :action";
		try {
            return entityManager.txExpr(em -> {
                List<Long> objList = new ArrayList<>();

                objList.addAll(em.createQuery(userObjectHql, Long.class)
                        .setParameter("userId", userId)
                        .setParameter("action", action).getResultList());

                objList.addAll(em.createQuery(userGroupHql, Long.class)
                        .setParameter("userId", userId)
                        .setParameter("action", action).getResultList());

                objList.addAll(em.createQuery(userOrgHql, Long.class)
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
