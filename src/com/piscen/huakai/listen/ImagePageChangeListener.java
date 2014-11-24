package com.piscen.huakai.listen;

import com.piscen.huakai.R;
import com.piscen.huakai.common.NewsXmlParser;
import com.piscen.huakai.view.SlideImageLayout;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @author wu_zhang
 * @2014-11-21上午11:03:53
 * @TODO  滑动页面更改事件监听器
 */
public class ImagePageChangeListener implements OnPageChangeListener {
	// 布局设置类
	private SlideImageLayout mSlideLayout = null;
	// 滑动标题
	private TextView mSlideTitle = null;
	
	private ImageView[] mImageCircleViews = null; 
	// 数据解析类
	private NewsXmlParser mParser = null; 
	public ImagePageChangeListener(SlideImageLayout mSlideLayout,
			TextView mSlideTitle, ImageView[] mImageCircleViews,
			NewsXmlParser mParser) {
		super();
		this.mSlideLayout = mSlideLayout;
		this.mSlideTitle = mSlideTitle;
		this.mImageCircleViews = mImageCircleViews;
		this.mParser = mParser;
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {  
//    	pageIndex = index;
    	mSlideLayout.setPageIndex(arg0);
    	mSlideTitle.setText(mParser.getSlideTitles()[arg0]);
    	
        for (int i = 0; i < mImageCircleViews.length; i++) {  
        	mImageCircleViews[arg0].setBackgroundResource(R.drawable.dot_selected);
            
            if (arg0 != i) {  
            	mImageCircleViews[i].setBackgroundResource(R.drawable.dot_none);  
            }  
        }
    }

}
