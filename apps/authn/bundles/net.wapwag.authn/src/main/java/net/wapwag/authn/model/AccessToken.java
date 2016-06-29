package net.wapwag.authn.model;

public class AccessToken {

	public final long userId;
	
	public final long expiration;
	
	public final String clientId;
	
	public final String handle;

	public AccessToken(long userId, long expiration, String clientId, String handle) {
		super();
		this.userId = userId;
		this.expiration = expiration;
		this.clientId = clientId;
		this.handle = handle;
	}

}
