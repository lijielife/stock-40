package com.youku.java.copyright.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.youku.java.copyright.runtime.BeansTest;
import com.youku.java.copyright.util.JedisUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BeansTest.class})
public class JedisUtilTest {
	
	@Autowired
	JedisUtil jedisUtil;

	@Test
	public void test() throws InterruptedException {
		
		jedisUtil.set("abc", "123", 10);
		System.out.println(jedisUtil.get("abc"));
		Thread.sleep(5000);
		System.out.println(jedisUtil.get("abc"));
	}
	
}
