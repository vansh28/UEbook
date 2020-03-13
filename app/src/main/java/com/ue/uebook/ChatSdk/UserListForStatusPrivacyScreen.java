package com.ue.uebook.ChatSdk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.ChatSdk.Adapter.USerListStatusAdapter;
import com.ue.uebook.ChatSdk.Pojo.ContactListResponse;
import com.ue.uebook.ChatSdk.Pojo.OponentData;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserListForStatusPrivacyScreen extends BaseActivity implements USerListStatusAdapter.ItemClick, View.OnClickListener {
    private ListView friendlist;
    private USerListStatusAdapter uSerListStatusAdapter;
    private ImageButton backbtn;
    private Intent intent ;
    private TextView screenName;
    private List<OponentData> selected_user_data;
    private List<String>selected_user_ids;
    private FloatingActionButton submit;
    private String user_ids="";
    private String visibility="";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_for_status_privacy_screen);
        friendlist = findViewById(R.id.friendlist);
        backbtn = findViewById(R.id.backbtnChat);
        screenName = findViewById(R.id.screenName);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
        selected_user_data = new ArrayList<>();
        selected_user_ids = new ArrayList<>();

        if (selected_user_ids.size()>0){
            submit.setVisibility(View.VISIBLE);
        }
        else {
            submit.setVisibility(View.GONE);
        }
        intent = getIntent();
        screenName.setText(intent.getStringExtra("type"));
        visibility=intent.getStringExtra("visibility");
        backbtn.setOnClickListener(this);
        getContactList(new SessionManager(getApplicationContext()).getUserID());

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getContactList(String  user_id) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforgetContactList( user_id,"No",new okhttp3.Callback() {
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
                final ContactListResponse form = gson.fromJson(myResponse, ContactListResponse.class);

                if (form.error==false && form.getUserList()!=null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

//                            groupAdapter = new GroupAdapter(CreateGroupScreen.this,form.getUserList(),form.getData());
//                            recyclerView.setAdapter(groupAdapter);
//                            groupAdapter.setItemClickListener(CreateGroupScreen.this);
//                            groupAdapter.notifyDataSetChanged();

                            uSerListStatusAdapter = new USerListStatusAdapter(UserListForStatusPrivacyScreen.this,form.getUserList());
                            uSerListStatusAdapter.setItemClickListener(UserListForStatusPrivacyScreen.this);
                            friendlist.setAdapter(uSerListStatusAdapter);


                        }
                    });
                }
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void ontItemClick(OponentData oponentData, int position, int id) {
        if (oponentData!=null){
            Log.e("pod",String.valueOf(position));

            if (id==1){
                selected_user_data.add(oponentData);
                selected_user_ids.add(oponentData.getUserId());
                if (selected_user_ids.size()>0){
                    submit.setVisibility(View.VISIBLE);
                }
                else {
                    submit.setVisibility(View.GONE);
                }
            }
            else if (id==2){
                if (selected_user_data.size()>0){
                    selected_user_data.remove(oponentData);
                    selected_user_ids.remove(oponentData.getUserId());
                    if (selected_user_ids.size()>0){
                        submit.setVisibility(View.VISIBLE);
                    }
                    else {
                        submit.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v==backbtn){
            finish();
        }
        else if (v==submit)
        {
            for(String s:selected_user_ids){
                if (user_ids == ""){
                    user_ids = s;
                }
                else {
                    user_ids =   user_ids + "," + s;
                }
            }
            changeStatus(new SessionManager(getApplicationContext()).getUserID(),visibility,user_ids);
            Log.e("userid",user_ids);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void changeStatus(String user_id, String visibility, String user_ids) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforChangeStatusPrivacy(user_id, visibility , user_ids, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                final String myResponse = response.body().string();
                Log.e("response", myResponse);
                Gson gson = new GsonBuilder().create();
                // final CheckPaymentDone form = gson.fromJson(myResponse, CheckPaymentDone.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        try {
//                            JSONObject obj = new JSONObject(myResponse);
//                            JSONObject  b = obj.getJSONObject("data");
//
//                            String val = b.getString("converted_amount");
//
//                            convertPrice = val;
//                            Log.e("USD",val);
//                            payment.setText("$"+val);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                });


            }
        });
    }




}
