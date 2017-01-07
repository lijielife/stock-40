package com.youku.java.copyright.view;

import java.util.HashMap;

import com.youku.java.copyright.bean.Customer;
import com.youku.java.copyright.bean.Good;
import com.youku.java.copyright.bean.Record;
import com.youku.java.copyright.util.DateTool;

public class RecordView extends HashMap<String, Object>{

	private static final long serialVersionUID = 1L;

	public RecordView(Record record, Customer customer, Good good) {
		
		put("id", record.getId());
		put("type", record.getType());
		put("customerId", record.getCustomerId());
		
		if(customer != null) {
			put("customerName", customer.getName());
		}else {
			put("customerName", "已删除");
		}
		put("goodId", record.getGoodId());
		
		if(good != null) {
			put("goodName", good.getName());
		}else {
			put("goodName", "已删除");
		}
		put("number", record.getNumber());
		put("productionPrice", record.getProductionPrice());
		put("otherPrice", record.getOtherPrice());
		put("sellPrice", record.getSellPrice());
		put("damagePrice", record.getDamagePrice());
		put("profitPrice", record.getProfitPrice());
		put("createTime", DateTool.standardSdf().format(record.getCreateTime()));
		
	}
}
