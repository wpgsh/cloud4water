package net.wapwag.authn.rest;

import static org.junit.Assert.*;

import org.junit.Test;

import net.wapwag.authn.AuthenticationServiceException;

public class InvalidRequestExceptionTest {

	@Test
	public void testInvalidRequestException() {
		InvalidRequestException authenticationServiceException = new InvalidRequestException();
		assertNotNull(authenticationServiceException);
	}

	@Test
	public void testInvalidRequestExceptionStringThrowable() {
		InvalidRequestException authenticationServiceException = new InvalidRequestException("Exception", new NullPointerException());
		assertNotNull(authenticationServiceException);
	}

	@Test
	public void testInvalidRequestExceptionString() {
		InvalidRequestException authenticationServiceException = new InvalidRequestException("Exception");
		assertNotNull(authenticationServiceException);
	}

	@Test
	public void testInvalidRequestExceptionThrowable() {
		InvalidRequestException authenticationServiceException = new InvalidRequestException(new NullPointerException());
		assertNotNull(authenticationServiceException);
	}

}
