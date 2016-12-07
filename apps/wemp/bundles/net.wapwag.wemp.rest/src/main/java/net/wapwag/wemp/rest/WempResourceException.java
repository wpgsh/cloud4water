package net.wapwag.wemp.rest;

import javax.ws.rs.core.Response;

public class WempResourceException extends Exception {

	public WempResourceException() {
	}

	public WempResourceException(String msg) {
		super(msg);
	}

	public WempResourceException(Throwable cause) {
		super(cause);
	}

    public WempResourceException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
