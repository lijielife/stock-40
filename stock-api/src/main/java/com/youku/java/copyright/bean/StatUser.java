package com.youku.java.copyright.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youku.java.copyright.annotation.Note;
import com.youku.java.raptor.exception.InvalidHttpArgumentException;
import com.youku.java.raptor.validation.IsInt;

/**
 * 员工统计
 * @author chenlisong
 *
 */
public class StatUser{
	
	@Note(name="主键ID")
	@IsInt(min=0,max=Long.MAX_VALUE)
	private Long id ;

	@Note(name="产品费用")
	private Double productionPrice = 0.0;

	@Note(name="其他费用")
	private Double otherPrice = 0.0;

	@Note(name="销售额")
	private Double sellPrice = 0.0;

	@Note(name="折损费")
	private Double damagePrice = 0.0;

	@Note(name="利润")
	private Double profitPrice = 0.0;

	@Note(name="用户ID")
	@IsInt(min=0,max=Long.MAX_VALUE)
	private Long userId ;

	@Note(name="时间")
	@JSONField (format="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date time;
	
	public StatUser() {}
	
	public StatUser(long userId) {
		this.userId = userId;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getProductionPrice() {
		return productionPrice;
	}

	public void setProductionPrice(Double productionPrice) {
		this.productionPrice = productionPrice;
	}

	public Double getOtherPrice() {
		return otherPrice;
	}

	public void setOtherPrice(Double otherPrice) {
		this.otherPrice = otherPrice;
	}

	public Double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(Double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public Double getDamagePrice() {
		return damagePrice;
	}

	public void setDamagePrice(Double damagePrice) {
		this.damagePrice = damagePrice;
	}

	public Double getProfitPrice() {
		return profitPrice;
	}

	public void setProfitPrice(Double profitPrice) {
		this.profitPrice = profitPrice;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.time = sdf.parse(time);
		} catch (ParseException e) {
			throw new InvalidHttpArgumentException("invalid params.beginTime parse error.");
		}
	}

	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
		}
		return "json parse failed";
	}
}
