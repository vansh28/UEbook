package com.ue.uebook.ChatSdk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.ChatSdk.Adapter.StatusListLineView;
import com.ue.uebook.ChatSdk.Adapter.ViewPagerStatus;
import com.ue.uebook.ChatSdk.Pojo.StatusViewDetail;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusViewScreen extends BaseActivity {
    private Intent intent;
    private String friendId;
    private List<StatusViewDetail>statusViewDetailList;
    private ViewPager viewPager;
    private RecyclerView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_view_screen);
        viewPager = findViewById(R.id.viewPager);
        listView = findViewById(R.id.listView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        listView.setLayoutManager(linearLayoutManager);
        statusViewDetailList = new ArrayList<>();
        intent = getIntent();
        friendId =intent.getStringExtra("friendId");
        getAllStatus(new SessionManager(getApplicationContext()).getUserID(),friendId);

    }

    public void getAllStatus(final String userID ,final String status_user_id ){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.testBaseUrl +"userstatus/userChatStatusList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e(" response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            if (jsonObject.getBoolean("error")==false){
                                JSONArray jsonObjectResponse = jsonObject.getJSONArray("data");
                                if (jsonObjectResponse!=null){
                                    for (int i=0; i<jsonObjectResponse.length();i++){
                                        StatusViewDetail statusmodel = new StatusViewDetail();
                                        JSONObject rec = jsonObjectResponse.getJSONObject(i);
                                        statusmodel.setMessage(rec.getString("message"));
                                        statusmodel.setMessage_type(rec.getString("message_type"));
                                        statusViewDetailList.add(statusmodel);
                                    }
                                  runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                         ViewPagerStatus viewPagerIngredent = new ViewPagerStatus(getApplicationContext(),statusViewDetailList);
                                            viewPager.setAdapter(viewPagerIngredent);

                                            StatusListLineView statusListLineView = new StatusListLineView(statusViewDetailList);
                                            listView.setAdapter(statusListLineView);

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

                            Toast.makeText(StatusViewScreen.this, data.optString("message","Something wrong!"), Toast.LENGTH_LONG).show();
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
                arguments.put("status_user_id",status_user_id);
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
