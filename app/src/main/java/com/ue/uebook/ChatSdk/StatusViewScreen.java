package com.ue.uebook.ChatSdk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.ChatSdk.Adapter.StatusListLineView;
import com.ue.uebook.ChatSdk.Adapter.StatusSeenAdapter;
import com.ue.uebook.ChatSdk.Adapter.ViewPagerStatus;
import com.ue.uebook.ChatSdk.Pojo.StatusViewDetail;
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

public class StatusViewScreen extends BaseActivity {
    private Intent intent;
    private String friendId;
    private List<StatusViewDetail>statusViewDetailList;
    private ViewPager viewPager;
    private RecyclerView listView;
    private LinearLayout my_linear_layout;
    private  ViewPagerStatus viewPagerIngredent;
    private int ownStatus;
    private int dotscount;
    private ImageView[] dots;
    private BottomSheetBehavior mBottomSheetBehavior;
    private RecyclerView viewSeenUserList;
    private StatusSeenAdapter statusSeenAdapter;
     private BottomSheetDialog mBottomSheetDialog;
    private LinearLayout bottom_sheet;
    private Display display;
    private  int height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_view_screen);
        viewPager = findViewById(R.id.viewPager);
        listView = findViewById(R.id.listView);
        my_linear_layout  = findViewById(R.id.SliderDots);
           my_linear_layout.setWeightSum(5f);
         display = getWindowManager().getDefaultDisplay();
        // ((display.getWidth()*20)/100)
         height = display.getHeight();// (
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        listView.setLayoutManager(linearLayoutManager);
        statusViewDetailList = new ArrayList<>();
        intent = getIntent();

        friendId =intent.getStringExtra("friendId");
        ownStatus = intent.getIntExtra("ownStatus",0);
           //showBottomSheet();
        if (ownStatus==1){
            statusViewDetailList = (List<StatusViewDetail>) intent.getSerializableExtra("arraylist");
            viewPagerIngredent = new ViewPagerStatus( StatusViewScreen.this,statusViewDetailList);
            viewPager.setAdapter(viewPagerIngredent);
            StatusListLineView statusListLineView = new StatusListLineView(statusViewDetailList);
            listView.setAdapter(statusListLineView);

//            int width = display.getWidth()/statusViewDetailList.size();
//            for(int i=0;i<statusViewDetailList.size();i++)
//            {
//                ImageView ii= new ImageView(this);
//                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
//                parms.setMargins(5,0,5,0);
//                ii.setLayoutParams(parms);
//                ii.setBackgroundResource(R.drawable.activedot);
//                my_linear_layout.addView(ii);
//            }

            if (statusViewDetailList.size()>0)
            {
                dotSlide();
            }
        }
        else if (ownStatus==2){
            getAllStatus(new SessionManager(getApplicationContext()).getUserID(),friendId);
        }
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
                                        statusmodel.setBg_color(rec.getString("bg_color"));
                                        statusmodel.setFont_style(rec.getString("font_style"));
                                        statusmodel.setCaption(rec.getString("caption"));
                                        statusmodel.setId(rec.getString("id"));
                                        statusmodel.setUserid(rec.getString("userid"));
                                        statusViewDetailList.add(statusmodel);
                                    }
                                  runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                          viewPagerIngredent = new ViewPagerStatus(StatusViewScreen.this,statusViewDetailList);
                                            viewPager.setAdapter(viewPagerIngredent);

                                            StatusListLineView statusListLineView = new StatusListLineView(statusViewDetailList);
                                            listView.setAdapter(statusListLineView);





                                             if (statusViewDetailList.size()>0){
                                                 dotSlide();
                                             }
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


    public void getOwnStatus(final String userID ){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.testBaseUrl +"userstatus/userOwnChatStatusList",
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
                                        statusmodel.setId(rec.getString("id"));
                                        statusmodel.setMessage(rec.getString("message"));
                                        statusmodel.setMessage_type(rec.getString("message_type"));
                                        statusmodel.setBg_color(rec.getString("bg_color"));
                                        statusmodel.setFont_style(rec.getString("font_style"));
                                        statusmodel.setCaption(rec.getString("caption"));
                                        statusmodel.setUserid(rec.getString("userid"));
                                        statusViewDetailList.add(statusmodel);

                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

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



    private void dotSlide() {

        dotscount = viewPagerIngredent.getCount();
        dots = new ImageView[dotscount];
        int width = display.getWidth()/statusViewDetailList.size();
        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(StatusViewScreen.this);

            dots[i].setBackground(ContextCompat.getDrawable(StatusViewScreen.this, R.drawable.nonactivedot));

            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
            parms.setMargins(5,0,5,0);
            dots[i].setLayoutParams(parms);

            my_linear_layout.addView(dots[i], parms);

        }

        dots[0].setBackground(ContextCompat.getDrawable(this, R.drawable.activedot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                  Log.e("onPageScrolled", String.valueOf(position));

                  if (ownStatus==2){
                      updateViewStats(statusViewDetailList.get(position).getUserid(),statusViewDetailList.get(position).getId(),new SessionManager(getApplication()).getUserID(),"update");
                  }
                  else   if (ownStatus==1){
                      updateViewStats(new SessionManager(getApplication()).getUserID(),statusViewDetailList.get(position).getId(),"","view");
                  }

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setBackground(ContextCompat.getDrawable(StatusViewScreen.this, R.drawable.nonactivedot));
                }
                dots[position].setBackground(ContextCompat.getDrawable(StatusViewScreen.this, R.drawable.activedot));
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void showBottomSheet() {
        final View bottomSheetLayout = getLayoutInflater().inflate(R.layout.statusbottomsheet, null);
        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(bottomSheetLayout);
        mBottomSheetDialog.setCanceledOnTouchOutside(false);
        RecyclerView recyclerView = bottomSheetLayout.findViewById(R.id.viewList);
        LinearLayoutManager linearLayoutManagers = new LinearLayoutManager(this);
        linearLayoutManagers.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManagers);
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) bottomSheetLayout.getParent());

        mBehavior.setPeekHeight(150);
        statusSeenAdapter = new StatusSeenAdapter();
        recyclerView.setAdapter(statusSeenAdapter);
        mBottomSheetDialog.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateViewStats(String user_id, String chatsTatusId, String viewuserid ,String flag)
    {
        ApiRequest request = new ApiRequest();
        request.requestforViewStatus(user_id , chatsTatusId,viewuserid ,flag,new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                final String myResponse = response.body().string();
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
    public void Add_Line() {

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View newRowView = inflater.inflate(R.layout.statusprogress, my_linear_layout, false);
            LinearLayout linearLayout = newRowView.findViewById(R.id.rootview);

            my_linear_layout.addView(newRowView);

    }




}
