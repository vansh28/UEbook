package com.ue.uebook.SplashActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.users.model.QBUser;
import com.ue.uebook.HomeActivity.HomeScreen;
import com.ue.uebook.LoginActivity.LoginScreen;
import com.ue.uebook.Quickblox_Chat.utils.SharedPrefsHelper;
import com.ue.uebook.Quickblox_Chat.utils.chat.ChatHelper;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

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
        final int status = new SessionManager(getApplicationContext()).getLoginStatus();

        Intent mainIntent = mainIntent = new Intent(SplashScreen.this, LoginScreen.class);

        switch (status) {
            case 0:
                mainIntent = new Intent(SplashScreen.this, LoginScreen.class);
                break;
            case 1:
                mainIntent = new Intent(SplashScreen.this, HomeScreen.class);
                break;
        }
        SplashScreen.this.startActivity(mainIntent);
        SplashScreen.this.finish();
    }
    private QBUser getUserFromSession() {
        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        QBSessionManager qbSessionManager = QBSessionManager.getInstance();
        if (qbSessionManager.getSessionParameters() == null) {
            ChatHelper.getInstance().destroy();
            return null;
        }
        Integer userId = qbSessionManager.getSessionParameters().getUserId();
        user.setId(userId);
        return user;
    }
}
