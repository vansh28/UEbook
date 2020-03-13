package com.ue.uebook.ChatSdk;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;

import java.io.IOException;
import java.util.List;

public class StatusPrivacyScreen extends BaseActivity implements View.OnClickListener {
    private ImageButton back_btn;
    private RadioGroup radioGroupstatus;
    private Intent intent;
    private List<String>userIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_privacy_screen);
        back_btn = findViewById(R.id.back_btn);
        radioGroupstatus = findViewById(R.id.radioGroupstatus);


        radioGroupstatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.allContact:

                        break;
                    case R.id.hidestatus:
                        Intent intent = new Intent(StatusPrivacyScreen.this,UserListForStatusPrivacyScreen.class);
                        intent.putExtra("type","Hide from");
                        intent.putExtra("visibility","hide_from");
                        startActivity(intent);
                        break;
                    case R.id.onlySharewith:
                        Intent intents = new Intent(StatusPrivacyScreen.this,UserListForStatusPrivacyScreen.class);
                        intents.putExtra("type","Only Share with");
                        intents.putExtra("visibility","visible_to");
                        startActivity(intents);
                        break;
                }

            }
        });
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==back_btn){
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
