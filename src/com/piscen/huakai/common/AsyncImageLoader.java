package com.piscen.huakai.common;

import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

public class AsyncImageLoader {
	
	//保存正在下载的图片URL集合，避免重复下载用
	private static HashSet<String> sDownloadingSet = new HashSet<String>();;
	//图片三种获取方式管理者，网络URL获取、内存缓存获取、外部文件缓存获取
	private static LoaderImpl impl = new LoaderImpl();;
	//线程池相关
	private static ExecutorService sExecutorService;
	//通知UI线程图片获取ok时使用
	private Handler handler; 
	private Context context;
	private ProgressDialog progressDialog;
	/**
	 * 异步加载图片完毕的回调接口  这个用于通知 activity
	 */
	public interface ImageCallback{
		public void onImageLoaded(Bitmap bitmap, String imageUrl);
	}
	
	public AsyncImageLoader(Context context){
		handler = new Handler();
		startThreadPoolIfNecessary();
		String defaultDir = context.getCacheDir().getAbsolutePath();
		setCachedDir(defaultDir);
		this.context = context;
	}
	
	/**
	 * 是否缓存图片至/data/data/package/cache/目录
	 * 默认不缓存
	 */
	public void setCache2File(boolean flag){
		impl.setCache2File(flag);
	}
	
	/**
	 * 设置缓存路径，setCache2File(true)时有效
	 */
	public void setCachedDir(String dir){
		impl.setCachedDir(dir);
	}

	
	/**开启线程池*/
	public static void startThreadPoolIfNecessary(){
		if(sExecutorService == null || sExecutorService.isShutdown() || sExecutorService.isTerminated()){
			sExecutorService = Executors.newFixedThreadPool(3);
			//sExecutorService = Executors.newSingleThreadExecutor();
		}
	}
	
	/**
	 * 异步下载图片，并缓存到memory中
	 */
	public void downloadImage(final String url, final ImageCallback callback){
		downloadImage(url, true, callback);
	}
	
	
	
	/**
	 * @param cache2Memory 是否缓存至memory中
	 * @param callback
	 */
	public void downloadImage(final String url, final boolean cache2Memory, final ImageCallback callback){
//		start();
		if(sDownloadingSet.contains(url)){
			Log.i("AsyncImageLoader", "图片正在下载-----！");
			return;
		}
		
		//看能够取到
		Bitmap bitmap = impl.getBitmapFromMemory(url);
		System.out.println("getBitmapFromMemory=="+ bitmap);
		
		//取不到去网络下载
		if(bitmap != null){
			if(callback != null){
//				succeed();
				callback.onImageLoaded(bitmap, url);
			}

		}else{

			//从网络端下载图片
			sDownloadingSet.add(url);
			sExecutorService.submit(new Runnable(){
				@Override
				public void run() {
					final Bitmap bitmap = impl.getBitmapFromUrl(url, cache2Memory);
					handler.post(new Runnable(){
						@Override
						public void run(){
							if(callback != null)
								callback.onImageLoaded(bitmap, url);
//							succeed();
							sDownloadingSet.remove(url);
						}
					});
				}
			});
		}
	}
	protected void start() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setTitle("提示");
		progressDialog.setMessage("加载中，请稍候...");
		progressDialog.show();
	}

	protected void succeed() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	protected void failed(String response) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	/**
	 * 预加载下一张图片，缓存至memory中
	 * @param url 
	 */
	public void preLoadNextImage(final String url){
		//将callback置为空，只将bitmap缓存到memory即可。
		downloadImage(url, null);
	}
	
}