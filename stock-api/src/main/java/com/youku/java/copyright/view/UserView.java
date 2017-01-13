package com.youku.java.copyright.view;

import java.math.BigDecimal;
import java.util.HashMap;

import com.youku.java.copyright.bean.User;
import com.youku.java.copyright.util.DateTool;

public class UserView extends HashMap<String, Object>{

	private static final long serialVersionUID = 1L;

	public UserView(User user, String type) {
		if("loginInfo".equals(type)) {
			put("name", user.getName());
			put("mobile", user.getMobile());
			put("companyName", user.getName()+"的公司");
			put("createTime", DateTool.standardSdf().format(user.getCreateTime()));
		}else if("stat".equals(type)) {
			put("name", user.getName());
			put("mobile", user.getMobile());
			put("companyName", user.getName()+"的公司");
			put("createTime", DateTool.standardSdf().format(user.getCreateTime()));
			put("productionPrice", user.getProductionPrice());
			put("otherPrice", user.getOtherPrice());
			put("sellPrice", user.getSellPrice());
			put("damagePrice", user.getDamagePrice());
			put("profitPrice", user.getProfitPrice());

			double stockPrice = user.getProductionPrice() - (user.getSellPrice() - user.getProfitPrice());
			BigDecimal bd = new BigDecimal(stockPrice);
			double newStockPrice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			put("stockPrice", newStockPrice);
		}
	}
}
