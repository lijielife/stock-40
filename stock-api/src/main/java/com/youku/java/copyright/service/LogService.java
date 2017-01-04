package com.youku.java.copyright.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youku.java.copyright.bean.Log;
import com.youku.java.copyright.mapperlog.LogMapper;

@Transactional(rollbackFor = Exception.class)
@Service
public class LogService {

	@Autowired
	private LogMapper logMapper;

	public List<Log> selectAll() {
		return logMapper.selectListByPage(Integer.MAX_VALUE, 0);
	}

}
