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
import java.util.Hashtable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PrepareContext {
	
    private static final String JNDI_DATA_SOURCE = "osgi:service/javax.sql.DataSource/(osgi.jndi.service.name=jdbc/WaterEquipment_MySQL)";
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
    
	public static EntityManagerFactory createEMF() throws Exception {		
		configureJNDI();
    	
		final MysqlDataSource dataSource = new MysqlDataSource();		
		dataSource.setURL("jdbc:mysql://139.196.254.159:3306/wemp");
		dataSource.setUser("cloud4water");
		dataSource.setPassword("wpg");
		
		final Name dataSourceName = mock(Name.class);
		
		when(nameParser.parse(JNDI_DATA_SOURCE)).thenReturn(dataSourceName);		
		when(root.lookup(dataSourceName)).thenReturn(dataSource);
    	
    	return Persistence.createEntityManagerFactory("waterequipment-jpa-mysql",
    			ImmutableMap.of(
//    					"hibernate.hbm2ddl.auto", "create",
    					"hibernate.show_sql", "true",
    					"hibernate.transaction.jta.platform", "org.hibernate.service.jta.platform.internal.SunOneJtaPlatform"));
	}

}
