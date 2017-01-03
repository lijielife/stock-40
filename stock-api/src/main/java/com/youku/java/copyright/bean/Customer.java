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
 * 客户
 * @author chenlisong
 *
 */
public class Customer{
	
	@Note(name="主键ID")
	@IsInt(min=0,max=Long.MAX_VALUE)
	private Long id ;

	@Note(name="类型")
	@IsInt(min=1, max=3)
	private Integer type;

	@Note(name="名称")
	@IsString(minLength=0,maxLength=15)
	private String name;

	@Note(name="手机号")
	@IsString(minLength=11,maxLength=11)
	private String mobile;

	@Note(name="公司")
	@IsString(minLength=0,maxLength=31)
	private String company;

	@Note(name="备注")
	@IsString(minLength=0,maxLength=255)
	private String remark;
	
	@Note(name="用户ID")
	@IsInt(min=0,max=Long.MAX_VALUE)
	private Long userId ;

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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
