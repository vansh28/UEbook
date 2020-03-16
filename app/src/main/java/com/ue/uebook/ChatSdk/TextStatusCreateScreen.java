package com.ue.uebook.ChatSdk;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class TextStatusCreateScreen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton changeBackground ,button_chat_emoji ,textStyleChanges;
    private EmojiconEditText textStatus;
    private String colorArray []={"#4B0082","#FFA07A","#FFA500","#00FF00","#008B8B"};
    private int pos=0;
    private RelativeLayout layout;
    EmojIconActions emojIcon;
    private int fontPos=0;
    private FloatingActionButton sendStatusToServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_status_create_screen);
        changeBackground=findViewById(R.id.changeBackground);
        button_chat_emoji = findViewById(R.id.button_chat_emoji);
        textStyleChanges = findViewById(R.id.textStyleChanges);
        sendStatusToServer = findViewById(R.id.sendStatusToServer);
        sendStatusToServer.setOnClickListener(this);
        textStyleChanges.setOnClickListener(this);
        changeBackground.setOnClickListener(this);
        layout = findViewById(R.id.layout);
        layout.setOnClickListener(this);
        textStatus = findViewById(R.id.textStatus);
        emojIcon = new EmojIconActions(this, layout, textStatus, button_chat_emoji);
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
        textStatus.addTextChangedListener(new TextWatcher() {
            @SuppressLint("RestrictedApi")
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0)
                {
                    sendStatusToServer.setVisibility(View.VISIBLE);
                }
                else {
                    sendStatusToServer.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v==changeBackground){
            if (pos==colorArray.length-1){
                pos=0;
                layout.setBackgroundColor(Color.parseColor(colorArray[0]));

            }
            else {
                pos++;
                layout.setBackgroundColor(Color.parseColor(colorArray[pos]));

            }

        }
        else if (v==textStyleChanges){
            if (fontPos==0){
                textStatus.setTypeface(null, Typeface.BOLD);
                fontPos++;
            }
            else if (fontPos==1){
                textStatus.setTypeface(null, Typeface.ITALIC);
                fontPos++;
            }
            else if (fontPos==2){
                textStatus.setTypeface(null, Typeface.BOLD_ITALIC);
                fontPos++;
            }
            else if (fontPos==3){
                textStatus.setTypeface(null, Typeface.NORMAL);
                fontPos=0;
            }
        }

        else if (v==sendStatusToServer){
            if (textStatus.getText().toString().isEmpty()){
                textStatus.setError("Enter your Status");
                textStatus.requestFocus();
                textStatus.setEnabled(true);
            }
            else {
                        UploadStatus(new SessionManager(getApplication()).getUserID(),"text",textStatus.getText().toString());
            }
        }

    }

    public void UploadStatus(final String userID ,final  String message_type ,final  String message ){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.testBaseUrl+"userstatus/addUserChatStatus",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e(" response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("error")==false) {
                                finish();
                            }
                            Toast.makeText(TextStatusCreateScreen.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                            Toast.makeText(TextStatusCreateScreen.this, data.optString("message","Something wrong!"), Toast.LENGTH_LONG).show();
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
                arguments.put("message_type",message_type);
                arguments.put("message",message);
                return arguments;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TextStatusCreateScreen.this);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

}
