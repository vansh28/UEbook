package com.ue.uebook.ChatSdk;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.ChatSdk.Adapter.ChatListAdapter;
import com.ue.uebook.ChatSdk.Pojo.AllchatResponse;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;

public class ChatListScreen extends BaseActivity implements View.OnClickListener ,ChatListAdapter.ItemClick {
    private RecyclerView chatList;
    private ChatListAdapter chatAdapter;
    private ImageButton backbtn;
    private FloatingActionButton newChatbtn;
    private TextView noUserFound;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

        backbtn.setOnClickListener(this);
        chatList.setNestedScrollingEnabled(false);
        getChatHistory(new SessionManager(getApplication()).getUserID());
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
    public void onUserChatClick(String channelID,String sendTo ,String name) {
//             Intent intent = new Intent(this,MessageScreen.class);
//              startActivity(intent);
        Intent intent = new Intent(this,MessageScreen.class);
        intent.putExtra("sendTo",sendTo);
        intent.putExtra("channelID",channelID);
        intent.putExtra("name",name);
        intent.putExtra("id",1);
        startActivity(intent);

       }

    @Override
    public void onUserProfileClick() {
        imagePreview("xxxxvccvsd");
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getChatHistory(String user_id) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforgetAllchatHistory(user_id, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final AllchatResponse form = gson.fromJson(myResponse, AllchatResponse.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (form.getUserList() != null) {
                            chatAdapter = new ChatListAdapter(form.getUserList(),ChatListScreen.this);
                            chatList.setAdapter(chatAdapter);
                            chatAdapter.setItemClickListener(ChatListScreen.this);

                        }
                    }
                });
            }
        });
    }
}
