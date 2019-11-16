package com.ue.uebook.ChatSdk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.R;

public class VideoScreen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton backbtn;
    private VideoView videoView;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_screen);
        intent =getIntent();
        String url = intent.getStringExtra("url");
        videoView=findViewById(R.id.videoView);
        backbtn=findViewById(R.id.back_video);
        backbtn.setOnClickListener(this);
        MediaController mediaController = new MediaController(VideoScreen.this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.setVideoPath(url);
        videoView.requestFocus();
        videoView.start();

    }

    @Override
    public void onClick(View v) {
        if (v==backbtn){
            finish();
        }
    }
}
