package net.wapwag.authn.dao;

import org.apache.aries.jpa.template.EmConsumer;
import org.apache.aries.jpa.template.EmFunction;

public interface TxAwareEntityManager {

	public void init() throws Exception;

	// see also https://issues.apache.org/jira/browse/ARIES-1432
	
	public void tx(EmConsumer emSupplier) throws Exception;
	
	public <T> T txExpr(EmFunction<T> emFunction) throws Exception;
}
