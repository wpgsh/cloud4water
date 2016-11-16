package net.wapwag.authn.rest;

import static org.junit.Assert.*;

import org.junit.Test;

import net.wapwag.authn.AuthenticationServiceException;

public class AuthenticationResourceExceptionTest {

	@Test
	public void testAuthenticationResourceException() {
		AuthenticationResourceException authenticationServiceException = new AuthenticationResourceException();
		assertNotNull(authenticationServiceException);
	}

	@Test
	public void testAuthenticationResourceExceptionStringThrowable() {
		AuthenticationResourceException authenticationServiceException = new AuthenticationResourceException(new NullPointerException());
		assertNotNull(authenticationServiceException);
	}

	@Test
	public void testAuthenticationResourceExceptionString() {
		AuthenticationResourceException authenticationServiceException = new AuthenticationResourceException("Exception");
		assertNotNull(authenticationServiceException);
	}

	@Test
	public void testAuthenticationResourceExceptionThrowable() {
		AuthenticationResourceException authenticationServiceException = new AuthenticationResourceException("Exception", new NullPointerException());
		assertNotNull(authenticationServiceException);
	}

}
