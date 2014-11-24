package com.piscen.huakai.dto;

import java.util.List;

public class MagzinePageListResp extends Response{
	private List<MagzinePage> list;

	public List<MagzinePage> getList() {
		return list;
	}

	public void setList(List<MagzinePage> list) {
		this.list = list;
	}
	
	
}
