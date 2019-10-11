package com.ue.uebook.SplashActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.ue.uebook.HomeActivity.HomeScreen;
import com.ue.uebook.LoginActivity.LoginScreen;
import com.ue.uebook.Quickblox_Chat.utils.SharedPrefsHelper;
import com.ue.uebook.Quickblox_Chat.utils.chat.ChatHelper;
import com.ue.uebook.Quickblox_Chat.utils.ui.activity.DialogsActivity;
import com.ue.uebook.Quickblox_Chat.utils.ui.activity.LoginActivity;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.util.Locale;

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
        setLocale(new SessionManager(getApplicationContext()).getCurrentLanguage());
        if (SharedPrefsHelper.getInstance().hasQbUser()) {
            restoreChatSession();
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

    private void restoreChatSession() {
        if (ChatHelper.getInstance().isLogged()) {
            DialogsActivity.start(this);
            finish();
        } else {
            QBUser currentUser = getUserFromSession();
            if (currentUser == null) {
                LoginActivity.start(this);
                finish();
            } else {
                loginToChat(currentUser);
            }
        }
    }

    private void loginToChat(final QBUser user) {
//        com.quickblox.sample.chat.java.ui.dialog.ProgressDialogFragment.show(getSupportFragmentManager(), R.string.dlg_restoring_chat_session);

        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {

//                com.quickblox.sample.chat.java.ui.dialog.ProgressDialogFragment.hide(getSupportFragmentManager());

                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                if (e.getMessage().equals("You have already logged in chat")) {
                    loginToChat(user);
                } else {
//                    com.quickblox.sample.chat.java.ui.dialog.ProgressDialogFragment.hide(getSupportFragmentManager());
                    loginToChat(user);

                }
            }
        });
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