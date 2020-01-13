package com.ue.uebook.ChatSdk;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
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
import com.ue.uebook.DownloadService;
import com.ue.uebook.FilePath;
import com.ue.uebook.FileUtil;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.ImageUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;
import com.ue.uebook.VideoSdk.VideoCall;
import com.ue.uebook.WebviewScreen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MessageScreen extends BaseActivity implements View.OnClickListener, ImageUtils.ImageAttachmentListener, MessageAdapter.ChatImageFileClick {
    private static final int REQUEST_PICK_VIDEO = 12;
    private Intent intent;
    private ImageButton back_btn, button_chat_attachment, morebtn, button_chat_send, gallerybtn, videobtn, audiobtn, filebtn;
    private ImageView userProfile,previewImage;
    private RecyclerView messageList;
    private EditText chat_message;
    private TextView oponent_name;
    private int screenID;
    private int mCurrentPosition = 0;
    private OponentData oponentData;
    private UserData userData;
    private File videofile, audioUrl, docfile;
    private MessageAdapter messageAdapter;
    private String filePath;
    private String fileName;
    private File imageurl;
    private Bitmap bitmap;
    ImageUtils imageUtils;
    public String chanelID = "";
    private int typevalue = 0;
    IntentFilter filter;
    private String sendToID;
    //gallery=1,video=2,audio=3,doc=4//
    private BottomSheetDialog mBottomSheetDialog;
    BroadcastReceiver receiver;
    private String imageUrl="df";
    public String imageProfile="c";
    private ProgressDialog progressDialog;
    private BroadcastReceiver mReceiver;
    private String channelID="";
    private VideoView videoview;
    private ProgressDialog mProgressDialog;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 99;
    private AsyncTask mMyTask;
    String docbaseUrl="http://docs.google.com/gview?embedded=true&url=";
    ProgressBar pb;
    Dialog dialog;
    int downloadedSize = 0;
    int totalSize = 0;
    TextView cur_val;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_screen);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        // Progress dialog horizontal style
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Progress dialog title
        mProgressDialog.setTitle("Downloading");
        // Progress dialog message
        mProgressDialog.setMessage("Please wait, we are downloading your image file...");
        back_btn = findViewById(R.id.backbtnMessage);
        oponent_name = findViewById(R.id.oponent_name);
        previewImage=findViewById(R.id.previewImage);
        intent = getIntent();
        videoview = findViewById(R.id.videoview);
        imageUtils = new ImageUtils(this);
        screenID = intent.getIntExtra("id", 0);
        imageUrl=intent.getStringExtra("imageUrl");
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
        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(videoview);
        videoview.setMediaController(controller);
        if (screenID == 2) {
            oponentData = (OponentData) intent.getSerializableExtra("oponentdata");
            userData = (UserData) intent.getSerializableExtra("userData");
            if (oponentData != null) {
                oponent_name.setText(oponentData.getName());
                sendToID=oponentData.userId;
                oponent_name.setText(oponentData.getName());
                GlideUtils.loadImage(MessageScreen.this, "http://dnddemo.com/ebooks/api/v1/upload/" + oponentData.getUrl(), userProfile, R.drawable.user_default, R.drawable.user_default);
                imageProfile=oponentData.getUrl();
            }
            if (oponentData.channelId != null) {
                chanelID = oponentData.channelId;
                sendToID=oponentData.userId;
                getChatHistory(new SessionManager(getApplication()).getUserID(), sendToID, chanelID, "text");
            }
        }
        else if (screenID==1){
            String sendTo=intent.getStringExtra("sendTo");
             channelID=intent.getStringExtra("channelID");
            String name=intent.getStringExtra("name");
            oponent_name.setText(name);
            sendToID=sendTo;
            chanelID=channelID;
            getChatHistory(new SessionManager(getApplication()).getUserID(), sendToID, channelID, "text");
            GlideUtils.loadImage(MessageScreen.this, "http://dnddemo.com/ebooks/api/v1/upload/" + imageUrl, userProfile, R.drawable.user_default, R.drawable.user_default);
            imageProfile=imageUrl;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if (v == back_btn) {
            Intent intent = new Intent(this,ChatListScreen.class);
            startActivity(intent);
            finish();
        } else if (v == userProfile) {


            imagePreview(imageUrl);
        } else if (v == button_chat_attachment) {
            showBottomSheet();
        } else if (v == morebtn) {
            showPopupmenu();
        } else if (v == button_chat_send) {
            if (typevalue == 0) {
//                Intent intent=new Intent("com.local.receiver");
//                sendBroadcast(intent);
                if (chat_message.getText().toString().isEmpty()) {
                       chat_message.setError("Enter your Message");
                       chat_message.requestFocus();
                } else {
                    sendMesaage(new SessionManager(getApplication()).getUserID(), "", sendToID, "text", chanelID, chat_message.getText().toString(), 0);

                }
            } else if (typevalue == 1) {
                previewImage.setVisibility(View.GONE);
                sendMesaage(new SessionManager(getApplication()).getUserID(), "", sendToID, "image", chanelID, chat_message.getText().toString(), 1);
            } else if (typevalue == 2) {
                if (videofile!=null){

                    videoview.setVisibility(View.GONE);
                    sendMesaage(new SessionManager(getApplication()).getUserID(), "", sendToID, "video", chanelID, chat_message.getText().toString(), 2);
                }
                else {
                    Toast.makeText(this, "Please Select Again", Toast.LENGTH_SHORT).show();
                }
            } else if (typevalue == 3) {
//                sendMesaage(new SessionManager(getApplication()).getUserID(), "", sendToID, "audio", chanelID, chat_message.getText().toString(), 3);
            } else if (typevalue == 4) {

//                sendMesaage(new SessionManager(getApplication()).getUserID(), "", sendToID, "docfile", chanelID, chat_message.getText().toString(), 4);
            }
        } else if (v == audiobtn) {
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
            }
            else {
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
        previewImage.setVisibility(View.VISIBLE);
        previewImage.setImageBitmap(file);

    }

    private void getAudioFile() {
        try {
            Intent audioIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(audioIntent, 132);
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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.videoCall:
                       Intent intent = new Intent(MessageScreen.this, VideoCall.class);
                       intent.putExtra("id",channelID);
                        intent.putExtra("receiverid",sendToID);
                       startActivity(intent);
                       return true;
                    case R.id.clearChat:
                         clearChatHistory(new SessionManager(getApplicationContext()).getUserID(),sendToID);
                        return true;

                    default:
                        return false;
                }
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
    private void getChatHistory(String user_id, String sendTO, String channelId, String type) {
        ApiRequest request = new ApiRequest();

        request.requestforgetChathistory(user_id, sendTO, channelId, type, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");

            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final ChatResponse form = gson.fromJson(myResponse, ChatResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (form.getChat_list() != null) {
                            messageAdapter = new MessageAdapter(MessageScreen.this, form.getChat_list(), new SessionManager(getApplication()).getUserID());
                            messageList.setAdapter(messageAdapter);
                            messageList.scrollToPosition(form.getChat_list().size() - 1);
                            messageAdapter.notifyDataSetChanged();
                            messageAdapter.setItemClickListener(MessageScreen.this);
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
                    initializePlayer(selectedVideo);
                    videoview.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Please Select Again", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == 111) {
                Uri selectedfile = data.getData();

                try {
                    docfile = FileUtil.from(MessageScreen.this, selectedfile);
                    sendMesaage(new SessionManager(getApplication()).getUserID(), "", sendToID, "docfile", chanelID, chat_message.getText().toString(), 4);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Please Select Again", Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == 132) {

                Uri selectedaudio = data.getData();
                try {
                    String audioPathStr = FilePath.getPath(MessageScreen.this, selectedaudio);
                    audioUrl = new File(audioPathStr);
                    sendMesaage(new SessionManager(getApplication()).getUserID(), "", sendToID, "audio", chanelID, chat_message.getText().toString(), 3);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Please Select Again", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == 123) {


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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendMesaage(String user_id, String tokenKey, String sendTO, String type, final String channelId, String message, int typeval) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://dnddemo.com/ebooks/api/v1/user_chat";
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        showLoadingIndicator();
        RequestBody requestBody;
        switch (typeval) {
            case 0:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(" tokenKey", tokenKey)
                        .addFormDataPart(" user_id", user_id)
                        .addFormDataPart(" sendTO", sendTO)
                        .addFormDataPart(" type", type)
                        .addFormDataPart(" channelId", channelId)
                        .addFormDataPart(" message", message)
                        .build();
                break;
            case 1:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(" tokenKey", tokenKey)
                        .addFormDataPart(" user_id", user_id)
                        .addFormDataPart(" sendTO", sendTO)
                        .addFormDataPart(" type", type)
                        .addFormDataPart(" channelId", channelId)
                        .addFormDataPart(" message", message)
                        .addFormDataPart("image_file", imageurl.getName(), RequestBody.create(MEDIA_TYPE_PNG, imageurl))

                        .build();
                break;
            case 2:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(" tokenKey", tokenKey)
                        .addFormDataPart(" user_id", user_id)
                        .addFormDataPart(" sendTO", sendTO)
                        .addFormDataPart(" type", type)
                        .addFormDataPart(" channelId", channelId)
                        .addFormDataPart(" message", message)
                        .addFormDataPart("video_file", videofile.getName(), RequestBody.create(MediaType.parse("video/mp4"), videofile))
                        .build();
                break;
            case 3:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(" tokenKey", tokenKey)
                        .addFormDataPart(" user_id", user_id)
                        .addFormDataPart(" sendTO", sendTO)
                        .addFormDataPart(" type", type)
                        .addFormDataPart(" channelId", channelId)
                        .addFormDataPart(" message", "Audio")
                        .addFormDataPart("audio_file", audioUrl.getName(), RequestBody.create(MediaType.parse("audio/*"), audioUrl))

                        .build();
                break;
            case 4:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(" tokenKey", tokenKey)
                        .addFormDataPart(" user_id", user_id)
                        .addFormDataPart(" sendTO", sendTO)
                        .addFormDataPart(" type", type)
                        .addFormDataPart(" channelId", channelId)
                        .addFormDataPart(" message", "file")
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
                Log.e("chatresponse",myResponse);
                final ChatResponse form = gson.fromJson(myResponse, ChatResponse.class);
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        chat_message.setText("");
                        typevalue=0;
                        chanelID = form.getChat_list().get(0).getChannelId();
                        getChatHistory(new SessionManager(getApplication()).getUserID(), sendToID, chanelID, "text");
                    }
                });
            }
        });
    }


    @Override
    public void onImageClick(String url ,String type) {
//        Log.d("shdjsjhd", url);
    if (type.equalsIgnoreCase("image"))
    {
        showfullImage(url);
//        mMyTask = new DownloadTask()

    }

    else   if (type.equalsIgnoreCase("video")){
        Intent intent = new Intent(this,VideoScreen.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }
    else   if (type.equalsIgnoreCase("audio")){
        gotoWebview(url);
    }
    else   if (type.equalsIgnoreCase("file")){
        gotoWebview(docbaseUrl+url);
    }

//        Intent intent = new Intent(this, DownloadService.class);
//        intent.putExtra("url", "https://images5.alphacoders.com/609/609173.jpg");
//        intent.putExtra("receiver", new DownloadReceiver(new Handler()));
//        startService(intent);


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onDownloadClick(final String url, String type , final String name) {
//
//
        showProgress(url);
        new Thread(new Runnable() {
            public void run() {
                downloadFiles(url , name);
            }
        }).start();
    }

    private void showfullImage(String url) {
        final Dialog nagDialog = new Dialog(MessageScreen.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        nagDialog.setCancelable(false);
        nagDialog.setContentView(R.layout.fullimageitem);
        ImageView ivPreview = nagDialog.findViewById(R.id.image);
        ImageView cancel_btn = nagDialog.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nagDialog.dismiss();
            }
        });
        GlideUtils.loadImage(MessageScreen.this, url, ivPreview, R.drawable.noimage, R.drawable.noimage);
        nagDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");

        mReceiver = new BroadcastReceiver() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                String msg_for_me = intent.getStringExtra("some_msg");
                //log our message value
                Log.i("InchooTutorial", msg_for_me);
                getChatHistory(new SessionManager(getApplication()).getUserID(), sendToID, channelID, "text");
            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //unregister our receiver
        this.unregisterReceiver(this.mReceiver);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,ChatListScreen.class);
        startActivity(intent);
        finish();
    }
    private class DownloadReceiver extends ResultReceiver {

        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            super.onReceiveResult(resultCode, resultData);

            if (resultCode == DownloadService.UPDATE_PROGRESS) {

                int progress = resultData.getInt("progress"); //get the progress

                progressDialog.setProgress(progress);
                progressDialog.setMessage("Images Is Downloading");
                progressDialog.show();

                if (progress == 100) {
                    progressDialog.dismiss();
                }
            }
        }
    }
    private void initializePlayer(Uri uri) {
        if (uri != null) {
            videoview.setVideoURI(uri);
        }
        videoview.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        if (mCurrentPosition > 0) {
                            videoview.seekTo(mCurrentPosition);
                        }
                        else {
                            videoview.seekTo(1);
                        }
                        // Start playing!
                        videoview.start();
                    }
                });
    }
    private void releasePlayer() {
        videoview.stopPlayback();
    }

    private  void  gotoWebview(String url){
        Intent intent = new Intent(this, WebviewScreen.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)


          public  void downloadFiles(String dwnload_file_path ,String name){

        try {
            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            //connect
            urlConnection.connect();

            //set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            //create a new file, to save the downloaded file
            File file = new File(SDCardRoot,name);

            FileOutputStream fileOutput = new FileOutputStream(file);

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();

            runOnUiThread(new Runnable() {
                public void run() {
                    pb.setMax(totalSize);
                }
            });

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //
                runOnUiThread(new Runnable() {
                    public void run() {
                        pb.setProgress(downloadedSize);
                        float per = ((float)downloadedSize/totalSize) * 100;
                        cur_val.setText("Downloaded " + downloadedSize + "KB / " + totalSize + "KB (" + (int)per + "%)" );

                    }
                });
            }
            //close the output stream when complete //
            fileOutput.close();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(MessageScreen.this,"Download Successfully",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                      // if you want close it..
                }
            });

        } catch (final MalformedURLException e) {
            showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            showError("Error : IOException " + e);
            e.printStackTrace();
        }
        catch (final Exception e) {
            showError("Error : Please check your internet connection " + e);
        }
    }

    void showError(final String err){
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MessageScreen.this, err, Toast.LENGTH_LONG).show();
            }
        });
    }

   public void showProgress(String file_path){
        dialog = new Dialog(MessageScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setTitle("Download Progress");
        TextView text = (TextView) dialog.findViewById(R.id.tv1);
        text.setText("Downloading file from ... " + file_path);
        cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText("Starting download...");
        dialog.show();
        pb = (ProgressBar)dialog.findViewById(R.id.progress_bar);
        pb.setIndeterminate(true);
//        pb.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void clearChatHistory(String user_id ,String receiver) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforgetClearChatHistory(user_id, receiver,new okhttp3.Callback() {
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getChatHistory(new SessionManager(getApplication()).getUserID(), sendToID, channelID, "text");

                    }
                });
//                final AllchatResponse form = gson.fromJson(myResponse, AllchatResponse.class);
            }
        });
    }
}
