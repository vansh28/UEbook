package com.ue.uebook.ChatSdk;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import io.github.lizhangqu.coreprogress.ProgressHelper;
import io.github.lizhangqu.coreprogress.ProgressUIListener;
import life.knowledge4.videotrimmer.K4LVideoTrimmer;
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TrimActivity extends AppCompatActivity implements OnTrimVideoListener  {
 Intent intent;
    private K4LVideoTrimmer videoTrimmer;
    private ProgressDialog mProgressDialog;
    private ProgressDialog progressdialog;
    private ProgressBar progress_horizontal;
    private RelativeLayout progress_layout;
    private TextView textViewProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trim);
        progress_horizontal = findViewById(R.id.progress_horizontal);
        progress_layout = findViewById(R.id.progress_layout);
        textViewProgress=findViewById(R.id.value123);
        CreateProgressDialog();
        intent = getIntent();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please wait...");
        videoTrimmer = ((K4LVideoTrimmer) findViewById(R.id.timeLine));
        String path= intent.getStringExtra("file");

        int type = intent.getIntExtra("id",0);
        if (type==2){
            try {
                String videoPathStr = getRealPathFromURI(Uri.parse(path));
                if (videoTrimmer != null && path!=null) {
                    videoTrimmer.setMaxDuration(30);
                    videoTrimmer.setOnTrimVideoListener(this);
                    //mVideoTrimmer.setDestinationPath("/storage/emulated/0/DCIM/CameraCustom/");
                    videoTrimmer.setVideoURI(Uri.parse(videoPathStr));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Please Select Again", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if (videoTrimmer != null && path!=null) {
                videoTrimmer.setMaxDuration(30);
                videoTrimmer.setOnTrimVideoListener(this);
                //mVideoTrimmer.setDestinationPath("/storage/emulated/0/DCIM/CameraCustom/");
                videoTrimmer.setVideoURI(Uri.parse(path));
            }
        }
    }
    @Override
    public void getResult(Uri uri) {
      requestforUploadStatus(2,new SessionManager(getApplication()).getUserID(),"",new File(String.valueOf(uri)));
    }
    private Locale getLocale() {
        Configuration config = getResources().getConfiguration();
        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }

        return sysLocale;
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config){
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config){
        return config.getLocales().get(0);
    }


    @Override
    public void cancelAction() {
        mProgressDialog.cancel();
        videoTrimmer.destroy();
        finish();
    }

    public void requestforUploadStatus(final  int type, final  String user_id, final  String caption , File videoFile) {
        OkHttpClient client = new OkHttpClient();
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        RequestBody requestBody;
        switch (type) {
            case 1:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("user_id",user_id)
                        .addFormDataPart("msg_type","image" )
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
        progressdialog = new ProgressDialog(TrimActivity.this);
        progressdialog.setIndeterminate(true);
        progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressdialog.setCancelable(false);
        progressdialog.setTitle("Uploading Status");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setMax(100);
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
