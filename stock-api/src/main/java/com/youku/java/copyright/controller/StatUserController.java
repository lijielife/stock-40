package com.youku.java.copyright.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.youku.java.copyright.bean.StatUser;
import com.youku.java.copyright.bean.User;
import com.youku.java.copyright.util.Constant.TimeType;
import com.youku.java.copyright.util.DateTool;
import com.youku.java.raptor.auth.NeedLogin;
import com.youku.java.raptor.exception.InvalidArgumentException;

@NeedLogin
@RestController
public class StatUserController extends BaseController{

	@RequestMapping(value = "/stat/users", method = RequestMethod.GET)
	public Object loginInfo(User loginInfo, 
			@RequestParam(required = false, value = "beginTime", defaultValue = "") String beginTime, 
			@RequestParam(required = false, value = "endTime", defaultValue = "") String endTime, 
			@RequestParam(required = false, value = "timeType", defaultValue = "") int timeType){
		Map<String,Object> result = new HashMap<String, Object>();

		if(beginTime == null || "".equals(beginTime)) {
			throw new InvalidArgumentException("开始时间必填");
		}
		
		if(endTime == null || "".equals(endTime)) {
			throw new InvalidArgumentException("开始时间必填");
		}
		
		Date begin = null;
		Date end = null;
		try {
			begin = DateTool.standardSdf.parse(beginTime);
		} catch (Exception e) {
			throw new InvalidArgumentException("开始时间格式错误");
		}
		try {
			end = DateTool.standardSdf.parse(endTime);
		} catch (Exception e) {
			throw new InvalidArgumentException("结束时间格式错误");
		}
		
		if(timeType == TimeType.DAY) {
			begin = DateTool.getBegin(begin);
			end = DateTool.getBegin(end);
		}else if(timeType == TimeType.MONTH) {
			begin = DateTool.getMonthBegin(begin);
			end = DateTool.getMonthBegin(end);
		}else {
			throw new InvalidArgumentException("请传入参数时间类型");
		}
		
		List<StatUser> statUsers = statUserService.selectByTime(begin, end, timeType);
		
		result.put("data", statUserService.fillingTime(statUsers, begin, end, timeType));
		return result;
	}
}
