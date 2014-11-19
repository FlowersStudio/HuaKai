package com.piscen.huakai.common;

import android.os.Environment;

public class BasePath {
	/**
	 * 数据文件的根路径 目前定在外部存储卡中
	 */
	public static final String BasePath=Environment.getExternalStorageDirectory().getAbsolutePath();
	/**
	 * 数据存放文件夹
	 */
	public static final String DBDirectoryName = "HuaKai";
	/**
	 * 图片的总文件夹的路径
	 */
	public static final String RootFileL = BasePath+"/"+DBDirectoryName+"/cache";
	
}
