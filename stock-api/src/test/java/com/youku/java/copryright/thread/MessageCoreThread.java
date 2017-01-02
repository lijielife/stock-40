package com.youku.java.copryright.thread;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.youku.java.copyright.util.HttpClient;

public class MessageCoreThread extends Thread{

	private CountDownLatch latch;
	
	private JSONObject msg;
	
	private List<String> show_videotype;
	
	private List<String> audiolang;
	
	private List<String> error;
	
	public MessageCoreThread(CountDownLatch latch, JSONObject msg, List<String> show_videotype,
			List<String> audiolang, List<String> error) {
		this.latch = latch;
		this.msg = msg;
		this.show_videotype = show_videotype;
		this.audiolang = audiolang;
		this.error = error;
	}
	
	@Override
	public void run() {
		long msgid = msg.getLongValue("msgid");
		String videoId = msg.getString("id");
		System.out.println("execute msgid:"+msgid+", videoId:"+videoId);
		try {
			if("youku".equals(msg.getString("site"))) {
				String url = "http://10.103.188.195/video.show?q=videoid:"+videoId+"&fd=title%20show_id%20audiolang%20md5%20videoid%20desc%20tudou_vid%20show_videostage%20show_videotype&pn=2&pl=100&ob=createtime";
				String response = HttpClient.get(url);

				System.out.println("response:"+response);
				JSONObject jsonObject = JSON.parseObject(response);
				int total = jsonObject.getIntValue("total");
				if(total > 0) {
					JSONArray results = jsonObject.getJSONArray("results");
					JSONObject msg = results.getJSONObject(0);
					if(msg != null) {
						if("正片".equals(msg.getString("show_videotype"))) {
							show_videotype.add(videoId);
						}
						if(msg.get("audiolang") != null) {
							audiolang.add(videoId);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			error.add(videoId);
		}finally {
			latch.countDown();
		}
	}
}
