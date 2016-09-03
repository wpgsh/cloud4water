package net.wapwag.wemp.h2.hibernate;

import org.apache.aries.jpa.template.EmConsumer;
import org.apache.aries.jpa.template.EmFunction;
import org.apache.aries.jpa.template.JpaTemplate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import net.wapwag.wemp.dao.TxAwareEntityManager;

@Component(property="osgi.name=waterequipment", scope=ServiceScope.SINGLETON)
public class H2TxAwareEntityManager implements TxAwareEntityManager {

	@Reference(target="(osgi.unit.name=waterequipment-jpa)")
	protected JpaTemplate jpa;
	
	@Override
	public void init() throws Exception {
		// TODO provide initialization sequence...
	}

	@Override
	public void tx(EmConsumer emSupplier) throws Exception {
		// TODO implement tx sequence
		throw new RuntimeException("TODO - Not implemented");
	}

	@Override
	public <T> T txExpr(EmFunction<T> emFunction) throws Exception {
		// TODO implement tx sequence with result
		throw new RuntimeException("TODO - Not implemented");
	}

}
