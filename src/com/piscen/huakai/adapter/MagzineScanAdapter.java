package com.piscen.huakai.adapter;

import java.util.ArrayList;
import java.util.List;

import com.piscen.huakai.act.Magzine_page;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * 
 * @author wu_zhang
 * @2014-11-18下午6:51:40
 * @TODO   浏览杂志，
 */
public class MagzineScanAdapter extends FragmentPagerAdapter {
	
	List<String> all_info = new ArrayList<String>();
	private String  root ="http://172.16.3.17/22/";
	private String[] URLS = new String[99];;


	public MagzineScanAdapter(FragmentManager fm) {
        super(fm);
		
        /**  惯例我在adapter初始化的时候造的假数据  **/
        for (int i = 0; i < 38; i++) {
//			URLS[i] = root + (i+1)+".jpg";
			URLS[i] = "http://metro2.sinaapp.com/images/img590x390.jpg";
		}	
		
        for (int i = 0; i < 99; i++) {
        	all_info.add(String.valueOf(i));
		}
		
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
    	return all_info.size();
    }
    
    @Override  
	public int getItemPosition(Object object) {  
	    return POSITION_NONE;  
	}  
    
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    	Magzine_page ff = (Magzine_page) super.instantiateItem(container, position);
        ff.setShowInfo(all_info.get(position), URLS[position]);
        return ff;
    }

	@Override
    public Fragment getItem(int position) {
		return new Magzine_page();
		 
    }
	
}