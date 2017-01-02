package com.youku.java.copyright.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.youku.java.copyright.bean.User;
import com.youku.java.copyright.exception.LoginFailException;
import com.youku.java.copyright.util.Constant.RedisNameSpace;
import com.youku.java.copyright.util.Constant.Time;
import com.youku.java.copyright.view.UserView;
import com.youku.java.raptor.auth.NeedLogin;

//@NeedLogin
@RestController
public class UserController extends BaseController{

	@NeedLogin
	@RequestMapping(value = "/needLogin", method = RequestMethod.GET)
	@ResponseBody
	public Object needLogin(){
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("data", userService.selectAll());
		return result;
	}
	
	@RequestMapping(value = "/notNeedLogin", method = RequestMethod.GET)
	@ResponseBody
	public Object notNeedLogin(){
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("data", userService.selectAll());
		return result;
	}

	@RequestMapping(value = "/users", method = RequestMethod.POST)
	@ResponseBody
	public Object userAdd(@RequestBody String body){
		User user = validation.getObject(body, User.class, new String[]{"name", "mobile", "password"});
		userService.insert(user);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("id", user.getId());
		return result;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Object login(@RequestBody String body){
		
		User user = validation.getObject(body, User.class, new String[]{"mobile", "password"});
		
		User loginInfo = userService.selectByMobile(user.getMobile());
		String ticket = UUID.randomUUID().toString();
		if(loginInfo != null && loginInfo.getPassword().equals(user.getPassword())) {
			jedisUtil.set(RedisNameSpace.LOGIN + ticket, JSON.toJSONString(loginInfo), Time.LOGIN_TIME);
		}else {
			throw new LoginFailException("账号或者密码错误");
		}
		
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("loginInfo", new UserView(loginInfo));
		result.put("ticket", ticket);
		return result;
	}

	//@RequestParam(required = false, value = "ticket", defaultValue = "") String ticket
	
	@RequestMapping(value = "/loginInfo", method = RequestMethod.GET)
	@NeedLogin
	public Object loginInfo(User loginInfo){
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("loginInfo", new UserView(loginInfo));
		return result;
	}
}
