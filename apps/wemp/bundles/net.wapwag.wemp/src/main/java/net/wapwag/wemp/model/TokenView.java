package net.wapwag.wemp.model;

import java.util.Set;

public class TokenView {

	private final String userId;
	
	private final String clientId;
	
	private final String handle;
	
	private final Set<String> scope;

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
