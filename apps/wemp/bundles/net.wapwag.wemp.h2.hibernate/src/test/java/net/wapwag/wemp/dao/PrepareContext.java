package net.wapwag.wemp.dao;

import com.google.common.collect.ImmutableMap;
import org.apache.geronimo.transaction.GeronimoUserTransaction;
import org.apache.geronimo.transaction.manager.GeronimoTransactionManager;
import org.h2.jdbcx.JdbcDataSource;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Hashtable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PrepareContext {
	
    private static final String JNDI_DATA_SOURCE = "osgi:service/javax.sql.DataSource/(osgi.jndi.service.name=jdbc/WaterEquipment_H2)";
    private static Context root = mock(Context.class);
    private static NameParser nameParser = mock(NameParser.class);
    
    private static boolean isJndiConfigured = false;
    
    static void configureJNDI() throws Exception {
    	if (!isJndiConfigured) {
	    	NamingManager.setInitialContextFactoryBuilder(new InitialContextFactoryBuilder() {
				@Override
				public InitialContextFactory createInitialContextFactory(Hashtable<?, ?> environment) throws NamingException {				
					when(root.getNameParser("")).thenReturn(nameParser);
					
					return new InitialContextFactory() {
						@Override
						public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
							return root;
						}
						
					};
				}
	    		
	    	});
	    	isJndiConfigured = true;
    	}
    }
    
    public static GeronimoTransactionManager transactionManager;
    public static GeronimoUserTransaction userTransaction;
    
    public static void newTransaction() throws Exception {
		transactionManager = new GeronimoTransactionManager();
		userTransaction = new GeronimoUserTransaction(transactionManager);		
    }
    
	public static EntityManagerFactory createEMF() throws Exception {		
		configureJNDI();
    	
		final JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
		
		final Name dataSourceName = mock(Name.class);
		
		when(nameParser.parse(JNDI_DATA_SOURCE)).thenReturn(dataSourceName);		
		when(root.lookup(dataSourceName)).thenReturn(dataSource);
		
		final Name txmName = mock(Name.class);
		final Name txName = mock(Name.class);
		
		when(nameParser.parse("java/SampleTransactionManager")).thenReturn(txmName);
		when(nameParser.parse("java/SampleUserTransaction")).thenReturn(txName);
		when(root.lookup(txmName)).then(__ -> transactionManager);
		when(root.lookup(txName)).then(__ -> userTransaction);
		
    	
		newTransaction();
		transactionManager.begin();
		try {
	    	return Persistence.createEntityManagerFactory("waterequipment-jpa-h2",
	    			ImmutableMap.of(
	    					"hibernate.hbm2ddl.auto", "create", 
	    					"hibernate.show_sql", "true",
	    					"hibernate.transaction.jta.platform", SimpleJTAPlatform.class.getName()));
		} finally {
			transactionManager.commit();
		}
	}

}
