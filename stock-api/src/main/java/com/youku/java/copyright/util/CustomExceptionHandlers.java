package com.youku.java.copyright.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.youku.java.copyright.exception.DataExistException;
import com.youku.java.copyright.exception.HttpException;
import com.youku.java.copyright.exception.LoginFailException;
import com.youku.java.copyright.exception.PermissionDeniedException;
import com.youku.java.copyright.exception.StateChangeException;
import com.youku.java.copyright.util.Constant.OpCode;
import com.youku.java.raptor.exception.ExceptionHandlers;
import com.youku.java.raptor.exception.ExceptionResponse;
import com.youku.java.raptor.util.Lang;
/*
 * @author chenlisong
 */
@Component
public class CustomExceptionHandlers extends ExceptionHandlers {
	
	@Autowired
	private Lang lang;

	@ExceptionHandler(DataExistException.class)
	public ExceptionResponse handleI(Exception e) {
		return new ExceptionResponse(601, lang.getLang(e.getMessage()));
    }

	@ExceptionHandler(StateChangeException.class)
	public ExceptionResponse handleII(Exception e) {
		return new ExceptionResponse(602, lang.getLang(e.getMessage()));
    }

	@ExceptionHandler(HttpException.class)
	public ExceptionResponse handlIII(Exception e) {
		return new ExceptionResponse(603, lang.getLang(e.getMessage()));
    }

	@ExceptionHandler(LoginFailException.class)
	public ExceptionResponse loginFail(Exception e) {
		return new ExceptionResponse(OpCode.LOGIN_FAIL, lang.getLang(e.getMessage()));
    }

	@ExceptionHandler(PermissionDeniedException.class)
	public ExceptionResponse permissionDenied(Exception e) {
		return new ExceptionResponse(OpCode.PERMISSION_DENIED, lang.getLang(e.getMessage()));
    }
	
}
