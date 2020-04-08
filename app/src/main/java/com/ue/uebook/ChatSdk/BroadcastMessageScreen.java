package com.ue.uebook.ChatSdk;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.ChatSdk.Adapter.BroadcastMessageAdapter;
import com.ue.uebook.ChatSdk.Pojo.Broadcastmessagepojo;
import com.ue.uebook.ChatSdk.Pojo.UserList;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.ImageUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class BroadcastMessageScreen extends AppCompatActivity implements View.OnClickListener ,ImageUtils.ImageAttachmentListener {
    private BroadcastMessageAdapter broadcastMessageAdapter;
    private RecyclerView messageList;
    private TextView broadcast_name;
    private Intent intent;
    private UserList userList;
    private String name,ids,channelId;
    private EmojiconEditText edit_chat_message;
    private SwipeRefreshLayout swipe_refresh_layout;
    private ImageButton    backbtnMessage,button_chat_attachment,button_chat_send ,button_chat_emoji, gallerybtn, videobtn, audiobtn,filebtn;
    EmojIconActions emojIcon;
    private RelativeLayout root_view;
    private File imageurl ,videofile,audioUrl,docfile;
    private int typevalue=0;
    private BottomSheetDialog mBottomSheetDialog;
    private String broadcast_No;
    ImageUtils imageUtils;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 99;
    private static final int REQUEST_PICK_VIDEO = 12;
    private List<Broadcastmessagepojo>broadcastmessageList;
    private Bitmap bitmap;
    private String filePath;
    private String fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_message_screen);
             messageList = findViewById(R.id.messageList);
        broadcast_name = findViewById(R.id.broadcast_name);
        button_chat_emoji = findViewById(R.id.button_chat_emoji);
        edit_chat_message = findViewById(R.id.edit_chat_message);
        button_chat_attachment = findViewById(R.id.button_chat_attachment);
        button_chat_send = findViewById(R.id.button_chat_send);
        backbtnMessage = findViewById(R.id.backbtnMessage);
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
        backbtnMessage.setOnClickListener(this);
        broadcastmessageList = new ArrayList<>();
        button_chat_emoji.setOnClickListener(this);
        button_chat_send.setOnClickListener(this);
        root_view = findViewById(R.id.root_view);
        button_chat_attachment.setOnClickListener(this);
        imageUtils = new ImageUtils(this);
        intent = getIntent();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        messageList.setLayoutManager(linearLayoutManager);
        name = intent.getStringExtra("name");
        ids=intent.getStringExtra("ids");
        broadcast_No = intent.getStringExtra("broadcast_No");
        broadcast_name.setText(name);
        getAllmessage(ids,broadcast_No);
        pullTorefreshswipe();
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
    private void pullTorefreshswipe() {
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onRefresh() {
                getAllmessage(ids,broadcast_No);
                swipe_refresh_layout.setRefreshing(false);
            }
        });
    }
    public void getAllmessage(final String receiver_ids ,final String broadcast_no){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.BaseUrl +"broadcastChatView",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (broadcastmessageList.size()>0)
                            broadcastmessageList.clear();
                        Log.e(" response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            if (jsonObject.getBoolean("error")==false){

                                JSONArray jsonObjectResponse = jsonObject.getJSONArray("broadcast_info");
                                if (jsonObjectResponse!=null){
                                    for (int i=0; i<jsonObjectResponse.length();i++){
                                        Broadcastmessagepojo statusmodel = new Broadcastmessagepojo();
                                        JSONObject rec = jsonObjectResponse.getJSONObject(i);
                                        statusmodel.setMessage(rec.getString("message"));
                                        statusmodel.setId(rec.getString("id"));
                                        statusmodel.setType(rec.getString("type"));
                                        broadcastmessageList.add(statusmodel);
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            broadcastMessageAdapter = new BroadcastMessageAdapter(broadcastmessageList);
                                            messageList.setAdapter(broadcastMessageAdapter);
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

                            Toast.makeText(BroadcastMessageScreen.this, data.optString("message","Something wrong!"), Toast.LENGTH_LONG).show();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> arguments = new HashMap<String, String>();
                arguments.put("send_by",new SessionManager(getApplication()).getUserID());
                arguments.put("receiver_ids",receiver_ids);
                arguments.put("broadcast_no",broadcast_no);
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
    private void getAudioFile() {
        try {
            Intent audioIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(audioIntent, 132);
        } catch (ActivityNotFoundException e) {

        }
    }
    @Override
    public void onClick(View v) {
              if (v==button_chat_send){

                  sendMesaage(new SessionManager(getApplication()).getUserID(),"",ids,"text",channelId,edit_chat_message.getText().toString(),0);
              }
              else if (v==button_chat_attachment){

                  showBottomSheet();
              }else if (v == audiobtn) {
                  typevalue = 3;
                  getAudioFile();
                  mBottomSheetDialog.dismiss();
              } else if (v == gallerybtn) {
                  PackageManager pm = this.getPackageManager();
                  int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, this.getPackageName());
                  if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                      typevalue = 1;
                      mBottomSheetDialog.dismiss();
                      imageUtils.imagepicker(1);
                  } else {
                      ActivityCompat.requestPermissions(this,
                              new String[]{Manifest.permission.CAMERA},
                              MY_PERMISSIONS_REQUEST_CAMERA);
                  }

              } else if (v == videobtn) {
                  typevalue = 2;
                  mBottomSheetDialog.dismiss();
                  try {
                      Intent mediaChooser = new Intent(Intent.ACTION_PICK);
                      mediaChooser.setType("video/*");
                      startActivityForResult(mediaChooser, REQUEST_PICK_VIDEO);
                  } catch (ActivityNotFoundException e) {
                      // Do nothing for now
                  }
              } else if (v == filebtn) {
                  typevalue = 4;
                  openFile(111);
                  mBottomSheetDialog.dismiss();

              }
              else if (v==backbtnMessage)
              {
                  finish();
              }

    }
    private void openFile(int CODE) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "application/pdf", "application/vnd.ms-powerpoint", "text/csv"};
        i.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(i, CODE);
    }

    private void showBottomSheet() {
        final View bottomSheetLayout = getLayoutInflater().inflate(R.layout.bottomlayoutchat, null);
        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(bottomSheetLayout);
        gallerybtn = mBottomSheetDialog.findViewById(R.id.galleryBtn);
        filebtn = mBottomSheetDialog.findViewById(R.id.fileBtn);
        audiobtn = mBottomSheetDialog.findViewById(R.id.audioBtn);
        videobtn = mBottomSheetDialog.findViewById(R.id.videoBtn);
        gallerybtn.setOnClickListener(this);
        filebtn.setOnClickListener(this);
        audiobtn.setOnClickListener(this);
        videobtn.setOnClickListener(this);
        mBottomSheetDialog.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendMesaage(String user_id, String chat_type, String sendTO, String type, final String channelId, String message, int typeval) {
        OkHttpClient client = new OkHttpClient();
        String url = ApiRequest.BaseUrl + "broadcastChat";
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        // showLoadingIndicator();
        RequestBody requestBody;
        switch (typeval) {
            case 0:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(" chat_type", chat_type)
                        .addFormDataPart(" send_by", user_id)
                        .addFormDataPart(" receiver_ids", sendTO)
                        .addFormDataPart(" message_type", type)
                        .addFormDataPart(" message", message)
                        .addFormDataPart(" broadcast_no", broadcast_No)
                        .build();
                break;
            case 1:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(" chat_type", chat_type)
                        .addFormDataPart(" user_id", user_id)
                        .addFormDataPart(" sendTO", sendTO)
                        .addFormDataPart(" type", type)
                        .addFormDataPart(" message", message)
                        .addFormDataPart("image_file", imageurl.getName(), RequestBody.create(MEDIA_TYPE_PNG, imageurl))
                        .addFormDataPart(" broadcast_no", broadcast_No)


                        .build();
                break;
            case 2:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(" chat_type", chat_type)
                        .addFormDataPart(" user_id", user_id)
                        .addFormDataPart(" sendTO", sendTO)
                        .addFormDataPart(" type", type)
                        .addFormDataPart(" message", message)
                        .addFormDataPart("video_file", videofile.getName(), RequestBody.create(MediaType.parse("video/mp4"), videofile))
                        .build();
                break;
            case 3:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(" chat_type", chat_type)
                        .addFormDataPart(" user_id", user_id)
                        .addFormDataPart(" sendTO", sendTO)
                        .addFormDataPart(" type", type)
                        .addFormDataPart(" message", "Audio")
                        .addFormDataPart("audio_file", audioUrl.getName(), RequestBody.create(MediaType.parse("audio/*"), audioUrl))

                        .build();
                break;
            case 4:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(" chat_type", chat_type)
                        .addFormDataPart(" user_id", user_id)
                        .addFormDataPart(" sendTO", sendTO)
                        .addFormDataPart(" type", type)
                        .addFormDataPart(" message", "file")
                        .addFormDataPart("pdf_file", docfile.getName(), RequestBody.create(MediaType.parse("text/csv"), docfile))
                        .build();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + 1);
        }
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                Log.e("chatresponse", myResponse);
            //    final ChatResponse form = gson.fromJson(myResponse, ChatResponse.class);
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        edit_chat_message.setText("");
                        typevalue = 0;
                        getAllmessage(ids,broadcast_No);
                    }
                });
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageUtils.request_permission_result(requestCode, permissions, grantResults);
    }
    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        this.bitmap = file;
        this.fileName = filename;
        String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageUtils.createImage(file, filename, path, false);
        filePath = getRealPathFromURI(uri.getPath());
        imageurl = (new File(imageUtils.getPath(uri)));
    }
    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = this.getApplicationContext().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }
}
