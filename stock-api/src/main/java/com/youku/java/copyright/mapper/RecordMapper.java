
package com.youku.java.copyright.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.youku.java.copyright.bean.Record;



public interface RecordMapper extends MapperI<Record>{
	
	public List<Record> selectById(@Param("id")long id, @Param("userId")long userId, @Param("type")int type, @Param("limit")int limit);
	
	public List<Record> selectByUserid(@Param("userId")long userId, @Param("type")int type, @Param("limit")int limit, 
			@Param("offset")int offset);
	
	public int countByUserid(@Param("userId")long userId, @Param("type")int type);
}
