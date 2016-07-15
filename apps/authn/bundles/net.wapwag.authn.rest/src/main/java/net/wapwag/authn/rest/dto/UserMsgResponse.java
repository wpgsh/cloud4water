package net.wapwag.authn.rest.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserMsgResponse {

	private final String msg;

	public UserMsgResponse(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
}
