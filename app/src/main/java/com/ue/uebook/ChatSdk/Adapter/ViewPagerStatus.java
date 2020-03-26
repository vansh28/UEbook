package com.ue.uebook.ChatSdk.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;
import com.ue.uebook.ChatSdk.Pojo.StatusViewDetail;
import com.ue.uebook.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class ViewPagerStatus  extends PagerAdapter {

    private AppCompatActivity context;
    private LayoutInflater layoutInflater;
    private List<StatusViewDetail>statusViewDetailList;
    private EmojiconTextView textView ;
    private Handler myHandler;
    private Runnable myRunnable;
    private RelativeLayout rootview,imageViewLayout ,videoLayout;
    private ImageView statusImage;
    private TextView caption ,videocaption;
    private VideoView videoView;
    Timer timer;
    TimerTask timerTask;
    public ViewPagerStatus(AppCompatActivity applicationContext, List<StatusViewDetail> statusViewDetailList) {
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
        statusImage = view.findViewById(R.id.statusImage);
        caption = view.findViewById(R.id.caption);
        imageViewLayout=view.findViewById(R.id.imageViewLayout);
        videocaption = view.findViewById(R.id.videocaption);
        videoLayout = view.findViewById(R.id.videoViewLayout);
        videoView = view.findViewById(R.id.videoView);
        if (statusViewDetailList.get(position).getMessage_type().equalsIgnoreCase("text")){
            textView.setVisibility(View.VISIBLE);
            imageViewLayout.setVisibility(View.GONE);
            videoLayout.setVisibility(View.GONE);
            textView.setText(statusViewDetailList.get(position).getMessage());
            rootview.setBackgroundColor(Color.parseColor(statusViewDetailList.get(position).getBg_color()));
            setFont(statusViewDetailList.get(position).getFont_style());
        }
        else if (statusViewDetailList.get(position).getMessage_type().equalsIgnoreCase("image")){
            textView.setVisibility(View.GONE);
            imageViewLayout.setVisibility(View.VISIBLE);
            videoLayout.setVisibility(View.GONE);
            Picasso.get()
                    .load("http://dnddemo.com/ebooks/"+ statusViewDetailList.get(position).getMessage())
                    .placeholder(R.drawable.noimage)
                    .error(R.drawable.noimage)
                    .fit().centerInside()
                    .into(statusImage);

            caption.setText(statusViewDetailList.get(position).getCaption());
          //  GlideUtils.loadImage( context, "http://dnddemo.com/ebooks/"+ statusViewDetailList.get(position).getMessage(), statusImage, R.drawable.noimage, R.drawable.noimage);

        }

        else if (statusViewDetailList.get(position).getMessage_type().equalsIgnoreCase("video"))
        {

            imageViewLayout.setVisibility(View.GONE);
             textView.setVisibility(View.GONE);
             videoLayout.setVisibility(View.GONE);
             videocaption.setText(statusViewDetailList.get(position).getCaption());
             videoView.setVideoURI(Uri.parse("http://dnddemo.com/ebooks/"+ statusViewDetailList.get(position).getMessage()));
             MediaController mc = new MediaController(context);
              videoView.setMediaController(mc);
              videoView.requestFocus();
              videoView.start();
        }

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
