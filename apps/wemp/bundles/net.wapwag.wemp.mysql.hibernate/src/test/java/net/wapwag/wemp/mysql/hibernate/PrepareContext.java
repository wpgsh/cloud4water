package net.wapwag.wemp.mysql.hibernate;

import com.google.common.collect.ImmutableMap;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.aries.transaction.internal.AriesPlatformTransactionManager;
import org.apache.geronimo.transaction.GeronimoUserTransaction;
import org.apache.geronimo.transaction.manager.GeronimoTransactionManager;

import java.util.Hashtable;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PrepareContext {
	
    private static final String JNDI_DATA_SOURCE = "osgi:service/javax.sql.DataSource/(osgi.jndi.service.name=jdbc/WaterEquipment_MySQL)";
    private static Context root = mock(Context.class);
    private static NameParser nameParser = mock(NameParser.class);
	private static final Map<String, String> HIBERNATE_CONFIG = ImmutableMap.of(
//        "hibernate.hbm2ddl.auto", "create",
        "hibernate.show_sql", "true",
        "hibernate.transaction.jta.platform", SimpleJTAPlatform.class.getName());
    
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

    /**
     * reduce the waste time of recreate and validate database by the hibernate
     * @return return true if you don't want to empty the database every times
     */
    public static boolean cleanDatabase() {
        return HIBERNATE_CONFIG.containsKey("hibernate.hbm2ddl.auto");
    }
    
    public static GeronimoTransactionManager transactionManager;
    public static GeronimoUserTransaction userTransaction;

	public static EntityManagerFactory createEMF() throws Exception {		
		configureJNDI();
    	
		final MysqlDataSource dataSource = new MysqlDataSource();		
		dataSource.setURL("jdbc:mysql://139.196.254.159:3306/wemp");
		dataSource.setUser("cloud4water");
		dataSource.setPassword("wpg");
		
		final Name dataSourceName = mock(Name.class);
		
		when(nameParser.parse(JNDI_DATA_SOURCE)).thenReturn(dataSourceName);		
		when(root.lookup(dataSourceName)).thenReturn(dataSource);
		//AriesPlatformTransactionManager tx = new AriesPlatformTransactionManager();
		
		transactionManager = new GeronimoTransactionManager();
		userTransaction = new GeronimoUserTransaction(transactionManager);
		
		final Name txmName = mock(Name.class);
		final Name txName = mock(Name.class);
		
		when(nameParser.parse("java/SampleTransactionManager")).thenReturn(txmName);
		when(nameParser.parse("java/SampleUserTransaction")).thenReturn(txName);
		when(root.lookup(txmName)).thenReturn(transactionManager);
		when(root.lookup(txName)).thenReturn(userTransaction);
    	
    	return Persistence.createEntityManagerFactory("waterequipment-jpa-mysql", HIBERNATE_CONFIG);
	}

}
