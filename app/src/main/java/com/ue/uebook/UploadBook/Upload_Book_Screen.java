package com.ue.uebook.UploadBook;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.ImageUtils;
import com.ue.uebook.LoginActivity.Pojo.RegistrationResponse;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;
import com.ue.uebook.UploadBook.Pojo.BookCategoryPojo;
import com.ue.uebook.UploadBook.Pojo.BookCategoryResponsePojo;
import com.ue.uebook.UploadBook.Pojo.UploadPojo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import in.gauriinfotech.commons.Commons;
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

import static com.facebook.FacebookSdk.getApplicationContext;

public class Upload_Book_Screen extends AppCompatActivity implements View.OnClickListener, ImageUtils.ImageAttachmentListener, AdapterView.OnItemSelectedListener, RecognitionListener {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 99;
    private static final int PICK_FILE_REQUEST = 12;
    private static final int REQUEST_PICK_VIDEO = 3;
    private ImageButton back_btn_uploadbook;
    private LinearLayout upload_cover_bookBtn;
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
    private Spinner book_category;
    ArrayList<String> categoryName;
    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private ImageView camera_btn, video_btn, audio_btn, documents_btn, cover_image_preview;
    private String uploadedFileName;
    private StringTokenizer tokens;
    private TextView filname_view;
    private ProgressDialog dialog;
    private Button publishBtn;
    private int categorytype;
    private EditText bookTitle, bookDesc,authorName;
    private File coverimage;
    private File docfile;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    static final int REQUEST_PERMISSION_KEY = 9;
    private ImageButton recordbtn;
    private ImageView profile_image_user_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__book__screen);
        back_btn_uploadbook = findViewById(R.id.back_btn_uploadbook);
        cover_image_layout = findViewById(R.id.cover_image_layout);
        bookTitle = findViewById(R.id.bookTitle_edit_text);
        bookDesc = findViewById(R.id.bookDesc_edit_text);
        recordbtn = findViewById(R.id.recordbtn);
        authorName =findViewById(R.id.authorName_edit_text);
        categoryName = new ArrayList<>();
        profile_image_user_upload=findViewById(R.id.profile_image_user_upload);
        cover_image_preview = findViewById(R.id.cover_image_preview);
        book_category = findViewById(R.id.book_category);
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
        book_category.setPrompt("Select Book Category");
        book_category.setOnItemSelectedListener(this);
        getBookCategory();
        String image = new SessionManager(getApplicationContext()).getUserimage();
        if (!image.isEmpty()) {
//            Glide.with(getActivity())
//                    .load("http://dnddemo.com/ebooks/api/v1/upload/"+image)
//                    .into(profile_image_user);
            GlideUtils.loadImage(this,"http://dnddemo.com/ebooks/api/v1/upload/"+image,profile_image_user_upload,R.drawable.user_default,R.drawable.user_default);
        } else {

            profile_image_user_upload.setImageResource(R.drawable.user_default);
        }

        String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO};
        if(!Function.hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_KEY);
        }

        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        /*
        Minimum time to listen in millis. Here 5 seconds
         */
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5000);
        recognizerIntent.putExtra("android.speech.extra.DICTATION_MODE", true);

        recordbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View p1)
            {

                speech.startListening(recognizerIntent);
                recordbtn.setEnabled(false);

                /*To stop listening
                    progressBar.setVisibility(View.INVISIBLE);
                    speech.stopListening();
                    recordbtn.setEnabled(true);
                 */
            }


        });


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
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

        } else if (view == documents_btn) {

            showFileChooser();
        } else if (view == publishBtn) {

            if (isvalidate()) {

                if (docfile!=null){
                    requestforUploadBook(new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(),docfile,authorName.getText().toString());
                }
                else {
                    requestforUploadBook(new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(),null,authorName.getText().toString());

                }
            }


        } else if (view == cover_image_layout) {
            imagePreview(bitmap);
        }
    }


    private void imagePreview(Bitmap file) {
        final Dialog previewDialog = new Dialog(this);
        previewDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        previewDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout
                , null));
        ImageView imageView = previewDialog.findViewById(R.id.image_view);
        imageView.setImageBitmap(file);
        Button ok_Btn = previewDialog.findViewById(R.id.buton_ok);
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
                    111);
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
        coverimage = (new File(imageUtils.getPath(uri)));
