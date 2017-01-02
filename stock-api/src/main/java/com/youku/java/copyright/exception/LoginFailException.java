package com.youku.java.copyright.exception;
/*
 * @author chenlisong
 */
public class LoginFailException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public LoginFailException() {
		super();
	}
	
	public LoginFailException(String s) {
		super(s);
	}
	
	public LoginFailException(Throwable t) {
		super(t);
	}
	
	public LoginFailException(String msg, Throwable t) {
		super(msg, t);
	}

}
