package net.wapwag.wemp.dao;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.Parent;
import net.wapwag.wemp.dao.model.geo.*;
import net.wapwag.wemp.dao.model.permission.User;
import net.wapwag.wemp.dao.model.project.Project;
import net.wapwag.wemp.dao.model.project.PumpEquipment;
import net.wapwag.wemp.dao.model.project.PumpRoom;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;

@Component(scope=ServiceScope.SINGLETON)
public class WaterEquipmentDaoImpl implements WaterEquipmentDao {
	
	private static Logger logger = LoggerFactory.getLogger(WaterEquipmentDaoImpl.class);

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
    public int saveCountry(Country country) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {
                em.merge(country);
                return 1;
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't save country", e);
        }
    }

    @Override
	public int saveObjectData(final ObjectData ObjectData) throws WaterEquipmentDaoException {
		try {
			return entityManager.txExpr(em -> {
                em.merge(ObjectData);
                return 1;
            });
		} catch (Exception e) {
            throw new WaterEquipmentDaoException("can't save object", e);
        }
	}

	@Override
	public int removeObjectData(long objectId) throws WaterEquipmentDaoException {
		return 0;
	}

	@Override
	public ObjectData getObjectData(long objId) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> em.find(ObjectData.class, objId));
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get object", e);
        }
	}

    @Override
    public List<User> getUsersByObject(long objId, String actionId) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {

                ObjectData objectData = em.find(ObjectData.class, objId);
                String sql = "select uo.userObjectId.user from UserObject uo " +
                        "where uo.userObjectId.objectData = :obj and uo.actionId = :actionId";
                return em.createQuery(sql, User.class)
                        .setParameter("obj", objectData)
                        .setParameter("actionId", actionId).getResultList();

            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get object", e);
        }
    }

    @Override
	public Country getCountry(Country country) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> em.find(Country.class, country.getId()));
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't save object dict", e);
        }
	}

	@Override
	public Area getArea(Area area) throws WaterEquipmentDaoException {
		try {
			return entityManager.txExpr(em -> em.createQuery("select area from Area area " +
                        "left join fetch area.provinceSet province " +
                        "left join fetch province.citySet city " +
                        "left join fetch city.countySet county " +
                        "left join fetch county.projectSet where area.id = :id", Area.class)
                .setParameter("id", area.getId()).getSingleResult());
		} catch (Exception e) {
			throw new WaterEquipmentDaoException("can't save area", e);
		}
	}

	@Override
	public Province getProvince(Province province) throws WaterEquipmentDaoException {
		return null;
	}

	@Override
	public City getCity(City city) throws WaterEquipmentDaoException {
		return null;
	}

	@Override
	public County getCounty(long countyId) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> em.createQuery("select county from County county " +
                        "where county.id = :id", County.class)
                        .setParameter("id", countyId).getSingleResult());
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get county", e);
        }
	}

    @Override
    public int saveProject(Project project) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {
                em.merge(project);
                return 1;
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't save project", e);
        }
    }

    @Override
    public int removeProject(long projectId) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {
                Project project = em.find(Project.class, projectId);
                em.remove(project);
                return 1;
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't remove project", e);
        }
    }

    @Override
    public int updateProject(Project project) throws WaterEquipmentDaoException {
        return 0;
    }

    @Override
    public Project getProject(Project project) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> em.createQuery("select project from Project project " +
                        "left join fetch project.pumpRoomSet pumpRoom " +
                        "left join fetch pumpRoom.pumpEquipmentSet pumpEquipment " +
                        "where project.id = :id", Project.class)
                        .setParameter("id", project.getId()).getSingleResult());
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get project", e);
        }
    }

    @Override
    public int savePumpRoom(PumpRoom pumpRoom) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {
                em.merge(pumpRoom);
                return 1;
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't save pumpRoom", e);
        }
    }

    @Override
    public int removePumpRoom(long pumpRoomId) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {
                PumpRoom pumpRoom = em.find(PumpRoom.class, pumpRoomId);
                em.remove(pumpRoom);
                return 1;
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't remove project", e);
        }
    }

    @Override
    public int updatePumpRoom(PumpRoom pumpRoom) throws WaterEquipmentDaoException {
        return 0;
    }

    @Override
    public PumpRoom getPumpRoom(PumpRoom pumpRoom) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> em.createQuery("select pumpRoom from PumpRoom pumpRoom " +
                        "left join fetch pumpRoom.pumpEquipmentSet pumpEquipment " +
                        "where pumpRoom.id = :id", PumpRoom.class)
                        .setParameter("id", pumpRoom.getId()).getSingleResult());
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get pumpRoom", e);
        }
    }

    @Override
    public int savePumpEquipment(PumpEquipment pumpEquipment) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {
                em.merge(pumpEquipment);
                return 1;
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't save pumpEquipment", e);
        }
    }

    @Override
    public int removePumpEquipment(long pumpEquipmentId) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> {
                PumpEquipment pumpEquipment = em.find(PumpEquipment.class, pumpEquipmentId);
                em.remove(pumpEquipment);
                return 1;
            });
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't remove pumpEquipment", e);
        }
    }

    @Override
    public int updatePumpEquipment(PumpEquipment pumpEquipment) throws WaterEquipmentDaoException {
        return 0;
    }

    @Override
    public PumpEquipment getPumpEquipment(PumpEquipment pumpEquipment) throws WaterEquipmentDaoException {
        try {
            return entityManager.txExpr(em -> em.createQuery("select pumpEquipment from PumpEquipment pumpEquipment " +
                    "where pumpEquipment.id = :id", PumpEquipment.class)
                    .setParameter("id", pumpEquipment.getId()).getSingleResult());
        } catch (Exception e) {
            throw new WaterEquipmentDaoException("can't get pumpEquipment", e);
        }
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

	@Override
	public boolean isAuthorized(long userId, String action, long objectId) throws WaterEquipmentDaoException {
		try {
			return entityManager.txExpr(em -> {
				ObjectData object = em.find(ObjectData.class, objectId);
				
				StringBuffer select0 = new StringBuffer();
				StringBuffer select1 = new StringBuffer();
				
				select0.append("select o0.id,uo0.userObjectId.user.id");
				select1.append("select o0.id,go0.groupObjectId.group.id");
				
				Class<?> clazz = object.getType().getObjectClass();
				
				StringBuffer from0 = new StringBuffer();
				StringBuffer from1 = new StringBuffer();
				
				from0.append("from "+clazz.getSimpleName()+" o0 left join o0.userObjectSet uo0");
				from1.append("from "+clazz.getSimpleName()+" o0 left join o0.groupObjectSet go0 left join go0.groupObjectId.group.userGroupSet ug0");
				
				StringBuffer where0 = new StringBuffer();
				StringBuffer where1 = new StringBuffer();
				
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
								objId = objId+"."+f.getName();
								if (clazz.isAnnotationPresent(Entity.class)) {
									select0.append(",uo"+idx+".userObjectId.user.id");
									from0.append(" left join "+objId+".userObjectSet uo"+idx);
									where0.append(" or (uo"+idx+" is null or (uo"+idx+".userObjectId.user.id = :userId and uo"+idx+".actionId = :action and uo"+idx+".transitive = 1))");
									
									select1.append(",go"+idx+".groupObjectId.group.id");
									from1.append(" left join "+objId+".groupObjectSet go"+idx+" left join go"+idx+".groupObjectId.group.userGroupSet ug"+idx);
									where1.append(" or (go"+idx+" is null or (ug"+idx+".userGroupId.user.id = :userId and go"+idx+".actionId = :action and go"+idx+".transitive = 1))");
									
									idx ++;
									continue;
								} else {
									throw new RuntimeException("Invalid parent class. A JPA Entity expected: "+clazz.getName());
								}
							}
						}
					}
					break;
				}
				
				where0.append(")");
				where1.append(")");
				
				String query0 = select0+" "+from0+" "+where0;
				logger.debug("isAuthorized: query0: {}. objectId={}, userId={}, action={}", query0, objectId, userId, action);				
				
				List result0 = em.createQuery(query0).
					setParameter("objectId", objectId).
					setParameter("userId", userId).
					setParameter("action",  action).
					getResultList();
								
				if (result0.size() > 0) {
					for (Object[] row : (List<Object[]>) result0) {
						if (logger.isDebugEnabled()) {
							logger.debug("isAuthorized: result0="+Arrays.asList(row));
						}
						if (row[0] != null) {
							for (int i=0; i<idx; i++) {
								if (row[i+1] != null) {
									return true;
								}
							}
						}
					}
				}
				
				String query1 = select1+" "+from1+" "+where1;
				logger.debug("isAuthorized: query1: {}. objectId={}, userId={}, action={}", query1, objectId, userId, action);
				
				List result1 = em.createQuery(query1).
						setParameter("objectId", objectId).
						setParameter("userId", userId).
						setParameter("action",  action).
						getResultList();
					
				if (result1.size() > 0) {
					for (Object[] row : (List<Object[]>) result1) {
						if (logger.isDebugEnabled()) {
							logger.debug("isAuthorized: result1="+Arrays.asList(row));
						}
						if (row[0] != null) {
							for (int i=0; i<idx; i++) {
								if (row[i+1] != null) {
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

}
