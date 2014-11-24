
package com.piscen.huakai.common;

import java.nio.charset.Charset;

import android.os.Environment;

public class Charsets {
	public static final Charset UTF_8 = Charset.forName("UTF-8");
	/**
	 * 保存路径
	 */
	 public final static String CACHE_PATH_SD = Environment.getExternalStorageDirectory()+"/tuanche/cache/";
}
