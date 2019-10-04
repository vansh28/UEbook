package com.ue.uebook.UploadBook;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.loader.content.CursorLoader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.FilePath;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.HomeActivity.HomeScreen;
import com.ue.uebook.ImageUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;
import com.ue.uebook.UploadBook.Pojo.BookCategoryResponsePojo;
import com.ue.uebook.UploadBook.Pojo.UploadPojo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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

public class Upload_Book_Screen extends BaseActivity implements View.OnClickListener, ImageUtils.ImageAttachmentListener, AdapterView.OnItemSelectedListener {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 99;
    private static final int PICK_FILE_REQUEST = 12;
    private static final int REQUEST_PICK_VIDEO = 4;
    private static final String CHANNEL_ID = "channelID";
    private ImageButton back_btn_uploadbook;
    private LinearLayout upload_cover_bookBtn,question_layout;
    private RelativeLayout cover_image_layout;
    private String filePath,selectedvideoPath;
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
    private TextView filname_view,upload_info,uploadcover_view;
    private ProgressDialog progressdialog;
    private Button publishBtn ,addQuestion;
    private int categorytype;
    private EditText bookTitle, bookDesc,authorName,questionEdit;
    private File coverimage , videofile;
    private File docfile ,audioUrl;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    static final int REQUEST_PERMISSION_KEY = 9;
    private ImageButton recordbtn;
    private ImageView profile_image_user_upload;
    private VideoView videoview;
    private int mCurrentPosition = 0;
    private ProgressBar upload_progress;
    Handler handler = new Handler();
    int status = 0;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    int id = 1;
    public int numberOfLines = 3;
    private List<String>questionList;
    List<EditText> allEds;

    // Tag for the instance state bundle.
    private static final String PLAYBACK_TIME = "play_time";
    private static final int ACTIVITY_CHOOSE_FILE = 33;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__book__screen);
        back_btn_uploadbook = findViewById(R.id.back_btn_uploadbook);
        questionList=new ArrayList<>();
        uploadcover_view=findViewById(R.id.uploadcover_view);
        videoview =  findViewById(R.id.videoview);
        addQuestion=findViewById(R.id.add_question);
        question_layout=findViewById(R.id.question_layout);
        addQuestion.setOnClickListener(this);
        cover_image_layout = findViewById(R.id.cover_image_layout);
        upload_progress=findViewById(R.id.upload_progress);
        bookTitle = findViewById(R.id.bookTitle_edit_text);
        bookDesc = findViewById(R.id.bookDesc_edit_text);
        recordbtn = findViewById(R.id.recordbtn);
        authorName =findViewById(R.id.authorName_edit_text);
        categoryName = new ArrayList<>();
        allEds = new ArrayList<EditText>();
        profile_image_user_upload=findViewById(R.id.profile_image_user_upload);
        cover_image_preview = findViewById(R.id.cover_image_preview);
        book_category = findViewById(R.id.book_category);
        cover_image_layout.setOnClickListener(this);
        publishBtn = findViewById(R.id.publish_book);
        publishBtn.setOnClickListener(this);
        imageUtils = new ImageUtils(this);
        upload_cover_bookBtn = findViewById(R.id.upload_cover_bookBtn);
        filname_view = findViewById(R.id.filname_view);
        filname_view.setOnClickListener(this);
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
        CreateProgressDialog();
        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(videoview);
        videoview.setMediaController(controller);
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }
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
 Add_Line();
    }

    @Override
    public void onClick(View view) {
        if (view == back_btn_uploadbook) {
            finish();
        } else if (view == upload_cover_bookBtn) {
            imageUtils.imagepicker(1);
        } else if (view == video_btn) {
            try {
                Intent mediaChooser = new Intent(Intent.ACTION_PICK);
                mediaChooser.setType("video/*");
                startActivityForResult(mediaChooser, REQUEST_PICK_VIDEO);
            } catch (ActivityNotFoundException e) {
                // Do nothing for now
            }
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
//            Intent chooseFile;
//            Intent intent;
//            chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
//            chooseFile.setType("*/*");
//            intent = Intent.createChooser(chooseFile, "Choose a file");
//            startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);

        } else if (view == publishBtn) {

            if (isvalidate()) {
                for(int i = 0; i < allEds.size(); i++){
                    if (!allEds.get(i).getText().toString().isEmpty()){
                        Log.d("Value ","Val " + allEds.get(i).getText());
                        questionList.add( allEds.get(i).getText().toString());
                    }
                }
                String json = new Gson().toJson(questionList );

                if (audioUrl==null&&videofile!=null){
                    requestforUploadBook(3,new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(),docfile,authorName.getText().toString(),json);
                }
                else  if (audioUrl!=null&&videofile == null){
                    requestforUploadBook(2,new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(),docfile,authorName.getText().toString(),json);

                }
                else  if (audioUrl!=null&&videofile != null)
                {
                    requestforUploadBook(1,new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(),docfile,authorName.getText().toString(),json);
                }
                else  if (audioUrl== null&&videofile == null){

                    requestforUploadBook(4,new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(),docfile,authorName.getText().toString(),json);

                }
//                if (docfile!=null){
//                }
//                else {
//                    requestforUploadBook(new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(),null,authorName.getText().toString());
//                }
          }



        } else if (view == cover_image_layout) {
            imagePreview(bitmap);
        }
        else if (view==filname_view){


        }
        else if (view==addQuestion){

                Add_Line();


        }
    }

    public void Add_Line() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newRowView = inflater.inflate(R.layout.questionitem,null);
         questionEdit=newRowView.findViewById(R.id.question_edit_text);
        allEds.add(questionEdit);
        questionEdit.setId(numberOfLines + 1);
        question_layout.addView(newRowView);
        numberOfLines++;

    }

