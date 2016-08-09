package net.wapwag.authn.util;

import net.wapwag.authn.AuthenticationService;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import javax.servlet.ServletException;
import java.util.function.Consumer;

/**
 * Used to get osgi service reference.
 * Created by Administrator on 2016/7/18.
 */
public class OSGIUtil {
	
	private static AuthenticationService testAuthnService = null;

    /**
     * This is a alternative approch to get the osgi bundle in the servlet layer.
     * @param fn Your logical operation codes which depend on the AuthenticationService.
     * @param cls Your servlet class which registered in the osgi framework.
     * @param <T> Your servlet gneretic which registered in the osgi framework.
     * @see Consumer
     * @throws ServletException throws exceptions if not get the bundle or occured error in the process.
     */
    public static final <T> void useAuthenticationService(Consumer<AuthenticationService> fn, Class<T> cls)
            throws ServletException {    	
    	AuthenticationService authenticationService; 
    
    	Bundle bundle = FrameworkUtil.getBundle(cls);
    	BundleContext ctx = null;
    	ServiceReference<AuthenticationService> reference  = null;
    	if (bundle != null) {
	        ctx = bundle.getBundleContext();
	        reference = ctx.getServiceReference(AuthenticationService.class);
	        authenticationService = ctx.getService(reference);
    	} else {
    		authenticationService = testAuthnService;
    	}

        if (authenticationService == null) {
            throw new ServletException("AuthenticationService reference not bound");
        } else {
            try {
                fn.accept(authenticationService);
            } catch (Throwable e) {
                throw new ServletException("Error processing request", e);
            }
            finally {
            	if (ctx != null && reference != null) {
            		ctx.ungetService(reference);
            	}
            }
        }
    }
    
    /**
     * FOR TEST PURPOSES: work around OSGi dependency injection in the JUnit
     * environment 
     * @param authnService
     */
    public static void setAuthenticationService(AuthenticationService authnService) {
    	testAuthnService = authnService;
    }

}
