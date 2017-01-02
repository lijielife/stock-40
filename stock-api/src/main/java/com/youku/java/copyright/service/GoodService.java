package com.youku.java.copyright.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youku.java.copyright.bean.Good;
import com.youku.java.copyright.bean.User;
import com.youku.java.copyright.exception.DataExistException;
import com.youku.java.copyright.exception.PermissionDeniedException;
import com.youku.java.copyright.mapper.GoodMapper;
import com.youku.java.copyright.util.CommonUtil;
import com.youku.java.copyright.util.DateTool;
import com.youku.java.copyright.util.MergerUtil;
import com.youku.java.raptor.exception.InvalidArgumentException;

@Service
public class GoodService {

	@Autowired
	private GoodMapper goodMapper;
	
	public Good selectOne(long id) {
		return goodMapper.selectOne(id);
	}
	
	public Good selectByName(long companyId, String name) {
		return goodMapper.selectByName(companyId, name);
	}
	
	public List<Good> selectByNameLike(long companyId, String name) {
		return goodMapper.selectByNameLike(companyId, name);
	}
	
	public void delete(long id, User loginInfo) {
		Good good = selectOne(id);
		if(good == null) {
			throw new InvalidArgumentException("找不到的商品");
		}
		
		if(loginInfo.getCompanyId().longValue() != good.getCompanyId().longValue()) {
			throw new PermissionDeniedException("权限不足");
		}
		
		goodMapper.delete(id);
	}
	
	public void update(Good good, User loginInfo) {
		Good src = selectOne(good.getId());
		if(src.getCompanyId().longValue() != loginInfo.getCompanyId().longValue()) {
			throw new PermissionDeniedException("权限不足");
		}
		try{
			src = (Good) MergerUtil.merger(src, good);
		}catch(Exception e) {
			throw new InvalidArgumentException("error:", e);
		}
		goodMapper.update(src);
	}
	
	public List<Good> selectByCompanyId(long companyId, int page, int pageSize) {
		return goodMapper.selectByCompanyId(companyId, pageSize, (page-1)*pageSize);
	}
	
	public int countByCompanyId(long companyId) {
		return goodMapper.countByCompanyId(companyId);
	}

	public long insert(Good good, User loginInfo) {
		Good src = selectByName(loginInfo.getCompanyId(), good.getName());
		if(src != null) {
			throw new DataExistException("该商品已存在");
		}
		
		good.setCompanyId(loginInfo.getCompanyId());
		good.setCreateTime(DateTool.standardSdf().format(new Date()));
		CommonUtil.setDefaultValue(good);
		return goodMapper.insert(good);
	}
}
