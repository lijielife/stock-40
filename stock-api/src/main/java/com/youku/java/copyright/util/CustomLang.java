package com.youku.java.copyright.util;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.youku.java.raptor.util.Lang;
/*
 * @author chenlisong
 */
@Component
@Primary
@Qualifier("lang")
public class CustomLang extends Lang {

	private static final long serialVersionUID = 1L;

	public CustomLang() {
		super();
		put("NEED_LOGIN", "需要登录");
		put("INVALID_PARAM", "参数错误");
		put("INTERNAL_EXCEPTION", "内部错误");
	
		//throw new Exception("A_B_C$A$A")
		put("DATAEXIST", "该数据已存在。");
	}
	
}
