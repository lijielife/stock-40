package com.youku.java.copyright.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youku.java.copyright.bean.User;
import com.youku.java.copyright.exception.DataExistException;
import com.youku.java.copyright.mapper.UserMapper;
import com.youku.java.copyright.util.CommonUtil;
import com.youku.java.copyright.util.DateTool;

@Transactional(rollbackFor = Exception.class)
@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	public List<User> selectAll() {
		return userMapper.selectListByPage(Integer.MAX_VALUE, 0);
	}
	
	public User selectByMobile(String mobile) {
		return userMapper.selectByMobile(mobile);
	}
	
	/**
	 * 为每个价格增加传入的值，例如：`productionPrice` = `productionPrice` + productionPrice
	 * @param id
	 * @param productionPrice
	 * @param otherPrice
	 * @param sellPrice
	 * @param damagePrice
	 * @param profitPrice
	 */
	public void updatePrice(long id, double productionPrice, double otherPrice, 
			double sellPrice, 
			double damagePrice, double profitPrice) {
		userMapper.updatePrice(id, productionPrice, otherPrice, sellPrice, 
				damagePrice, profitPrice);
	}
	
	public long insert(User user) {
		User src = selectByMobile(user.getMobile());
		if(src != null) {
			throw new DataExistException("手机号已存在，无需注册");
		}
		
		user.setCreateTime(DateTool.standardSdf().format(new Date()));
		CommonUtil.setDefaultValue(user);
		return userMapper.insert(user);
	}
}
