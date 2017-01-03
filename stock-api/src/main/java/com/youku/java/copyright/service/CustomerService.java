package com.youku.java.copyright.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youku.java.copyright.bean.Customer;
import com.youku.java.copyright.bean.User;
import com.youku.java.copyright.exception.DataExistException;
import com.youku.java.copyright.exception.PermissionDeniedException;
import com.youku.java.copyright.mapper.CustomerMapper;
import com.youku.java.copyright.util.CommonUtil;
import com.youku.java.copyright.util.DateTool;
import com.youku.java.copyright.util.MergerUtil;
import com.youku.java.raptor.exception.InvalidArgumentException;

@Service
public class CustomerService {

	@Autowired
	private CustomerMapper customerMapper;

	public long insert(Customer customer, User loginInfo) {
		
		//如果数据已存在则提示异常
		Customer src = selectByMobile(loginInfo.getId(), customer.getType(), customer.getMobile());
		if(src != null) {
			throw new DataExistException("已存在的联系人");
		}
		
		customer.setCreateTime(DateTool.standardSdf().format(new Date()));
		customer.setUserId(loginInfo.getId());
		CommonUtil.setDefaultValue(customer);
		customerMapper.insert(customer);
		return customer.getId();
	}
	
	public void delete(long id, User loginInfo) {
		Customer customer = customerMapper.selectOne(id);
		if(customer.getUserId().longValue() != loginInfo.getId().longValue()) {
			throw new PermissionDeniedException("权限不足");
		}
		customerMapper.delete(id);
	}
	
	public void update(Customer customer, User loginInfo) {
		
		//如果要更新的数据已存在则提示异常
		Customer tmp = selectByMobile(loginInfo.getId(), customer.getType(), customer.getMobile());
		if(tmp != null && tmp.getId() != customer.getId()) {
			throw new DataExistException("已存在的联系人");
		}
		
		Customer src = customerMapper.selectOne(customer.getId());
		if(customer.getUserId().longValue() != loginInfo.getId().longValue()) {
			throw new PermissionDeniedException("权限不足");
		}
		try{
			src = (Customer) MergerUtil.merger(src, customer);
		}catch(Exception e) {
			throw new InvalidArgumentException("error:", e);
		}
		
		customerMapper.update(src);
	}
	
	public Customer selectByMobile(long userId, int type, String mobile) {
		return customerMapper.selectByMobile(userId, type, mobile);
	}
	
	public List<Customer> selectByType(long userId, int type, int page, int pageSize) {
		return customerMapper.selectByType(userId, type, pageSize, (page-1)*pageSize);
	}
	
	public int countByType(long userId, int type) {
		return customerMapper.countByType(userId, type);
	}
	
}
