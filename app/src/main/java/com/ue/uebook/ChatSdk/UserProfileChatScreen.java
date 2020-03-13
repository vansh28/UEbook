package com.ue.uebook.ChatSdk;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.R;

public class UserProfileChatScreen extends AppCompatActivity implements View.OnClickListener {
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
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==back_btn){
            finish();
        }
    }
}
