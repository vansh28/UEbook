package com.ue.uebook.ChatSdk;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AddMemberToGroupScreen extends BaseActivity implements View.OnClickListener, ContactListAdapter.ItemClick {

    private RecyclerView friendlist;
    private ImageButton backbtn;
    private ContactListAdapter contactListAdapter;
    private String groupId="";
    private Intent intent;
    private int screen_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_to_group_screen);
        friendlist = findViewById(R.id.friendlist);
        intent = getIntent();
        screen_id=intent.getIntExtra("id",0);
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
                        if (screen_id==1)
                        {
                            getGroupMember(new SessionManager(getApplicationContext()).getUserID(),groupId,userId);
                        }
                        else if (screen_id==2){
                            addMemberInBroadcast(groupId,userId);
                            Toast.makeText(AddMemberToGroupScreen.this, "progress", Toast.LENGTH_SHORT).show();
                        }

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
    public void addMemberInBroadcast(final String broadcast_id ,final String friend_id ){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.BaseUrl +"broadcastChatAddUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e(" response", response);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            JSONObject data = new JSONObject(responseBody);

                            Toast.makeText(AddMemberToGroupScreen.this, data.optString("message","Something wrong!"), Toast.LENGTH_LONG).show();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> arguments = new HashMap<String, String>();
                arguments.put("user_id",new SessionManager(getApplication()).getUserID());
                arguments.put("broadcast_id",broadcast_id);
                arguments.put("friend_id",friend_id);
                return arguments;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


}
