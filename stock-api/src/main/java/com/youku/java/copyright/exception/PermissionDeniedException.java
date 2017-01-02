package com.youku.java.copyright.exception;
/*
 * @author chenlisong
 */
public class PermissionDeniedException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public PermissionDeniedException() {
		super();
	}
	
	public PermissionDeniedException(String s) {
		super(s);
	}
	
	public PermissionDeniedException(Throwable t) {
		super(t);
	}
	
	public PermissionDeniedException(String msg, Throwable t) {
		super(msg, t);
	}

}
