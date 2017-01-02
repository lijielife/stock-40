package com.youku.java.copyright.view;

import java.util.HashMap;

import com.youku.java.copyright.bean.User;
import com.youku.java.copyright.util.DateTool;

public class UserView extends HashMap<String, Object>{

	private static final long serialVersionUID = 1L;

	public UserView(User user) {
		put("name", user.getName());
		put("mobile", user.getMobile());
		put("companyName", user.getName()+"的公司");
		put("createTime", DateTool.standardSdf().format(user.getCreateTime()));
	}
	
}
