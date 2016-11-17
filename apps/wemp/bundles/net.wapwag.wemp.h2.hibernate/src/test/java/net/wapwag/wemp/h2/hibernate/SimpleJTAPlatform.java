package net.wapwag.wemp.h2.hibernate;

import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

public class SimpleJTAPlatform extends AbstractJtaPlatform {

	@Override
	protected TransactionManager locateTransactionManager() {
		return (TransactionManager) jndiService().locate("java/SampleTransactionManager");
	}

	@Override
	protected UserTransaction locateUserTransaction() {
		return (UserTransaction) jndiService().locate("java/UserTransaction");
	}

}
