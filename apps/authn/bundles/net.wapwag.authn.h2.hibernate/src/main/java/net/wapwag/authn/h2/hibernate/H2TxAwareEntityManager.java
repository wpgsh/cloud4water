package net.wapwag.authn.h2.hibernate;

import javax.persistence.EntityManager;

import org.apache.aries.jpa.template.EmConsumer;
import org.apache.aries.jpa.template.EmFunction;
import org.apache.aries.jpa.template.JpaTemplate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import net.wapwag.authn.dao.TxAwareEntityManager;

@Component(property="osgi.name=user")
public class H2TxAwareEntityManager implements TxAwareEntityManager {
	
	private static Logger logger = LoggerFactory.getLogger(H2TxAwareEntityManager.class);
	
	private static final ImmutableList<String> ddl =
		ImmutableList.of(
			// 0
			"create table if not exists users("
			+ "  id bigint primary key"
			+ ")",
			// 1
			"merge into users(id) values(1)",
			// 2
			"create table if not exists access_tokens("
		    + "  user_id bigint,"
		    + "  client_id varchar(32),"
		    + "  expiration bigint,"
		    + "  handle varchar(16) primary key"
		    + ")",
		    // 3
		    "merge into access_tokens(user_id, client_id, expiration, handle) values(1, 'client1', 9223372036854775807, 'token1')");
		
	@Reference(target="(osgi.unit.name=user-jpa)")
	protected JpaTemplate jpa;
	
	@Override
	public void init() throws Exception {
		for (String _sql : ddl) {
			logger.info("H2 DB Initialization: {}", _sql);
			try {			
				final String sql = _sql;
				tx(new EmConsumer() {
					@Override
					public void accept(EntityManager em) {
						em.createNativeQuery(sql).executeUpdate();
					}					
				});				
			} catch (Exception e) {
				throw new Exception("Error executing DDL query: "+_sql, e);
			}
		}
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

	/*
	
	Alternative implementation (RESOURCE_LOCAL transactions)
	
	@Reference(target="(osgi.unit.name=user-jpa)")
	protected EntityManager entityManager;
	
	@Reference
	protected Coordinator coordinator;

	@Override
	public void tx(EmConsumer emConsumer) throws Exception {
		EntityManager em = entityManager;
		
		Coordination c = coordinator.begin(H2TxAwareEntityManager.class.getName(), 0);
		try {
			synchronized (em) {
				boolean txActive = em.getTransaction().isActive();
				if (!txActive) {
					em.getTransaction().begin();
				}
				
				try {
					emConsumer.accept(em);
				} catch (Throwable e) {
					if (!txActive) {
						try {
							em.getTransaction().rollback();
						} catch (Throwable ex) {
							logger.warn("Error during rollback", ex);
						}
					}
					throw new InvocationTargetException(e);
				}
				
				if (!txActive) {
					em.getTransaction().commit();
				}
			}
		} finally {
			c.end();
		}
	}

	@Override
	public <T> T txExpr(EmFunction<T> emFunction) throws Exception {
		EntityManager em = entityManager;
		
		Coordination c = coordinator.begin(H2TxAwareEntityManager.class.getName(), 0);
		try {
			synchronized (em) {
				boolean txActive = em.getTransaction().isActive();
				if (!txActive) {
					em.getTransaction().begin();
				}
				
				T result;
				try {
					result = emFunction.apply(em);
				} catch (Throwable e) {
					if (!txActive) {
						try {
							em.getTransaction().rollback();
						} catch (Throwable ex) {
							logger.warn("Error during rollback", ex);
						}
					}
					throw new InvocationTargetException(e);
				}
				
				if (!txActive) {
					em.getTransaction().commit();
				}
				
				return result;
			}
		} finally {
			c.end();
		}
	}
	
	
	 */

}
