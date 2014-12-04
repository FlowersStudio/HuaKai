package com.piscen.huakai.act;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.piscen.huakai.R;
import com.piscen.huakai.adapter.MagzineScanAdapter;
import com.piscen.huakai.common.GsonUtils;
import com.piscen.huakai.common.RequestUrls;
import com.piscen.huakai.common.ResponseState;
import com.piscen.huakai.common.StringUtils;
import com.piscen.huakai.dto.FirstPageListReq;
import com.piscen.huakai.dto.FirstPageListResp;
import com.piscen.huakai.dto.MagzinePage;
import com.piscen.huakai.dto.MagzinePageListReq;
import com.piscen.huakai.dto.MagzinePageListResp;
import com.piscen.huakai.http.HttpHandler;
import com.piscen.huakai.http.HttpRequest;
import com.piscen.huakai.http.JsonUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

public class MagzineAct extends FragmentActivity implements Magzine_page.getInfo{
	private MagzineScanAdapter mAdapter;
	public static ViewPager mPager;
	//期数
	private String Periods;
	//类型
	private String type;
	private Intent intent;
	private List<MagzinePage> list;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mAdapter = new MagzineScanAdapter(getSupportFragmentManager(),list);
			mAdapter.notifyDataSetChanged();
			mPager.setAdapter(mAdapter);

		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_magzine);
		intent = getIntent();
		Periods = intent.getStringExtra("Periods");
		type = intent.getStringExtra("type");
		query();
		initView();
	}
	//得到杂志列表
	private void query(){
		MagzinePageListReq request = new MagzinePageListReq();
		request.setRequestorname(StringUtils.execute(request));
		request.setPeriods(Periods);
		request.setType(type);
		Gson gson = GsonUtils.getGson();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("json",  gson.toJson(request)));
		new HttpRequest(FdqueryHandler).post(RequestUrls.BATH_URL, list,null);	
	}
	private Handler FdqueryHandler = new HttpHandler(this){

		protected void succeed(String response) {
			System.out.println("response:"+response);
			initState(response);
		};
		protected void failed(String response) { 

		};
	};
	private void initState(String response){
		MagzinePageListResp resp = JsonUtil.json2Entity(response, "MagzinePageListResp");
//		int code = Integer.valueOf(resp.getCode());
//		if( code == ResponseState.SUCCESSED){
			list = resp.getList();
			System.out.println("response......:"+response);
				GetInfoDate();
//		}
//		if(code == ResponseState.SYSERROR){
//			Toast.makeText(this, "系统错误", 1).show();
//		}
//		if(code == ResponseState.DATANULL){
//			Toast.makeText(this, "数据为空", 1).show();
//		}
//		if(code == ResponseState.PARAMETERERROR){
//			Toast.makeText(this, "参数错误", 1).show();
//		}
	}

	private void initView() {
		mPager = (ViewPager)findViewById(R.id.viewpager);

//		GetInfoDate();
	}
	@Override
	public void GetInfoDate() {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
				if(list!=null){
					handler.sendMessage(new Message());
				}
//			}
//		}).start();
	}
}
