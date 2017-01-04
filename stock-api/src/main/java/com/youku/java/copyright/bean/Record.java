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
import com.youku.java.raptor.validation.IsString;

/**
 * 记录，进货，销售，退货
 * @author chenlisong
 *
 */
public class Record{
	
	@Note(name="主键ID")
	@IsInt(min=0,max=Long.MAX_VALUE)
	private Long id ;

	@Note(name="类型")
	@IsInt(min=1, max=3)
	private Integer type;
	
	@Note(name="销售ID")
	@IsInt(min=0,max=Long.MAX_VALUE)
	private Long customerId ;
	
	@Note(name="商品ID")
	@IsInt(min=0,max=Long.MAX_VALUE)
	private Long goodId ;
	
	@Note(name="用户ID")
	@IsInt(min=0,max=Long.MAX_VALUE)
	private Long userId ;

	@Note(name="数量")
	@IsString(minLength=0,maxLength=Integer.MAX_VALUE)
	private Integer number;

	@Note(name="产品价格")
	@IsInt(min=0,max=Long.MAX_VALUE)
	private Double productionPrice ;

	@Note(name="其他价格")
	@IsInt(min=0,max=Long.MAX_VALUE)
	private Double otherPrice ;

	@Note(name="折损价格")
	@IsInt(min=0,max=Long.MAX_VALUE)
	private Double damagePrice ;

	@Note(name="销售价格")
	@IsInt(min=0,max=Long.MAX_VALUE)
	private Double sellPrice ;

	@Note(name="利润")
	@IsInt(min=0,max=Long.MAX_VALUE)
	private Double profitPrice ;

	@Note(name="创建时间")
	@JSONField (format="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getGoodId() {
		return goodId;
	}

	public void setGoodId(Long goodId) {
		this.goodId = goodId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
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

	public Double getDamagePrice() {
		return damagePrice;
	}

	public void setDamagePrice(Double damagePrice) {
		this.damagePrice = damagePrice;
	}

	public Double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(Double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public Double getProfitPrice() {
		return profitPrice;
	}

	public void setProfitPrice(Double profitPrice) {
		this.profitPrice = profitPrice;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.createTime = sdf.parse(createTime);
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
