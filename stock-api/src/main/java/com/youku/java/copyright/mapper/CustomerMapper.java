package com.youku.java.copyright.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.youku.java.copyright.bean.Customer;



public interface CustomerMapper extends MapperI<Customer>{
	
	public Customer selectByMobile(@Param("userId")long userId, @Param("type")int type, 
			@Param("mobile")String mobile);
	
	public List<Customer> selectByType(@Param("userId")long userId, @Param("type")int type, 
			@Param("limit")int limit, @Param("offset")int offset);

	public int countByType(@Param("userId")long userId, @Param("type")int type);
	
}
