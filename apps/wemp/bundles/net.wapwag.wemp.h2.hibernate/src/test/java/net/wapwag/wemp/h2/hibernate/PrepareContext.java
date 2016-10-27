package net.wapwag.wemp.h2.hibernate;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.h2.jdbcx.JdbcDataSource;

import com.google.common.collect.ImmutableMap;

import static org.mockito.Mockito.*;

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
    
	public static EntityManagerFactory createEMF() throws Exception {		
		configureJNDI();
    	
		final JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL("jdbc:h2:~/wpg-wemp");
		dataSource.setUser("sa");
		dataSource.setPassword("");
		
		final Name dataSourceName = mock(Name.class);
		
		when(nameParser.parse(JNDI_DATA_SOURCE)).thenReturn(dataSourceName);		
		when(root.lookup(dataSourceName)).thenReturn(dataSource);
    	
    	return Persistence.createEntityManagerFactory("waterequipment-jpa-h2",
    			ImmutableMap.of(
    					"hibernate.hbm2ddl.auto", "create", 
    					"hibernate.show_sql", "true",
    					"hibernate.transaction.jta.platform", "org.hibernate.service.jta.platform.internal.SunOneJtaPlatform"));
	}

}
