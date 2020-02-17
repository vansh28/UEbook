package com.ue.uebook.ChatSdk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.ChatSdk.Adapter.ContactListAdapter;
import com.ue.uebook.ChatSdk.Pojo.ContactListResponse;
import com.ue.uebook.ChatSdk.Pojo.MemberListResponse;
import com.ue.uebook.ChatSdk.Pojo.OponentData;
import com.ue.uebook.ChatSdk.Pojo.UserData;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;

public class AddMemberToGroupScreen extends BaseActivity implements View.OnClickListener, ContactListAdapter.ItemClick {

    private RecyclerView friendlist;
    private ImageButton backbtn;
    private ContactListAdapter contactListAdapter;
    private String groupId="";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_to_group_screen);
        friendlist = findViewById(R.id.friendlist);
        intent = getIntent();
        groupId = intent.getStringExtra("groupid");
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        friendlist.setLayoutManager(linearLayoutManager);
        getContactList(new SessionManager(getApplicationContext()).getUserID());
    }

    @Override
    public void onClick(View v) {
        if (v==backbtn){
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getContactList(String user_id) {
        ApiRequest request = new ApiRequest();
        request.requestforgetContactList(user_id,"No", new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final ContactListResponse form = gson.fromJson(myResponse, ContactListResponse.class);
                if (form.error == false && form.getUserList() != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            contactListAdapter = new ContactListAdapter(AddMemberToGroupScreen.this , form.getUserList(), form.getData());
                            friendlist.setAdapter(contactListAdapter);
                            contactListAdapter.setItemClickListener(AddMemberToGroupScreen.this);
                            contactListAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                  runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }
        });
    }


    @Override
    public void onContactListItemClick(OponentData oponentData, UserData userData)
    {
        confirmUser(oponentData.getUserId());

    }


    @Override
    public void onProfileClick(String url) {

    }
    public void confirmUser(final String userId ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to add ")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        getGroupMember(new SessionManager(getApplicationContext()).getUserID(),groupId,userId);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getGroupMember(String user_id, String groupID ,String memberId) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();

        request.requestforgetGroupMember(user_id, groupID,memberId,"add_member", new okhttp3.Callback() {
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
                final MemberListResponse form = gson.fromJson(myResponse, MemberListResponse.class);
                if (form.getError() == false && form.getUser_list() != null) {
//
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                    finish();
                        }
                    });
//
//
                } else {


                }
            }
        });
    }


}
