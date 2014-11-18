package com.piscen.huakai.dto;

import android.graphics.Bitmap;
/**
 * 
 * @author wu_zhang
 * @2014-11-18下午6:52:22
 * @TODO  杂志预览
 */
public class MagazineCover {
	private String name;//期数
	private Bitmap cover;//封面
	private String title;//标题
	private String time;//日期
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Bitmap getCover() {
		return cover;
	}
	public void setCover(Bitmap cover) {
		this.cover = cover;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
