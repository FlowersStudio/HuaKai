package com.piscen.huakai.http;


import com.piscen.huakai.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * 处理HTTP返回的Handler父类
 */
public class HttpHandler extends Handler {

	private Context context;
	private ProgressDialog progressDialog;
	protected boolean showProgress = true;
	private final static String TAG = "com.piscen.huakai.http";

	public HttpHandler(Context context) {
		this.context = context;
	}

	public HttpHandler(Context context , boolean showProgress) {
		this.context = context;
		this.showProgress = showProgress;
	}

	protected void start() {
		if(!showProgress) {
			return;
		}
		progressDialog = new ProgressDialog(context);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setTitle(context.getString(R.string.dialog_title));
		progressDialog.setMessage(context.getString(R.string.dialog_request));
		progressDialog.show();
	}

	protected void succeed(String response) {
		if (showProgress && progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	protected void failed(String response) {
		if (showProgress && progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	protected void otherHandleMessage(Message message) {
	}

	public void handleMessage(Message message) {
		switch (message.what) {
		case HttpRequest.REQUEST_START:
			if(context!=null && !((Activity) context).isFinishing()){
				start();}
			break;
		case HttpRequest.REQUEST_SUCCEED:
			if(progressDialog != null) {
				if(context!=null && !((Activity) context).isFinishing()){
					progressDialog.dismiss();
				}}
			String response = (String) message.obj;
			try {
				succeed(response);
			} catch (Exception e1) {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}
			break;
		case HttpRequest.REQUEST_ERROR:
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			Exception e = (Exception) message.obj;
			Log.e(TAG, "http connection fail : " + e.getMessage());
			failed(e.getMessage());
			if(context!=null && !((Activity) context).isFinishing()){
				Toast.makeText(context, context.getString(R.string.dialog_request_error), Toast.LENGTH_LONG).show();
			}
			break;
		case HttpRequest.REQUEST_NOT_SC_OK:
			if(context!=null && !((Activity) context).isFinishing()){
				Toast.makeText(context, context.getString(R.string.dialog_request_error), Toast.LENGTH_LONG).show();
			}
			break;
		} 
		otherHandleMessage(message);
	}

}