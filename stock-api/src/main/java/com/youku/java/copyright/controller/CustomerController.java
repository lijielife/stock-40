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

import com.youku.java.copyright.bean.Customer;
import com.youku.java.copyright.bean.User;
import com.youku.java.copyright.exception.PermissionDeniedException;
import com.youku.java.raptor.auth.NeedLogin;
import com.youku.java.raptor.exception.InvalidArgumentException;

@NeedLogin
@RestController
public class CustomerController extends BaseController{

	@RequestMapping(value = "/customers", method = RequestMethod.POST)
	@ResponseBody
	public Object add(User loginInfo, @RequestBody String body){
		Customer good = validation.getObject(body, Customer.class, new String[]{"type","name","mobile"});
		customerService.insert(good, loginInfo);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("id", good.getId());
		return result;
	}

	@RequestMapping(value = "/customers", method = RequestMethod.DELETE)
	@ResponseBody
	public Object del(
			User loginInfo,
			@RequestParam(required = false, value = "id", defaultValue = "0") Long id){

		Map<String,Object> result = new HashMap<String, Object>();
		customerService.delete(id, loginInfo);
		result.put("id", id);
		return result;
	}

	@RequestMapping(value = "/customers", method = RequestMethod.PUT)
	@ResponseBody
	public Object update(User loginInfo, @RequestBody String body){
		Customer good = validation.getObject(body, Customer.class, new String[]{"id", "name"});
		customerService.updateHttp(good, loginInfo);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("id", good.getId());
		return result;
	}

	@RequestMapping(value = "/customers", method = RequestMethod.GET)
	public Object loginInfo(User loginInfo, 
			@RequestParam(required = false, value = "id", defaultValue = "0") long id, 
			@RequestParam(required = false, value = "type", defaultValue = "0") int type, 
			@RequestParam(required = false, value = "name", defaultValue = "") String name, 
			@RequestParam(required = false, value = "page", defaultValue = "1") int page, 
			@RequestParam(required = false, value = "pageSize", defaultValue = "20") int pageSize){
		Map<String,Object> result = new HashMap<String, Object>();
		name = name.trim();
		
		if(id > 0) {
			Customer customer = customerService.selectOne(id);
			List<Customer> customers = new ArrayList<Customer>();
			if(customer == null) {
				throw new InvalidArgumentException("找不到的数据");
			}else if(customer.getUserId().longValue() != loginInfo.getId().longValue()) {
				throw new PermissionDeniedException("权限不足");
			}else {
				customers.add(customer);
			}
			result.put("data", customers);
		}else if(type > 0 && page > 0 && pageSize > 0) {
			result.put("data", customerService.selectByType(loginInfo.getId(), type, name, page, pageSize));
			result.put("total", customerService.countByType(loginInfo.getId(), type, name));
			result.put("page", page);
			result.put("pageSize", pageSize);
		}else {
			throw new InvalidArgumentException("找不到的查询条件");
		}

		return result;
	}
}
