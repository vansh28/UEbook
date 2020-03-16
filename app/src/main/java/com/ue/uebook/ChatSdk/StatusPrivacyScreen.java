package com.ue.uebook.ChatSdk;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;
import java.util.List;

public class StatusPrivacyScreen extends BaseActivity implements View.OnClickListener {
    private ImageButton back_btn;
    private RadioGroup radioGroupstatus;
    private Intent intent;
    private List<String>userIds;
    private String statusValue="1";
    private RadioButton allContact,hideStatus,onlyShareStatus;
    private Button okStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_privacy_screen);
        back_btn = findViewById(R.id.back_btn);
        radioGroupstatus = findViewById(R.id.radioGroupstatus);
        allContact = findViewById(R.id.allContact);
        hideStatus =findViewById(R.id.hidestatus);
        onlyShareStatus =findViewById(R.id.onlySharewith);
        okStatus = findViewById(R.id.okStatus);
        okStatus.setOnClickListener(this);
        allContact.setOnClickListener(this);
        hideStatus.setOnClickListener(this);
        onlyShareStatus.setOnClickListener(this);
        statusValue = new SessionManager(getApplicationContext()).getStatus();
        if (statusValue.equalsIgnoreCase("1")){
            radioGroupstatus.check(R.id.allContact);
        }
        else    if (statusValue.equalsIgnoreCase("2")){
            radioGroupstatus.check(R.id.hidestatus);
        }
        else    if (statusValue.equalsIgnoreCase("3")){
            radioGroupstatus.check(R.id.onlySharewith);
        }
//        radioGroupstatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch(checkedId){
//                    case R.id.allContact:
//                        new SessionManager(getApplicationContext()).setStatus("1");
//                        break;
//                    case R.id.hidestatus:
//                        new SessionManager(getApplicationContext()).setStatus("2");
//                        Intent intent = new Intent(StatusPrivacyScreen.this,UserListForStatusPrivacyScreen.class);
//                        intent.putExtra("type","Hide from");
//                        intent.putExtra("visibility","hide_from");
//                        startActivity(intent);
//                        break;
//                    case R.id.onlySharewith:
//                        new SessionManager(getApplicationContext()).setStatus("3");
//                        Intent intents = new Intent(StatusPrivacyScreen.this,UserListForStatusPrivacyScreen.class);
//                        intents.putExtra("type","Only Share with");
//                        intents.putExtra("visibility","visible_to");
//                        startActivity(intents);
//                        break;
//                }
//
//            }
//        });
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==back_btn){
            finish();
        }
        else if (v==hideStatus){
            statusValue="2";
                        Intent intent = new Intent(StatusPrivacyScreen.this,UserListForStatusPrivacyScreen.class);
                        intent.putExtra("type","Hide from");
                        intent.putExtra("visibility","hide_from");
                        startActivity(intent);
        }
        else if (v==onlyShareStatus){
            statusValue="3";
                        Intent intents = new Intent(StatusPrivacyScreen.this,UserListForStatusPrivacyScreen.class);
                        intents.putExtra("type","Only Share with");
                        intents.putExtra("visibility","visible_to");
                        startActivity(intents);
        }
        else if (v==allContact){
            statusValue="1";
            changeStatus(new SessionManager(getApplicationContext()).getUserID(),"all","");
        }

        else if (v==okStatus){
            new SessionManager(getApplicationContext()).setStatus(statusValue);

            finish();
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
