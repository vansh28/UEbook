package com.ue.uebook.AuthorProfileActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.R;

public class PendingRequestScreen extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView pendinglist;
    private FriendRequestAdapter friendRequestAdapter;
    private ImageButton backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_request_screen);
        pendinglist=findViewById(R.id.pendinglist);
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        pendinglist.setLayoutManager(linearLayoutManagerPopularList);
        friendRequestAdapter = new FriendRequestAdapter();
        pendinglist.setAdapter(friendRequestAdapter);
        backbtn=findViewById(R.id.back_pending);
        backbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view==backbtn){
            fileList();
        }
    }
}
