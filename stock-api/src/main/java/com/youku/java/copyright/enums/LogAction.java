package com.youku.java.copyright.enums;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public enum LogAction {

	NotFindType(1000,"找不到动作", 0),
	
	NotFind(100,"找不到动作", NotFindType.getCode()),
	
	//日志动作
	//版权方增加
	
	Proprietor(999,"版权方", 0),
	
	ProprietorAdd(101,"版权方增加", Proprietor.getCode()),

	ProprietorDel(102,"版权方删除", Proprietor.getCode()),

	ProprietorUpdate(103,"版权方修改", Proprietor.getCode()),

	// --- production begin 
	
	Production(998, "作品", 0),

	ProductionAdd(104,"作品增加", Production.getCode()),

	ProductionDel(105,"作品删除", Production.getCode()),

	ProductionUpdateInfo(106,"作品修改基本信息", Production.getCode()),

	//1-2 作品修改状态，因为更新策略导致 
	ProductionUpdateStrategy(107,"作品修改状态，因为更新策略导致 ", Production.getCode()),

	//1-3 接口删除作品
	ProductionCallDel(108,"接口删除作品", Production.getCode()),

	//1-4 接口提交应用策略
	ProductionApplyStrategy(109,"接口提交应用策略", Production.getCode()),

	//1-5 接口提交重新应用策略
	ProductionReApplyStrategy(110,"接口提交重新应用策略", Production.getCode()),

	//2-1 作业执行更新策略
	ProductionUpdateStrategying(111,"作业执行更新策略", Production.getCode()),

	//2-2 作业任务完成策略更新后
	ProductionUpdate2create(112,"作业任务完成策略更新后", Production.getCode()),

	//2-3 作业任务删除节目
	ProductionDeleteShow(113,"作业任务删除节目", Production.getCode()),

	//2-4 作业任务删除节目完成
	ProductionDeleteShowing(114,"作业任务删除节目完成", Production.getCode()),

	//2-5 作业任务删除样本
	ProductionDeleteSample(115,"作业任务删除样本", Production.getCode()),

	//2-5 作业任务删除样本
	ProductionCreate2Transfer(116,"作品状态从创建到转移", Production.getCode()),

	//2-5 作业任务删除样本
	ProductionTransfer2Create(117,"作品状态从转移到创建", Production.getCode()),
	
	// --- production end 

	// --- show begin 
	Show(997, "节目", 0),
	
	ShowAdd(201,"节目增加", Show.getCode()),

	ShowAddAndBind(202,"节目增加并绑定", Show.getCode()),

	ShowDel(203,"节目删除", Show.getCode()),

	ShowUpdateInfo(204,"节目仅更新信息", Show.getCode()),
	
	//1-2 节目绑定
	ShowBind(205,"节目绑定", Show.getCode()),

	//1-4 节目绑定
	ShowReBind(206,"节目重新绑定", Show.getCode()),

	//1-5 解除绑定
	ShowRelieveWait(207,"解除绑定", Show.getCode()),

	//2-1 作业任务删除不在同步样本
	DeleteSample(208,"作业任务删除不在同步样本", Show.getCode()),

	//2-2 作业任务执行解除绑定
	ShowRelieve(209,"作业任务执行解除绑定", Show.getCode()),

	//2-3 作业任务完成解除绑定
	ShowRelieveing(210,"作业任务完成解除绑定", Show.getCode()),

	//2-4 作业任务同步样本完成
	ShowSync(211,"作业任务同步样本完成", Show.getCode()),

	ShowSync2Transfer(212,"节目同步到转移", Show.getCode()),

	ShowTransfer2Sync(213,"节目转移到同步", Show.getCode()),

	// --- show end 
	
	// --- sample begin 
	
	Sample(996, "样本", 0),

	SampleAdd(301,"样本增加", Sample.getCode()),

	SampleDel(302,"样本删除", Sample.getCode()),

	SampleUpdateInfo(303,"仅更新信息", Sample.getCode()),

	//1-2
	SampleTransfer(304,"样本转移", Sample.getCode()),

	//1-2
	SampleUpdateStrategy(305,"样本更新策略", Sample.getCode()),

	//1-3
	SampleAskDel(306,"样本请求删除", Sample.getCode()),

	//2-1
	SampleSync(307,"样本同步", Sample.getCode()),

	//2-2
	SampleUpdateStrategying(308,"样本正在更新策略", Sample.getCode()),

	//2-3
	SampleTransferFinish(309,"样本转移完成", Sample.getCode()),

	//2-3
	SampleUpdateStrategyWait(310,"样本等待更新策略", Sample.getCode()),
	
	//2-6
	Fingerprint(311,"指纹底层检查", Sample.getCode()),
	// --- sample end 

	// --- analogue begin 

	Analogue(995,"副本", 0),
	//副本增加
	AnalogueAdd(401,"副本增加", Analogue.getCode()),

	AnalogueSampleAdd(402,"副本样本关系绑定", Analogue.getCode()),

	AnalogueSampleDel(403,"副本样本关系解除", Analogue.getCode()),

	AnalogueStrategyAdd(404,"副本更新策略", Analogue.getCode()),

	AnalogueStrategyHunmanAdd(405,"单视频策略更新", Analogue.getCode()),
	
	// --- analogue end 

	// --- copyrightState begin 

	Copyright(994,"版权状态", 0),
	//版权状态增加
	CopyrightStateAdd(501,"版权状态增加", Copyright.getCode()),

	CopyrightStateDel(502,"版权状态删除", Copyright.getCode()),

	// --- copyrightState end 

	// --- strategy begin 

	Strategy(993,"策略", 0),
	//版权状态增加
	StrategyAdd(601,"策略增加", Strategy.getCode()),

	StrategyDel(602,"策略删除", Strategy.getCode()),

	StrategyUpdate(603,"策略修改", Strategy.getCode()),

	StrategyPackageAdd(604,"策略包增加", Strategy.getCode());

	// --- strategy end
	private Integer code;
	
	private String name;
	
	private Integer parentCode;

	private LogAction(Integer code, String name, Integer parentCode) {
		this.code = code;
		this.name = name;
		this.parentCode = parentCode;
	}

	public static LogAction valueOf(Integer code) {
		
		if(code == null) {
			return NotFind;
		}
		
		for(LogAction la : LogAction.values()) {
			if(la.getCode().intValue() == code.intValue()) {
				return la;
			}
		} 
		return NotFind;
	}
	
	public static List<JSONObject> getJsonList() {
		Map<Integer, JSONObject> map = new HashMap<Integer, JSONObject>();
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(LogAction la : LogAction.values()) {
			JSONObject codeObject = new JSONObject();
			codeObject.put("code", la.getCode());
			codeObject.put("name", la.getName());
			
			if(la.getParentCode().intValue() == 0) {;
				codeObject.put("codes", new JSONArray());
				list.add(codeObject);
				map.put(la.getCode(), codeObject);
			}else {
				JSONObject temp = map.get(la.getParentCode());
				temp.getJSONArray("codes").add(codeObject);
			}
		} 
		return list;
	}
	
	public static void main(String[] args) {
		Calendar now = Calendar.getInstance();  
        System.out.println("年: " + now.get(Calendar.MONTH));  		
	}
	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentCode() {
		return parentCode;
	}

	public void setParentCode(Integer parentCode) {
		this.parentCode = parentCode;
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
