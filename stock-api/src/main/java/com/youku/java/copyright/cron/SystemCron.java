package com.youku.java.copyright.cron;

import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/**
 * @author chenlisong
 */
@Component
public class SystemCron extends BaseCron{	
	
	@Scheduled(cron="${cron.test}")
    public void test () throws Exception{
    	MDC.put("logFileName", "test");
		log.info("test execute.");
		System.out.println("test execute.");
    }
}