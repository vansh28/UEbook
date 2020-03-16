package com.ue.uebook.ChatSdk;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.ChatSdk.Adapter.USerListStatusAdapter;
import com.ue.uebook.ChatSdk.Pojo.ContactListResponse;
import com.ue.uebook.ChatSdk.Pojo.ContactListStatus;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListForStatusPrivacyScreen extends BaseActivity implements USerListStatusAdapter.ItemClick, View.OnClickListener {
    private ListView friendlist;
    private USerListStatusAdapter uSerListStatusAdapter;
    private ImageButton backbtn;
    private Intent intent ;
    private TextView screenName;
    private List<ContactListStatus> selected_user_data;
    private List<String>selected_user_ids;
    private FloatingActionButton submit;
    private String user_ids="";
    private String visibility="";
    private List<ContactListStatus>contactListStatusList;

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
        contactListStatusList = new ArrayList<>();
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
        checkUserList(new SessionManager(getApplicationContext()).getUserID(),visibility);


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

//                            uSerListStatusAdapter = new USerListStatusAdapter(UserListForStatusPrivacyScreen.this,form.getUserList());
//                            uSerListStatusAdapter.setItemClickListener(UserListForStatusPrivacyScreen.this);
//                            friendlist.setAdapter(uSerListStatusAdapter);


                        }
                    });
                }
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void ontItemClick(ContactListStatus oponentData, int position, int id) {
        if (oponentData!=null){
            Log.e("pod",String.valueOf(position));

            if (id==1){
                selected_user_data.add(oponentData);
                selected_user_ids.add(oponentData.getId());
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
                    selected_user_ids.remove(oponentData.getId());
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
            UploadStatus(new SessionManager(getApplicationContext()).getUserID(),user_ids,visibility);
            Log.e("userid",user_ids);
        }
    }

    public void UploadStatus(final String userID ,final  String user_ids_selected ,final  String visibilitys ){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.testBaseUrl+"userstatus/userChatStatusSetting",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e(" response", response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        });

//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            if (jsonObject.getString("status").equals("true")) {
//                                String orderNumber = jsonObject.getString("order_no");
//                                String order_id = jsonObject.getString("order_id");
//                                Intent intent = new Intent(BillingScreen.this,ThankyouScreen.class);
//                                intent.putExtra("orderNumber",orderNumber);
//                                intent.putExtra("order_id",order_id);
//                                startActivity(intent);
//                                finish();
//
//                            }
//                            Toast.makeText(BillingScreen.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
//
//                        }
//
//                        catch (Exception e) {
//                            e.printStackTrace();
//                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject data = new JSONObject(responseBody);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                            Toast.makeText(UserListForStatusPrivacyScreen.this, data.optString("message","Something wrong!"), Toast.LENGTH_LONG).show();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> arguments = new HashMap<String, String>();
                arguments.put("user_id",userID);
                arguments.put("visibility_flag",visibilitys);
                arguments.put("user_ids",user_ids_selected);
                return arguments;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UserListForStatusPrivacyScreen.this);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    public void checkUserList(final String userID ,final  String visibilitys ){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.testBaseUrl+"userstatus/selectedUsersListForStatus",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e(" response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("error")==false){
                                JSONArray jsonObjectResponse = jsonObject.getJSONArray("data");
                                if (jsonObjectResponse!=null){
                                    for (int i=0; i<jsonObjectResponse.length();i++){
                                        ContactListStatus contactListStatus = new ContactListStatus();
                                        JSONObject rec = jsonObjectResponse.getJSONObject(i);
                                        contactListStatus.setId(rec.getString("id"));
                                        contactListStatus.setUser_name(rec.getString("user_name"));
                                        contactListStatus.setIs_in_list(rec.getString("is_in_list"));
                                        contactListStatus.setUrl(rec.getString("url"));
                                        contactListStatusList.add(contactListStatus);
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            uSerListStatusAdapter = new USerListStatusAdapter(UserListForStatusPrivacyScreen.this,contactListStatusList);
                                            uSerListStatusAdapter.setItemClickListener(UserListForStatusPrivacyScreen.this);
                                            friendlist.setAdapter(uSerListStatusAdapter);
                                        }
                                    });
                                }
                            }

                        }

                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject data = new JSONObject(responseBody);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                            Toast.makeText(UserListForStatusPrivacyScreen.this, data.optString("message","Something wrong!"), Toast.LENGTH_LONG).show();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> arguments = new HashMap<String, String>();
                arguments.put("user_id",userID);
                arguments.put("visibility_flag",visibilitys);
                return arguments;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UserListForStatusPrivacyScreen.this);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }




}
