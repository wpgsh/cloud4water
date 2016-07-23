package net.wapwag.authn.h2.hibernate;

import com.google.common.collect.ImmutableList;
import net.wapwag.authn.dao.TxAwareEntityManager;
import org.apache.aries.jpa.template.EmConsumer;
import org.apache.aries.jpa.template.EmFunction;
import org.apache.aries.jpa.template.JpaTemplate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

@Component(property="osgi.name=user")
public class H2TxAwareEntityManager implements TxAwareEntityManager {
	
	private static Logger logger = LoggerFactory.getLogger(H2TxAwareEntityManager.class);
	
	private static final ImmutableList<String> ddl =
		ImmutableList.of(
            //Reinitialize database
			"drop table if exists users",
            "drop table if exists access_tokens",
            "drop table if exists registered_clients",
			// 0
			"create table if not exists users("
				+ "  id bigint primary key AUTO_INCREMENT,"
				+ "  enabled varchar(1),"
				+ "  username varchar(20),"
				+ "  password_hash varchar(32),"
				+ "  password_salt varchar(32),"
				+ "  homepage varchar(50),"
				+ "  name varchar(50),"
				+ "  title varchar(50),"
				+ "  avatar varchar(50),"
				+ "  avatar_id bigint,"
				+ "  phone1 varchar(50),"
				+ "  phone2 varchar(50),"
				+ "  email varchar(50),"
				+ "  email_verified varchar(1),"
				+ "  email_verification_token varchar(32),"
				+ "  email_verification_expiration varchar(1)"
			+ ")",
			// 1
            "insert into users(enabled, username, password_hash, password_salt, homepage, name, title, avatar, avatar_id, phone1, phone2, email, email_verified, email_verification_token, email_verification_expiration) values("
            + "'1', 'test1', 'dfdf', 'dfdfd', 'http://www.baidu.com', 'test', 'title', 'sds', 1, '121212', '121212', '1163525902@qq.com', '1', 'dfdf', '1')",
			"insert into users(enabled, username, password_hash, password_salt, homepage, name, title, avatar, avatar_id, phone1, phone2, email, email_verified, email_verification_token, email_verification_expiration) values("
            + "'1', 'test2', 'dfdf', 'dfdfd', 'http://www.baidu.com', 'test', 'title', 'sds', 1, '121212', '121212', '1163525902@qq.com', '1', 'dfdf', '1')",
			// 2
            "create table if not exists access_tokens("
				+ "  handle varchar(32) primary key,"
				+ "  user_id bigint,"
				+ "  client_id bigint,"
				+ "  scope varchar(100),"
				+ "  authorization_code varchar(32) unique,"
				+ "  ac_expiration bigint"
            + ")",
		    // 3
//		    "insert into access_tokens(user_id, client_id, authorization_code, handle, ac_expiration, scope) values(1, 1, 'dsfjdjfk23skjdsds1','token1', 9223372036854775807, '1,2,3,4')",
		    "insert into access_tokens(user_id, client_id, authorization_code, handle, ac_expiration, scope) values(2, 1, 'dsfjdjfk23skjdsds2','token2', 9223372036854775807, '1,2,3,4')",
		    "insert into access_tokens(user_id, client_id, authorization_code, handle, ac_expiration, scope) values(1, 3, 'dsfjdjfk23skjdsds3','token3', 9223372036854775807, '1,2,3,4')",
		    "insert into access_tokens(user_id, client_id, authorization_code, handle, ac_expiration, scope) values(1, 4, 'dsfjdjfk23skjdsds4','token4', 9223372036854775807, '1,2,3,4')",
            //4
            "create table if not exists registered_clients("
            + "  id bigint primary key AUTO_INCREMENT,"
            + "  client_id varchar(32),"
            + "  client_secret varchar(32),"
            + "  redirect_uri varchar(100) unique"
            + ")",
            //5
            "insert into registered_clients(client_id, client_secret, redirect_uri) values('client1', 'dfdjfjkdkj23klaa1', 'http://www.baidu.com')",
            "insert into registered_clients(client_id, client_secret, redirect_uri) values('client2', 'dfdjfjkdkj23klaa2', 'http://www.baidu.com2')",
            "insert into registered_clients(client_id, client_secret, redirect_uri) values('client3', 'dfdjfjkdkj23klaa3', 'http://www.baidu.com3')",
            "insert into registered_clients(client_id, client_secret, redirect_uri) values('client4', 'dfdjfjkdkj23klaa4', 'http://www.baidu.com4')",
            "insert into registered_clients(client_id, client_secret, redirect_uri) values('wpgclient', 'dfdjfjkdkj23klaa1', 'http://localhost:8181/authn/wpg')"
        );

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
