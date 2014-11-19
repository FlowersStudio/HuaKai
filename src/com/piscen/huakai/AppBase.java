package com.piscen.huakai;

import java.util.LinkedList;
import java.util.List;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.view.Menu;
/**
 * 
 * @author wu_zhang
 * @2014-11-17下午5:24:40
 * @TODO
 */
public class AppBase extends Application {
	public static AppBase instance;

	/**  
	 * 自定义Activity栈，退出应用时统一销毁
	 */
	private static List<Activity> activityList = new LinkedList<Activity>();

	/**
	 * 日志标记
	 */
	private String tag;
	/**
	 * 用户设备ID
	 */
	private String deviceId;
	/**
	 * 用户MAC地址
	 */
	private String macAddr;
	/**
	 * 用户SDK版本
	 */
	private int sdk;
	/**
	 * 包路径名词
	 */
	private String packName;
	/**
	 * 应用当前版本
	 */
	private int versionCode;
	/**
	 * 应用当前版本名称
	 */
	private String versionName;
	/**
	 * 待处理工单轮询时间（秒）
	 */
	public static final int ORDER_POLL_TIME = 600;// 3 * 60;
	
	/**
	 * 初始化各全局变量
	 */
	public void onCreate() {
		super.onCreate();
		setTag("HUAKAI");
		setSdk(android.os.Build.VERSION.SDK_INT);
		setPackName(getPackageName());
		instance = this;
	}


	public static Context getInstance() {
		return instance;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getMacAddr() {
		return macAddr;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packageName) {
		this.packName = packageName;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}

	public int getSdk() {
		return sdk;
	}

	public void setSdk(int sdk) {
		this.sdk = sdk;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}



	/**
	 * 判断SD Card是否可用
	 */
	public boolean isCanUseSdCard() {
		try {
			return Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	public  static void removeActivity(Activity activity) {
		activityList.remove(activity);
	}
	public int actListSize() {
		return activityList.size();
	}

	public List<Activity> getActivityList() {
		return activityList;
	}

	public Activity getLastActivity() {
		return activityList.get(actListSize() - 1);
	}

	public boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(1000);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}


	/**
	 * 退出应用
	 */
	 public static  void exit() {
       for(int i=activityList.size()-1;i>0;i--){
           activityList.get(i).finish();
       }
       System.exit(0);    
   }

}
