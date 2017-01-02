package com.youku.java.copyright.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youku.java.copyright.util.Config;

@Service
public class BatchCommandService {

	@Autowired
	private Config config;
		
	private Log log = LogFactory.getLog(BatchCommandService.class);
	
	public void method() {
		log.info("begin");
		log.info("end");
		System.out.println("batch");
	}
	
}
