package net.wapwag.wemp.rest.bindings;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorResponse {

	private String errorMessage;

	@XmlElement
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public static class ErrorResponseBuilder {
		
		private ErrorResponse errorResponse = new ErrorResponse();
		
		public ErrorResponseBuilder setErrorMessage(String errorMessage) {
			errorResponse.setErrorMessage(errorMessage);
			return this;
		}
		
		public ErrorResponse build() {
			return errorResponse;
		}
		
	}
	
	public static ErrorResponseBuilder newErrorResponse() {
		return new ErrorResponseBuilder();
	}
	
}
