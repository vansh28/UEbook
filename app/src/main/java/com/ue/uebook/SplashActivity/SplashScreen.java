package com.ue.uebook.SplashActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.ue.uebook.LoginActivity.LoginScreen;
import com.ue.uebook.R;

public class SplashScreen extends AppCompatActivity {
    private Handler myHandler;
    private Runnable myRunnable;
    private ImageView splashImageView;
    // private int[] splashimages = {R.drawable.splash};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        showSplashimage();
    }

    private void showSplashimage() {
        myHandler = new Handler();
        myRunnable = new Runnable() {
            int i = 0;
            public void run() {
                if (i <1) {
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
//
//        final int status = Prefrence.getInstance(getApplicationContext()).getLoginStatus();
//
//        Intent mainIntent = mainIntent = new Intent(Splash.this, LoginActivity.class);
//
//        switch (status) {
//            case 0:
//                mainIntent = new Intent(Splash.this, LoginActivity.class);
//                break;
//            case 1:
//                mainIntent = new Intent(Splash.this, LoginActivity.class);
//                break;
//// case 3:
//// mainIntent = new Intent(SplashScreen.this, UserHealthRecord.class);
//// break;
//// case 4:
//// if (RenewPreferece.getInstance(getAppl icationContext()).getDeviceConnected())
//// mainIntent = new Intent(SplashScreen.this, Home.class);
//// else
//// mainIntent = new Intent(SplashScreen.this, Home.class);
//// break;
//        }
//        Splash.this.startActivity(mainIntent);
//        Splash.this.finish();

        Intent intent = new Intent(SplashScreen.this , LoginScreen.class);
        startActivity(intent);
        finish();


    }

}
