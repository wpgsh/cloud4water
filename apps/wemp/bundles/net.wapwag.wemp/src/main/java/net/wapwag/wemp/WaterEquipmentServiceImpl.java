package net.wapwag.wemp;

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
	

}
