package com.piscen.huakai.listen;

import java.util.List;

import com.piscen.huakai.R;
import com.piscen.huakai.dto.TopNews;
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
	List<TopNews> list;
	private ImageView[] mImageCircleViews = null; 
	public ImagePageChangeListener(SlideImageLayout mSlideLayout,
			TextView mSlideTitle, ImageView[] mImageCircleViews,
			List<TopNews> list) {
		super();
		this.mSlideLayout = mSlideLayout;
		this.mSlideTitle = mSlideTitle;
		this.mImageCircleViews = mImageCircleViews;
		this.list = list;
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {  
//    	pageIndex = index;
    	mSlideLayout.setPageIndex(arg0);
    	mSlideTitle.setText(list.get(arg0).getSlideTitles());
    	
        for (int i = 0; i < mImageCircleViews.length; i++) {  
        	mImageCircleViews[arg0].setBackgroundResource(R.drawable.dot_selected);
            
            if (arg0 != i) {  
            	mImageCircleViews[i].setBackgroundResource(R.drawable.dot_none);  
            }  
        }
    }

}
