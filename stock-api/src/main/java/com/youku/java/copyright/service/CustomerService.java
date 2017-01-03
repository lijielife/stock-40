package com.youku.java.copyright.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youku.java.copyright.mapper.CustomerMapper;

@Service
public class CustomerService {

	@Autowired
	private CustomerMapper customerMapper;

}
