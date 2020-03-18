package com.ue.uebook.ChatSdk.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ue.uebook.ChatSdk.Pojo.StatusViewDetail;
import com.ue.uebook.R;

import java.util.List;

public class ViewPagerStatus  extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<StatusViewDetail>statusViewDetailList;
    private TextView textView ;

    public ViewPagerStatus(Context applicationContext, List<StatusViewDetail> statusViewDetailList) {
        this.context=applicationContext;
        this.statusViewDetailList=statusViewDetailList;

    }

    @Override
    public int getCount() {

        return statusViewDetailList.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.detailsimageview, null);
        textView = view.findViewById(R.id.textView);
        textView.setText(statusViewDetailList.get(position).getMessage());
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);

        return view;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }




}
