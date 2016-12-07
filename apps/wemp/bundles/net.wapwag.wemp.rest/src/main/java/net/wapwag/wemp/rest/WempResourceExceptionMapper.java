package net.wapwag.wemp.rest;

import net.wapwag.wemp.rest.bindings.ErrorResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Component(service = WempResourceExceptionMapper.class)
public class WempResourceExceptionMapper implements ExceptionMapper<Throwable> {

    private static Logger logger = LoggerFactory.getLogger(WempResourceExceptionMapper.class);

	@Override
	public Response toResponse(Throwable throwable) {

        logger.error(ExceptionUtils.getStackTrace(throwable));

        Response.Status errorStatus = Response.Status.BAD_REQUEST;

        // use defualt response for the java rs exception class
        if (throwable instanceof WebApplicationException) {
            WebApplicationException webApplicationException = (WebApplicationException) throwable;
            return webApplicationException.getResponse();
        } else if (throwable instanceof RuntimeException) {
            errorStatus = Response.Status.BAD_REQUEST;
        } else if (throwable instanceof Exception) {
            errorStatus = Response.Status.INTERNAL_SERVER_ERROR;
        }
		return Response.status(errorStatus).
                type(MediaType.APPLICATION_JSON).
                entity(ErrorResponse.newErrorResponse().
                    setErrorMessage(throwable.getMessage()).build())
				.build();
	}

}
