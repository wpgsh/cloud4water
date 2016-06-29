package net.wapwag.authn.dao;


import javax.persistence.EntityManager;

import org.apache.aries.jpa.template.EmFunction;
import org.apache.aries.jpa.template.JpaTemplate;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.User;

@Component
public class UserDaoImpl implements UserDao {

	@Reference(target="(osgi.name=user)")
	protected TxAwareEntityManager entityManager;
	
	@Activate
	protected void init() throws Exception {
		entityManager.init();
	}
	
	@Deactivate
	protected void destroy() {
		
	}
	
	@Override
	public User getUser(final long uid) throws UserDaoException {
		try {
			return entityManager.txExpr(new EmFunction<User>() {
				@Override
				public User apply(EntityManager em) {
					return em.find(User.class, uid);
				}				
			});			
		} catch (Exception e) {
			throw new UserDaoException("Cannot get user entity", e);
		}
	}

	@Override
	public AccessToken lookupAccessToken(final String handle) throws UserDaoException {
		try {
			return entityManager.txExpr(new EmFunction<AccessToken>() {
				@Override
				public AccessToken apply(EntityManager em) {
					return em.find(AccessToken.class, handle);
				}				
			});			
		} catch (Exception e) {
			throw new UserDaoException("Cannot get access token", e);
		}
	}

}
