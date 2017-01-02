package com.youku.java.copyright.mapper;

import org.apache.ibatis.annotations.Param;

import com.youku.java.copyright.bean.User;



public interface UserMapper extends MapperI<User>{
	
	public User selectByMobile(@Param("mobile")String mobile);
}
