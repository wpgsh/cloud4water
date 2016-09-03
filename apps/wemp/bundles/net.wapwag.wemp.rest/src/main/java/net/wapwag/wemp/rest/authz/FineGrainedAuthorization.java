package net.wapwag.wemp.rest.authz;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.thingswise.appframework.jaxrs.utils.Authorization;

/**
 * The authorization scheme to implement a fine-grained user authorization.
 * 
 * For every resource method there is a configuration setting which defines
 * what permission a user needs on a target to be able to execute this method.
 * 
 * @author Alexander Lukichev
 *
 */
@Authorization
@Retention(RetentionPolicy.RUNTIME)
public @interface FineGrainedAuthorization {
	/**
	 * The required permission
	 * 
	 * @return
	 */
	public String permission();
	/**
	 * The expression that results in target id. Curly-braced parameters
	 * are substituted with corresponding path parameter values
	 * 
	 * @return
	 */
	public String target();

}
