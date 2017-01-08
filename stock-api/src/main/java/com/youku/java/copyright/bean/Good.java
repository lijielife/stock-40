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
 * 商品
 * @author chenlisong
 *
 */
public class Good{
	
	@Note(name="主键ID")
	@IsInt(min=0,max=Long.MAX_VALUE)
	private Long id ;

	@Note(name="名称")
	@IsString(minLength=0,maxLength=31)
	private String name;
	
	@Note(name="用户id")
	private Long userId;
	
	@Note(name="数量")
	@IsInt(min=0, max=Integer.MAX_VALUE)
	private Integer number;
	
	@Note(name="单价")
	private Double unitPrice;
	
	@Note(name="进货记录ID")
	private Long recordId;
	
	@Note(name="批次剩余数量")
	@IsInt(min=0, max=Integer.MAX_VALUE)
	private Integer stockNumber;

	@Note(name="创建时间")
	@JSONField (format="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	
	@Note(name="库存价格")
	private Double productionPrice;
	
	@Note(name="销售额")
	private Double sellPrice;
	
	@Note(name="售出总数")
	private Integer sellNumber;
	
	@Note(name="总利润")
	private Double profitPrice;
	
	@Note(name="折损费")
	private Double damagePrice;
	
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

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Integer getStockNumber() {
		return stockNumber;
	}

	public void setStockNumber(Integer stockNumber) {
		this.stockNumber = stockNumber;
	}

	public Double getProfitPrice() {
		return profitPrice;
	}

	public void setProfitPrice(Double profitPrice) {
		this.profitPrice = profitPrice;
	}

	public Integer getSellNumber() {
		return sellNumber;
	}

	public void setSellNumber(Integer sellNumber) {
		this.sellNumber = sellNumber;
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
