package com.piscen.huakai.act;


import com.piscen.huakai.AppBase;
import com.piscen.huakai.R;
import com.piscen.huakai.common.CrashHandler;
import com.piscen.huakai.common.CrashHandler.OnExcepteionCrashListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.util.Log;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
/**
 * 
 * @author wu_zhang
 * @2014-11-17下午5:24:48
 * @TODO
 */
public class BaseActivity extends FragmentActivity implements OnExcepteionCrashListener{
	private CrashHandler crash;
	protected AppBase app;
	private Toast mToast;
	FeedbackAgent fb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		app = (AppBase) getApplication();
		app.addActivity(this);
		crash = CrashHandler.getInstance();  
		crash.init(getApplicationContext());
		crash.setOnExcepteionCrashListener(this);
		
	}

	@Override
	public void OnExcepteionCrash(Context mContext) {
		// TODO Auto-generated method stub

	}
	public void showToast(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	@Override
	public void finish() {
		super.finish();
		MobclickAgent.onKillProcess(this);
	}
}
