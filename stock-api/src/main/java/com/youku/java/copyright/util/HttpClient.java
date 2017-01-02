package com.youku.java.copyright.util;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.youku.java.copyright.exception.HttpException;

@Component
public class HttpClient {
	
	private static final int TIMEOUT = 5000;
	
	private static CloseableHttpClient httpclient;  
	
	private static Log log = LogFactory.getLog(HttpClient.class);

    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);  
        cm.setDefaultMaxPerRoute(50);
        httpclient = HttpClients.custom().setConnectionManager(cm).
                setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE).build();
    }
	
	/**
	 * 直接将json串赋值到body中。
	 * @param url
	 * @param jsonString
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String post(String url,String jsonString) throws ClientProtocolException, IOException{
		String result = "";
		HttpPost httpPost = null ;
		try{
			httpPost = new HttpPost(url);
			StringEntity se = new StringEntity(jsonString,"UTF-8");
			httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
			httpPost.setEntity(se);
			
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT).setConnectTimeout(TIMEOUT).build();//设置请求和传输超时时间
			httpPost.setConfig(requestConfig);
			
			long begin = System.currentTimeMillis();
			HttpResponse resp = httpclient.execute(httpPost);
            long use = System.currentTimeMillis() - begin;
            if(use > 200) {
                log.info("connect url("+use+"ms) > myTimeOut(200ms). url : " + url+", paramsMap : " + jsonString);
            }
			HttpEntity entity = resp.getEntity();

			if(resp.getStatusLine().getStatusCode() == 200){
			    result = EntityUtils.toString(entity);//取出应答字符串  
			}
		}catch(Exception e){
			throw new HttpException(e);
		}finally{
			if(httpPost != null){
				httpPost.abort();
			}
		}
		return result;
	}

	
	/**
	 * 直接将json串赋值到body中。
	 * @param url
	 * @param jsonString
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String put(String url,String jsonString) throws ClientProtocolException, IOException{
		String result = "";
		HttpPut httpPut = null ;
		try{
			httpPut = new HttpPut(url);
			StringEntity se = new StringEntity(jsonString,"UTF-8");
			httpPut.setHeader("Content-Type", "application/json;charset=UTF-8");
//			httpPut.setHeader("Auth-Info", "{\"isLogin\":1,\"userId\":0,\"userName\":\"system-batch\"}");
			httpPut.setEntity(se);
			
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT).setConnectTimeout(TIMEOUT).build();//设置请求和传输超时时间
			httpPut.setConfig(requestConfig);
			
			long begin = System.currentTimeMillis();
			HttpResponse resp = httpclient.execute(httpPut);
            long use = System.currentTimeMillis() - begin;
            if(use > 200) {
                log.info("connect url("+use+"ms) > myTimeOut(200ms). url : " + url+", paramsMap : " + jsonString);
            }
			HttpEntity entity = resp.getEntity();

			if(resp.getStatusLine().getStatusCode() == 200){
			    result = EntityUtils.toString(entity);//取出应答字符串  
			}
		}catch(Exception e){
			throw new HttpException(e);
		}finally{
			if(httpPut != null){
				httpPut.abort();
			}
		}
		return result;
	}
	
	
	/**
	 * 直接将json串赋值到body中。
	 * @param url
	 * @param jsonString
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String get(String url) throws ClientProtocolException, IOException{
		String result = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();  
		HttpGet httpGet = null ;
		try{
			httpGet = new HttpGet(url);
			httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
//			httpGet.setHeader("Auth-Info", "{\"isLogin\":1,\"userId\":0,\"userName\":\"system-batch\"}");
			
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT).setConnectTimeout(TIMEOUT).build();//设置请求和传输超时时间
			httpGet.setConfig(requestConfig);
			
			long begin = System.currentTimeMillis();
			HttpResponse resp = httpclient.execute(httpGet);
            long use = System.currentTimeMillis() - begin;
            if(use > 200) {
                log.info("connect url("+use+"ms) > myTimeOut(200ms). url : " + url);
            }
			HttpEntity entity = resp.getEntity();
			if(resp.getStatusLine().getStatusCode() == 200){
			    result = EntityUtils.toString(entity);//取出应答字符串  
			}
		}catch(Exception e){
			throw new HttpException(e);
		}finally{
			if(httpGet != null){
				httpGet.abort();
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		String url = "http://10.103.188.195/video.show?q=videoid:376617904&fd=title%20show_id%20md5%20videoid%20desc%20tudou_vid%20show_videostage%20show_videotype&pn=2&pl=100&ob=createtime";
		String response = "";
		String fingerResp = "";
		
		String fingerprint = "http://dnainterlayer.qd.intra.tudou.com/dna-interlayer/api/findSampleStatus?sid=1&did=201&md5=92159f1e7f058d6e27d026301cb68fb2";
		try {
			response = get(url);
			JSONObject jsonObject = JSON.parseObject(response);
			String show_videotype = jsonObject.getJSONArray("results").getJSONObject(0).getString("show_videotype");
			System.out.println("正片".equals(show_videotype));
			
			fingerResp = get(fingerprint);
			String msg = JSON.parseObject(fingerResp).getString("msg");
			System.out.println("初始完成".equals(msg));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(response);
		System.out.println(fingerResp);
		
	}
}
