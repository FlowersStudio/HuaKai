/**
 * 
 */
package com.piscen.huakai.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class GsonUtils {
	private static final String pattern = "MMM dd, yyyy hh:mm:ss a";
	/**
	 * 注册了日期反解析格式的gson实例。 
	 * 
	 * @return Gson实例
	 */
	public static Gson getGson() {
		return new GsonBuilder()
		   .setDateFormat(pattern).create();
		
	}
}
