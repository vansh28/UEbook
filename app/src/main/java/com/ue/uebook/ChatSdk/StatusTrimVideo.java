package com.ue.uebook.ChatSdk;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import io.github.lizhangqu.coreprogress.ProgressHelper;
import io.github.lizhangqu.coreprogress.ProgressUIListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StatusTrimVideo extends BaseActivity implements View.OnClickListener {
    private ImageView status_image ,backbtn;
    private Intent intent;
    private String type;
    private String file;
    private RelativeLayout root_view;
    private ImageButton button_chat_emoji,button_status_send ,playvideo;
    private EmojiconEditText edit_chat_message;
    EmojIconActions emojIcon;
    final int PIC_CROP = 1;
    private ImageButton button_upload_send;
    private Bitmap bmp;
    private VideoView videoview;
    private boolean isplaying =false;
    private boolean isImage = true;
    private File videoFile ,imageFile;
    private ProgressDialog progressdialog;
    private ProgressBar progress_horizontal;
    private RelativeLayout progress_layout;
    private TextView textViewProgress;
    int status = 0;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_trim_video);
        status_image = findViewById(R.id.status_image);
        progress_horizontal = findViewById(R.id.progress_horizontal);
        root_view = findViewById(R.id.root_view);
        progress_layout = findViewById(R.id.progress_layout);
        textViewProgress=findViewById(R.id.value123);
        button_chat_emoji= findViewById(R.id.button_chat_emoji);
        button_status_send = findViewById(R.id.button_chat_send);
        button_status_send.setOnClickListener(this);
        button_chat_emoji.setOnClickListener(this);
        videoview =findViewById(R.id.videoview);
        edit_chat_message = findViewById(R.id.edit_chat_message);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(this);
        intent = getIntent();

        playvideo = findViewById(R.id.playvideo);
        playvideo.setOnClickListener(this);
        CreateProgressDialog();
        type = intent.getStringExtra("type");
        if (type.equalsIgnoreCase("image")){
            file = intent.getStringExtra("file");
            if(file!=null)
            {
                isImage = true;
                imageFile = new File(file);
                status_image.setVisibility(View.VISIBLE);
                videoview.setVisibility(View.GONE);
                bmp = (BitmapFactory.decodeFile(file));
                status_image.setImageBitmap(bmp);
            }
        }
        else if (type.equalsIgnoreCase("Gallaryimage")){
            isImage = true;
            file = intent.getStringExtra("file");
            imageFile = new File(file);
            status_image.setVisibility(View.VISIBLE);
            videoview.setVisibility(View.GONE);
            bmp = (BitmapFactory.decodeFile(file));
            status_image.setImageBitmap(bmp);
        }

        else if (type.equalsIgnoreCase("Gallaryvideo"))
        {
            isImage = false;
            playvideo.setVisibility(View.VISIBLE);
            playvideo.setBackgroundResource(R.drawable.play);
            file = intent.getStringExtra("file");
             videoFile = new File(file);
             Uri uri = Uri.parse(file);
             videoview.setVideoURI(uri);
             videoview.requestFocus();
            status_image.setVisibility(View.GONE);
            videoview.setVisibility(View.VISIBLE);
        }

        else if (type.equalsIgnoreCase("video")){
            isImage = false;
            playvideo.setVisibility(View.VISIBLE);
            playvideo.setBackgroundResource(R.drawable.play);
            file = intent.getStringExtra("file");
            Uri uri = Uri.parse(file);
            videoview.setVideoURI(uri);
            videoview.requestFocus();
            status_image.setVisibility(View.GONE);
            videoview.setVisibility(View.VISIBLE);
            try {
                String videoPathStr = getRealPathFromURI(uri);
                videoFile = new File(videoPathStr);
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Please Select Again", Toast.LENGTH_SHORT).show();
            }
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

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playvideo.setBackgroundResource(R.drawable.play);
                isplaying=false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v==backbtn){
            finish();
        }
        else if (v==button_status_send){
            if (isImage){
                requestforUploadStatus(1,new SessionManager(getApplicationContext()).getUserID(),edit_chat_message.getText().toString());
            }
            else
                {
                    requestforUploadStatus(2,new SessionManager(getApplicationContext()).getUserID(),edit_chat_message.getText().toString());

              //  addVideoToStatus(new SessionManager(getApplicationContext()).getUserID(), videoFile,edit_chat_message.getText().toString(),"video");
            }
        }
        else if (v==playvideo){
               if (isplaying){
                   playvideo.setBackgroundResource(R.drawable.play);
                   videoview.pause();
                   isplaying=false;
               }
               else {
                   playvideo.setBackgroundResource(R.drawable.pause);
                   videoview.start();
                   isplaying=true;
               }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                status_image.setImageBitmap(thumbnail);
            }
        }
    }




    public int getOrientation(Uri selectedImage) {
        int orientation = 0;
        final String[] projection = new String[]{MediaStore.Images.Media.ORIENTATION};
        final Cursor cursor = getContentResolver().query(selectedImage, projection, null, null, null);
        if(cursor != null) {
            final int orientationColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);
            if(cursor.moveToFirst()) {
                orientation = cursor.isNull(orientationColumnIndex) ? 0 : cursor.getInt(orientationColumnIndex);
            }
            cursor.close();
        }
        return orientation;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public void requestforUploadStatus(final  int type,final  String user_id, final  String caption) {
        OkHttpClient client = new OkHttpClient();
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        RequestBody requestBody;
        switch (type) {
            case 1:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("user_id",user_id)
                        .addFormDataPart("msg_type","image" )
                        .addFormDataPart("image_file", imageFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, imageFile))
                        .addFormDataPart("caption",caption)
                        .build();
                break;
            case 2:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("user_id",user_id)
                        .addFormDataPart("msg_type","video" )
                        .addFormDataPart("video_file", videoFile.getName(), RequestBody.create(MediaType.parse("video/mp4"), videoFile))
                        .addFormDataPart("caption",caption)
                        .build();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + 1);
        }
        RequestBody requestBodys = ProgressHelper.withProgress(requestBody, new ProgressUIListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onUIProgressStart(long totalBytes) {
                super.onUIProgressStart(totalBytes);
                Toast.makeText(getApplicationContext(), "Uploading Start", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
//                progressdialog.show();
//                Log.e("percentb", String.valueOf(percent));
//                progressdialog.setProgress(30);

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                progress_horizontal.getProgressDrawable().setColorFilter(
                        Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
                progress_layout.setVisibility(View.VISIBLE);
                progress_horizontal.setVisibility(View.VISIBLE);
                progress_horizontal.setProgress((int) (100*percent));
                textViewProgress.setText(String.valueOf((int) (100*percent))+"%");

            }
            @Override
            public void onUIProgressFinish() {
                super.onUIProgressFinish();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressdialog.dismiss();
                progress_horizontal.setVisibility(View.GONE);
                progress_layout.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        Request request = new Request.Builder()
                .url( ApiRequest.testBaseUrl+"userstatus/addUserChatStatus")
                .post(requestBodys)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Gson gson = new GsonBuilder().create();
                Log.e("response",res);
                //  final UploadPojo form = gson.fromJson(res, UploadPojo.class);
                finish();
            }
        });
    }
    public void CreateProgressDialog() {
        progressdialog = new ProgressDialog(StatusTrimVideo.this);
        progressdialog.setIndeterminate(true);
        progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressdialog.setCancelable(false);
        progressdialog.setTitle("Uploading Status");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setMax(100);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progress_horizontal.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
