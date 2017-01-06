package com.youku.java.copyright.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youku.java.copyright.bean.Record;
import com.youku.java.copyright.bean.StatUser;
import com.youku.java.copyright.mapper.StatUserDayMapper;
import com.youku.java.copyright.mapper.StatUserMonthMapper;
import com.youku.java.copyright.util.Constant.RecordType;
import com.youku.java.copyright.util.Constant.Time;
import com.youku.java.copyright.util.Constant.TimeType;
import com.youku.java.copyright.util.DateTool;

@Transactional(rollbackFor = Exception.class)
@Service
public class StatUserService {

	@Autowired
	private StatUserDayMapper statUserDayMapper;

	@Autowired
	private StatUserMonthMapper statUserMonthMapper;
	
	private org.apache.commons.logging.Log logger = LogFactory.getLog(StatUserService.class);

	public long insertDay(StatUser statUser, Date time) {
		if(time == null) {
			return 0l;
		}
		statUser.setTime(DateTool.standardSdf().format(time));
		statUserDayMapper.deleteByTime(statUser.getUserId(), time);
		return statUserDayMapper.insert(statUser);
	}
	
	public long insertMonth(StatUser statUser, Date time) {
		if(time == null) {
			return 0l;
		}
		statUser.setTime(DateTool.standardSdf().format(time));
		statUserMonthMapper.deleteByTime(statUser.getUserId(), time);
		return statUserMonthMapper.insert(statUser);
	}
	
	public Date selectLastDay() {
		Date lastTime = null;
		StatUser last = statUserDayMapper.selectLast();
		if(last != null) {
			lastTime = last.getTime();
		}else {
			try {
				lastTime = DateTool.standardSdf().parse(Time.STAT_BEGIN_TIME);
				lastTime = DateTool.addDays(lastTime, -1);
			} catch (ParseException e) {
				logger.error("error:", e);
			}
		}
		return lastTime;
	}
	
	public Date selectLastMonth() {
		Date lastTime = null;
		StatUser last = statUserMonthMapper.selectLast();
		if(last != null) {
			lastTime = last.getTime();
		}else {
			try {
				lastTime = DateTool.standardSdf().parse(Time.STAT_BEGIN_TIME);
				lastTime = DateTool.addMonth(lastTime, -1);
			} catch (ParseException e) {
				logger.error("error:", e);
			}
		}
		return lastTime;
	}
	
	public void statData(Map<Long, StatUser> statUserMap, List<Record> records, Date time,
			boolean isWrite) {
		
		if(records != null && records.size() > 0) {
			dataCycle: for(Record record : records) {
				if(record == null || record.getUserId() == null || record.getUserId() <= 0) {
					continue dataCycle;
				}
				
				StatUser temp = statUserMap.get(record.getUserId());
				if(temp == null) {
					temp = new StatUser(record.getUserId());
				}
				
				if(record.getType() == RecordType.STOCK) {
					temp.setProductionPrice(temp.getProductionPrice() + record.getProductionPrice());
					temp.setOtherPrice(temp.getOtherPrice() + record.getOtherPrice());
				}else if(record.getType() == RecordType.SELL) {
					temp.setSellPrice(temp.getSellPrice() + record.getSellPrice());
					temp.setProfitPrice(temp.getProfitPrice() + record.getProfitPrice());
				}else if(record.getType() == RecordType.DAMAGE) {
					temp.setDamagePrice(temp.getDamagePrice() + record.getDamagePrice());
				}
				statUserMap.put(temp.getUserId(), temp);
			}
		}
		
		if(isWrite) {
			if(statUserMap != null && statUserMap.size() > 0) {
				for(long userId : statUserMap.keySet()) {
					insertDay(statUserMap.get(userId), time);
				}
			}else {
				insertDay(new StatUser(0l), time);
			}
		}
	}
	
	public List<StatUser> selectById(Date time, long id, int limit) {
		return statUserDayMapper.selectById(time, id, limit);
	}
	
	public List<StatUser> selectByTime(Date begin, Date end, int timeType) {
		if(timeType == TimeType.DAY) {
			return statUserDayMapper.selectByTime(begin, end);
		}else if(timeType == TimeType.MONTH) {
			return statUserMonthMapper.selectByTime(begin, end);
		}
		return null;
	}
	
	public List<StatUser> fillingTime(List<StatUser> statUsers, Date begin, Date end, int type) {
		List<StatUser> list = new ArrayList<StatUser>();
		
		Date index = begin;
		while(true) {
			StatUser temp = new StatUser();
			temp.setTime(DateTool.standardSdf().format(index));
			if(statUsers != null && statUsers.size() > 0) {
				productionList: for(StatUser statUser : statUsers) {
					if((statUser.getTime().getTime()/1000l) == (temp.getTime().getTime()/1000l)) {
						temp = statUser;
						break productionList;
					}
				}
			}
			list.add(temp);
			
			if(type == TimeType.DAY) {
				index = DateTool.addDays(index, 1);
			}else if(type == TimeType.MONTH) {
				index = DateTool.addMonth(index, 1);
			}
			
			if(index.getTime() > end.getTime()) {
				break;
			}
		}
		
//		for(long i = begin.getTime(); i <= end.getTime(); i+=length) {
//			StatUser temp = new StatUser();
//			temp.setTime(DateTool.standardSdf().format(new Date(i)));
//			if(statUsers != null && statUsers.size() > 0) {
//				productionList: for(StatUser statUser : statUsers) {
//					if((statUser.getTime().getTime()/length) == (temp.getTime().getTime()/length)) {
//						temp = statUser;
//						break productionList;
//					}
//				}
//			}
//			list.add(temp);
//		}
		return list;
	}
}
