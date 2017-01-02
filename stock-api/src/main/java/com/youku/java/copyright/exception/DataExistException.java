package com.youku.java.copyright.exception;
/*
 * @author chenlisong
 */
public class DataExistException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public DataExistException() {
		super();
	}
	
	public DataExistException(String s) {
		super(s);
	}
	
	public DataExistException(Throwable t) {
		super(t);
	}
	
	public DataExistException(String msg, Throwable t) {
		super(msg, t);
	}

}