//        requestforUploadBook(new File(imageUtils.getPath(uri)));

        if (file != null) {
            cover_image_layout.setVisibility(View.VISIBLE);
            cover_image_preview.setImageBitmap(file);
        } else {
            cover_image_layout.setVisibility(View.GONE);
        }


    }

    public void requestforUploadBook(final String userid, final String category_id, final String booktitle, final File profile_image, final String book_description ,File docFile,final String author_name) {
        String url = null;
        dialog.show();
        dialog.setTitle("Uploading");

        url = " http://dnddemo.com/ebooks/api/v1/addNewBook";
        OkHttpClient client = new OkHttpClient();
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userid)
                .addFormDataPart("category_id", category_id)
                .addFormDataPart("book_title", booktitle)
                .addFormDataPart("book_description", book_description)
                .addFormDataPart("author_name", author_name)

                .addFormDataPart("pdf_url", docfile.getName(), RequestBody.create(MediaType.parse("text/csv"), docFile))
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_VIDEO) {
            if (data == null) {


            } else if (requestCode == 1) {

            }


        }
        else if (requestCode==111){

                // Get the Uri of the selected file
                Uri uri = data.getData();
                String uriString = uri.toString();
                File myFile = new File(uriString);
                String path = myFile.getAbsolutePath();

            }

        }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageUtils.request_permission_result(requestCode, permissions, grantResults);
    }


    public void requestforUploadVideo(final File profile_image) {
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

    private void getBookCategory() {
        ApiRequest request = new ApiRequest();
        request.requestforGetbookCategory(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", e.getLocalizedMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myresponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final BookCategoryResponsePojo form = gson.fromJson(myresponse, BookCategoryResponsePojo.class);

                if (form != null) {
                    for (int i = 0; i < form.getResponse().size(); i++)
                        categoryName.add(form.getResponse().get(i).getCategory_name());
                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        book_category.setAdapter(new ArrayAdapter<String>(Upload_Book_Screen.this, android.R.layout.simple_spinner_dropdown_item, categoryName));

                    }
                });

            }


        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        categorytype = i + 1;

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private Boolean isvalidate() {
        String booktitle = bookTitle.getText().toString();
        String bookdesc = bookDesc.getText().toString();
        Bitmap file = bitmap;
        if (!booktitle.isEmpty()) {

            if (!bookdesc.isEmpty()) {
                if (file != null) {
                    return true;
                } else {
                    Toast.makeText(this, "Please Upload Book Cover Image", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(this, "Please Enter Your Book Description", Toast.LENGTH_SHORT).show();
                return false;
            }

        } else {
            Toast.makeText(this, "Please Enter Your Book Title", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
        int column_index = 0;
        if (cursor != null) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        } else
            return uri.getPath();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (speech != null) {
            speech.destroy();
            Log.d("Log", "destroy");
        }

    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d("Log", "onBeginningOfSpeech");
//        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d("Log", "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.d("Log", "onEndOfSpeech");
//        progressBar.setVisibility(View.INVISIBLE);
        recordbtn.setEnabled(true);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d("Log", "FAILED " + errorMessage);
//        progressBar.setVisibility(View.INVISIBLE);
        bookDesc.setText(errorMessage);
        recordbtn.setEnabled(true);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.d("Log", "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.d("Log", "onPartialResults");

        ArrayList<String> matches = arg0.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        /* To get all close matchs
        for (String result : matches)
        {
            text += result + "\n";
        }
        */
        text = matches.get(0); //  Remove this line while uncommenting above codes
        bookDesc.setText(text);
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.d("Log", "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        Log.d("Log", "onResults");

    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.d("Log", "onRmsChanged: " + rmsdB);
//        progressBar.setProgress((int) rmsdB);

    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}











