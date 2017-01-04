package com.youku.java.copyright.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youku.java.copyright.bean.Good;
import com.youku.java.copyright.bean.User;
import com.youku.java.copyright.exception.DataExistException;
import com.youku.java.copyright.exception.PermissionDeniedException;
import com.youku.java.copyright.mapper.GoodMapper;
import com.youku.java.copyright.util.CommonUtil;
import com.youku.java.copyright.util.DateTool;
import com.youku.java.copyright.util.MergerUtil;
import com.youku.java.raptor.exception.InvalidArgumentException;

@Transactional(rollbackFor = Exception.class)
@Service
public class GoodService {

	@Autowired
	private GoodMapper goodMapper;
	
	public Good selectOne(long id) {
		return goodMapper.selectOne(id);
	}
	
	public Good selectByName(long userId, String name) {
		return goodMapper.selectByName(userId, name);
	}
	
	public List<Good> selectByNameLike(long userId, String name, int limit) {
		return goodMapper.selectByNameLike(userId, name, limit);
	}
	
	public void delete(long id, User loginInfo) {
		Good good = selectOne(id);
		if(good == null) {
			throw new InvalidArgumentException("找不到的商品");
		}
		
		if(loginInfo.getId().longValue() != good.getUserId().longValue()) {
			throw new PermissionDeniedException("权限不足");
		}
		
		goodMapper.delete(id);
	}
	
	public void update(Good good, User loginInfo) {
		Good src = selectOne(good.getId());
		if(src.getUserId().longValue() != loginInfo.getId().longValue()) {
			throw new PermissionDeniedException("权限不足");
		}
		try{
			src = (Good) MergerUtil.merger(src, good);
		}catch(Exception e) {
			throw new InvalidArgumentException("error:", e);
		}
		goodMapper.update(src);
	}
	
	public List<Good> selectByUserId(long userId, int page, int pageSize) {
		return goodMapper.selectByUserId(userId, pageSize, (page-1)*pageSize);
	}
	
	public int countByUserId(long userId) {
		return goodMapper.countByUserId(userId);
	}

	public long insert(Good good, User loginInfo) {
		Good src = selectByName(loginInfo.getId(), good.getName());
		if(src != null) {
			throw new DataExistException("该商品已存在");
		}
		
		good.setUserId(loginInfo.getId());
		good.setCreateTime(DateTool.standardSdf().format(new Date()));
		CommonUtil.setDefaultValue(good);
		return goodMapper.insert(good);
	}
	
	public int updateNumber(long id, int number, long recordId) {
		int update = goodMapper.updateNumber(id, number);
		if(update <= 0) {
			update = updateNumberRecord(id, number, recordId);
		}
		return update;
	}
	
	private int updateNumberRecord(long id, int number, long recordId) {
		return goodMapper.updateNumberRecord(id, number, recordId);
	}
}
