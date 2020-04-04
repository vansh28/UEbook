package com.ue.uebook.ChatSdk;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ue.uebook.ChatSdk.Adapter.StarredAdapter;
import com.ue.uebook.ChatSdk.Pojo.StarredMessageResponse;
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

public class StarredMessageScreen extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView starredlistMessage;
    private ImageButton backbtn;
    private StarredAdapter starredAdapter;
    private List<StarredMessageResponse>messageResponseList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starred_message_screen);
        starredlistMessage = findViewById(R.id.starredlistMessage);
        backbtn = findViewById(R.id.back);
        messageResponseList = new ArrayList<>();
        backbtn.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        starredlistMessage.setLayoutManager(linearLayoutManager);

        viewStarredmESSAGE();
    }

    @Override
    public void onClick(View v) {
        if (v==backbtn){
            finish();
        }
    }

    public void viewStarredmESSAGE( ){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, ApiRequest.testBaseUrl +"userschat/favoriteChat",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e(" statusview_response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            if (jsonObject.getBoolean("error") == true) {
                                JSONArray jsonObjectResponse = jsonObject.getJSONArray("data");
                                if (jsonObjectResponse != null) {
                                    for (int i = 0; i < jsonObjectResponse.length(); i++) {
                                        StarredMessageResponse statusmodel = new StarredMessageResponse();
                                        JSONObject rec = jsonObjectResponse.getJSONObject(i);
                                        statusmodel.setId(rec.getString("id"));
                                        statusmodel.setChat_id(rec.getString("chat_id"));
                                        statusmodel.setCreated_at(rec.getString("created_at"));
                                        statusmodel.setChat_info_sender(rec.getString("chat_info_sender"));
                                        statusmodel.setMessage_type(rec.getString("message_type"));
                                        statusmodel.setMessage(rec.getString("message"));
                                        statusmodel.setUser_name(rec.getString("user_name"));
                                        statusmodel.setUrl(rec.getString("url"));
                                        messageResponseList.add(statusmodel);

                                    }
                                 runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            starredAdapter = new StarredAdapter(StarredMessageScreen.this, messageResponseList,new SessionManager(getApplicationContext()).getUserID());
                                            starredlistMessage.setAdapter(starredAdapter);
                                        }
                                    });
                                }
                            }
                        } catch (Exception e) {
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

                            Toast.makeText(StarredMessageScreen.this, data.optString("message","Something wrong!"), Toast.LENGTH_LONG).show();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> arguments = new HashMap<String, String>();
                arguments.put("user_id",new SessionManager(getApplicationContext()).getUserID());
                arguments.put("flag","view" );
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
