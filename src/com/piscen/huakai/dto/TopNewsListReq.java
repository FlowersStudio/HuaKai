package com.piscen.huakai.dto;

import java.util.List;
/**
 * 
 * @author wu_zhang
 * @2014-11-24下午5:48:13
 * @TODO 
 */
public class TopNewsListReq extends Response {
	private List<TopNews> list;

	public List<TopNews> getList() {
		return list;
	}

	public void setList(List<TopNews> list) {
		this.list = list;
	}
	
}
