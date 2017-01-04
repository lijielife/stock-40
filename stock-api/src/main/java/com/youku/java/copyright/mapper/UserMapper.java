package com.youku.java.copyright.mapper;

import org.apache.ibatis.annotations.Param;

import com.youku.java.copyright.bean.User;



public interface UserMapper extends MapperI<User>{
	
	public User selectByMobile(@Param("mobile")String mobile);
	
	public void updatePrice(@Param("id")long id, @Param("productionPrice")double productionPrice, 
			@Param("otherPrice")double otherPrice, @Param("sellPrice")double sellPrice, 
			@Param("damagePrice")double damagePrice, @Param("profitPrice")double profitPrice);
}
