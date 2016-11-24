package net.wapwag.authn.model;

public class UserMsgResponse {
	
	private boolean flag;

	private final String msg;

	public UserMsgResponse(boolean flag, String msg) {
		this.msg = msg;
		this.flag = flag;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public boolean getFlag(){
		return flag;
	}
}
