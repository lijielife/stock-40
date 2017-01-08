package com.youku.java.copyright.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.youku.java.copyright.bean.Good;
import com.youku.java.copyright.bean.User;
import com.youku.java.copyright.exception.PermissionDeniedException;
import com.youku.java.raptor.auth.NeedLogin;
import com.youku.java.raptor.exception.InvalidArgumentException;

@NeedLogin
@RestController
public class GoodController extends BaseController{

	@RequestMapping(value = "/goods", method = RequestMethod.POST)
	@ResponseBody
	public Object add(User loginInfo, @RequestBody String body){
		Good good = validation.getObject(body, Good.class, new String[]{"name"});
		goodService.insert(good, loginInfo);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("id", good.getId());
		return result;
	}

	@RequestMapping(value = "/goods", method = RequestMethod.DELETE)
	@ResponseBody
	public Object del(
			User loginInfo,
			@RequestParam(required = false, value = "id", defaultValue = "0") Long id){

		Map<String,Object> result = new HashMap<String, Object>();
		goodService.delete(id, loginInfo);
		result.put("id", id);
		return result;
	}

	@RequestMapping(value = "/goods", method = RequestMethod.PUT)
	@ResponseBody
	public Object update(User loginInfo, @RequestBody String body){
		Good good = validation.getObject(body, Good.class, new String[]{"id", "name"});
		goodService.update(good, loginInfo);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("id", good.getId());
		return result;
	}

	@RequestMapping(value = "/goods", method = RequestMethod.GET)
	public Object loginInfo(User loginInfo, 
			@RequestParam(required = false, value = "id", defaultValue = "0") long id, 
			@RequestParam(required = false, value = "name", defaultValue = "") String name, 
			@RequestParam(required = false, value = "limit", defaultValue = "5") int limit, 
			@RequestParam(required = false, value = "page", defaultValue = "1") int page, 
			@RequestParam(required = false, value = "pageSize", defaultValue = "20") int pageSize){
		Map<String,Object> result = new HashMap<String, Object>();
		name = name.trim();
		
		if(id > 0) {
			Good good = goodService.selectOne(id);
			List<Good> goods = new ArrayList<Good>();
			if(good == null) {
				throw new InvalidArgumentException("找不到的数据");
			}else if(good.getUserId().longValue() != loginInfo.getId().longValue()) {
				throw new PermissionDeniedException("权限不足");
			}else {
				goods.add(good);
			}
			result.put("data", goods);
		}else if(name != null && !"".equals(name)) {
			result.put("data", goodService.selectByNameLike(loginInfo.getId(), name, limit));
		}else if(page > 0 && pageSize > 0) {
			result.put("data", goodService.selectByUserId(loginInfo.getId(), page, pageSize));
			result.put("total", goodService.countByUserId(loginInfo.getId()));
			result.put("page", page);
			result.put("pageSize", pageSize);
		}else {
			throw new InvalidArgumentException("找不到的查询条件");
		}

		return result;
	}
}
