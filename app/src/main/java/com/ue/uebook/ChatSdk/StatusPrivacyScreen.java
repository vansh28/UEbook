package com.ue.uebook.ChatSdk;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.R;

public class StatusPrivacyScreen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_privacy_screen);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==back_btn){
            finish();
        }
    }
}
