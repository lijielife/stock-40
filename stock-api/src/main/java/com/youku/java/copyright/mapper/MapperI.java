package com.youku.java.copyright.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;
/*
 * @author chenlisong
 */
public interface MapperI<T> {

	T selectOne(@Param("id") long id);
	
	List<T> selectListByPage(@Param("limit") int limit, @Param("offset") int offset);
	
	List<T> selectListByIds(@Param("ids")List<Long> ids);
	
	int countByIds(@Param("ids") List<Long> ids);

	long insert(T t);
	
	void update(T t);

	void delete(long id);
	
	void deleteAll();
	
}
