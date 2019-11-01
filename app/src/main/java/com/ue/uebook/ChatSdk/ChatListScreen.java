package com.ue.uebook.ChatSdk;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ue.uebook.ChatSdk.Adapter.ChatListAdapter;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;

public class ChatListScreen extends AppCompatActivity implements View.OnClickListener ,ChatListAdapter.ItemClick {
    private RecyclerView chatList;
    private ChatListAdapter chatAdapter;
    private ImageButton backbtn;
    private FloatingActionButton newChatbtn;
    private TextView noUserFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        chatList=findViewById(R.id.chatList);
        backbtn=findViewById(R.id.backbtnChat);
        newChatbtn=findViewById(R.id.newChatbtn);
        newChatbtn.setOnClickListener(this);
        noUserFound=findViewById(R.id.noUserFound);
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(linearLayoutManagerPopularList);
        chatAdapter = new ChatListAdapter();
        chatList.setAdapter(chatAdapter);
        chatAdapter.setItemClickListener(this);
        backbtn.setOnClickListener(this);
        chatList.setNestedScrollingEnabled(false);
    }
    @Override
    public void onClick(View v) {
        if (v==backbtn){
            finish();
        }
        else if (v==newChatbtn){
            Intent intent = new Intent(this,FriendListScreen.class);
            startActivity(intent);
        }
    }

    @Override
    public void onUserChatClick() {
             Intent intent = new Intent(this,MessageScreen.class);
              startActivity(intent);

       }

    @Override
    public void onUserProfileClick() {
        imagePreview("jdj");
    }
    private void imagePreview(String file) {
        final Dialog previewDialog = new Dialog(this);
        previewDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        previewDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout
                , null));
        ImageView imageView = previewDialog.findViewById(R.id.image_view);
        GlideUtils.loadImage(ChatListScreen.this, "http://dnddemo.com/ebooks/api/v1/upload/" + file, imageView, R.drawable.user_default, R.drawable.user_default);
        Button ok_Btn = previewDialog.findViewById(R.id.buton_ok);
        ok_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewDialog.dismiss();
            }
        });
        previewDialog.show();
    }
}
