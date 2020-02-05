package com.ue.uebook.ChatSdk;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class GroupMessageScreen extends BaseActivity implements View.OnClickListener {
    private Intent intent;
    private String groupID;
    private EditText edit_chat_message;
    private ImageButton button_chat_send ,backbtnMessage;
    private TextView group_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message_screen);
        intent = getIntent();
        groupID=intent.getStringExtra("groupid");
        backbtnMessage=findViewById(R.id.backbtnMessage);
        group_name=findViewById(R.id.group_name);
        group_name.setText(intent.getStringExtra("name"));
        backbtnMessage.setOnClickListener(this);
        edit_chat_message=findViewById(R.id.edit_chat_message);
        button_chat_send=findViewById(R.id.button_chat_send);
        button_chat_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
         if (v==backbtnMessage){
                finish();
         }
         else if (v==button_chat_send){
             sendMesaage(groupID,new SessionManager(getApplicationContext()).getUserID(),"text",edit_chat_message.getText().toString(),0);
         }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendMesaage(String group_id, String send_by, String message_type , String message, int typeval) {
        OkHttpClient client = new OkHttpClient();
        String url = ApiRequest.BaseUrl+"groupsChat";
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        showLoadingIndicator();
        RequestBody requestBody;
        switch (typeval) {
            case 0:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(" group_id", group_id)
                        .addFormDataPart(" send_by", send_by)
                        .addFormDataPart(" message_type", message_type)
                        .addFormDataPart(" message", message)
                        .build();
                break;
//            case 1:
//                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                        .addFormDataPart(" tokenKey", tokenKey)
//                        .addFormDataPart(" user_id", user_id)
//                        .addFormDataPart(" sendTO", sendTO)
//                        .addFormDataPart(" type", type)
//                        .addFormDataPart(" channelId", channelId)
//                        .addFormDataPart(" message", message)
//                     //   .addFormDataPart("image_file", imageurl.getName(), RequestBody.create(MEDIA_TYPE_PNG, imageurl))
//
//                        .build();
//                break;
//            case 2:
//                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                        .addFormDataPart(" tokenKey", tokenKey)
//                        .addFormDataPart(" user_id", user_id)
//                        .addFormDataPart(" sendTO", sendTO)
//                        .addFormDataPart(" type", type)
//                        .addFormDataPart(" channelId", channelId)
//                        .addFormDataPart(" message", message)
//                 //       .addFormDataPart("video_file", videofile.getName(), RequestBody.create(MediaType.parse("video/mp4"), videofile))
//                        .build();
//                break;
//            case 3:
//                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                        .addFormDataPart(" tokenKey", tokenKey)
//                        .addFormDataPart(" user_id", user_id)
//                        .addFormDataPart(" sendTO", sendTO)
//                        .addFormDataPart(" type", type)
//                        .addFormDataPart(" channelId", channelId)
//                        .addFormDataPart(" message", "Audio")
//                      //  .addFormDataPart("audio_file", audioUrl.getName(), RequestBody.create(MediaType.parse("audio/*"), audioUrl))
//
//                        .build();
//                break;
//            case 4:
//                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                        .addFormDataPart(" tokenKey", tokenKey)
//                        .addFormDataPart(" user_id", user_id)
//                        .addFormDataPart(" sendTO", sendTO)
//                        .addFormDataPart(" type", type)
//                        .addFormDataPart(" channelId", channelId)
//                        .addFormDataPart(" message", "file")
//                      //  .addFormDataPart("pdf_file", docfile.getName(), RequestBody.create(MediaType.parse("text/csv"), docfile))
//                        .build();
//                break;
            default:
                throw new IllegalStateException("Unexpected value: " + 1);
        }
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                Log.e("chatresponse", myResponse);
//                final ChatResponse form = gson.fromJson(myResponse, ChatResponse.class);
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        edit_chat_message.setText("");
//                        typevalue = 0;
//                        chanelID = form.getChat_list().get(0).getChannelId();
//                        getChatHistory(new SessionManager(getApplication()).getUserID(), sendToID, chanelID, "text");
                    }
                });
            }
        });
    }



}
