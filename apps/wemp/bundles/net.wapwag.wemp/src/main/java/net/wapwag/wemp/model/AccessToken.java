package net.wapwag.wemp.model;

import java.util.Set;

public class AccessToken {

	public final String userId;
	
	public final String clientId;
	
	public final String handle;
	
	public final Set<String> scope;

	public AccessToken(String userId, String clientId, String handle, Set<String> scope) {
		super();
		this.userId = userId;
		this.clientId = clientId;
		this.handle = handle;
		this.scope = scope;
	}

}
