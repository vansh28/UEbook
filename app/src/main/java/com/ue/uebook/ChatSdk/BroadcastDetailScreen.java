package com.ue.uebook.ChatSdk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.AppBarLayout;
import com.ue.uebook.BaseActivity;
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

public class BroadcastDetailScreen extends BaseActivity implements View.OnClickListener ,BroadcastmemberAdapter.ItemClick {
private RecyclerView recyclerList;
private List<GroupMemberList>groupMemberList;
private Intent intent;
private String broadcastNo,broadcastName;
private TextView groupname,maintitle;
private LinearLayout addMember;
private ImageButton backbtn;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    private LinearLayout mTitleContainer  ,exitGroup;
    private TextView mTitle ,group_name ,memberCount;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_detail_screen);
        recyclerList = findViewById(R.id.recyclerList);
        exitGroup = findViewById(R.id.exitGroup);
        exitGroup.setOnClickListener(this);
        mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle          = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
        groupMemberList = new ArrayList<>();
        intent = getIntent();
        groupname = findViewById(R.id.groupname);
        addMember = findViewById(R.id.addMember);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(this);
        maintitle=findViewById(R.id.maintitle);
        addMember.setOnClickListener(this);
        broadcastNo = intent.getStringExtra("broadcastNo");
        broadcastName = intent.getStringExtra("broadcastName");
        groupname.setText(broadcastName);
        maintitle.setText(broadcastName);
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
                                            broadcastmemberAdapter.setItemClickListener(BroadcastDetailScreen.this);
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

    @Override
    public void onClick(View v) {
        if (v==addMember){
            Intent intent = new Intent(this, AddMemberToGroupScreen.class);
            intent.putExtra("id",2);
            intent.putExtra("groupid",broadcastNo);
            startActivity(intent);
        }
        else if (v==backbtn){
            finish();
        }
        else if (v==exitGroup){

        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle","onRestart invoked");

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something here
                getAllmemberList(broadcastNo);

            }
        }, 1000);

    }

    @Override
    public void onMemberItemClick(View v, GroupMemberList groupMemberList) {
              showFilterPopup(v,groupMemberList);
    }
    private void showFilterPopup(View v , GroupMemberList groupMemberList) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.remove);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.remove:
                       deletemember(broadcastNo,groupMemberList.getId());
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }
    public void deletemember(final String broadcast_id ,final  String friend_id){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.BaseUrl +"broadcastChatRemoveUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e(" response", response);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getAllmemberList(broadcastNo);
                            }
                        });

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
