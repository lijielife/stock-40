package com.youku.java.copyright.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.youku.java.copyright.annotation.Note;
import com.youku.java.raptor.exception.InvalidArgumentException;
/*
 * @author chenlisong
 */
public class CommonUtil {

	private static Log log = LogFactory.getLog(CommonUtil.class);
	
	/**
	 * 根据传入的对象集合，拿出对象中某一字段的集合
	 * @param list
	 * @param fieldName
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> entity(List<? extends Object> list,String fieldName,Class<? extends Object> t){
		List<T> result = new ArrayList<T>();
		if(list == null || list.size() <= 0){
			return result;
		}
		for(Object o : list){
			
			String getMethod = "get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
			try {
				result.add((T) o.getClass().getDeclaredMethod(getMethod).invoke(o));
			} catch (Exception e) {
				throw new RuntimeException("CommonUtil entity error ."+e);
			}
		}
		return result;
	}
	
	public static Object addColumn(Object object,Object append, String[] ignore){
		List<String> ignoreList = Arrays.asList(ignore);
		
		Field[] fields = object.getClass().getDeclaredFields();
		if(fields == null || fields.length <= 0){
			return null;
		}
		for(Field field : fields){
			String fieldName = field.getName();
			//"id"跳过
			if("serialVersionUID".equals(fieldName) || "id".equals(fieldName)
					 || "time".equals(fieldName) || "userId".equals(fieldName)) {
				continue;
			}
			
			if(ignoreList.contains(fieldName)) {
				continue;
			}
			
			try{
				if(fieldName != null && fieldName.length() > 0){
					String getMethodName = "get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
					Method getMethod = object.getClass().getDeclaredMethod(getMethodName);
					Object objectValue = getMethod.invoke(object);
					Object appendValue = getMethod.invoke(append);
					String setMethodName = "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
					
					if(field.getType() == Double.class){
						Method setMethod = object.getClass().getDeclaredMethod(setMethodName, Double.class);
						setMethod.invoke(object, (Double)objectValue + (Double)appendValue);
					}else if(field.getType() == Integer.class){
						Method setMethod = object.getClass().getDeclaredMethod(setMethodName, Integer.class);
						setMethod.invoke(object, (Integer)objectValue + (Integer)appendValue);
					}
				}
			}catch (Exception e) {
				log.error("error:", e);
			}
		}
		return object;
	}
	
	public static void setDefaultValues(List<? extends Object> objs) {
		if(objs == null || objs.size() <= 0) {
			return ;
		}
		for(Object obj : objs) {
			setDefaultValue(obj);
		}
	}
	
	/**
	 * 传入对象，检测如果有field是null的，根据field类型自动设置默认值
	 * @param obj
	 */
	public static void setDefaultValue(Object obj){
		try{
			if(obj == null){
				return ;
			}
			Field[] fields = obj.getClass().getDeclaredFields();
			if(fields == null || fields.length <= 0){
				return ;
			}
			for(Field field : fields){
				String fieldName = field.getName();
				//"id"跳过
				if("serialVersionUID".equals(fieldName) || "id".equals(fieldName)) {
					continue;
				}
				
				if(fieldName != null && fieldName.length() > 0){
					String getMethodName = "get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
					Method getMethod = obj.getClass().getDeclaredMethod(getMethodName);
					Object getObject = getMethod.invoke(obj);
					if(getObject == null){
						String setMethodName = "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
						Method setMethod = null;
						
						if(field.getType() != Date.class){
							setMethod = obj.getClass().getDeclaredMethod(setMethodName,field.getType());
						}else{
							try{
								setMethod = obj.getClass().getDeclaredMethod(setMethodName,Date.class);
							}catch (Exception e) {
								setMethod = obj.getClass().getDeclaredMethod(setMethodName,String.class);
							}
						}
						
						if(field.getType() == String.class){
							setMethod.invoke(obj,"");	
						}else if(field.getType() == Integer.class){
							setMethod.invoke(obj,0);	
						}else if(field.getType() == Boolean.class){
							setMethod.invoke(obj,false);	
						}else if(field.getType() == Double.class){
							setMethod.invoke(obj,0.0);	
						}else if(field.getType() == Date.class){
							try{
								setMethod.invoke(obj,DateTool.standardSdf().parse("1970-01-01 08:00:00"));	
							}catch (Exception e) {
								setMethod.invoke(obj,"1970-01-01 08:00:00");	
							}
						}else if(field.getType() == Long.class){
							setMethod.invoke(obj,0l);	
						}
					}else {
						//对string字符串做trim处理
						if(field.getType() == String.class){
							String setMethodName = "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
							Method setMethod = obj.getClass().getDeclaredMethod(setMethodName,String.class);
							setMethod.invoke(obj,getObject.toString().trim());	
						}
					}
				}
			}
		}catch(Exception e){
			log.error("exception:"+e);
			throw new InvalidArgumentException("exception:" + e);
		}
	}
	
