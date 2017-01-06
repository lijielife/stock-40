package com.youku.java.copyright.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.youku.java.copyright.bean.StatUser;

public interface StatUserMonthMapper extends MapperI<StatUser>{
	
	public void deleteByTime(@Param("userId") long userId, @Param("time") Date time);
	
	public StatUser selectLast();
	
	public List<StatUser> selectByTime(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

}
