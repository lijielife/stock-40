package com.youku.java.copyright.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.youku.java.copyright.bean.StatUser;

public interface StatUserDayMapper extends MapperI<StatUser>{
	
	public void deleteByTime(@Param("userId") long userId, @Param("time") Date time);
	
	public StatUser selectLast();
	
	public List<StatUser> selectById(@Param("time")Date time, @Param("id")long id, @Param("limit")int limit);
	
	public List<StatUser> selectByTime(@Param("userId") long userId, @Param("beginTime") Date beginTime, 
			@Param("endTime") Date endTime);
}
