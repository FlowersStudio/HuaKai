package com.piscen.huakai.adapter;

import java.util.List;

import com.piscen.huakai.R;
import com.piscen.huakai.dto.MagazineCover;

import android.app.Activity;
import android.content.Context;
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
	private List<MagazineCover> list;
	private LayoutInflater inflater;
	private MagazineCover info;
	private Context context;
	private Activity activity;
	private ViewHolder viewholder;
	public Main_GridViewAdapter(List<MagazineCover> list, Context context,
			Activity activity) {
		super();
		this.list = list;
		this.context = context;
		this.activity = activity;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
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
		viewholder.main_grid_image.setImageBitmap(info.getCover());
		viewholder.main_grid_text.setText(info.getTitle());
		
		return convertView;
	}
	
	class ViewHolder{
		TextView main_grid_text;
		ImageView main_grid_image;
	}
	
}
