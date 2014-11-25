package com.piscen.huakai.act;

import com.piscen.huakai.R;
import com.piscen.huakai.common.AsyncImageLoader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Magzine_page extends Fragment {

    public  ImageView image;
	public  getInfo mCallBack;
	private String num;
	private String URL;
	private ProgressDialog progressDialog;
    //oncreate view
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
	
            View v = inflater.inflate(R.layout.pagers, null);
            image = (ImageView)v.findViewById(R.id.backageground);
            
		return v;
    }
	
	
	public interface getInfo {
		public void GetInfoDate();
	}
	
	
	public void  setShowInfo(String num,String URL){
		this.num = num;	
		this.URL = URL;
		System.out.println("this.url===="+ this.URL);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			mCallBack = (getInfo)activity;
		}catch (Exception e) {

		}
	}
	@Override
	public void onStart() {
		super.onStart();
		/** 这个是更新界面的数据的*/
		try {
            // 获取这个页面的天气预报
	    	//getCurrentWeater("URL", "CityName");
	    	// 设置这个 fragment 的 背景图片
	    	String backImageUrl = URL;
	    	setBackImage(backImageUrl);
		
		} catch (Exception e) {
			//出现异常重新获取数据
			mCallBack.GetInfoDate();
		}
	}
//	protected void start() {
//		progressDialog = new ProgressDialog(getActivity().getApplicationContext());
//		progressDialog.setCanceledOnTouchOutside(false);
//		progressDialog.setTitle("提示");
//		progressDialog.setMessage("加载中，请稍候...");
//		progressDialog.show();
//	}
//
//	protected void succeed() {
//		if (progressDialog != null && progressDialog.isShowing()) {
//			progressDialog.dismiss();
//		}
//	}
	
	 /**
     * 设置
     */
    private void setBackImage(String backImageUrl) {
    	//得到loader
    	AsyncImageLoader loader = new AsyncImageLoader(getActivity().getApplicationContext());
    	//将图片缓存至外部文件中  
        loader.setCache2File(true); //false  
        //设置外部缓存文件夹  
        loader.setCachedDir(getActivity().getCacheDir().getAbsolutePath()); 
      
        System.out.println("111 address=="+ getActivity().getCacheDir().getAbsolutePath() );
//        start();
        //开始加载图片
        loader.downloadImage(backImageUrl, true/*false*/, new AsyncImageLoader.ImageCallback() {  
			@Override  
            public void onImageLoaded(Bitmap bitmap, String imageUrl) { 
                
            	if(bitmap != null){
                	image.setBackgroundDrawable(new BitmapDrawable(bitmap));
//                    succeed();
                }else{  
                    //下载失败，设置默认图片  
                	Log.e("imageFild", "imageFild");
                }  
            }  
        });  
	}
   
}
