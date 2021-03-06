package com.ue.uebook.ChatSdk.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
    private ProgressBar progressBar;
    private MediaController mediacontroller;

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
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        textView = view.findViewById(R.id.textView);
        rootview = view.findViewById(R.id.rootview);
        statusImage = view.findViewById(R.id.statusImage);
        caption = view.findViewById(R.id.caption);
        imageViewLayout = view.findViewById(R.id.imageViewLayout);
        videocaption = view.findViewById(R.id.videocaption);
        videoLayout = view.findViewById(R.id.videoViewLayout);
        videoView = view.findViewById(R.id.videoView);
        mediacontroller = new MediaController(context);
        mediacontroller.setAnchorView(videoView);
        videoView.stopPlayback();
        if (statusViewDetailList.get(position).getMessage_type().equalsIgnoreCase("text")) {
            progressBar.setVisibility(View.GONE);
            videoView.stopPlayback();
            textView.setVisibility(View.VISIBLE);
            imageViewLayout.setVisibility(View.GONE);
            videoLayout.setVisibility(View.GONE);
            textView.setText(statusViewDetailList.get(position).getMessage());
            rootview.setBackgroundColor(Color.parseColor(statusViewDetailList.get(position).getBg_color()));
            setFont(statusViewDetailList.get(position).getFont_style());
        } else if (statusViewDetailList.get(position).getMessage_type().equalsIgnoreCase("image")) {
            textView.setVisibility(View.GONE);
            videoView.stopPlayback();
            imageViewLayout.setVisibility(View.VISIBLE);
            videoLayout.setVisibility(View.GONE);
            caption.setText(statusViewDetailList.get(position).getCaption());
            Glide.with(context)
                    .load("http://dnddemo.com/ebooks/" + statusViewDetailList.get(position).getMessage())

                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(statusImage);
        }


//            Picasso.get()
//                    .load("http://dnddemo.com/ebooks/"+ statusViewDetailList.get(position).getMessage())
//                    .placeholder(R.drawable.noimage)
//                    .error(R.drawable.noimage)
//                    .fit().centerInside()
//                    .into(statusImage);

        //  GlideUtils.loadImage( context, "http://dnddemo.com/ebooks/"+ statusViewDetailList.get(position).getMessage(), statusImage, R.drawable.noimage, R.drawable.noimage);


        else if (statusViewDetailList.get(position).getMessage_type().equalsIgnoreCase("video"))
        {
            videoView.setMediaController(mediacontroller);
            progressBar.setVisibility(View.GONE);
            imageViewLayout.setVisibility(View.GONE);
             textView.setVisibility(View.GONE);
             videoLayout.setVisibility(View.GONE);
             videocaption.setText(statusViewDetailList.get(position).getCaption());
            videoView.setZOrderOnTop(true);
            videoView.setVideoURI(Uri.parse("http://dnddemo.com/ebooks/"+ statusViewDetailList.get(position).getMessage()));
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
//                            if (statusViewDetailList.get(position).getMessage_type().equalsIgnoreCase("image")||statusViewDetailList.get(position).getMessage_type().equalsIgnoreCase("text")) {
//                                if (vp.getCurrentItem() < vp.getAdapter().getCount())
//
//                                    Log.e("pos", String.valueOf(vp.getCurrentItem()));
//
//                                vp.setCurrentItem(vp.getCurrentItem() + 1);
//
//                            }
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
