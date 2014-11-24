package com.piscen.huakai.dto;
	/**
	 * 
	 * @author wu_zhang
	 * @2014-11-24下午5:50:42
	 * @TODO 具体杂志页面
	 */
public class MagzinePage {
	private int pagination;//页数
	private String pageImage;//杂志图片
	public int getPagination() {
		return pagination;
	}
	public void setPagination(int pagination) {
		this.pagination = pagination;
	}
	public String getPageImage() {
		return pageImage;
	}
	public void setPageImage(String pageImage) {
		this.pageImage = pageImage;
	}
	
}
