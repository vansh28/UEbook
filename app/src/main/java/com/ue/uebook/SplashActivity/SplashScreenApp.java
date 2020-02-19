package com.ue.uebook.SplashActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.ChatSdk.MessageScreen;
import com.ue.uebook.Dashboard.StartActivityHome;
import com.ue.uebook.HomeActivity.HomeScreen;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.util.Locale;

public class SplashScreenApp extends AppCompatActivity {
    private Handler myHandler;
    private Runnable myRunnable;
    private Intent intent;
    private String senderId,channelID,senderName,senderimage;
    private Integer screenid;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        intent = getIntent();
        senderId=intent.getStringExtra("sendTo");
        channelID=intent.getStringExtra("channel_id");
        senderName=intent.getStringExtra("name");
        senderimage=intent.getStringExtra("imageUrl");
        screenid=intent.getIntExtra("id",0);
        if (screenid==1)
        {
            Intent intent = new Intent(this, MessageScreen.class);
            intent.putExtra("sendTo",senderId);
            intent.putExtra("channelID",channelID);
            intent.putExtra("name",senderName);
            intent.putExtra("imageUrl",senderimage);
            intent.putExtra("id",1);
            startActivity(intent);
            finish();
        }
        else {
            showSplashimage();
        }
    }
    private void showSplashimage() {
        myHandler = new Handler();
        myRunnable = new Runnable() {
            int i = 0;

            public void run() {
                if (i < 1) {
                    i++;
                    myHandler.postDelayed(this, 2000);
                } else {
                    showSplash();
                    myHandler.removeCallbacks(myRunnable);
                }
            }
        };
        myHandler.post(myRunnable);

    }
    private void showSplash() {
        final int status = new SessionManager(getApplicationContext()).getLoginStatus();
        Intent mainIntent = mainIntent = new Intent(SplashScreenApp.this, HomeScreen.class);

        switch (status) {
            case 0:
                mainIntent = new Intent(SplashScreenApp.this, StartActivityHome.class);
                break;
            case 1:
                mainIntent = new Intent(SplashScreenApp.this, HomeScreen.class);
                break;
        }
        SplashScreenApp.this.startActivity(mainIntent);
        SplashScreenApp.this.finish();
    }
          public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
         }
}