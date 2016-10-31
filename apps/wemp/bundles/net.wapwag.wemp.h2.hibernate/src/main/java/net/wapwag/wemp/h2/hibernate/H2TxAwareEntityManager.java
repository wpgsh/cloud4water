package net.wapwag.wemp.h2.hibernate;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.aries.jpa.template.EmConsumer;
import org.apache.aries.jpa.template.EmFunction;
import org.apache.aries.jpa.template.JpaTemplate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import net.wapwag.wemp.dao.TxAwareEntityManager;

@Component(property="osgi.name=waterequipment", scope=ServiceScope.SINGLETON)
public class H2TxAwareEntityManager implements TxAwareEntityManager {

	@Reference(target="(osgi.unit.name=waterequipment-jpa-h2)")
	protected JpaTemplate jpa;
	
	@Override
	public void init() throws Exception {
		// TODO provide initialization sequence...
	}

	@Override
	public void tx(EmConsumer emConsumer) throws Exception {
		try {
			jpa.tx(emConsumer);
		} catch (Throwable e) {
			throw new Exception("Error executing query (tx)", e);
		}
	}

	@Override
	public <T> T txExpr(EmFunction<T> emFunction) throws Exception {
		try {
			return jpa.txExpr(emFunction);
		} catch (Throwable e) {
			throw new Exception("Error executing query (txExpr)", e);
		}
	}
	
}
