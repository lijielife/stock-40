package com.youku.java.copyright.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.youku.java.copyright.bean.Good;



public interface GoodMapper extends MapperI<Good>{
	
	public Good selectByName(@Param("userId")long userId, @Param("name")String name);
	
	public List<Good> selectByNameLike(@Param("userId")long userId, @Param("name")String name, 
			@Param("limit")int limit);
	
	public List<Good> selectByUserId(@Param("userId")long userId, @Param("limit")int limit, 
			@Param("offset")int offset);
	
	public int countByUserId(@Param("userId")long userId);
	
	public int updateNumberRecord(@Param("id")long id, @Param("number")int number, 
			@Param("recordId")long recordId, @Param("productionPrice")double productionPrice);
	
	public int updateNumber(@Param("id")long id, @Param("number")int number, 
			@Param("productionPrice")double productionPrice);
	
}
