package com.ue.uebook.ChatSdk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ue.uebook.ChatSdk.Adapter.BroadcastmemberAdapter;
import com.ue.uebook.ChatSdk.Pojo.GroupMemberList;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BroadcastDetailScreen extends AppCompatActivity {
private RecyclerView recyclerList;
private List<GroupMemberList>groupMemberList;
private Intent intent;
private String broadcastNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_detail_screen);
        recyclerList = findViewById(R.id.recyclerList);
        groupMemberList = new ArrayList<>();
        intent = getIntent();
        broadcastNo = intent.getStringExtra("broadcastNo");
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerList.setLayoutManager(linearLayoutManagerPopularList);
        getAllmemberList(broadcastNo);
    }

    public void getAllmemberList(final String broadcast_id){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.BaseUrl +"broadcastChatMemberList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e(" response", response);
                        if (groupMemberList.size()>0)
                            groupMemberList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            if (jsonObject.getBoolean("error")==false){
                                JSONArray jsonObjectResponse = jsonObject.getJSONArray("data");
                                if (jsonObjectResponse!=null){
                                    for (int i=0; i<jsonObjectResponse.length();i++){
                                        GroupMemberList statusmodel = new GroupMemberList();
                                        JSONObject rec = jsonObjectResponse.getJSONObject(i);
                                        statusmodel.setUser_name(rec.getString("user_name"));
                                        statusmodel.setId(rec.getString("id"));
                                        statusmodel.setUrl(rec.getString("url"));
                                       // broadcastmessageList.add(statusmodel);
                                        groupMemberList.add(statusmodel);
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            BroadcastmemberAdapter  broadcastmemberAdapter = new BroadcastmemberAdapter(BroadcastDetailScreen.this,groupMemberList);
                                            recyclerList.setAdapter(broadcastmemberAdapter);

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
                            String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            JSONObject data = new JSONObject(responseBody);

                            Toast.makeText(BroadcastDetailScreen.this, data.optString("message","Something wrong!"), Toast.LENGTH_LONG).show();
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
