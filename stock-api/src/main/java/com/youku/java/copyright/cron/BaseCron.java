package com.youku.java.copyright.cron;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.youku.java.copyright.service.LogService;
import com.youku.java.copyright.service.UserService;
import com.youku.java.copyright.util.Config;

@Component
public class BaseCron{

	@Autowired
	UserService testService;

	@Autowired
	LogService logService;
	
	@Autowired
	Config config;
	
	Log log = LogFactory.getLog(BaseCron.class);
}
