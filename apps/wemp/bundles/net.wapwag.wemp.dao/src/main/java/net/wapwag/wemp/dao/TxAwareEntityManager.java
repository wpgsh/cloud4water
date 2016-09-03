package net.wapwag.wemp.dao;

import org.apache.aries.jpa.template.EmConsumer;
import org.apache.aries.jpa.template.EmFunction;

/**
 * This interface needs to be implemented in the particular *.hibernate
 * bundle.
 * 
 * @author Alexander Lukichev
 *
 */
public interface TxAwareEntityManager {
	/**
	 * Initialization steps. Called on every instantiation
	 * of {@link WaterEquipmentDao}
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception;

	// see also https://issues.apache.org/jira/browse/ARIES-1432
	
	/**
	 * Run a sequence of actions inside a single transaction
	 * 
	 * @param emSupplier
	 * @throws Exception
	 */
	public void tx(EmConsumer emSupplier) throws Exception;
	/**
	 * Run a sequence of actions inside a single transaction.
	 * The sequence can have a result.
	 * 
	 * @param emFunction
	 * @return
	 * @throws Exception
	 */
	public <T> T txExpr(EmFunction<T> emFunction) throws Exception;
}
