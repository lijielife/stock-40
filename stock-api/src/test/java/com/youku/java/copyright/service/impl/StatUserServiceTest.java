package com.youku.java.copyright.service.impl;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.youku.java.copyright.mapper.StatUserDayMapper;
import com.youku.java.copyright.runtime.BeansTest;
import com.youku.java.copyright.service.CustomerService;
import com.youku.java.copyright.service.StatUserService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BeansTest.class})
public class StatUserServiceTest {

	@Autowired
	StatUserService statUserService;
	
	@Autowired
	StatUserDayMapper statUserDayMapper;
	
	@Autowired
	CustomerService customerService;
	
	
//	@Test
	public void selectOne() {
//		System.out.println(customerService.selectOne(1l));
		System.out.println(statUserDayMapper.selectOne(1l));
	}
	
}
