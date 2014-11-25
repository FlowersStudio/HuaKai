package com.piscen.huakai.view;

import java.util.ArrayList;
import java.util.List;

import com.piscen.huakai.R;
import com.piscen.huakai.common.ImageDownloader;
import com.piscen.huakai.common.OnImageDownload;
import com.piscen.huakai.common.RequestUrls;
import com.piscen.huakai.dto.TopNews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
/**
 * 
 * @author wu_zhang
 * @2014-11-17下午5:25:18
 * @TODO
 */
public class SlideImageLayout {
	// 包含图片的ArrayList
	private ArrayList<ImageView> mImageList = null;
	private Context mContext = null;
	// 圆点图片集合
	private ImageView[] mImageViews = null; 
	private ImageView mImageView = null;
	// 表示当前滑动图片的索引
	private int pageIndex = 0;
	List<TopNews> list;
	public SlideImageLayout(Context context,List<TopNews> List) {
		this.mContext = context;
		mImageList = new ArrayList<ImageView>();
		this.list = List;
	}
	
	/**
	 * 生成滑动图片区域布局
	 * @param id
	 * @return
	 */
	public View getSlideImageLayout(String  url,Activity activity){
		// 包含TextView的LinearLayout
		LinearLayout imageLinerLayout = new LinearLayout(mContext);
		LinearLayout.LayoutParams imageLinerLayoutParames = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT,
				1);
		
		ImageView iv = new ImageView(mContext);
//		iv.setBackgroundResource(id);
		setImageAsync(iv, url,activity);
		iv.setOnClickListener(new ImageOnClickListener());
		imageLinerLayout.addView(iv,imageLinerLayoutParames);
		mImageList.add(iv);
		
		return imageLinerLayout;
	}
	ImageDownloader mDownloader;
	//异步加载顶部滑动图片
	private void setImageAsync(ImageView v,final String url,Activity activity){
		v.setTag(url);
		if (mDownloader == null) {  
			mDownloader = new ImageDownloader();  
		}  
		//这句代码的作用是为了解决convertView被重用的时候，图片预设的问题  
		v.setImageResource(R.drawable.ic_launch);  
		if (mDownloader != null) {  
			//异步下载图片  
			mDownloader.imageDownload(url, v, "/yanbin",activity, new OnImageDownload() {  
				@Override  
				public void onDownloadSucc(Bitmap bitmap,  
						String c_url,ImageView mimageView) {  
					// 通过 tag 来防止图片错位
					if (mimageView.getTag() != null
							&& mimageView.getTag().equals(url)) {
						mimageView.setImageBitmap(bitmap);
						mimageView.setTag("");  
					}
				}  
			});  
		}
	}
	/**
	 * 获取LinearLayout
	 * @param view
	 * @param width
	 * @param height
	 * @return
	 */
	public View getLinearLayout(View view,int width,int height){
		LinearLayout linerLayout = new LinearLayout(mContext);
		LinearLayout.LayoutParams linerLayoutParames = new LinearLayout.LayoutParams(
				width, 
				height,
				1);
		// 这里最好也自定义设置，有兴趣的自己设置。
		linerLayout.setPadding(10, 0, 10, 0);
		linerLayout.addView(view, linerLayoutParames);
		
		return linerLayout;
	}
	
	/**
	 * 设置圆点个数
	 * @param size
	 */
	public void setCircleImageLayout(int size){
		mImageViews = new ImageView[size];
	}
	
	/**
	 * 生成圆点图片区域布局对象
	 * @param index
	 * @return
	 */
	public ImageView getCircleImageLayout(int index){
		mImageView = new ImageView(mContext);  
		mImageView.setLayoutParams(new LayoutParams(10,10));
        mImageView.setScaleType(ScaleType.FIT_XY);
        
        mImageViews[index] = mImageView;
         
        if (index == 0) {  
            //默认选中第一张图片
            mImageViews[index].setBackgroundResource(R.drawable.dot_selected);  
        } else {  
            mImageViews[index].setBackgroundResource(R.drawable.dot_none);  
        }  
         
        return mImageViews[index];
	}
	
	/**
	 * 设置当前滑动图片的索引
	 * @param index
	 */
	public void setPageIndex(int index){
		pageIndex = index;
	}
	
	// 滑动页面点击事件监听器
    private class ImageOnClickListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		Toast.makeText(mContext, list.get(pageIndex).getSlideTitles(), Toast.LENGTH_SHORT).show();
    		Toast.makeText(mContext, list.get(pageIndex).getSlideUrls(), Toast.LENGTH_SHORT).show();
    	}
    }
}
