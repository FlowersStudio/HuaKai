/*
 * Copyright (C) 2012 yueyueniao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.piscen.huakai.act;

import com.piscen.huakai.R;
import com.piscen.huakai.listen.UserFeedBackListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;


public class RightFragment extends Fragment implements OnClickListener {
	private TextView right_user;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		View view = inflater.inflate(R.layout.right, null);
		right_user = (TextView)view.findViewById(R.id.right_user);
		right_user.setOnClickListener(this);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("MainScreen"); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("MainScreen"); 
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_user:
			onUserFeedBackCheck();
			break;
		default:
			break;
		}
	}
	UserFeedBackListener userFeedBackListener;
	public void setUserFeedBackListener(UserFeedBackListener userFeedBackListene) {
		userFeedBackListener = userFeedBackListene;
	}
	protected void onUserFeedBackCheck() {
		userFeedBackListener.showUserFeedBack();
	}
}
