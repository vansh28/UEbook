package com.ue.uebook.AuthorProfileActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.AuthorProfileActivity.pojo.FrirndRequestData;
import com.ue.uebook.AuthorProfileActivity.pojo.RequestData;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PendingRequestScreen extends BaseActivity implements View.OnClickListener  ,FriendRequestAdapter.ItemClick{
    private RecyclerView pendinglist;
    private FriendRequestAdapter friendRequestAdapter;
    private ImageButton backbtn;
    private List<RequestData> data;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_request_screen);
        pendinglist=findViewById(R.id.pendinglist);
        data= new ArrayList<>();
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        pendinglist.setLayoutManager(linearLayoutManagerPopularList);
        backbtn=findViewById(R.id.back_pending);
        pendingRequest(new SessionManager(getApplicationContext()).getUserID());
        backbtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (view==backbtn){
            finish();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void pendingRequest(String userID) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforgetPendingRequest(userID, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                hideLoadingIndicator();
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final FrirndRequestData form = gson.fromJson(myResponse, FrirndRequestData.class);
                data=form.getData();
                if (form.getError().equals("false")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pendinglist.setVisibility(View.VISIBLE);
                            friendRequestAdapter = new FriendRequestAdapter(PendingRequestScreen.this, form.getData());
                            pendinglist.setAdapter(friendRequestAdapter);
                            friendRequestAdapter.notifyDataSetChanged();
                            friendRequestAdapter.setItemClickListener(PendingRequestScreen.this);
                        }
                    });
                }
                else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pendinglist.setVisibility(View.GONE);
                        }
                    });
                }
 }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void pendingRequestConfirm(String frndId,String status) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforAcceptRequest(frndId,status, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                String myResponse = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pendingRequest(new SessionManager(getApplicationContext()).getUserID());
                    }
                });
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onConfirmclick(int position, String frndId) {
        pendingRequestConfirm(frndId,"1");
    }
    @Override
    public void ondeleteclick(int position, String book_id) {

    }
}
