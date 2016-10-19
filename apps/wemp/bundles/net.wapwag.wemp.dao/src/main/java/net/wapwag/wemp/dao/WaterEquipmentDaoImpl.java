package net.wapwag.wemp.dao;

import java.util.ArrayList;
import java.util.List;

import net.wapwag.wemp.dao.model.*;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

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
	public int removeObjectData(String objectId) throws WaterEquipmentDaoException {
		return 0;
	}

	@Override
	public ObjectData getObjectData(ObjectData ObjectData) throws WaterEquipmentDaoException {
		return new ObjectData();
	}

	@Override
	public int saveObjectDict(ObjectDict objectDict) throws WaterEquipmentDaoException {
		try {
			return entityManager.txExpr(em -> {
				em.merge(objectDict);
				return 1;
			});
		} catch (Exception e) {
			throw new WaterEquipmentDaoException("can't save object dict", e);
		}
	}

	@Override
	public int removeObjectDict(int objectDictId) throws WaterEquipmentDaoException {
		try {
			return entityManager.txExpr(em -> {
				ObjectDict objectDict = em.find(ObjectDict.class, objectDictId);
                em.remove(objectDict);
				return 1;
			});
		} catch (Exception e) {
			throw new WaterEquipmentDaoException("can't save object dict", e);
		}
	}

	@Override
	public ObjectData getObjectDict(ObjectDict objectDict) throws WaterEquipmentDaoException {
		return null;
	}

	@Override
	public Country getCountry(Country country) throws WaterEquipmentDaoException {
		return null;
	}

	@Override
	public Area getArea(Area area) throws WaterEquipmentDaoException {
		try {
			return entityManager.txExpr(em -> {
				return em.find(Area.class, area.getId());
			});
		} catch (Exception e) {
			throw new WaterEquipmentDaoException("can't save object dict", e);
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
	public County getCounty(County county) throws WaterEquipmentDaoException {
		return null;
	}

	@Override
	public Town getTown(Town town) throws WaterEquipmentDaoException {
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
