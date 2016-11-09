package net.wapwag.wemp.ui;

import net.wapwag.wemp.WaterEquipmentService;
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
	
	private static WaterEquipmentService waterEquipmentTestService = null;

    /**
     * This is a alternative approch to get the osgi bundle in the servlet layer.
     * @param fn Your logical operation codes which depend on the WaterEquipmentService.
     * @param cls Your servlet class which registered in the osgi framework.
     * @param <T> Your servlet gneretic which registered in the osgi framework.
     * @see Consumer
     * @throws ServletException throws exceptions if not get the bundle or occured error in the process.
     */
    @SuppressWarnings("Duplicates")
    static final <T> void useWaterEquipmentService(Consumer<WaterEquipmentService> fn, Class<T> cls)
            throws ServletException {    	
    	WaterEquipmentService waterEquipmentService; 
    
    	Bundle bundle = FrameworkUtil.getBundle(cls);
    	BundleContext ctx = null;
    	ServiceReference<WaterEquipmentService> reference  = null;
    	if (bundle != null) {
	        ctx = bundle.getBundleContext();
	        reference = ctx.getServiceReference(WaterEquipmentService.class);
	        waterEquipmentService = ctx.getService(reference);
    	} else {
    		waterEquipmentService = waterEquipmentTestService;
    	}

        if (waterEquipmentService == null) {
            throw new ServletException("WaterEquipmentService reference not bound");
        } else {
            try {
                fn.accept(waterEquipmentService);
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
     * @param waterEquipmentService
     */
    static void setWaterEquipmentService(WaterEquipmentService waterEquipmentService) {
        waterEquipmentTestService = waterEquipmentService;
    }

}
