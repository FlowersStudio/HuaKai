package com.piscen.huakai.act;

import java.util.ArrayList;
import java.util.List;

import com.piscen.huakai.R;
import com.piscen.huakai.adapter.Main_GridViewAdapter;
import com.piscen.huakai.adapter.SlideImageAdapter;
import com.piscen.huakai.common.NewsXmlParser;
import com.piscen.huakai.dto.MagazineCover;
import com.piscen.huakai.listen.ImagePageChangeListener;
import com.piscen.huakai.view.MyGridView;
import com.piscen.huakai.view.SlideImageLayout;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author wu_zhang
 * @2014-11-17下午5:24:31
 * @TODO  
 */ 
public class MainAct extends Fragment{
	// 滑动图片的集合
	private ArrayList<View> mImagePageViewList = null;
	private ViewPager mViewPager = null;
	// 包含圆点图片的View
	private ViewGroup mImageCircleView = null;
	private ImageView[] mImageCircleViews = null; 
	// 滑动标题
	private TextView mSlideTitle = null;
	
	// 布局设置类
	private SlideImageLayout mSlideLayout = null;
	// 数据解析类
	private NewsXmlParser mParser = null; 
	private MyGridView main_gridview;
	//预览数据
	private int [] bitmap = { R.drawable.first, R.drawable.two, R.drawable.three, R.drawable.four,R.drawable.ic_launch,R.drawable.six};
	private String [] titles = {"第一期","第二期","第三期","第四期","第五期","第六期"};
	private List<MagazineCover> list = new ArrayList<MagazineCover>();
	private Main_GridViewAdapter adapter;
	//显示sliding
	private  LinearLayout showLeft,showRight;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.page_topic_news, null);
		getMagazineCoverData();
		// 初始化
		initeViews(view); 
		return view;
	}

	/**
	 * 得到显示数据  暂时只是固定数据
	 */
	private List<MagazineCover> getMagazineCoverData() {
			for (int i = 0; i < 6; i++) {
				MagazineCover m = new MagazineCover();
				m.setCover(BitmapFactory.decodeResource(getResources(), bitmap[i]));
				m.setTitle(titles[i]);
				list.add(m);
			}
			return list;
	}

	/**
	 * 初始化
	 */
	private void initeViews(View view){
		// 滑动图片区域
		mImagePageViewList = new ArrayList<View>();
		mViewPager = (ViewPager)view.findViewById(R.id.image_slide_page);  
		
		// 圆点图片区域
		mParser = new NewsXmlParser();
		int length = mParser.getSlideImages().length;
		mImageCircleViews = new ImageView[length];
		mImageCircleView = (ViewGroup)view.findViewById(R.id.layout_circle_images);
		mSlideLayout = new SlideImageLayout(getActivity().getApplicationContext());
		mSlideLayout.setCircleImageLayout(length);
		
		for(int i = 0; i < length; i++){
			mImagePageViewList.add(mSlideLayout.getSlideImageLayout(mParser.getSlideImages()[i]));
			mImageCircleViews[i] = mSlideLayout.getCircleImageLayout(i);
			mImageCircleView.addView(mSlideLayout.getLinearLayout(mImageCircleViews[i], 10, 10));
		}
		
		// 设置默认的滑动标题
		mSlideTitle = (TextView)view.findViewById(R.id.tvSlideTitle);
		mSlideTitle.setText(mParser.getSlideTitles()[0]);
		
		
		// 设置ViewPager
        mViewPager.setAdapter(new SlideImageAdapter(mImagePageViewList));  
        mViewPager.setOnPageChangeListener(new ImagePageChangeListener(mSlideLayout, mSlideTitle, mImageCircleViews, mParser));
        
        main_gridview = (MyGridView) view.findViewById(R.id.main_gridview);
        main_gridview.setOnItemClickListener(listener);
        //加载数据
        adapter = new Main_GridViewAdapter(list, getActivity(), getActivity());
        main_gridview.setAdapter(adapter);
        
        showLeft = (LinearLayout) view.findViewById(R.id.showLeft);
        showRight = (LinearLayout) view.findViewById(R.id.showRight);
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		showLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showLeft();
			}
		});

		showRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showRight();
			}
		});
	}
	//点击item跳转
	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(getActivity(),MagzineAct.class);
			startActivity(intent);
		}
	};
	
}
