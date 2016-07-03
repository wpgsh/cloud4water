package net.wapwag.authn.ui;

import org.ops4j.pax.web.service.WebContainer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.HttpContext;

import com.thingswise.appframework.http.utils.WebContainerConfigurator;
import com.thingswise.appframework.http.utils.WebContainerConsumer;

@Component
public class WebApp {

	@Reference
	protected WebContainer webContainer;
	
	@Activate
	protected void activate() throws Exception {
		// initialize webapp
		
		WebContainerConfigurator.create(webContainer).
			distributedSession(true).
			webapp(new WebContainerConsumer() {
				@Override
				public void apply(WebContainer webContainer, HttpContext httpContext) throws Exception {
					// enable default jsp support
					webContainer.registerJsps(null, httpContext);
					//webContainer.registerJspServlet(new String[] { "/login" }, httpContext, "login.jsp");
					// register static resources at /static
					webContainer.registerResources("/static", "/static", httpContext);
				}				
			}).
			activate("authn");
	}
	
	@Deactivate
	protected void deactivate() throws Exception {
		WebContainerConfigurator.create(webContainer).
			distributedSession(true).
			webapp(new WebContainerConsumer() {
				@Override
				public void apply(WebContainer webContainer, HttpContext httpContext) throws Exception {
					webContainer.unregisterJsps(httpContext);					
				}				
			}).deactivate("authn");		
	}

}
