package com.piscen.huakai.dto;

import java.util.List;

public class FirstPageListResp extends Response{//返回首页数据

private List<Magazine> list;


public List<Magazine> getList() {
	return list;
}

public void setList(List<Magazine> list) {
	this.list = list;
}


	
}
