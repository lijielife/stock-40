package com.youku.java.copyright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.youku.java.copyright.service.CustomerService;
import com.youku.java.copyright.service.GoodService;
import com.youku.java.copyright.service.LogService;
import com.youku.java.copyright.service.RecordService;
import com.youku.java.copyright.service.StatUserService;
import com.youku.java.copyright.service.UserService;
import com.youku.java.copyright.util.JedisUtil;
import com.youku.java.raptor.auth.NeedLogin;
import com.youku.java.raptor.validation.Validation;


@NeedLogin
@RestController
public class BaseController {

	@Autowired
	UserService userService;
	
	@Autowired
	GoodService goodService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	RecordService recordService;
	
	@Autowired
	StatUserService statUserService;
	
	@Autowired
	LogService logService;
	
	@Autowired
	Validation validation;
	
	@Autowired
	JedisUtil jedisUtil;
	
}
