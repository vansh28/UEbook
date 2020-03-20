package com.ue.uebook.ChatSdk;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.LoginActivity.Pojo.LoginResponse;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;

public class UserProfileChatScreen extends BaseActivity implements View.OnClickListener {
    private ImageButton back_btn;
    private ImageButton editName ,editAbout;
    private TextView userName,userAbout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_chat_screen);
        back_btn = findViewById(R.id.back_btn);
        editName = findViewById(R.id.nameEdit);
        editAbout =findViewById(R.id.editAbout);
        userName = findViewById(R.id.userName);
        userAbout =findViewById(R.id.abouUser);
        editName.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        UserInfo(new SessionManager(getApplicationContext()).getUserID());
    }

    @Override
    public void onClick(View v) {
        if (v==back_btn){
            finish();
        }
        else if (v==editName){

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void UserInfo(String userID) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforgetUserInfo(userID, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                String myResponse = response.body().string();

                Gson gson = new GsonBuilder().create();
                final LoginResponse form = gson.fromJson(myResponse, LoginResponse.class);
                if (form.getError() == false) {
                  runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          userName.setText(form.getResponse().getUser_name());
                          userAbout.setText(form.getResponse().getAbout_me());

                        }
                    });
                }
            }
        });
    }



}
