package com.piscen.huakai.common;

import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

public class AsyncImageLoader {
	
	//�����������ص�ͼƬURL���ϣ������ظ�������
	private static HashSet<String> sDownloadingSet = new HashSet<String>();;
	//ͼƬ���ֻ�ȡ��ʽ�����ߣ�����URL��ȡ���ڴ滺���ȡ���ⲿ�ļ������ȡ
	private static LoaderImpl impl = new LoaderImpl();;
	//�̳߳����
	private static ExecutorService sExecutorService;
	//֪ͨUI�߳�ͼƬ��ȡokʱʹ��
	private Handler handler; 

	/**
	 * �첽����ͼƬ��ϵĻص��ӿ�  �������֪ͨ activity
	 */
	public interface ImageCallback{
		public void onImageLoaded(Bitmap bitmap, String imageUrl);
	}
	
	public AsyncImageLoader(Context context){
		handler = new Handler();
		startThreadPoolIfNecessary();
		String defaultDir = context.getCacheDir().getAbsolutePath();
		setCachedDir(defaultDir);
	}
	
	/**
	 * �Ƿ񻺴�ͼƬ��/data/data/package/cache/Ŀ¼
	 * Ĭ�ϲ�����
	 */
	public void setCache2File(boolean flag){
		impl.setCache2File(flag);
	}
	
	/**
	 * ���û���·����setCache2File(true)ʱ��Ч
	 */
	public void setCachedDir(String dir){
		impl.setCachedDir(dir);
	}

	
	/**�����̳߳�*/
	public static void startThreadPoolIfNecessary(){
		if(sExecutorService == null || sExecutorService.isShutdown() || sExecutorService.isTerminated()){
			sExecutorService = Executors.newFixedThreadPool(3);
			//sExecutorService = Executors.newSingleThreadExecutor();
		}
	}
	
	/**
	 * �첽����ͼƬ�������浽memory��
	 */
	public void downloadImage(final String url, final ImageCallback callback){
		downloadImage(url, true, callback);
	}
	
	
	
	/**
	 * @param cache2Memory �Ƿ񻺴���memory��
	 * @param callback
	 */
	public void downloadImage(final String url, final boolean cache2Memory, final ImageCallback callback){
		
		if(sDownloadingSet.contains(url)){
			Log.i("AsyncImageLoader", "ͼƬ��������-----��");
			return;
		}
		
		//���ܹ�ȡ��
		Bitmap bitmap = impl.getBitmapFromMemory(url);
		System.out.println("getBitmapFromMemory=="+ bitmap);
		
		//ȡ����ȥ��������
		if(bitmap != null){
			if(callback != null){
				callback.onImageLoaded(bitmap, url);
			}

		}else{

			//�����������ͼƬ
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
							
							sDownloadingSet.remove(url);
						}
					});
				}
			});
		}
	}
	

	/**
	 * Ԥ������һ��ͼƬ��������memory��
	 * @param url 
	 */
	public void preLoadNextImage(final String url){
		//��callback��Ϊ�գ�ֻ��bitmap���浽memory���ɡ�
		downloadImage(url, null);
	}
	
}