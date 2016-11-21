package net.wapwag.authn.h2.hibernate;


import static net.wapwag.authn.h2.hibernate.PrepareContext.transactionManager;

import javax.persistence.EntityManager;

import com.google.common.collect.ImmutableList;

/**
 * Data insert before other test,
 * <p>Use {@link org.junit.Assert} instead of .{@link junit.framework.TestCase} which suggested for the junit4</p>
 * Created by Administrator on 2016/10/31 0031.
 */
class DataInsert {

    private static EntityManager em;
    
    private static final ImmutableList<String> ddl =
    		ImmutableList.of(
                //Reinitialize database
    			"drop table if exists users",
                "drop table if exists access_tokens",
                "drop table if exists registered_clients",
                "drop table if exists image",
    			// 0
    			"create table if not exists users("
    				+ "  id bigint primary key AUTO_INCREMENT,"
    				+ "  enabled varchar(1),"
    				+ "  username varchar(20),"
    				+ "  password_hash varchar(50),"
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
                + "'1', 'test1', 'bff5c1b718386f23ac472d983a02907671f8932d', '1478156919009', 'http://www.baidu.com', 'test', 'title', 'sds', 1, '121212', '121212', '1163525902@qq.com', '1', 'dfdf', '1')",
    			"insert into users(enabled, username, password_hash, password_salt, homepage, name, title, avatar, avatar_id, phone1, phone2, email, email_verified, email_verification_token, email_verification_expiration) values("
                + "'1', 'test2', 'bff5c1b718386f23ac472d983a02907671f8932d', '1478156919009', 'http://www.baidu.com', 'test', 'title', 'sds', 1, '121212', '121212', '1163525902@qq.com', '1', 'dfdf', '1')",
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
    		    "insert into access_tokens(user_id, client_id, authorization_code, handle, ac_expiration, scope) values(1, 1, 'dsfjdjfk23skjdsds1','token1', 9223372036854775807, '1,2,3,4')",
    		    "insert into access_tokens(user_id, client_id, authorization_code, handle, ac_expiration, scope) values(2, 1, 'dsfjdjfk23skjdsds2','token2', 9223372036854775807, '1,2,3,4')",
    		    "insert into access_tokens(user_id, client_id, authorization_code, handle, ac_expiration, scope) values(1, 3, 'dsfjdjfk23skjdsds3','token3', 9223372036854775807, '1,2,3,4')",
    		    "insert into access_tokens(user_id, client_id, authorization_code, handle, ac_expiration, scope) values(1, 4, 'dsfjdjfk23skjdsds4','token4', 9223372036854775807, '1,2,3,4')",
                //4
                "create table if not exists registered_clients("
                + "  id bigint primary key AUTO_INCREMENT,"
                + "  client_id varchar(32),"
                + "  client_secret varchar(32),"
                + "  client_vendor varchar(32),"
                + "  redirect_uri varchar(100) unique"
                + ")",
                //5
                "insert into registered_clients(client_id, client_secret, redirect_uri, client_vendor) values('client1', 'dfdjfjkdkj23klaa1', 'http://www.baidu.com', 'wapwag')",
                "insert into registered_clients(client_id, client_secret, redirect_uri, client_vendor) values('client2', 'dfdjfjkdkj23klaa2', 'http://www.baidu.com2', 'wapwag')",
                "insert into registered_clients(client_id, client_secret, redirect_uri, client_vendor) values('client3', 'dfdjfjkdkj23klaa3', 'http://www.baidu.com3', 'wapwag')",
                "insert into registered_clients(client_id, client_secret, redirect_uri, client_vendor) values('client4', 'dfdjfjkdkj23klaa4', 'http://www.baidu.com4', 'wapwag')",
                "insert into registered_clients(client_id, client_secret, redirect_uri, client_vendor) values('wpgclient', 'dfdjfjkdkj23klaa1', 'http://localhost:8181/authn/wpg', 'wapwag')",
                //6
                "create table if not exists image("
                        + "  id varchar(32) primary key,"
                        + "  image MEDIUMBLOB"
                 + ")"
            );

    static {
        try {
            em = PrepareContext.createEMF().createEntityManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void initialData() throws Exception {
        try {
            transactionManager.begin();

            try {			
				for (String _sql : ddl) {
					em.createNativeQuery(_sql).executeUpdate();
				};
    		} catch (Exception e) {
    			throw new Exception("Error executing DDL update", e);
    		}

            transactionManager.commit();
        } finally {
            em.close();
        }
    }
}
