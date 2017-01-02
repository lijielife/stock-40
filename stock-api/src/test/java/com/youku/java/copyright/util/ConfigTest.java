package com.youku.java.copyright.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.youku.java.copyright.runtime.BeansTest;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BeansTest.class})
public class ConfigTest {

	
	@Autowired
	private Config config;
	
	@Test
	public void getConfig() throws Exception {
		System.out.println(config.coreDbIp);
	}
	
}