	/**
	 * 传入JSON对象转换成指定的对象实例
	 * @param json
	 * @param t
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T parseJson2Object(String json,Class<? extends Object> t) throws ClassNotFoundException,
						InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, 
						IllegalArgumentException, InvocationTargetException{
		JSONObject jsonObject = JSON.parseObject(json);
		Class resultClass = Class.forName(t.getName());
		Object obj = resultClass.newInstance();
		for(Field field : t.getDeclaredFields()){
			String setMethodName = "set"+field.getName().substring(0,1).toUpperCase()+field.getName().substring(1);
			Method setMethod = null;
			
			if(jsonObject.get(field.getName()) == null){
				continue;
			}
			
			if(field.getType() == String.class){
				setMethod = obj.getClass().getDeclaredMethod(setMethodName,field.getType());
				setMethod.invoke(obj,jsonObject.getString(field.getName()));	
			}else if(field.getType() == Integer.class){
				setMethod = obj.getClass().getDeclaredMethod(setMethodName,field.getType());
				setMethod.invoke(obj,jsonObject.getIntValue(field.getName()));	
			}else if(field.getType() == Long.class){
				setMethod = obj.getClass().getDeclaredMethod(setMethodName,field.getType());
				setMethod.invoke(obj,jsonObject.getLongValue(field.getName()));	
			}else if(field.getType() == Date.class){
				try{
					setMethod = obj.getClass().getDeclaredMethod(setMethodName,Date.class);
					setMethod.invoke(obj,jsonObject.getDate(field.getName()));	
				}catch (Exception e) {
					setMethod = obj.getClass().getDeclaredMethod(setMethodName,String.class);
					setMethod.invoke(obj,DateTool.standardSdf().format(jsonObject.getDate(field.getName())));	
				}
			}
		}
		
		return (T)obj;
	}
	
	@SuppressWarnings({ "unchecked", "null" })
	public static <T> List<T> parseJson2List(String json,Class<? extends Object> t) throws ClassNotFoundException,
	InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, 
	IllegalArgumentException, InvocationTargetException{
		JSONArray jsonArray = JSON.parseArray(json);
		if(jsonArray == null && jsonArray.size() <= 0) {
			return null;
		}
		List<T> list = new ArrayList<T>();
		for(int i=0 ; i < jsonArray.size() ;i++){
			list.add((T)parseJson2Object(jsonArray.getJSONObject(i).toJSONString(), t));
		}
		return list;
	}
	
	public static String parseMap2Param(Map<String,Object> map) {
		StringBuffer params = new StringBuffer("");
		if(map != null && map.size() > 0) {
			for(String key : map.keySet()) {
				params.append("&"+key+"="+map.get(key));
			}
			if(params.length() > 0) {
				return params.substring(1);
			}
		}
		
		return params.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T clone(T obj){  
        T cloneObj = null;  
        try {  
            //写入字节流  
            ByteArrayOutputStream out = new ByteArrayOutputStream();  
            ObjectOutputStream obs = new ObjectOutputStream(out);  
            obs.writeObject(obj);  
            obs.close();  
              
            //分配内存，写入原始对象，生成新对象  
            ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());  
            ObjectInputStream ois = new ObjectInputStream(ios);  
            //返回生成的新对象  
            cloneObj = (T) ois.readObject();  
            ois.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return cloneObj;  
    }
	
	@SuppressWarnings("rawtypes")
	public static Map<String,String> getNoteMap(Class c) {
		Map<String,String> noteMap = new LinkedHashMap<String,String>();

		if(c == null) {
			return noteMap;
		}
		
		
		Field[] fields = c.getDeclaredFields();
		if(fields != null && fields.length > 0) {
			for(Field field : fields) {
				if(field.getType() == Integer.class || field.getType() == Long.class || 
						field.getType() == String.class || field.getType() == Date.class || 
						field.getType() == Double.class) {
					Note note = field.getAnnotation(Note.class);
					if(note != null) {
						noteMap.put(field.getName(), note.name());
					}
				}
			}
		}
		return noteMap;
	}
	
	public static void writeFile(List<String> list,String filepath){
		File file = new File(filepath);
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(file,true);
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath, true), "UTF-8"));
        	for(String str : list){
            	bw.write(str);
                bw.newLine();
            }
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
	
	public static String readFile(String filepath){
		File file = new File(filepath);
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            return br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
            	fr.close();
            	br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		return "";
	}
	
	public static void writeFile(String msg,String filepath){
		File file = new File(filepath);
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(file,true);
//            bw = new BufferedWriter(fw);
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath, true), "UTF-8"));
        	bw.write(msg);
            bw.newLine();
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

}
