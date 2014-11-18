package com.piscen.huakai.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 
 * @author wu_zhang
 * @2014-11-17下午5:25:36
 * @TODO 文件操作
 */
public class FileAccess {
	/**
	 * String --> InputStream
	 * @param str
	 * @return
	 */
	public static InputStream String2InputStream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}
}