//    private void displayFromFile(File file) {
//
//        Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
//        pdfFileName = getFileName(uri);
//
//        pdfView.fromFile(file)
//                .defaultPage(pageNumber)
//                .onPageChange(this)
//                .enableAnnotationRendering(true)
//                .onLoad(this)
//                .scrollHandle(new DefaultScrollHandle(this))
//                .spacing(10) // in dp
//                .onPageError(this)
//                .load();
//    }
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

  
    private void showFileChooser(){

        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};

        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(intent, 111);
        } catch (ActivityNotFoundException e) {
            // Do nothing for now
        }

    }
    private void getAudioFile() {
        try {
            Intent audioIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(audioIntent, 12);
        } catch (ActivityNotFoundException e) {
            // Do nothing for now
        }


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

    public void requestforUploadBook(final int type ,final String userid, final String category_id, final String booktitle, final File profile_image, final String book_description ,File docFile,final String author_name ,String questiondata ) {
        String url = null;

        String docName;
        File doc = null;

//        File file = new File(videoPath);
        url = " http://dnddemo.com/ebooks/api/v1/addNewBook";
        OkHttpClient client = new OkHttpClient();
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        RequestBody requestBody;
        switch ( type){
            case 1:
                requestBody    = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", userid)
                        .addFormDataPart("category_id", category_id)
                        .addFormDataPart("book_title", booktitle)
                        .addFormDataPart("book_description", book_description)
                        .addFormDataPart("author_name", author_name)
//                .addFormDataPart("pdf_url", docfile.getName(), RequestBody.create(MediaType.parse("text/csv"), docFile))
                        .addFormDataPart("thubm_image", profile_image.getName(), RequestBody.create(MEDIA_TYPE_PNG, profile_image))
                        .addFormDataPart("video_url", videofile.getName(),RequestBody.create(MediaType.parse("video/mp4"), videofile))
                        .addFormDataPart("audio_url", audioUrl.getName(), RequestBody.create(MediaType.parse("audio/*"), audioUrl))
                        .addFormDataPart("questiondata",questiondata)
                        .build();
                break;
            case 2:
                requestBody    = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", userid)
                        .addFormDataPart("category_id", category_id)
                        .addFormDataPart("book_title", booktitle)
                        .addFormDataPart("book_description", book_description)
                        .addFormDataPart("author_name", author_name)
                        .addFormDataPart("thubm_image", profile_image.getName(), RequestBody.create(MEDIA_TYPE_PNG, profile_image))
                        .addFormDataPart("audio_url", audioUrl.getName(), RequestBody.create(MediaType.parse("audio/*"), audioUrl))
                        .addFormDataPart("questiondata",questiondata)

                        .build();
                break;
            case 3:
                requestBody    = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", userid)
                        .addFormDataPart("category_id", category_id)
                        .addFormDataPart("book_title", booktitle)
                        .addFormDataPart("book_description", book_description)
                        .addFormDataPart("author_name", author_name)
                        .addFormDataPart("thubm_image", profile_image.getName(), RequestBody.create(MEDIA_TYPE_PNG, profile_image))
                        .addFormDataPart("video_url", videofile.getName(),RequestBody.create(MediaType.parse("video/mp4"), videofile))
                        .addFormDataPart("questiondata",questiondata)

                        .build();
                break;
            case 4:
                requestBody    = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", userid)
                        .addFormDataPart("category_id", category_id)
                        .addFormDataPart("book_title", booktitle)
                        .addFormDataPart("book_description", book_description)
                        .addFormDataPart("author_name", author_name)
                        .addFormDataPart("thubm_image", profile_image.getName(), RequestBody.create(MEDIA_TYPE_PNG, profile_image))
                        .addFormDataPart("questiondata",questiondata)

                        .build();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + 1);
        }


        

        RequestBody requestBodys = ProgressHelper.withProgress(requestBody, new ProgressUIListener() {

            //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onUIProgressStart(long totalBytes) {
                super.onUIProgressStart(totalBytes);
                Log.e("TAG", "onUIProgressStart:" + totalBytes);
                Toast.makeText(getApplicationContext(), "Uploading Start", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                progressdialog.show();

                progressdialog.setProgress((int) (100 * percent));



            }

            //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
            @Override
            public void onUIProgressFinish() {
                super.onUIProgressFinish();
                Log.e("TAG", "onUIProgressFinish:");
                Toast.makeText(getApplicationContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Upload_Book_Screen.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        });

        Request request = new Request.Builder()
                .url(url)
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

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_PICK_VIDEO) {
//                    String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
//                    videofile = new File(path);
                    Uri selectedVideo = data.getData();
                    try {
                        String videoPathStr =getPath(selectedVideo);
                        videofile = new File(videoPathStr);
                        initializePlayer(selectedVideo);
                        videoview.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this,"Please Select Again",Toast.LENGTH_SHORT).show();
                    }
                }

             else if (requestCode == 111) {

//                    String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
//                    docfile = new File(path);
//                    filname_view.setVisibility(View.VISIBLE);
//                    filname_view.setText(docfile.getName());
//                    Uri uri = data.getData();
//                    String FilePath = getRealPathFromURI(uri);
//                    docfile = new File(FilePath);
                Uri selectedfile = data.getData();
                try {
                    String filePathStr = getRealPathFromURI( selectedfile);
                    docfile = new File(filePathStr);
//                    filname_view.setVisibility(View.VISIBLE);
//                    filname_view.setText(docfile.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this,"Please Select Again",Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == 12) {

                Uri selectedaudio = data.getData();
                try {
                    String audioPathStr = FilePath.getPath(Upload_Book_Screen.this, selectedaudio);
                    audioUrl = new File(audioPathStr);
                    filname_view.setVisibility(View.VISIBLE);
                    filname_view.setText(audioUrl.getName());

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this,"Please Select Again",Toast.LENGTH_SHORT).show();
                }


            }
        }
        }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageUtils.request_permission_result(requestCode, permissions, grantResults);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
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
    @Override
    protected void onStop() {
        super.onStop();

        // Media playback takes a lot of resources, so everything should be
        // stopped and released at this time.
        releasePlayer();
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
//                    uploadcover_view.setError("Upload Cover Image");
                    return false;
                }
            } else {
                bookDesc.setError("Enter Your Book Description");
                bookDesc.requestFocus();
                bookDesc.setEnabled(true);
                return false;
            }

        } else {
            bookTitle.setError("Enter Book Title");
            bookTitle.requestFocus();
            bookTitle.setEnabled(true);
            return false;
        }

    }



    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        }

    private void initializePlayer(Uri uri) {
        // Show the "Buffering..." message while the video loads.

        if (uri != null){
            videoview.setVideoURI(uri);
        }
        // Listener for onPrepared() event (runs after the media is prepared).
        videoview.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {

                        // Hide buffering message.


                        // Restore saved position, if available.
                        if (mCurrentPosition > 0) {
                            videoview.seekTo(mCurrentPosition);
                        } else {
                            // Skipping to 1 shows the first frame of the video.
                            videoview.seekTo(1);
                        }

                        // Start playing!
                        videoview.start();
                    }
                });

        // Listener for onCompletion() event (runs after media has finished
        // playing).

    }

    private void releasePlayer() {
        videoview.stopPlayback();
    }

    public void CreateProgressDialog()
    {

        progressdialog = new ProgressDialog(Upload_Book_Screen.this);

        progressdialog.setIndeterminate(false);

        progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressdialog.setCancelable(false);
        progressdialog.setTitle("Uploading Book");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setMax(100);
//        progressdialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//
//
//            }
//        });
//        progressdialog.setButton(DialogInterface.BUTTON_POSITIVE, "Upload In Background", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });




    }

//
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public static String getPathFromUri(final Context context, final Uri uri) {

    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }

            // TODO handle non-primary volumes
        }
        // DownloadsProvider
        else if (isDownloadsDocument(uri)) {

            final String id = DocumentsContract.getDocumentId(uri);
            final Uri contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

            return getDataColumn(context, contentUri, null, null);
        }
        // MediaProvider
        else if (isMediaDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            final String selection = "_id=?";
            final String[] selectionArgs = new String[] {
                    split[1]
            };

            return getDataColumn(context, contentUri, selection, selectionArgs);
        }
    }
    // MediaStore (and general)
    else if ("content".equalsIgnoreCase(uri.getScheme())) {

        // Return the remote address
        if (isGooglePhotosUri(uri))
            return uri.getLastPathSegment();

        return getDataColumn(context, uri, null, null);
    }
    // File
    else if ("file".equalsIgnoreCase(uri.getScheme())) {
        return uri.getPath();
    }

    return null;
}

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());

    }



}













