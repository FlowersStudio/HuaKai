package com.piscen.huakai.adapter;

import java.util.ArrayList;
import java.util.List;

import com.piscen.huakai.act.Magzine_page;
import com.piscen.huakai.dto.MagzinePage;

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
	
	private List<MagzinePage> list;
	public MagzineScanAdapter(FragmentManager fm,List<MagzinePage> list) {
		super(fm);
		this.list = list;
		 notifyDataSetChanged();
	}

//	public MagzineScanAdapter(FragmentManager fm) {
//        super(fm);
//		
        /**  惯例我在adapter初始化的时候造的假数据  **/
//        for (int i = 0; i < 38; i++) {
////			URLS[i] = root + (i+1)+".jpg";
//			URLS[i] = "http://metro2.sinaapp.com/images/img590x390.jpg";
//		}	
//		
//        for (int i = 0; i < 99; i++) {
//        	all_info.add(String.valueOf(i));
//		}
//		
//        notifyDataSetChanged();
//    }

    @Override
    public int getCount() {
    	return list.size();
    }
    
    @Override  
	public int getItemPosition(Object object) {  
	    return POSITION_NONE;  
	}  
    
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    	Magzine_page ff = (Magzine_page) super.instantiateItem(container, position);
        ff.setShowInfo(""+list.get(position).getPagination(), list.get(position).getPageImage());
        return ff;
    }

	@Override
    public Fragment getItem(int position) {
		return new Magzine_page();
		 
    }
	
}