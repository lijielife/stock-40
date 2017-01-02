package com.youku.java.copyright.exception;
/*
 * @author chenlisong
 */
public class StateChangeException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public StateChangeException() {
		super();
	}
	
	public StateChangeException(String s) {
		super(s);
	}
	
	public StateChangeException(Throwable t) {
		super(t);
	}
	
	public StateChangeException(String msg, Throwable t) {
		super(msg, t);
	}

}
