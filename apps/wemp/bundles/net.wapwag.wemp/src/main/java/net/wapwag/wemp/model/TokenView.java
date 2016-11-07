package net.wapwag.wemp.model;

import java.util.Set;

public class TokenView {

	public final String userId;
	
	public final String clientId;
	
	public final String handle;
	
	public final Set<String> scope;

	public TokenView(String userId, String clientId, String handle, Set<String> scope) {
		super();
		this.userId = userId;
		this.clientId = clientId;
		this.handle = handle;
		this.scope = scope;
	}

	public static TokenView newInstance(String userId, String clientId, String handle, Set<String> scope) {
		return new TokenView(userId, clientId, handle, scope);
	}

}
