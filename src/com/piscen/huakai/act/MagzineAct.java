package com.piscen.huakai.act;

import java.util.ArrayList;
import java.util.List;

import com.piscen.huakai.R;
import com.piscen.huakai.adapter.MagzineScanAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MagzineAct extends FragmentActivity implements ArrayListFragment.getInfo{
	private MagzineScanAdapter mAdapter;
    public static ViewPager mPager;
    
    private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
				mAdapter = new MagzineScanAdapter(getSupportFragmentManager());
				mAdapter.notifyDataSetChanged();
		        mPager.setAdapter(mAdapter);
		   
			}
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_magzine);
		initView();
	}

	private void initView() {
		 mPager = (ViewPager)findViewById(R.id.viewpager);
	        GetInfoDate();
	}
	 //这个其实是先获取各种 图片的地址 我做成假的放在adapter中了,
    //所以直接去塞入adapter
	@Override
	public void GetInfoDate() {
	    new Thread(new Runnable() {
			@Override
		   public void run() {
					handler.sendMessage(new Message());
			}
		}).start();
	}
}
