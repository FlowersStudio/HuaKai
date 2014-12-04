package com.piscen.huakai.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import com.piscen.huakai.common.Charsets;
import com.piscen.huakai.common.GsonUtils;
import com.piscen.huakai.common.StringUtils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 *	HTTP请求线程类
 */
public class HttpRequest implements Runnable {

	private final static String TAG = "com.tuanche.fault.http";

	private Handler handler;
	private HttpClient client;
	private HttpEntity entity;
	private String token;

	private List<NameValuePair> params;
	private static final int GET = 0;
	private static final int POST = 1;

	public static final int REQUEST_START = 0;
	public static final int REQUEST_ERROR = 1;
	public static final int REQUEST_SUCCEED = 2;
	public static final int REQUEST_NOT_SC_OK = 3;

	private String url;
	private int method;

	public HttpRequest() {
		this(new Handler());
	}

	public HttpRequest(Handler handler) {
		this.handler = handler;
	}

	public void create(int method, String url, HttpEntity entity, String token) {
		this.method = method;
		this.url = url;
		this.entity = entity;
		this.token = token;
		HttpConnection.getInstance().push(this);
	}

	public void get(String url) {
		create(GET, url, null, null);
	}

	public void get(String url, String token) {
		create(GET, url, null, token);
	}

	public void get(String url, List<NameValuePair> params, String token) {
		this.method = GET;
		this.url = url;
		this.params = params;
		this.token = token;
		HttpConnection.getInstance().push(this);
	}

	/*
	 * 一般格式参数POST
	 */
	public void post(String url, List<NameValuePair> data, String token) {
		try {
			create(POST, url, new UrlEncodedFormEntity(data, HTTP.UTF_8), token);
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	/*
	 * JSON格式数据POST
	 */
	public void post(String url, String jsonString, String token) {
		try {
			StringEntity entity = new StringEntity(jsonString, "UTF-8");
			entity.setContentType("application/json");
			create(POST, url, entity, token);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		handler.sendMessage(Message.obtain(handler, REQUEST_START));
		client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), HttpConnection.CONN_TIMEOUT); // 6s
		HttpConnectionParams.setSoTimeout(client.getParams(), HttpConnection.SO_TIMEOUT); // 10s 
		try {
			HttpResponse response = null;
			switch (method) {
			case GET:
				HttpGet getMethod = null;
				if (params != null) {
					URI uri = new URI(url + "?" + URLEncodedUtils.format(params, "UTF-8"));
					getMethod = new HttpGet(uri);
				} else {
					getMethod = new HttpGet(url);
				}

				if (StringUtils.isNotEmpty(token)) {
					getMethod.setHeader("Cookie", "JSESSIONID="+token);
				}
				response = client.execute(getMethod);
				break;
			case POST:
				HttpPost httpPost = new HttpPost(url);
				httpPost.addHeader("Cookie", "JSESSIONID="+token);
				httpPost.setEntity(entity);
				response = client.execute(httpPost);
				break;
			}	
			if (response != null) {
				int statusCode = response.getStatusLine().getStatusCode();				
				Log.d(TAG, "httpPost request return " + statusCode);
				if (statusCode == HttpStatus.SC_OK) {
					processEntity(response.getEntity());
				} else {
					handler.sendMessage(Message.obtain(handler, REQUEST_NOT_SC_OK, null));					
				}
			}			
		} catch (Exception e) { 
			handler.sendMessage(Message.obtain(handler, REQUEST_ERROR, e));
		}
		HttpConnection.getInstance().finish(this);
	}

	private void processEntity(HttpEntity entity) throws IllegalStateException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), Charsets.UTF_8));
		StringBuffer strBuffer = new StringBuffer();
		String line = null;
		String result = null;
		while ((line = br.readLine()) != null) {
			strBuffer.append(line);
		}
		result = strBuffer.toString();
		result = StringUtils.trim(result);
		if(result.startsWith("<html")) {// only json format, there is a error from server if see html
			HttpResult<String> hr = new HttpResult<String>(2, "服务器端解析错误！",  null);
			result = GsonUtils.getGson().toJson(hr);
		}
		handler.sendMessage(Message.obtain(handler, REQUEST_SUCCEED, result));
	}

	/**
	 * 静态HTTP GET请求，用于后台无UI的HTTP请求
	 * @param url
	 * @param params
	 * @param token
	 * @return
	 */
	public static String doGet(String url, List<NameValuePair> params, String token) {
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 6000);
		HttpResponse response = null;
		HttpGet getMethod = null;
		String ret = null;
		try {
			if (params != null) {
				URI uri = new URI(url + "?" + URLEncodedUtils.format(params, "UTF-8"));
				getMethod = new HttpGet(uri);
			} else {
				getMethod = new HttpGet(url);
			}

			if (StringUtils.isNotEmpty(token)) {
				getMethod.setHeader("Cookie", "JSESSIONID="+token);
			}
			response = client.execute(getMethod);
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), Charsets.UTF_8));
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				strBuffer.append(line);
			}
			ret = strBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	
}