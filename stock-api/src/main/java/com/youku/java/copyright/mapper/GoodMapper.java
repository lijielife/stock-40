package com.youku.java.copyright.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.youku.java.copyright.bean.Good;



public interface GoodMapper extends MapperI<Good>{
	
	public Good selectByName(@Param("companyId")long companyId, @Param("name")String name);
	
	public List<Good> selectByNameLike(@Param("companyId")long companyId, @Param("name")String name);
	
	public List<Good> selectByCompanyId(@Param("companyId")long companyId, @Param("limit")int limit, @Param("offset")int offset);
	
	public int countByCompanyId(@Param("companyId")long companyId);
	
}
