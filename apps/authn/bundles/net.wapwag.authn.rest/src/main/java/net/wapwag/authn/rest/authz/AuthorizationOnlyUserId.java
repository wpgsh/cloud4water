package net.wapwag.authn.rest.authz;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.thingswise.appframework.jaxrs.utils.Authorization;

@Authorization
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizationOnlyUserId {
	
	public String target();
	
}
