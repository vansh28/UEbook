package com.ue.uebook.ChatSdk;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.ChatSdk.Adapter.ChatListAdapter;
import com.ue.uebook.ChatSdk.Pojo.AllchatResponse;
import com.ue.uebook.ChatSdk.Pojo.Data;
import com.ue.uebook.ChatSdk.Pojo.UserList;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatListScreen extends BaseActivity implements View.OnClickListener ,ChatListAdapter.ItemClick {
    private RecyclerView chatList;
    private ChatListAdapter chatAdapter;
    private ImageButton backbtn;
    private FloatingActionButton newChatbtn;
    private TextView noUserFound ,noHistoryView;
    private SwipeRefreshLayout swipe_refresh_layout;
    private EditText edittext_search;
    private Data data;
    private List<UserList>userListList;
    private BroadcastReceiver mReceiver;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        userListList= new ArrayList<>();
        edittext_search=findViewById(R.id.edittext_search);
        swipe_refresh_layout=findViewById(R.id.swipe_refresh_layout);
        noHistoryView=findViewById(R.id.noHistoryView);
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
        pullTorefreshswipe();
        edittext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                 if (charSequence.length()>0){
                     chatAdapter.filter(charSequence.toString());
                 }
                 else {
                     chatAdapter = new ChatListAdapter(data,userListList,ChatListScreen.this,new SessionManager(getApplicationContext()).getUserID());
                     chatList.setAdapter(chatAdapter);
                     chatAdapter.setItemClickListener(ChatListScreen.this);
                     noHistoryView.setVisibility(View.GONE);
                 }

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v==backbtn)
        {
            finish();
        }
        else if (v==newChatbtn){
            Intent intent = new Intent(this,FriendListScreen.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onUserChatClick(String channelID,String sendTo ,String name ,String image ,int type) {
                 if (type==11){
                     Intent intent = new Intent(this,MessageScreen.class);
                     intent.putExtra("sendTo",sendTo);
                     intent.putExtra("channelID",channelID);
                     intent.putExtra("name",name);
                     intent.putExtra("imageUrl",image);
                     intent.putExtra("id",1);
                     startActivity(intent);
                     finish();
                 }
                 else {
                     Toast.makeText(ChatListScreen.this,"pro",Toast.LENGTH_LONG).show();
                 }
       }

    @Override
    public void onUserProfileClick(String imageurl ,String name) {
        imagePreview(imageurl);
    }

    @Override
    public void onBroadcastClick(UserList userList) {

    }

    private void imagePreview(String file) {
        final Dialog previewDialog = new Dialog(this);
        previewDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        previewDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout
                , null));
        ImageView imageView = previewDialog.findViewById(R.id.image_view);
        GlideUtils.loadImage(ChatListScreen.this, ApiRequest.BaseUrl+"upload/" + file, imageView, R.drawable.user_default, R.drawable.user_default);
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
        if (userListList.size()>0)
            userListList.clear();
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
                        if (form.getUserList()!= null) {
                            noHistoryView.setVisibility(View.GONE);
                            userListList=form.getUserList();
                            data=form.getData();
                            chatAdapter = new ChatListAdapter(data,userListList,ChatListScreen.this,new SessionManager(getApplicationContext()).getUserID());
                            chatList.setAdapter(chatAdapter);
                            chatAdapter.setItemClickListener(ChatListScreen.this);

                        }
                        else {
                            noHistoryView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getChatHistorys(String user_id) {
        ApiRequest request = new ApiRequest();
       hideLoadingIndicator();
        if (userListList.size()>0)
            userListList.clear();
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
                        if (form.getUserList()!= null) {
                            userListList=form.getUserList();
                            data=form.getData();
                            chatAdapter = new ChatListAdapter(data,userListList,ChatListScreen.this,new SessionManager(getApplicationContext()).getUserID());
                            chatList.setAdapter(chatAdapter);
                            chatAdapter.setItemClickListener(ChatListScreen.this);
                            noHistoryView.setVisibility(View.GONE);

                        }
                        else {
                            noHistoryView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }
    private void pullTorefreshswipe(){
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onRefresh() {
                getChatHistory(new SessionManager(getApplication()).getUserID());
                swipe_refresh_layout.setRefreshing(false);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");

        mReceiver = new BroadcastReceiver() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                String msg_for_me = intent.getStringExtra("some_msg");
                //log our message value
                Log.i("InchooTutorial", msg_for_me);
                getChatHistorys(new SessionManager(getApplication()).getUserID());
            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //unregister our receiver
        this.unregisterReceiver(this.mReceiver);
    }

}
