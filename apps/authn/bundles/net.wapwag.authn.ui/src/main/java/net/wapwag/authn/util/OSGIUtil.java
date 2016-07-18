package net.wapwag.authn.util;

import net.wapwag.authn.AuthenticationService;
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
        BundleContext ctx = FrameworkUtil.getBundle(cls).getBundleContext();
        ServiceReference<AuthenticationService> reference = ctx.getServiceReference(AuthenticationService.class);
        AuthenticationService authenticationService = ctx.getService(reference);

        if (authenticationService == null) {
            throw new ServletException("AuthenticationService reference not bound");
        } else {
            try {
                fn.accept(authenticationService);
            } catch (Throwable e) {
                throw new ServletException("Error processing request", e);
            }
            finally {
                ctx.ungetService(reference);
            }
        }
    }

}
