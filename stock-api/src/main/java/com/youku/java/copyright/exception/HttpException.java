package com.youku.java.copyright.exception;
/*
 * @author chenlisong
 */
public class HttpException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public HttpException() {
		super();
	}
	
	public HttpException(String s) {
		super(s);
	}
	
	public HttpException(Throwable t) {
		super(t);
	}
	
	public HttpException(String msg, Throwable t) {
		super(msg, t);
	}

}
