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
import android.support.v4.util.LruCache;

/**
 * @author Administrator
 * @desc �첽����ͼƬ������
 */
public class LoaderImpl {

	 //�Ƿ񻺴�ͼƬ�������ļ�
	 private boolean diskcache = true;
	 //����Ŀ¼,Ĭ����/data/data/package/cache/Ŀ¼
 	 private String cachedDir;
	 
 	 // ����������� ����  ������
	 private static ConcurrentHashMap<String, SoftReference<Bitmap>> current_hashmap = 
			                            new ConcurrentHashMap<String, SoftReference<Bitmap>>();
	 // LruCache ��ǿ���ý�  ͼƬ����     LinkedHashMap 
	 final static int memClass = (int) Runtime.getRuntime().maxMemory(); 
	
	 private static LruCache<String, Bitmap>  imageCache = new LruCache<String, Bitmap>(memClass/5) {
	  protected int sizeOf(String key, Bitmap value) {  
          if(value != null) {  
              // ����洢bitmap��ռ�õ��ֽ���  
              return value.getRowBytes() * value.getHeight();  
          } else {  
              return 0;  
          }  
      }  
        
      @Override  
      protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {  
          if(oldValue != null) {  
              // ��Ӳ���û�����������ʱ����ʹ��LRU�㷨�����û�б�ʹ�õ�ͼƬת�������û���  
        	  current_hashmap.put(key, new SoftReference<Bitmap>(oldValue));  
        	  System.out.println("ͼƬ��� 2������ ");
          }  
      }  
	};
	
	
	/**
	 * �Ƿ񻺴�ͼƬ���ⲿ�ļ�
	 * @param flag 
	 */
	public void setCache2File(boolean diskcache){
		diskcache = diskcache;
	}
	
	/**
	 * ���û���ͼƬ���ⲿ�ļ���·��
	 * @param cacheDir
	 */
	public void setCachedDir(String cacheDir){
		this.cachedDir = cacheDir;
	}
	
	/**
	 * �����������ͼƬ
	 */
	public Bitmap getBitmapFromUrl(String url, boolean cache2Memory){
		Bitmap bitmap = null;
		try{
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection)u.openConnection();  
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			System.out.println("��������=="+ url);
			
			if(cache2Memory){
				//1.����bitmap���ڴ���������
				imageCache.put(url, bitmap);
				if(diskcache){
					System.out.println("���浽����==="+ url);
					//2.����bitmap��/data/data/packageName/cache/�ļ�����
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
	 * ���ڴ滺���л�ȡbitmap
	 * @param url
	 * @return bitmap or null.
	 */
	public Bitmap getBitmapFromMemory(String url){
		 Bitmap bitmap = null;
		 synchronized (imageCache) {
				bitmap = imageCache.get(url);
				if (null != bitmap) {
					imageCache.remove(url);
					// ���� LRU��Least Recently Used ��������ʹ���㷨 �ڴ��㷨 �ͽ� �� ԭ�� �ŵ���λ
					imageCache.put(url, bitmap);
					System.out.println(" �ڻ���1����ͼƬ�� =" + url);
					return bitmap;
				}
			}
		 
		 
	    // 2. ������ ������
		SoftReference<Bitmap> soft = current_hashmap.get(url);
		if (soft != null) {
			//�õ� ������ �е�ͼƬ
			Bitmap soft_bitmap = soft.get();      
			if (null != soft_bitmap) {
				System.out.println(" �ڻ���2����ͼƬ�� =" + url);
				return soft_bitmap;
			}
		} else {
			// û��ͼƬ�Ļ� �����keyɾ��
			current_hashmap.remove(url);      
		}
		
		
		//���ⲿ�����ļ���ȡ
		if(diskcache){
			bitmap = getBitmapFromFile(url);
			if(bitmap != null)
				imageCache.put(url, bitmap);
		}
		
		return bitmap;
	}
	
	/**
	 * ���ⲿ�ļ������л�ȡbitmap
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
			e.printStackTrace();
			bitmap = null;
		}
		return bitmap;
	}
	
	
	/**  
     * MD5 ����  
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
     * MD5 ����  
    private static String getMD5Str(Object...objects){
    	StringBuilder stringBuilder=new StringBuilder();
    	for (Object object : objects) {
			stringBuilder.append(object.toString());
		}
    	return getMD5Str(stringBuilder.toString());
    }*/ 
}