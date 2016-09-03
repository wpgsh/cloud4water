package net.wapwag.wemp.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Component;

@Provider
@Component(service=WempResourceExceptionMapper.class)
public class WempResourceExceptionMapper implements ExceptionMapper<WempResourceException> {

	@Override
	public Response toResponse(WempResourceException exception) {
		// TODO provide appropriate mapping
		throw new RuntimeException("TODO - not implemented");
	}

}
