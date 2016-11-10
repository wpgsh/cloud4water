package net.wapwag.authn;

import static org.junit.Assert.*;

import org.junit.Test;

public class AuthenticationServiceExceptionTest {

	@Test
	public void testAuthenticationServiceException() {
		AuthenticationServiceException authenticationServiceException = new AuthenticationServiceException();
		assertNotNull(authenticationServiceException);
	}

	@Test
	public void testAuthenticationServiceExceptionString() {
		AuthenticationServiceException authenticationServiceException = new AuthenticationServiceException("Exception");
		assertNotNull(authenticationServiceException);
	}

	@Test
	public void testAuthenticationServiceExceptionThrowable() {
		AuthenticationServiceException authenticationServiceException = new AuthenticationServiceException(new NullPointerException());
		assertNotNull(authenticationServiceException);
	}

	@Test
	public void testAuthenticationServiceExceptionStringThrowable() {
		AuthenticationServiceException authenticationServiceException = new AuthenticationServiceException("Exception", new NullPointerException());
		assertNotNull(authenticationServiceException);
	}

}
