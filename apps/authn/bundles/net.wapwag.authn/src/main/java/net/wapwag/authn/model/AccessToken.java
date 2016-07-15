package net.wapwag.authn.model;

import com.google.common.collect.ImmutableSet;

public class AccessToken {

	public final long userId;
	
	public final long expiration;
	
	public final String clientId;
	
	public final String handle;
	
	public final ImmutableSet<String> scope;

	public AccessToken(long userId, long expiration, String clientId, String handle, ImmutableSet<String> scope) {
		super();
		this.userId = userId;
		this.expiration = expiration;
		this.clientId = clientId;
		this.handle = handle;
		this.scope = scope;
	}

}
