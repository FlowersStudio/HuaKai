package com.piscen.huakai.dto;

import java.util.List;

public class FirstPageListResp extends Response{//返回首页数据

private List<Magazine> list;//首页封面数据

private List<TopNews> listnews; //顶部滑动图片数据



public List<TopNews> getListnews() {
	return listnews;
}

public void setListnews(List<TopNews> listnews) {
	this.listnews = listnews;
}

public List<Magazine> getList() {
	return list;
}

public void setList(List<Magazine> list) {
	this.list = list;
}


	
}
