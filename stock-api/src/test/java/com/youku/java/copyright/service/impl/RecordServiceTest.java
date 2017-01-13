package com.youku.java.copyright.service.impl;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.youku.java.copyright.bean.Record;
import com.youku.java.copyright.bean.User;
import com.youku.java.copyright.runtime.BeansTest;
import com.youku.java.copyright.service.RecordService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BeansTest.class})
public class RecordServiceTest {

	@Autowired
	RecordService recordService;
	
//	@Test
	public void sell() {
		User loginInfo = new User();
		loginInfo.setId(2l);
		
		Record record = new Record();
		record.setCustomerId(2l);
		record.setGoodId(2l);
		record.setNumber(20);
		record.setSellPrice(6000.0);
		record.setUserId(2l);
		
		recordService.sell(record, loginInfo);
	}
	
//	@Test
	public void stock() {
		User loginInfo = new User();
		loginInfo.setId(2l);
		
		Record record = new Record();
		record.setCustomerId(2l);
		record.setGoodId(2l);
		record.setNumber(30);
		record.setProductionPrice(1000.0);
		record.setOtherPrice(45.0);
		record.setUserId(2l);
		
		recordService.stock(record, loginInfo);
	}
	
}
