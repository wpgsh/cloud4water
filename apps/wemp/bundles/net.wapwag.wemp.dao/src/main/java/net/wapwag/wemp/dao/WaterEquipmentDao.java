package net.wapwag.wemp.dao;

/**
 * Water Equipment DAO methods
 * 
 *
 */
public interface WaterEquipmentDao {

	// TODO add relevant methods
	
	/**
	 * An action that consists of several steps to be performed
	 * within a single transaction
	 * 
	 * @author Alexander Lukichev
	 *
	 * @param <E> an exception class that can be thrown 
	 */
	@FunctionalInterface
	static interface ComplexAction<E extends Exception> {
		
		void apply() throws E;
		
	}	
	
	/**
	 * Execute a complex action within single a transaction. Throws a checked
	 * exception
	 * 
	 * @param action the action to execute
	 * @param exClass the checked exception class
	 * @throws E
	 */
	<E extends Exception> void tx(ComplexAction<E> action, Class<E> exClass) throws E;
	
	/**
	 *
	 * An action that consists of several steps to be performed
	 * within a single transaction and returns some value
     *
	 * @author Alexander Lukichev
	 *
	 * @param <T> the type of the returned value
	 * @param <E> an exception class that can be thrown
	 */
	@FunctionalInterface
	static interface ComplexActionWithResult<T, E extends Exception> {
		
		T apply() throws E;
		
	}
	
	/**
	 * Execute a complex action within single a transaction. Throws a checked
	 * exception or returns a typed value
	 * 
	 * @param action the action to execute
	 * @param exClass the checked exception class
	 * @throws E
	 */
	<T, E extends Exception> T txExpr(ComplexActionWithResult<T, E> action, Class<E> exClass) throws E;

	
}
