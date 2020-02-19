package com.ue.uebook.ChatSdk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;
import com.ue.uebook.TouchImageView;

public class OponentUserDetailsScren extends BaseActivity implements View.OnClickListener {
    private ImageButton backbtn;
    private TextView userName;
    private TouchImageView usetImage;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oponent_user_details_scren);
        intent = getIntent();
        backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(this);
        userName=findViewById(R.id.username);
        usetImage=findViewById(R.id.userImage);
        Integer id = intent.getIntExtra("id",0);
        if (id==1){
            GlideUtils.loadImage(OponentUserDetailsScren.this,  intent.getStringExtra("image"), usetImage, R.drawable.user_default, R.drawable.user_default);

        }
        else {
            GlideUtils.loadImage(OponentUserDetailsScren.this, ApiRequest.BaseUrl + "upload/" + intent.getStringExtra("image"), usetImage, R.drawable.user_default, R.drawable.user_default);

        }
        userName.setText(intent.getStringExtra("name"));
    }

    @Override
    public void onClick(View v) {
        if (v==backbtn){
            finish();
        }
    }
}
