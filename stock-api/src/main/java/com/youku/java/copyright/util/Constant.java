package com.youku.java.copyright.util;
/**
 * 系统配置，一经定义只能添加，不能修改！
 */
public class Constant {
	
	public static class OpCode {
		public static final short REPEAT_OBJECTID = 601;
		
		public static final short LOGIN_FAIL = 604;
		
		public static final short PERMISSION_DENIED = 605;
		
	}
	
	public static class Stock {
		//登陆有效期
		public static final String LOGIN_URL = "http://www.kehue.com/login.html";
		
	}
	
	public static class Time {
		//登陆有效期
		public static final int LOGIN_TIME = 60 * 10;
		
	}
	
	public static class RedisNameSpace {
		public static final String NAMESPACE = "stock";
		
		public static final String LOGIN = NAMESPACE+"_login_user_id_";
	}
	
	public static class TimeType {
		public static final int MONTH = 1;

		public static final int WEEK = 2;

		public static final int DAY = 2;
		
		public static final int HOUR = 3;
	}
	
	public static class Length {
		public static final long MONTH = 1000l * 60 * 60 * 24 * 30;

		public static final long DAY = 1000l * 60 * 60 * 24;
		
		public static final long HOUR = 1000l * 60 * 60;
	}
	
}