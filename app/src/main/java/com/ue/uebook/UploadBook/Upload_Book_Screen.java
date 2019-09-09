package com.ue.uebook.UploadBook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.ImageUtils;
import com.ue.uebook.R;
import com.ue.uebook.UploadBook.Pojo.UploadPojo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Upload_Book_Screen extends AppCompatActivity implements View.OnClickListener ,ImageUtils.ImageAttachmentListener{
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 99;
    private static final int PICK_FILE_REQUEST = 12;
    private static final int REQUEST_PICK_VIDEO =3 ;
    private ImageButton back_btn_uploadbook;
    private LinearLayout upload_cover_bookBtn ;
    private RelativeLayout cover_image_layout;
    private String filePath;
    private String fileName;
    private String encodedString;
    private String uriData;
    private Bitmap bitmap;
    ImageUtils imageUtils;
    private Uri video;
   private String videoPath;
    String decodableString;


    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private ImageView camera_btn, video_btn, audio_btn, documents_btn,cover_image_preview;
    private   String uploadedFileName ,first;
    private StringTokenizer tokens;
    private TextView filname_view;
    private  String value;
    private ProgressDialog dialog;
    private Button  publishBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__book__screen);
        back_btn_uploadbook = findViewById(R.id.back_btn_uploadbook);
        cover_image_layout=findViewById(R.id.cover_image_layout);
        cover_image_preview=findViewById(R.id.cover_image_preview);
        cover_image_layout.setOnClickListener(this);
        publishBtn = findViewById(R.id.publish_book);
        publishBtn.setOnClickListener(this);
       imageUtils = new ImageUtils(this);
        dialog = new ProgressDialog(this);
        upload_cover_bookBtn = findViewById(R.id.upload_cover_bookBtn);
        filname_view = findViewById(R.id.filname_view);
        camera_btn = findViewById(R.id.camera_btn);
        video_btn = findViewById(R.id.video_Btn);
        audio_btn = findViewById(R.id.audio_Btn);
        documents_btn = findViewById(R.id.documents_Btn);
        camera_btn.setOnClickListener(this);
        video_btn.setOnClickListener(this);
        audio_btn.setOnClickListener(this);
        documents_btn.setOnClickListener(this);
        upload_cover_bookBtn.setOnClickListener(this);
        back_btn_uploadbook.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == back_btn_uploadbook) {
            finish();
        } else if (view == upload_cover_bookBtn) {
            imageUtils.imagepicker(1);

        } else if (view == video_btn) {
            Intent pickVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
            pickVideoIntent.setType("video/*");
            startActivityForResult(pickVideoIntent, REQUEST_PICK_VIDEO);

        } else if (view == audio_btn) {
            getAudioFile();
        } else if (view == camera_btn) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);

        } else if (view == documents_btn) {

            showFileChooser();
        }
        else if (view == publishBtn){



        }
        else if (view==cover_image_layout)
        {
            imagePreview(bitmap);
        }
    }



    private void  imagePreview(Bitmap file){
        final Dialog previewDialog = new Dialog(this);
        previewDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        previewDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout
                , null));
        ImageView imageView = previewDialog.findViewById(R.id.image_view);
        imageView.setImageBitmap(file);
        Button ok_Btn=previewDialog.findViewById(R.id.buton_ok);
        ok_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                previewDialog.dismiss();

            }
        });

        previewDialog.show();
    }




    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    11);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void getAudioFile() {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload, 12);
    }


    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        this.bitmap = file;
        this.fileName = filename;
        String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageUtils.createImage(file, filename, path, false);
        filePath = getRealPathFromURI(uri.getPath());
        requestforUploadBook(new File(imageUtils.getPath(uri)));

        if (file!=null){
            cover_image_layout.setVisibility(View.VISIBLE);
            cover_image_preview.setImageBitmap(file);
        }
        else {
            cover_image_layout.setVisibility(View.GONE);
        }



    }

    public void requestforUploadBook( final File profile_image ) {
        String url = null;
        dialog.show();
        dialog.setTitle("Uploading");

        url = " http://dnddemo.com/ebooks/api/v1/addNewBook";
        OkHttpClient client = new OkHttpClient();
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "1")
                .addFormDataPart("category_id", "1")
                .addFormDataPart("book_title", "book")
                .addFormDataPart("thubm_image", profile_image.getName(), RequestBody.create(MEDIA_TYPE_PNG, profile_image))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dialog.dismiss();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                dialog.dismiss();
                Gson gson = new GsonBuilder().create();
                final UploadPojo form = gson.fromJson(res, UploadPojo.class);

            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUtils.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_PICK_VIDEO){
         if (data!=null){


         }
         else if (requestCode==1)
         {


         }

        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageUtils.request_permission_result(requestCode, permissions, grantResults);
    }


    public void requestforUploadVideo( final File profile_image ) {
        String url = null;
        dialog.show();
        dialog.setTitle("Uploading");

        url = " http://dnddemo.com/ebooks/api/v1/addNewBook";
        OkHttpClient client = new OkHttpClient();
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("*/*");
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "1")
                .addFormDataPart("category_id", "1")
                .addFormDataPart("book_title", "book")
                .addFormDataPart("video_url", profile_image.getName(), RequestBody.create(MEDIA_TYPE_PNG, profile_image))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dialog.dismiss();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                dialog.dismiss();
                Gson gson = new GsonBuilder().create();
                final UploadPojo form = gson.fromJson(res, UploadPojo.class);

            }
        });
    }



}











