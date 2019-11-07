package com.ue.uebook.ChatSdk;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.ChatSdk.Adapter.MessageAdapter;
import com.ue.uebook.ChatSdk.Pojo.ChatResponse;
import com.ue.uebook.ChatSdk.Pojo.OponentData;
import com.ue.uebook.ChatSdk.Pojo.UserData;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.File;
import java.io.IOException;

public class MessageScreen extends BaseActivity implements View.OnClickListener {
    private Intent intent;
    private ImageButton back_btn, button_chat_attachment, morebtn, button_chat_send;
    private ImageView userProfile;
    private RecyclerView messageList;
    private EditText chat_message;
    private TextView oponent_name;
    private int screenID;
    private OponentData oponentData;
    private UserData userData;
    private MessageAdapter messageAdapter;

    private String filePath;
    private String fileName;
    private File  imageurl;
    private Bitmap bitmap;
    private String chanelID="";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_screen);
        back_btn = findViewById(R.id.backbtnMessage);
        oponent_name=findViewById(R.id.oponent_name);
        intent = getIntent();
        screenID=intent.getIntExtra("id",0);
        userProfile = findViewById(R.id.image_user_chat);
        chat_message = findViewById(R.id.edit_chat_message);
        messageList = findViewById(R.id.messageList);
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        messageList.setLayoutManager(linearLayoutManagerPopularList);


        button_chat_attachment = findViewById(R.id.button_chat_attachment);
        morebtn = findViewById(R.id.morebtn);
        button_chat_send = findViewById(R.id.button_chat_send);
        morebtn.setOnClickListener(this);
        button_chat_send.setOnClickListener(this);
        button_chat_attachment.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        userProfile.setOnClickListener(this);
        if (screenID==2){
            oponentData= (OponentData) intent.getSerializableExtra("oponentdata");
            userData= (UserData) intent.getSerializableExtra("userData");
            if (oponentData!=null){
                oponent_name.setText(oponentData.getName());
                GlideUtils.loadImage(MessageScreen.this, "http://dnddemo.com/ebooks/api/v1/upload/" + oponentData.getUrl(), userProfile, R.drawable.user_default, R.drawable.user_default);

            }
            if (oponentData.channelId!=null){
                chanelID=oponentData.channelId;
            }

        }
            getChatHistory(new SessionManager(getApplication()).getUserID(),oponentData.userId,chanelID,"text");
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if (v == back_btn) {
            finish();
        } else if (v == userProfile) {
            imagePreview(oponentData.getUrl());
        } else if (v == button_chat_attachment) {

        } else if (v == morebtn) {
            showPopupmenu();
        } else if (v == button_chat_send) {
            sendMesaage(new SessionManager(getApplication()).getUserID(),"",oponentData.userId,"message",chanelID,chat_message.getText().toString());
        }
    }
    private void imagePreview(String file) {
        final Dialog previewDialog = new Dialog(this);
        previewDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        previewDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout
                , null));
        ImageView imageView = previewDialog.findViewById(R.id.image_view);
        GlideUtils.loadImage(MessageScreen.this, "http://dnddemo.com/ebooks/api/v1/upload/" + file, imageView, R.drawable.user_default, R.drawable.user_default);
        Button ok_Btn = previewDialog.findViewById(R.id.buton_ok);
        ok_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewDialog.dismiss();

            }
        });
        previewDialog.show();
    }
    private void showPopupmenu() {
        PopupMenu popup = new PopupMenu(MessageScreen.this, morebtn);
        popup.getMenuInflater()
                .inflate(R.menu.chatmoremenu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });
        popup.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendMesaage(String  user_id, String tokenKey, String sendTO, String type, final String channelId, String message) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforChat( user_id,tokenKey,sendTO,type,channelId,message,new okhttp3.Callback() {
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
                final ChatResponse form = gson.fromJson(myResponse, ChatResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chat_message.setText("");
                        getChatHistory(new SessionManager(getApplication()).getUserID(),oponentData.userId,oponentData.channelId,"text");
                    }
                });
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getChatHistory(String  user_id,String sendTO,String channelId,String type) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforgetChathistory( user_id,sendTO,channelId,type,new okhttp3.Callback() {
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
                final ChatResponse form = gson.fromJson(myResponse, ChatResponse.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (form.getChat_list()!=null){
                            messageAdapter = new MessageAdapter(form.getChat_list(),new SessionManager(getApplication()).getUserID());
                            messageList.setAdapter(messageAdapter);
                            messageList.scrollToPosition(form.getChat_list().size() - 1);
                            messageAdapter.notifyDataSetChanged();

                        }

                    }
                });
            }
        });
    }

}

