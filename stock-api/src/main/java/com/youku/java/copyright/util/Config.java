package com.youku.java.copyright.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {

	@Value("${core.mysql.ip}")
	public String coreDbIp;
	
	@Value("${core.mysql.port}")
	public String coreDbPort;
	
	@Value("${core.mysql.dbName}")
	public String coreDbName;
	
	@Value("${core.mysql.userName}")
	public String coreDbUserName;
	
	@Value("${core.mysql.password}")
	public String coreDbPassword;
	
	@Value("${log.mysql.ip}")
	public String logDbIp;
	
	@Value("${log.mysql.port}")
	public String logDbPort;
	
	@Value("${log.mysql.dbName}")
	public String logDbName;
	
	@Value("${log.mysql.userName}")
	public String logDbUserName;
	
	@Value("${log.mysql.password}")
	public String logDbPassword;

	@Value("${redis.use}")
	public boolean redisUse;

	@Value("${redis.ipport}")
	public String redisIpPort;
	
}
