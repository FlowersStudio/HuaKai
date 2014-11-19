package com.piscen.huakai.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;

/**
 * @author wu_zhang
 * @desc 异步加载图片管理器
 */
public class LoaderImpl {

	 //是否缓存图片至本地文件
	 private boolean diskcache = true;
	 //缓存目录,默认是/data/data/package/cache/目录
 	 private String cachedDir = BasePath.RootFileL;
	 
 	 // 定义二级缓存 容器  软引用
	 private static ConcurrentHashMap<String, SoftReference<Bitmap>> current_hashmap = 
			                            new ConcurrentHashMap<String, SoftReference<Bitmap>>();
	 // LruCache 用强引用将  图片放入     LinkedHashMap 
	 final static int memClass = (int) Runtime.getRuntime().maxMemory(); 
	
	 private static LruCache<String, Bitmap>  imageCache = new LruCache<String, Bitmap>(memClass/5) {
	  protected int sizeOf(String key, Bitmap value) {  
          if(value != null) {  
              // 计算存储bitmap所占用的字节数  
              return value.getRowBytes() * value.getHeight();  
          } else {  
              return 0;  
          }  
      }  
        
      @Override  
      protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {  
          if(oldValue != null) {  
              // 当硬引用缓存容量已满时，会使用LRU算法将最近没有被使用的图片转入软引用缓存  
        	  current_hashmap.put(key, new SoftReference<Bitmap>(oldValue));  
        	  System.out.println("图片存进 2级缓存 ");
          }  
      }  
	};
	
	
	/**
	 * 是否缓存图片至外部文件
	 * @param flag 
	 */
	public void setCache2File(boolean diskcache){
		this.diskcache = diskcache;
	}
	
	/**
	 * 设置缓存图片到外部文件的路径
	 * @param cacheDir
	 */
	public void setCachedDir(String cacheDir){
		this.cachedDir = cacheDir;
	}
	
	/**
	 * 从网络端下载图片
	 */
	public Bitmap getBitmapFromUrl(String url, boolean cache2Memory){
		Bitmap bitmap = null;
		try{
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection)u.openConnection();  
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			System.out.println("网络下载=="+ url);
			
			if(cache2Memory){
				//1.缓存bitmap至内存软引用中
				imageCache.put(url, bitmap);
				if(diskcache){
					System.out.println("缓存到本地==="+ url);
					//2.缓存bitmap至/data/data/packageName/cache/文件夹中
					String fileName = getMD5Str(url);
					String filePath = this.cachedDir + "/" +fileName;
					FileOutputStream fos = new FileOutputStream(filePath);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				}
			}
			is.close();
			conn.disconnect();
			return bitmap;
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 从内存缓存中获取bitmap
	 * @param url
	 * @return bitmap or null.
	 */
	public Bitmap getBitmapFromMemory(String url){
		 Bitmap bitmap = null;
		 synchronized (imageCache) {
				bitmap = imageCache.get(url);
				if (null != bitmap) {
					imageCache.remove(url);
					// 按照 LRU是Least Recently Used 近期最少使用算法 内存算法 就近 就 原则 放到首位
					imageCache.put(url, bitmap);
					System.out.println(" 在缓存1中找图片了 =" + url);
					return bitmap;
				}
			}
		 
		 
	    // 2. 到二级 缓存找
		SoftReference<Bitmap> soft = current_hashmap.get(url);
		if (soft != null) {
			//得到 软连接 中的图片
			Bitmap soft_bitmap = soft.get();      
			if (null != soft_bitmap) {
				System.out.println(" 在缓存2中找图片了 =" + url);
				return soft_bitmap;
			}
		} else {
			// 没有图片的话 把这个key删除
			current_hashmap.remove(url);      
		}
		
		
		//从外部缓存文件读取
		if(diskcache){
			bitmap = getBitmapFromFile(url);
			if(bitmap != null)
				imageCache.put(url, bitmap);
		}
		
		return bitmap;
	}
	
	/**
	 * 从外部文件缓存中获取bitmap
	 * @param url
	 * @return
	 */
	private Bitmap getBitmapFromFile(String url){
		Bitmap bitmap = null;
		String fileName = getMD5Str(url);
		if(fileName == null){
			return null;
		}
		String filePath = cachedDir + "/" + fileName;
		try {
			FileInputStream fis = new FileInputStream(filePath);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			bitmap = null;
		}
		return bitmap;
	}
	
	
	/**  
     * MD5 加密  
     */   
    private static String getMD5Str(String str) {   
        MessageDigest messageDigest = null;   
        try {   
            messageDigest = MessageDigest.getInstance("MD5");   
            messageDigest.reset();   
            messageDigest.update(str.getBytes("UTF-8"));   
        } catch (NoSuchAlgorithmException e) {   
            System.out.println("NoSuchAlgorithmException caught!");   
            return null;
        } catch (UnsupportedEncodingException e) {   
            e.printStackTrace();
            return null;
        }   
   
        byte[] byteArray = messageDigest.digest();   
        StringBuffer md5StrBuff = new StringBuffer();   
        for (int i = 0; i < byteArray.length; i++) {               
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)   
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));   
            else   
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));   
        }   
   
        return md5StrBuff.toString();   
    }  

	/**  
     * MD5 加密  
    private static String getMD5Str(Object...objects){
    	StringBuilder stringBuilder=new StringBuilder();
    	for (Object object : objects) {
			stringBuilder.append(object.toString());
		}
    	return getMD5Str(stringBuilder.toString());
    }*/ 
}