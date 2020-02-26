package com.ue.uebook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoViewScreen extends AppCompatActivity {
    private VideoView videoView;
    private String videoUrl="";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view_screen);
        videoView = findViewById(R.id.videoview);
        intent=getIntent();
        videoUrl=intent.getStringExtra("url");
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(videoUrl));
        videoView.start();
    }
}
