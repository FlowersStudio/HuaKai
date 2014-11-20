package com.piscen.huakai.act;

import com.piscen.huakai.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

public class FlashActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flash);
		new CountDownTimer(2000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
			}
			@Override
			public void onFinish() {
				Intent intent = new Intent(FlashActivity.this,SlidingActivity.class);
				startActivity(intent);
				overridePendingTransition(R.drawable.zoomin, R.drawable.zoomout);
				finish();
			}
		}.start();
	}
}
