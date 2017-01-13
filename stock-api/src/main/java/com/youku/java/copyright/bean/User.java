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
 * 用户
 * @author chenlisong
 *
 */
public class User{
	
	@Note(name="主键ID")
	@IsInt(min=0,max=Long.MAX_VALUE)
	private Long id ;

	@Note(name="名称")
	@IsString(minLength=0,maxLength=15)
	private String name;

	@Note(name="手机号")
	@IsString(minLength=11,maxLength=11)
	private String mobile;

	@Note(name="密码")
	@IsString(minLength=0,maxLength=31)
	private String password;

	@Note(name="创建时间")
	@JSONField (format="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	
	private Double productionPrice;
	
	private Double otherPrice;
	
	private Double sellPrice;
	
	private Double damagePrice;
	
	private Double profitPrice;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreateTime() {
		return createTime;
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
