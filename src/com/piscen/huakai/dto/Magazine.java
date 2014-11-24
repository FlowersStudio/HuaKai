package com.piscen.huakai.dto;
/**
 * 
 * @author wu_zhang
 * @2014-11-24下午5:50:18
 * @TODO 杂志列表
 */
public class Magazine {

	private String cover;//封面
	private String time;//时间
	private String type;//杂志类型
	private String periods;//杂志期数
	
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPeriods() {
		return periods;
	}
	public void setPeriods(String periods) {
		this.periods = periods;
	}
}
