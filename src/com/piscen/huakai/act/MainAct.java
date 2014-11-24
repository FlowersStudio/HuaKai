package com.piscen.huakai.act;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.piscen.huakai.R;
import com.piscen.huakai.adapter.Main_GridViewAdapter;
import com.piscen.huakai.adapter.SlideImageAdapter;
import com.piscen.huakai.common.GsonUtils;
import com.piscen.huakai.common.NewsXmlParser;
import com.piscen.huakai.common.RequestUrls;
import com.piscen.huakai.common.ResponseState;
import com.piscen.huakai.common.StringUtils;
import com.piscen.huakai.dto.FirstPageListReq;
import com.piscen.huakai.dto.FirstPageListResp;
import com.piscen.huakai.dto.Magazine;
import com.piscen.huakai.http.HttpHandler;
import com.piscen.huakai.http.HttpRequest;
import com.piscen.huakai.http.JsonUtil;
import com.piscen.huakai.listen.ImagePageChangeListener;
import com.piscen.huakai.view.MyGridView;
import com.piscen.huakai.view.SlideImageLayout;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
	private List<Magazine> list = new ArrayList<Magazine>();
	private Main_GridViewAdapter adapter;
	//显示sliding
	private  LinearLayout showLeft,showRight;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.page_topic_news, null);
		// 初始化
		initeViews(view); 
		query();
		return view;
	}

	/**
	 * 得到显示数据  暂时只是固定数据
	 */
	//	private List<Magazine> getMagazineCoverData() {
	//			for (int i = 0; i < 6; i++) {
	//				Magazine m = new Magazine();
	//				m.setCover(BitmapFactory.decodeResource(getResources(), bitmap[i]));
	//				m.setTitle(titles[i]);
	//				list.add(m);
	//			}
	//			return list;

	//	}
	/**
	 * 查询服务器数据
	 * 
	 */
	private void query(){
		//访问实体
		FirstPageListReq request = new FirstPageListReq();
		//设置要传的参数
		request.setRequestorname(StringUtils.execute(request));
		Gson gson = GsonUtils.getGson();
		//转json
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("json",  gson.toJson(request)));
		//访问   
		new HttpRequest(FdqueryHandler).post(RequestUrls.BATH_URL, list,null);			
	}
	//访问返回的数据
	private Handler FdqueryHandler = new HttpHandler(getActivity()){
		protected void succeed(String response) {
			initState(response);
		};
		protected void failed(String response) { 
			
		};
	};
	private void initState(String response){
		FirstPageListResp resp = JsonUtil.json2Entity(response, "FirstPageListResp");
		int code = Integer.valueOf(resp.getCode());
		if( code == ResponseState.SUCCESSED){
			list = resp.getList();
			if(list != null){

				main_gridview.setOnItemClickListener(listener);
				//加载数据
				adapter = new Main_GridViewAdapter(list, getActivity(), getActivity());
				main_gridview.setAdapter(adapter);
			} 
		}
		if(code == ResponseState.SYSERROR){
			((BaseActivity) getActivity()).showToast("系统错误");
		}
		if(code == ResponseState.DATANULL){
			((BaseActivity) getActivity()).showToast("数据为空");
		}
		if(code == ResponseState.PARAMETERERROR){
			((BaseActivity) getActivity()).showToast("参数错误");
		}
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

		main_gridview = (MyGridView) view.findViewById(R.id.main_gridview);

		// 设置ViewPager
		mViewPager.setAdapter(new SlideImageAdapter(mImagePageViewList));  
		mViewPager.setOnPageChangeListener(new ImagePageChangeListener(mSlideLayout, mSlideTitle, mImageCircleViews, mParser));

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
