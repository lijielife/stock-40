package com.youku.java.copyright.cron;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.youku.java.copyright.bean.Record;
import com.youku.java.copyright.bean.StatUser;
import com.youku.java.copyright.util.CommonUtil;
import com.youku.java.copyright.util.Constant.TimeLength;
import com.youku.java.copyright.util.DateTool;
/**
 * @author chenlisong
 */
@Component
public class SystemCron extends BaseCron{	
	
	/**
	 * * 将进货，销售，折损记录以天维度统计
	 * 
	 */
	@Scheduled(cron="${cron.stat.day}")
    public void statDay () throws Exception{
    	MDC.put("logFileName", "statDay");
		log.info("statDay execute.");
		
		//找到要遍历的天
		Date dayTime = statUserService.selectLastDay();
		
		dayCycle: do {
			dayTime = DateTool.addDays(dayTime, 1);
			Date now = new Date();
			if(now.getTime() - dayTime.getTime() < TimeLength.DAY) {
				break dayCycle;
			}
			
			Map<Long, StatUser> statUserMap = new HashMap<Long, StatUser>();
			
			Date index = new Date(dayTime.getTime()-1);
			dataCycle: do {
				List<Record> records = recordService.selectByTime(index, 1000);
				List<Record> data = new ArrayList<Record>();
				if(records == null || records.size() <= 0) {
					break dataCycle;
				}
				
				if(records.get(0).getCreateTime().getTime() - dayTime.getTime() > TimeLength.DAY) {
					break dataCycle;
				}
				
				recordCycle: for(Record record : records) {
					if(record.getCreateTime().getTime() < dayTime.getTime()) {
						continue recordCycle;
					}else if(record.getCreateTime().getTime() - dayTime.getTime() < TimeLength.DAY) {
						data.add(record);
						index = record.getCreateTime();
					}else {
						break recordCycle;
					}
				}
				statUserService.statData(statUserMap, data, dayTime, false);
				data = new ArrayList<Record>();
			} while (true);
			statUserService.statData(statUserMap, null, dayTime, true);
			
		} while (true);
		
		log.info("sleep minute:" + 10);
		Thread.sleep(TimeLength.MINUTE * 10);
    }
	

	
	/**
	 * * 将进货，销售，折损记录以月维度统计
	 * 
	 */
	@Scheduled(cron="${cron.stat.month}")
    public void statMonth () throws Exception{
		
		MDC.put("logFileName", "statMonth");
		log.info("statMonth execute.");
		
		Date monthTime = statUserService.selectLastMonth();
		
		monthCycle: do {
			monthTime = DateTool.addMonth(monthTime, 1);
			
			Date dayLastTime = statUserService.selectLastDay();
			if(dayLastTime.getTime() - monthTime.getTime() < TimeLength.MONTH) {
				break monthCycle;
			}
			
			long id = 0;
			Map<Long, StatUser> statUserMap = new HashMap<Long, StatUser>();
			
			dayCycle: do {
				List<StatUser> statUsers = statUserService.selectById(monthTime, id, 200);
				if(statUsers == null || statUsers.size() <= 0) {
					break dayCycle;
				}
				dataCycle: for (StatUser statUser : statUsers) {
					if (statUser.getUserId() == 0 || 
							statUser.getTime().getTime() < monthTime.getTime()) {
						id = statUser.getId();
						continue dataCycle;
					} else if (statUser.getTime().getTime() - monthTime.getTime() < TimeLength.MONTH) {
						StatUser temp = statUserMap.get(statUser.getUserId());
						if (temp == null) {
							temp = new StatUser(statUser.getUserId());
						}
						temp = (StatUser) CommonUtil.addColumn(temp, statUser,
								new String[] {});
						statUserMap.put(temp.getUserId(), temp);

						id = statUser.getId();
					} else if (statUser.getTime().getTime() - monthTime.getTime() >= TimeLength.MONTH) {
						break dayCycle;
					}
				}
			} while (true);
			
			if(statUserMap != null && statUserMap.size() > 0) {
				for(Long userId : statUserMap.keySet()) {
					statUserService.insertMonth(statUserMap.get(userId), monthTime);
				}
			}else {
				statUserService.insertMonth(new StatUser(0l), monthTime);
			}
			
		} while (true);
		
		log.info("sleep hour:" + 10);
		Thread.sleep(TimeLength.HOUR * 10);
		
	}
}