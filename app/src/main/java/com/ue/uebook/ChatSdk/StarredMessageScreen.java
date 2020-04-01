package com.ue.uebook.ChatSdk;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.ChatSdk.Adapter.StarredAdapter;
import com.ue.uebook.R;

public class StarredMessageScreen extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView starredlistMessage;
    private ImageButton backbtn;
    private StarredAdapter starredAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starred_message_screen);
        starredlistMessage = findViewById(R.id.starredlistMessage);
        backbtn = findViewById(R.id.back);
        backbtn.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        starredlistMessage.setLayoutManager(linearLayoutManager);
        starredAdapter = new StarredAdapter();
        starredlistMessage.setAdapter(starredAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v==backbtn){
            finish();
        }
    }
}
