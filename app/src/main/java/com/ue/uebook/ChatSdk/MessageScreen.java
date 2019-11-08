package com.ue.uebook.ChatSdk;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.ChatSdk.Adapter.MessageAdapter;
import com.ue.uebook.ChatSdk.Pojo.ChatResponse;
import com.ue.uebook.ChatSdk.Pojo.OponentData;
import com.ue.uebook.ChatSdk.Pojo.UserData;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.FilePath;
import com.ue.uebook.FileUtil;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.ImageUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MessageScreen extends BaseActivity implements View.OnClickListener ,ImageUtils.ImageAttachmentListener{
    private static final int REQUEST_PICK_VIDEO =1 ;
    private Intent intent;
    private ImageButton back_btn, button_chat_attachment, morebtn, button_chat_send ,gallerybtn,videobtn,audiobtn,filebtn;
    private ImageView userProfile ;
    private RecyclerView messageList;
    private EditText chat_message;
    private TextView oponent_name;
    private int screenID;
    private OponentData oponentData;
    private UserData userData;
    private File videofile,audioUrl,docfile;
    private MessageAdapter messageAdapter;
    private String filePath;
    private String fileName;
    private File  imageurl;
    private Bitmap bitmap;
    ImageUtils imageUtils;
    private String chanelID="";
    private int typevalue=0;            //gallery=1,video=2,audio=3,doc=4//
    private BottomSheetDialog mBottomSheetDialog;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_screen);
        back_btn = findViewById(R.id.backbtnMessage);
        oponent_name=findViewById(R.id.oponent_name);
        intent = getIntent();
        imageUtils = new ImageUtils(this);
        screenID=intent.getIntExtra("id",0);
        userProfile = findViewById(R.id.image_user_chat);
        chat_message = findViewById(R.id.edit_chat_message);
        messageList = findViewById(R.id.messageList);
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        messageList.setLayoutManager(linearLayoutManagerPopularList);
        button_chat_attachment = findViewById(R.id.button_chat_attachment);
        morebtn = findViewById(R.id.morebtn);
        button_chat_send = findViewById(R.id.button_chat_send);
        morebtn.setOnClickListener(this);
        button_chat_send.setOnClickListener(this);
        button_chat_attachment.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        userProfile.setOnClickListener(this);
        if (screenID==2){
            oponentData= (OponentData) intent.getSerializableExtra("oponentdata");
            userData= (UserData) intent.getSerializableExtra("userData");
            if (oponentData!=null){
                oponent_name.setText(oponentData.getName());
                GlideUtils.loadImage(MessageScreen.this, "http://dnddemo.com/ebooks/api/v1/upload/" + oponentData.getUrl(), userProfile, R.drawable.user_default, R.drawable.user_default);

            }
            if (oponentData.channelId!=null){
                chanelID=oponentData.channelId;
                getChatHistory(new SessionManager(getApplication()).getUserID(),oponentData.userId,chanelID,"");

            }

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if (v == back_btn) {
            finish();
        } else if (v == userProfile) {
            imagePreview(oponentData.getUrl());
        } else if (v == button_chat_attachment) {
                   showBottomSheet();
        } else if (v == morebtn) {
            showPopupmenu();
        } else if (v == button_chat_send) {
            if (typevalue==0){
                sendMesaage(new SessionManager(getApplication()).getUserID(),"",oponentData.userId,"text",chanelID,chat_message.getText().toString(),0);
            }
            else  if (typevalue==1){
                sendMesaage(new SessionManager(getApplication()).getUserID(),"",oponentData.userId,"image",chanelID,chat_message.getText().toString(),1);
            }
            else  if (typevalue==2){
                sendMesaage(new SessionManager(getApplication()).getUserID(),"",oponentData.userId,"video",chanelID,chat_message.getText().toString(),2);
            }
            else  if (typevalue==3){
                sendMesaage(new SessionManager(getApplication()).getUserID(),"",oponentData.userId,"audio",chanelID,chat_message.getText().toString(),3);
            }
            else  if (typevalue==4){
                sendMesaage(new SessionManager(getApplication()).getUserID(),"",oponentData.userId,"docfile",chanelID,chat_message.getText().toString(),4);
            }
        }
        else if (v==audiobtn){
            typevalue=3;
            getAudioFile();
            mBottomSheetDialog.dismiss();
        }
        else if (v==gallerybtn){
            typevalue=1;
            mBottomSheetDialog.dismiss();
           imageUtils.imagepicker(1);
        }
        else if (v==videobtn){
            typevalue=2;
            mBottomSheetDialog.dismiss();
            try {
                Intent mediaChooser = new Intent(Intent.ACTION_PICK);
                mediaChooser.setType("video/*");
                startActivityForResult(mediaChooser, REQUEST_PICK_VIDEO);
            } catch (ActivityNotFoundException e) {
                // Do nothing for now
            }
        }
        else if (v==filebtn){
            typevalue=4;
          openFile(111);
            mBottomSheetDialog.dismiss();

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageUtils.request_permission_result(requestCode, permissions, grantResults);
    }
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        this.bitmap = file;
        this.fileName = filename;
        String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageUtils.createImage(file, filename, path, false);
        filePath = getRealPathFromURI(uri.getPath());
        imageurl = (new File(imageUtils.getPath(uri)));

    }
    private void getAudioFile() {
        try {
            Intent audioIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(audioIntent, 12);
        } catch (ActivityNotFoundException e) {

        }
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
    private void openFile(int CODE) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "application/pdf", "application/vnd.ms-powerpoint", "text/csv"};
        i.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(i, CODE);
    }

    private void imagePreview(String file) {
        final Dialog previewDialog = new Dialog(this);
        previewDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        previewDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout
                , null));
        ImageView imageView = previewDialog.findViewById(R.id.image_view);
        GlideUtils.loadImage(MessageScreen.this, "http://dnddemo.com/ebooks/api/v1/upload/" + file, imageView, R.drawable.user_default, R.drawable.user_default);
        Button ok_Btn = previewDialog.findViewById(R.id.buton_ok);
        ok_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewDialog.dismiss();

            }
        });
        previewDialog.show();
    }
    private void showPopupmenu() {
        PopupMenu popup = new PopupMenu(MessageScreen.this, morebtn);
        popup.getMenuInflater()
                .inflate(R.menu.chatmoremenu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });
        popup.show();
    }
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void sendMesaage(String  user_id, String tokenKey, String sendTO, String type, final String channelId, String message) {
//        ApiRequest request = new ApiRequest();
//        showLoadingIndicator();
//        request.requestforChat( user_id,tokenKey,sendTO,type,channelId,message,new okhttp3.Callback() {
//            @Override
//            public void onFailure(okhttp3.Call call, IOException e) {
//                Log.d("error", "error");
//                hideLoadingIndicator();
//            }
//            @Override
//            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
//                hideLoadingIndicator();
//                final String myResponse = response.body().string();
//                Gson gson = new GsonBuilder().create();
//                final ChatResponse form = gson.fromJson(myResponse, ChatResponse.class);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        chat_message.setText("");
//                        getChatHistory(new SessionManager(getApplication()).getUserID(),oponentData.userId,oponentData.channelId,"text");
//                    }
//                });
//            }
//        });
//    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getChatHistory(String  user_id,String sendTO,String channelId,String type) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforgetChathistory( user_id,sendTO,channelId,type,new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                hideLoadingIndicator();
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final ChatResponse form = gson.fromJson(myResponse, ChatResponse.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (form.getChat_list()!=null){
                            messageAdapter = new MessageAdapter(MessageScreen.this,form.getChat_list(),new SessionManager(getApplication()).getUserID());
                            messageList.setAdapter(messageAdapter);
                            messageList.scrollToPosition(form.getChat_list().size() - 1);
                            messageAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }
    private void showBottomSheet() {
        final View bottomSheetLayout = getLayoutInflater().inflate(R.layout.bottomlayoutchat, null);

        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(bottomSheetLayout);
        gallerybtn=mBottomSheetDialog.findViewById(R.id.galleryBtn);
        filebtn=mBottomSheetDialog.findViewById(R.id.fileBtn);
        audiobtn=mBottomSheetDialog.findViewById(R.id.audioBtn);
        videobtn=mBottomSheetDialog.findViewById(R.id.videoBtn);
        gallerybtn.setOnClickListener(this);
        filebtn.setOnClickListener(this);
        audiobtn.setOnClickListener(this);
       videobtn.setOnClickListener(this);
       mBottomSheetDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_PICK_VIDEO) {
                Uri selectedVideo = data.getData();
                try {
                    String videoPathStr = getPath(selectedVideo);
                    videofile = new File(videoPathStr);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Please Select Again", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == 111) {
                Uri selectedfile = data.getData();

                try {
                    docfile = FileUtil.from(MessageScreen.this, selectedfile);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Please Select Again", Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == 12) {

                Uri selectedaudio = data.getData();
                try {
                    String audioPathStr = FilePath.getPath(MessageScreen.this, selectedaudio);
                    audioUrl = new File(audioPathStr);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Please Select Again", Toast.LENGTH_SHORT).show();
                }
            }
            else if (requestCode==123){


            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private void sendMesaage(String  user_id, String tokenKey, String sendTO, String type ,final String channelId, String message,int typeval) {
        OkHttpClient client = new OkHttpClient();
        String url="http://dnddemo.com/ebooks/api/v1/user_chat";
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        RequestBody requestBody;
        switch (typeval) {
            case 0:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(" tokenKey",  tokenKey)
                        .addFormDataPart(" user_id",  user_id)
                        .addFormDataPart(" sendTO",  sendTO)
                        .addFormDataPart(" type",  type)
                        .addFormDataPart(" channelId",  channelId)
                        .addFormDataPart(" message",  message)
                        .build();
                break;
            case 1:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(" tokenKey",  tokenKey)
                        .addFormDataPart(" user_id",  user_id)
                        .addFormDataPart(" sendTO",  sendTO)
                        .addFormDataPart(" type",  type)
                        .addFormDataPart(" channelId",  channelId)
                        .addFormDataPart(" message",  message)
                        .addFormDataPart("image_file", imageurl.getName(), RequestBody.create(MEDIA_TYPE_PNG, imageurl))

                        .build();
                break;
            case 2:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(" tokenKey",  tokenKey)
                        .addFormDataPart(" user_id",  user_id)
                        .addFormDataPart(" sendTO",  sendTO)
                        .addFormDataPart(" type",  type)
                        .addFormDataPart(" channelId",  channelId)
                        .addFormDataPart(" message",  message)
                        .addFormDataPart("video_file", videofile.getName(), RequestBody.create(MediaType.parse("video/mp4"), videofile))
                        .build();
                break;
            case 3:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(" tokenKey",  tokenKey)
                        .addFormDataPart(" user_id",  user_id)
                        .addFormDataPart(" sendTO",  sendTO)
                        .addFormDataPart(" type",  type)
                        .addFormDataPart(" channelId",  channelId)
                        .addFormDataPart(" message",  "Audio")
                        .addFormDataPart("audio_file", audioUrl.getName(), RequestBody.create(MediaType.parse("audio/*"), audioUrl))

                        .build();
                break;
            case 4:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(" tokenKey",  tokenKey)
                        .addFormDataPart(" user_id",  user_id)
                        .addFormDataPart(" sendTO",  sendTO)
                        .addFormDataPart(" type",  type)
                        .addFormDataPart(" channelId",  channelId)
                        .addFormDataPart(" message",  "file")
                        .addFormDataPart("pdf_file", docfile.getName(), RequestBody.create(MediaType.parse("text/csv"), docfile))
                        .build();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + 1);
        }
        Request request = new Request.Builder()
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
                hideLoadingIndicator();
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final ChatResponse form = gson.fromJson(myResponse, ChatResponse.class);
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        chat_message.setText("");
                        chanelID=form.getChat_list().get(0).getChannelId();
                        getChatHistory(new SessionManager(getApplication()).getUserID(),oponentData.userId,chanelID,"text");
                    }
                });
            }
        });
    }


}

