package com.youku.java.copyright.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.youku.java.copyright.util.Constant.TimeType;
import com.youku.java.raptor.exception.InvalidArgumentException;
/*
 * @author chenlisong
 */
public class DateTool {

    public static SimpleDateFormat standardSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
	public static SimpleDateFormat standardSdf() {
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   return sdf;
	}
    
	public static SimpleDateFormat standardSdf2() {
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
	   return sdf;
	}
	
	public static Date addHours(Date date,int hours) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		date = calendar.getTime();
		return date;
	}
	
	public static Date addDays(Date date,int days) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		date = calendar.getTime();
		return date;
	}
	
	public static Date addYears(Date date,int years) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, years);
		date = calendar.getTime();
		return date;
	}
	
	public static Date addMonth(Date date,int months) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, months);
		date = calendar.getTime();
		return date;
	}
	
	/**
	 * 获取一天中开始的时间
	 * @return
	 */
	public static Date getBegin(Date time){
		Calendar calendar = new GregorianCalendar();   
		calendar.setTime(time);
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 0);  
		calendar.set(Calendar.SECOND, 0);  
		
		return calendar.getTime();
	}
	
	/**
	 * 获取一天中结束的时间
	 * @return
	 */
	public static Date getEnd(Date time){
		Calendar calendar = new GregorianCalendar();   
		calendar.setTime(time);
		calendar.set(Calendar.HOUR_OF_DAY, 23);  
		calendar.set(Calendar.MINUTE, 59);  
		calendar.set(Calendar.SECOND, 59);  
		
		return calendar.getTime();
	}
	
	/**
	 * 获取一天中开始的时间
	 * @return
	 */
	public static Date getHourBegin(Date time){
		Calendar calendar = new GregorianCalendar();   
		calendar.setTime(time);
		calendar.set(Calendar.MINUTE, 0);  
		calendar.set(Calendar.SECOND, 0);  
		
		return calendar.getTime();
	}
	
	/**
	 * 获取一天中结束的时间
	 * @return
	 */
	public static Date getHourEnd(Date time){
		Calendar calendar = new GregorianCalendar();   
		calendar.setTime(time);
		calendar.set(Calendar.MINUTE, 59);  
		calendar.set(Calendar.SECOND, 59);  
		
		return calendar.getTime();
	}
	
	/**
	 * 获取一月开始的时间
	 * @return
	 */
	public static Date getMonthBegin(Date time){
		Calendar calendar = new GregorianCalendar();   
		calendar.setTime(time);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 0);  
		calendar.set(Calendar.SECOND, 0);  
		return calendar.getTime();
	}
	
	/**
	 * 获取一月结束的时间
	 * @return
	 */
	public static Date getMonthEnd(Date time){
		Calendar calendar = new GregorianCalendar();   
		calendar.setTime(time);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE)); 
		calendar.set(Calendar.HOUR_OF_DAY, 23);  
		calendar.set(Calendar.MINUTE, 59);  
		calendar.set(Calendar.SECOND, 59);  
		return calendar.getTime();
	}
	
	/**
	 * 检测是否是同一年的数据
	 * @param begin
	 * @param end
	 * @return
	 */
	public static boolean checkOneYear(Date begin,Date end) {
		if(begin == null || end == null) {
			throw new IllegalArgumentException("INVALID_PARAM");
		}
		
		Calendar cbegin = Calendar.getInstance();
		cbegin.setTime(begin);
		Calendar cend = Calendar.getInstance();
		cend.setTime(end);
		return cend.get(Calendar.YEAR) == cbegin.get(Calendar.YEAR);
	}
	
	/**
	 * 检测是否是同一小时的数据
	 * @param begin
	 * @param end
	 * @return
	 */
	public static boolean checkOneHour(Date begin,Date end) {
		if(begin == null || end == null) {
			throw new IllegalArgumentException("INVALID_PARAM");
		}
		
		long hourSeconds = 1000l * 60 * 60;
		return begin.getTime() / hourSeconds == end.getTime() / hourSeconds;
	}
	
	/**
	 * 检测是否是同一月份的数据
	 * @param begin
	 * @param end
	 * @return
	 */
	public static boolean checkOneMonth(Date begin,Date end) {
		if(begin == null || end == null) {
			throw new IllegalArgumentException("INVALID_PARAM");
		}
		
		Calendar cbegin = Calendar.getInstance();
		cbegin.setTime(begin);
		Calendar cend = Calendar.getInstance();
		cend.setTime(end);
		return cend.get(Calendar.MONTH) == cbegin.get(Calendar.MONTH);
	}
	
	/**
	 * 从开始时间和结束时间，判断二者时间是跨月还是跨天，还是跨小时
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static int getTimeType (Date begin, Date end) {
		if(begin == null || end == null) {
			throw new InvalidArgumentException("开始时间和结束时间不能为空");
		}
		
		if(begin.getTime() >= end.getTime()) {
			throw new InvalidArgumentException("结束时间需要大于等于开始时间");
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(begin);
		int beginYear = calendar.get(Calendar.YEAR);
		int beginMonth = calendar.get(Calendar.MONTH) + 1;
		int beginDay = calendar.get(Calendar.DAY_OF_MONTH);

		calendar.setTime(end);
		int endYear = calendar.get(Calendar.YEAR);
		int endMonth = calendar.get(Calendar.MONTH) + 1;
		int endDay = calendar.get(Calendar.DAY_OF_MONTH);
		if(beginYear != endYear) {
			throw new InvalidArgumentException("无法跨年查询");
		}
		
		if(beginMonth != endMonth) {
			return TimeType.MONTH;
		}
		if(beginDay != endDay) {
			return TimeType.DAY;
		}
		return TimeType.HOUR;
	}
	
	public static Date getBeginDateByType(Date begin, Date end, boolean isBegin) {
		int timeType = DateTool.getTimeType(begin, end);
		if(timeType == TimeType.MONTH) {
			begin = DateTool.getMonthBegin(begin);
			end = DateTool.getMonthEnd(end);
		}else if(timeType == TimeType.DAY) {
			begin = DateTool.getBegin(begin);
			end = DateTool.getEnd(end);
		}else if(timeType == TimeType.HOUR) {
			begin = DateTool.getHourBegin(begin);
			end = DateTool.getHourEnd(end);
		}
		return isBegin ? begin : end;
	}
	
	public static void main(String[] args) {

		Date begin = new Date();
		System.out.println(standardSdf().format(begin));
		Date end = addHours(begin, 23);
		System.out.println(standardSdf().format(end));

		System.out.println(standardSdf().format(getBeginDateByType(begin, end, true)));
		System.out.println(standardSdf().format(getBeginDateByType(begin, end, false)));
		
		
//		Date now2 = new Date();
//		Date now2 = addDays(now, 2);
//		System.out.println(now.getTime());
//		System.out.println(now2.getTime());
//		System.out.println(checkOneHour(now, now2));
		
//		System.out.println(new Date());
//		System.out.println(getBegin(new Date()));
//		System.out.println(getEnd(new Date()));
//		System.out.println(getMonthBegin(new Date()));
//		System.out.println(getMonthEnd(new Date()));
//		System.out.println(addMonth(new Date(), -1));
//		System.out.println(addDays(new Date(), -1));
//		
//		System.out.println("---------");
//		Date now = DateTool.addMonth(new Date(), -1);
//    	Date begin = DateTool.getMonthBegin(now);
//    	Date end = DateTool.getMonthEnd(now);
//    	System.out.println(begin);
//    	System.out.println(end);
//    	System.out.println(DateTool.getMonthEnd(new Date()));
//    	System.out.println(DateTool.addYears(new Date(), 30));
//
//		System.out.println("---------");
//		Date d1 = new Date();
//		Date d2 = addDays(d1, 1);
//		Date d5 = addDays(d1, 4);
//		System.out.println(d5.getTime() - d1.getTime());
//		System.out.println(standardSdf.format(d1));
//		System.out.println(standardSdf.format(d5));
//		List<Date> exist = new ArrayList<Date>();
//		exist.add(d2);
//		System.out.println(getUnknowDate(exist, d1, d5));
		
	}

}
