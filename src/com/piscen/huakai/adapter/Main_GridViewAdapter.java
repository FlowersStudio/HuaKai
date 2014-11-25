package com.piscen.huakai.adapter;

import java.util.List;

import com.piscen.huakai.R;
import com.piscen.huakai.common.ImageDownloader;
import com.piscen.huakai.common.ImageLoadUtil;
import com.piscen.huakai.common.OnImageDownload;
import com.piscen.huakai.common.RequestUrls;
import com.piscen.huakai.dto.Magazine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @author wu_zhang
 * @2014-11-18下午6:54:30
 * @TODO 预览
 */

public class Main_GridViewAdapter extends BaseAdapter{
	private List<Magazine> list;
	private LayoutInflater inflater;
	private Magazine info;
	private Context context;
	private Activity activity;
	private ViewHolder viewholder;
	private ImageDownloader mDownloader;
	public Main_GridViewAdapter(List<Magazine> list, Context context,
			Activity activity) {
		super();
		this.list = list;
		this.context = context;
		this.activity = activity;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			viewholder = new ViewHolder();
			convertView = inflater.inflate(R.layout.acvitity_main_grid, null);
			viewholder.main_grid_image = (ImageView) convertView.findViewById(R.id.main_grid_image);
			viewholder.main_grid_text = (TextView) convertView.findViewById(R.id.main_grid_text);

			convertView.setTag(viewholder);
		}else{
			viewholder = (ViewHolder) convertView.getTag();
		}
		info = list.get(position);
		//		viewholder.main_grid_image.setImageBitmap(info.getCover());
		viewholder.main_grid_text.setText(info.getPeriods());
		viewholder.main_grid_image.setTag(RequestUrls.IMAGE_URL+"/"+info.getCover());
		if (mDownloader == null) {  
			mDownloader = new ImageDownloader();  
		}  
		//这句代码的作用是为了解决convertView被重用的时候，图片预设的问题  
		viewholder.main_grid_image.setImageResource(R.drawable.ic_launch);  
		if (mDownloader != null) {  
			//异步下载图片  
			mDownloader.imageDownload(RequestUrls.IMAGE_URL+"/"+info.getCover(), viewholder.main_grid_image, "/yanbin",activity, new OnImageDownload() {  
				@Override  
				public void onDownloadSucc(Bitmap bitmap,  
						String c_url,ImageView mimageView) {  
					// 通过 tag 来防止图片错位
					if (mimageView.getTag() != null
							&& mimageView.getTag().equals(RequestUrls.IMAGE_URL+"/"+info.getCover())) {
						mimageView.setImageBitmap(bitmap);
						mimageView.setTag("");  
					}
				}  
			});  
		}
		return convertView;
	}

	class ViewHolder{
		TextView main_grid_text;
		ImageView main_grid_image;
	}
}
