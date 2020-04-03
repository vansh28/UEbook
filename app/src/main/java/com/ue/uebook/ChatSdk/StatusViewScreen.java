package com.ue.uebook.ChatSdk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
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
import com.ue.uebook.ChatSdk.Pojo.Statusmodel;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;
import com.ue.uebook.SimpleGestureFilter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class StatusViewScreen extends BaseActivity implements View.OnClickListener {
    private Intent intent;
    private String friendId;
    private SimpleGestureFilter detector;

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
    private Button viewUser ;
    private ImageButton deleteStatus;
    private TextView totalview;
    private List<Statusmodel>seenViewList;
    private RecyclerView recyclerView;
    private String chat_status_id;
    private boolean isreply=true;
    private LinearLayout layout_chat_send_container;
    private EmojiconEditText edit_chat_message;
    private boolean iskeypadshow=false;
    private ImageButton button_chat_emoji ,button_chat_send ,selectTime;
    EmojIconActions emojIcon;
    private RelativeLayout root_view;
    private String chanelID,message,messagtype,Frndchat_statusID ;
    private int checkStatus=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_view_screen);
        viewPager = findViewById(R.id.viewPager);
        listView = findViewById(R.id.listView);
        viewUser = findViewById(R.id.viewUser);
        button_chat_emoji = findViewById(R.id.button_chat_emoji);
        button_chat_send = findViewById(R.id.button_chat_send);
        button_chat_send.setOnClickListener(this);
        root_view=findViewById(R.id.root_view);
        viewUser.setOnClickListener(this);
        viewUser.setVisibility(View.GONE);
        seenViewList = new ArrayList<>();
        edit_chat_message = findViewById(R.id.edit_chat_message);
        my_linear_layout  = findViewById(R.id.SliderDots);
        layout_chat_send_container = findViewById(R.id.layout_chat_send_container);
           my_linear_layout.setWeightSum(5f);
        selectTime = findViewById(R.id.selectTime);
        selectTime.setOnClickListener(this);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
         display = wm.getDefaultDisplay();
        // ((display.getWidth()*20)/100)
         height = display.getHeight();// (
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        listView.setLayoutManager(linearLayoutManager);
        statusViewDetailList = new ArrayList<>();
        intent = getIntent();

        friendId =intent.getStringExtra("friendId");
        ownStatus = intent.getIntExtra("ownStatus",0);

        // Set the gesture detector as the double tap
        // listener.
        View myView = findViewById(R.id.view);
        myView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = MotionEventCompat.getActionMasked(event);

                switch(action) {

                    case (MotionEvent.ACTION_UP) :
                        if (ownStatus==1)

                        {
                            showBottomSheet();
                        }
                        else {
                        layout_chat_send_container.setVisibility(View.VISIBLE);
                        edit_chat_message.requestFocus();
                        edit_chat_message.setEnabled(true);
                        viewUser.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }

                        return true;
                }

                  return true;
            }
        });
        if (ownStatus==1){
            isreply = false;
            viewUser.setText("View");
            viewUser.setVisibility(View.VISIBLE);
            selectTime.setVisibility(View.VISIBLE);
            statusViewDetailList = (List<StatusViewDetail>) intent.getSerializableExtra("arraylist");
            viewPagerIngredent = new ViewPagerStatus( StatusViewScreen.this,statusViewDetailList);
            viewPager.setAdapter(viewPagerIngredent);
            StatusListLineView statusListLineView = new StatusListLineView(statusViewDetailList);
            listView.setAdapter(statusListLineView);
            chat_status_id = statusViewDetailList.get(0).getId();
            checkStatusVisibility(chat_status_id);
             getStatusView(new SessionManager(getApplicationContext()).getUserID(),statusViewDetailList.get(0).getId());
            if (statusViewDetailList.size()>0)
            {
                dotSlide();
            }
        }
        else if (ownStatus==2){
            isreply = true;
            viewUser.setText("Reply");
            selectTime.setVisibility(View.GONE);
            viewUser.setVisibility(View.VISIBLE);
            getAllStatus(new SessionManager(getApplicationContext()).getUserID(),friendId);

        }
        emojIcon = new EmojIconActions(this, root_view, edit_chat_message, button_chat_emoji);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("fff", "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("fff", "Keyboard closed");
            }
        });





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
                        if (statusViewDetailList.size()>0)
                            statusViewDetailList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            if (jsonObject.getBoolean("error")==false){
                                chanelID = jsonObject.getString("channel_id");
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
                                            message = statusViewDetailList.get(0).getMessage();
                                            messagtype =statusViewDetailList.get(0).getMessage_type();
                                            Frndchat_statusID=statusViewDetailList.get(0).getId();



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
    private void dotSlide() {

        dotscount = viewPagerIngredent.getCount();
        dots = new ImageView[dotscount];


        int width = display.getWidth()/statusViewDetailList.size();
        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(StatusViewScreen.this);


            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
            parms.setMargins(5,0,5,0);
            dots[i].setLayoutParams(parms);

            dots[i].setBackground(ContextCompat.getDrawable(StatusViewScreen.this, R.drawable.nonactivedot));
            my_linear_layout.addView(dots[i], parms);


        }

        dots[0].setBackground(ContextCompat.getDrawable(this, R.drawable.activedot));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                  Log.e("onPageScrolled", String.valueOf(position));

                  if (ownStatus==2){
                      updateViewStats(statusViewDetailList.get(position).getUserid(),statusViewDetailList.get(position).getId(),new SessionManager(getApplication()).getUserID(),"update");
                      message = statusViewDetailList.get(position).getMessage();
                      messagtype =statusViewDetailList.get(position).getMessage_type();
                      Frndchat_statusID=statusViewDetailList.get(position).getId();

                  }
                  else   if (ownStatus==1){

                      chat_status_id = statusViewDetailList.get(position).getId();
                      checkStatusVisibility(chat_status_id);
                      getStatusView(new SessionManager(getApplicationContext()).getUserID(),statusViewDetailList.get(position).getId());
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
         recyclerView = bottomSheetLayout.findViewById(R.id.viewList);
        deleteStatus = bottomSheetLayout.findViewById(R.id.delete_Status);
        totalview = bottomSheetLayout.findViewById(R.id.total_viewStatus);
        deleteStatus.setOnClickListener(this);
        totalview.setText("Viewed by "+ String.valueOf(seenViewList.size()));
        LinearLayoutManager linearLayoutManagers = new LinearLayoutManager(this);
        linearLayoutManagers.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManagers);
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) bottomSheetLayout.getParent());
        statusSeenAdapter = new StatusSeenAdapter(StatusViewScreen.this,seenViewList);
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


    @Override
    public void onClick(View v) {
        if (v==viewUser){
            if (isreply){
                iskeypadshow=true;
                layout_chat_send_container.setVisibility(View.VISIBLE);
                edit_chat_message.requestFocus();
                edit_chat_message.setEnabled(true);
                viewUser.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
            else {
                layout_chat_send_container.setVisibility(View.GONE);
                viewUser.setVisibility(View.VISIBLE);
                showBottomSheet();
            }

        }
        else if (v==deleteStatus){
            deleteStatus(new SessionManager(getApplicationContext()).getUserID(),chat_status_id);
        }
        else if (v==button_chat_send)
        {
            replyToFrnd(messagtype,message,chanelID,edit_chat_message.getText().toString(),Frndchat_statusID);
        }
        else if (v==selectTime){
            showPopupmenu();
        }
    }
    public void getStatusView(final String userID ,final String chat_status_id ){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.testBaseUrl +"userstatus/viewChatStatus",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e(" statusview_response", response);
                        if (seenViewList.size()>0)
                            seenViewList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            if (jsonObject.getBoolean("error")==false){
                                JSONArray jsonObjectResponse = jsonObject.getJSONArray("data");
                                if (jsonObjectResponse!=null){
                                    for (int i=0; i<jsonObjectResponse.length();i++){
                                        Statusmodel statusmodel = new Statusmodel();
                                        JSONObject rec = jsonObjectResponse.getJSONObject(i);
                                        statusmodel.setUrl(rec.getString("url"));
                                        statusmodel.setUser_name(rec.getString("user_name"));
                                      //  statusViewDetailList.add(statusmodel);
                                        seenViewList.add(statusmodel);
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
                arguments.put("chat_status_id",chat_status_id);
                arguments.put("is_update_or_view","view" );
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

    public void deleteStatus(final String userID ,final String chat_status_id ){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.testBaseUrl +"userstatus/deleteChatStatus",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               finish();
                           }
                       });
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
                arguments.put("chat_status_id",chat_status_id);
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
//    @Override
//    public void onBackPressed() {
//        if (iskeypadshow){
//            InputMethodManager imm = (InputMethodManager)getSystemService(
//                    Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(edit_chat_message.getWindowToken(), 0);
//            iskeypadshow=false;
//        }
//        else {
//                 finish();
//        }
   public void replyToFrnd(final String message_type ,final String message ,final String channel_id ,final String reply_message ,final String Frndchat_status_id){
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setCancelable(true);
    progressDialog.setMessage(getResources().getString(R.string.please_wait));
    progressDialog.show();
    StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.testBaseUrl +"userstatus/commentOnChatStatus",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    Log.e(" statusview_response", response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            layout_chat_send_container.setVisibility(View.GONE);
                            viewUser.setText("Reply");
                            viewUser.setVisibility(View.VISIBLE);
                            InputMethodManager imm = (InputMethodManager)getSystemService(
                         Context.INPUT_METHOD_SERVICE);
                      imm.hideSoftInputFromWindow(edit_chat_message.getWindowToken(), 0);
                        }
                    });
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
            arguments.put("user_id",new SessionManager(getApplicationContext()).getUserID());
            arguments.put("chat_status_id",Frndchat_status_id);
            arguments.put("message_type",message_type);
            arguments.put("message",message);
            arguments.put("reply_message",reply_message);
            arguments.put("status_user_id",friendId);
            arguments.put("channel_id",channel_id);
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
    public void changeStatusVisibility(final String status_id ,final String status_visible_time){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.testBaseUrl +"userstatus/userOwnChatStatusSetting",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e(" statusview_response", response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                checkStatusVisibility(chat_status_id);
                            }
                        });
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
                arguments.put("user_id",new SessionManager(getApplicationContext()).getUserID());
                arguments.put("chat_status_id",status_id);
                arguments.put("status_visible_time",status_visible_time);
                arguments.put("view_chat_status","update");

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
    private void showPopupmenu() {
        PopupMenu popup = new PopupMenu(StatusViewScreen.this, selectTime);
        popup.getMenuInflater()
                .inflate(R.menu.statusvisibility, popup.getMenu());

        if (checkStatus==2){
            popup.getMenu().findItem(R.id.item2).setChecked(true);
        }
        else {
            popup.getMenu().findItem(R.id.item1).setChecked(true);
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        changeStatusVisibility(chat_status_id,"1 days");
                        return true;
                    case R.id.item2:
                        changeStatusVisibility(chat_status_id,"always");
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }


    public void checkStatusVisibility(final String status_id ){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.testBaseUrl +"userstatus/userOwnChatStatusSetting",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e(" statusview_response", response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                                    if (jsonObject.getBoolean("error")==false){
                                        JSONObject jsonObjectResponse = jsonObject.getJSONObject("data");
                                        if (jsonObjectResponse!=null) {

                                            if (jsonObjectResponse.getString("status_visible_time").equalsIgnoreCase("always")){
                                                checkStatus = 2;
                                            }
                                            else {
                                                checkStatus = 1;
                                            }
                                        }
                                    }
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }



                            }
                        });
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
                arguments.put("user_id",new SessionManager(getApplicationContext()).getUserID());
                arguments.put("chat_status_id",status_id);
                arguments.put("view_chat_status","view");

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
