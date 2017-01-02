package com.youku.java.copyright.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.youku.java.copyright.runtime.BeansTest;
import com.youku.java.raptor.validation.Validation;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BeansTest.class})
public class ValidationTest {

	@Autowired
	private Validation validation;
	
	@Test
	public void insert(){
//		validation.getObject("{\"videoId\":1,\"platform\":1,\"aa\":1}",
//				Analogue.class, new String[]{"videoId","platform"});
		
	}
}
