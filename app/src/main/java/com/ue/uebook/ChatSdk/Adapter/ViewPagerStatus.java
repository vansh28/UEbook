package com.ue.uebook.ChatSdk.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ue.uebook.ChatSdk.Pojo.StatusViewDetail;
import com.ue.uebook.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class ViewPagerStatus  extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<StatusViewDetail>statusViewDetailList;
    private EmojiconTextView textView ;
    private Handler myHandler;
    private Runnable myRunnable;
    private LinearLayout rootview;
    Timer timer;
    TimerTask timerTask;
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
        rootview = view.findViewById(R.id.rootview);
        textView.setText(statusViewDetailList.get(position).getMessage());
        if (statusViewDetailList.get(position).getBg_color()!=null){
            rootview.setBackgroundColor(Color.parseColor(statusViewDetailList.get(position).getBg_color()));
        }
        else {

        }
        setFont(statusViewDetailList.get(position).getFont_style());

        ViewPager vp = (ViewPager) container;

//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                vp.post(new Runnable(){
//
//                    @Override
//                    public void run() {
//                        if (vp.getCurrentItem()+1==statusViewDetailList.size()){
//
//                        }
//                        else {
//                            if (vp.getCurrentItem() < vp.getAdapter().getCount())
//
//                                Log.e("pos",String.valueOf(vp.getCurrentItem()));
//
//                                vp.setCurrentItem(vp.getCurrentItem() + 1);
//
//                        }
//
//                    }
//                });
//            }
//        };
//        timer = new Timer();
//        timer.schedule(timerTask, 5000, 5000);



        vp.addView(view, 0);
        return view;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }

    private void setFont(String fontNmae){
        switch(fontNmae) {
            case "BOLD":
                textView.setTypeface(null, Typeface.BOLD);
                break;
            case "ITALIC":
                textView.setTypeface(null, Typeface.ITALIC);
                break;
            case "BOLD_ITALIC":
                textView.setTypeface(null, Typeface.BOLD_ITALIC);
                break;
            case "NORMAL":
                textView.setTypeface(null, Typeface.NORMAL);
                break;

        }    }




}
