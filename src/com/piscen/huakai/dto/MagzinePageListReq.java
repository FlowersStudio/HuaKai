package com.piscen.huakai.dto;

public class MagzinePageListReq extends Request{
	private String type;//杂志类型
	private String periods;//杂志期数
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
