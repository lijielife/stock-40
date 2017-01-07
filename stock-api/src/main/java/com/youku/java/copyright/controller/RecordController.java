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

import com.youku.java.copyright.bean.Record;
import com.youku.java.copyright.bean.User;
import com.youku.java.copyright.exception.PermissionDeniedException;
import com.youku.java.copyright.util.Constant.RecordType;
import com.youku.java.raptor.auth.NeedLogin;
import com.youku.java.raptor.exception.InvalidArgumentException;

@NeedLogin
@RestController
public class RecordController extends BaseController{

	@RequestMapping(value = "/records", method = RequestMethod.POST)
	@ResponseBody
	public Object add(User loginInfo, @RequestBody String body){
		Map<String,Object> result = new HashMap<String, Object>();
		
		Record record = validation.getObject(body, Record.class, new String[]{"type", "customerId", "goodId", "number"});
		
		customerService.checkOwner(loginInfo, record.getCustomerId());
		goodService.checkOwner(loginInfo, record.getGoodId());
		
		if(record.getType() == RecordType.STOCK) {
			validation.getObject(body, Record.class, new String[]{"customerId", "goodId", "number", "productionPrice"});
			recordService.stock(record, loginInfo);
		}else if(record.getType() == RecordType.SELL) {
			validation.getObject(body, Record.class, new String[]{"customerId", "goodId", "number", "sellPrice"});
			recordService.sell(record, loginInfo);
		}else if(record.getType() == RecordType.DAMAGE) {
			validation.getObject(body, Record.class, new String[]{"customerId", "goodId", "number", "damagePrice"});
			recordService.damage(record, loginInfo);
		}else {
			throw new InvalidArgumentException("录入无效的类型");
		}
		result.put("id", record.getId());
		return result;
	}

	@RequestMapping(value = "/records", method = RequestMethod.GET)
	public Object loginInfo(User loginInfo, 
			@RequestParam(required = false, value = "id", defaultValue = "0") long id, 
			@RequestParam(required = false, value = "type", defaultValue = "0") int type, 
			@RequestParam(required = false, value = "page", defaultValue = "1") int page, 
			@RequestParam(required = false, value = "pageSize", defaultValue = "20") int pageSize){
		Map<String,Object> result = new HashMap<String, Object>();
		
		if(id > 0) {
			List<Record> records = new ArrayList<Record>();
			Record record = recordService.selectOne(id);
			if(record == null) {
				throw new InvalidArgumentException("找不到的数据");
			}else if(record.getUserId().longValue() != loginInfo.getId().longValue()) {
				throw new PermissionDeniedException("权限不足");
			}else {
				records.add(record);
			}
			result.put("data", recordService.convert2View(records));
		}else if(page > 0 && pageSize > 0) {
			List<Record> records = recordService.selectByUserid(loginInfo, type, page, pageSize);
			result.put("data", recordService.convert2View(records));
			result.put("total", recordService.countByUserid(loginInfo, type));
			result.put("page", page);
			result.put("pageSize", pageSize);
		}else {
			throw new InvalidArgumentException("找不到的查询条件");
		}

		return result;
	}
}
