package net.wapwag.wemp.dao;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.geo.*;
import net.wapwag.wemp.dao.model.permission.User;
import net.wapwag.wemp.dao.model.project.Project;
import net.wapwag.wemp.dao.model.project.PumpEquipment;
import net.wapwag.wemp.dao.model.project.PumpRoom;
import org.osgi.service.component.annotations.*;

import java.util.ArrayList;
import java.util.List;

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
                String sql = "select uo.userObjectId.user from UserObjectR uo " +
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

}
