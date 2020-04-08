package com.ue.uebook.ChatSdk;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.ue.uebook.ChatSdk.Adapter.CreategroupAdapter;
import com.ue.uebook.ChatSdk.Adapter.GroupItemListAdapter;
import com.ue.uebook.ChatSdk.Pojo.ContactListResponse;
import com.ue.uebook.ChatSdk.Pojo.OponentData;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BroadCastUserList extends BaseActivity implements CreategroupAdapter.ItemClick, GroupItemListAdapter.ItemClick, View.OnClickListener {
    private RecyclerView memberList;
    private ListView userList;
    private CreategroupAdapter contactList;
    private GroupItemListAdapter groupItemListAdapter;
    private List<String>userIDForChat;
    private List<OponentData>groupPopleList;
    private ImageButton backbtn;
    private FloatingActionButton createBroadcast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_cast_user_list);
        memberList = findViewById(R.id.memberList);
        userList = findViewById(R.id.userlist);
        createBroadcast = findViewById(R.id.createBroadcast);
        createBroadcast.setOnClickListener(this);
        backbtn = findViewById(R.id.back);
        backbtn.setOnClickListener(this);
        groupPopleList = new ArrayList<>();
        userIDForChat = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        memberList.setLayoutManager(linearLayoutManager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getContactList(new SessionManager(getApplication()).getUserID());
        }

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

                            contactList = new CreategroupAdapter(BroadCastUserList.this,form.getUserList());
                            contactList.setItemClickListener(BroadCastUserList.this);
                            userList.setAdapter(contactList);

                        }
                    });
                }
            }
        });
    }

    @Override
    public void ontItemClick(OponentData oponentData, int position, int id) {
        if (oponentData!=null){
            Log.e("pod",String.valueOf(position));
            if (id==1){
                groupPopleList.add(oponentData);
                addKist(groupPopleList);
                userIDForChat.add(oponentData.getUserId());
            }
            else if (id==2){
                if (groupPopleList.size()>0){
                    groupPopleList.remove(oponentData);
                    addKist(groupPopleList);
                    userIDForChat.remove(oponentData.getUserId());
                }
            }
        }
    }
    @SuppressLint("RestrictedApi")
    private void addKist(List<OponentData> oponentData){
        if (oponentData.size()>0){
            memberList.setVisibility(View.VISIBLE);
            createBroadcast.setVisibility(View.VISIBLE);
            groupItemListAdapter= new GroupItemListAdapter(BroadCastUserList.this,oponentData);
            memberList.setAdapter(groupItemListAdapter);
            groupItemListAdapter.setItemClickListener(BroadCastUserList.this);
            groupItemListAdapter.notifyDataSetChanged();
        }
        else {
            createBroadcast.setVisibility(View.GONE);
            memberList.setVisibility(View.GONE);
        }

    }

    @Override
    public void ongroupListItemClick(OponentData oponentData, int position) {

    }

    @Override
    public void onClick(View v) {
        if (v==backbtn){
            finish();
        }
        else if (v==createBroadcast){
                      if (groupPopleList.size()>1){
                          String userIDs ="";
                          for(String s:userIDForChat){
                              if (userIDs == ""){
                                  userIDs = s;
                              }
                              else {
                                  userIDs =   userIDs + "," + s;
                              }
                          }
                             createBroadcast(userIDs);
                      }
                      else {
                          Toast.makeText(this,"Please Select more than one User",Toast.LENGTH_SHORT).show();
                      }
//            if (groupPopleList.size()>1) {
//                JSONArray ja = new JSONArray();
//                for (int i = 0; i < groupPopleList.size(); i++) {
//                    JSONObject jo = new JSONObject();
//                    try {
//                        jo.put("receiver_id",groupPopleList.get(i).getUserId());
//                        if (groupPopleList.get(i).getChannelId()==null){
//                            jo.put("channel_id","");
//                        }
//                        else {
//                            jo.put("channel_id",groupPopleList.get(i).getChannelId());
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    ja.put(jo);
//                }
//
//                Log.e("user", String.valueOf(ja));
//                createBroadcast(String.valueOf(ja));
//            }
//            else {
//                Toast.makeText(this,"Please Select more than one User",Toast.LENGTH_SHORT).show();
//            }
           // sendAnswer(String.valueOf(ja));
        }

        }

    public void createBroadcast(final String receiver_ids ){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.BaseUrl +"broadcastChat",
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

                            Toast.makeText(BroadCastUserList.this, data.optString("message","Something wrong!"), Toast.LENGTH_LONG).show();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> arguments = new HashMap<String, String>();
                arguments.put("send_by",new SessionManager(getApplication()).getUserID());
                arguments.put("receiver_ids",receiver_ids);
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
