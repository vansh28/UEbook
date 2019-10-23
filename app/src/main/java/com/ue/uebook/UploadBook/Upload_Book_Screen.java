package com.ue.uebook.UploadBook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.FilePath;
import com.ue.uebook.FileUtil;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.HomeActivity.HomeScreen;
import com.ue.uebook.ImageUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;
import com.ue.uebook.UploadBook.Pojo.BookCategoryResponsePojo;
import com.ue.uebook.UploadBook.Pojo.UploadPojo;
import com.ue.uebook.UploadBook.Pojo.VerifyISBNPojo;

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

public class Upload_Book_Screen extends BaseActivity implements View.OnClickListener, ImageUtils.ImageAttachmentListener, AdapterView.OnItemSelectedListener, RecognitionListener {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 99;
    private static final int PICK_FILE_REQUEST = 12;
    private static final int REQUEST_PICK_VIDEO = 4;
    private static final String CHANNEL_ID = "channelID";
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 122;
    private ImageButton back_btn_uploadbook;
    private LinearLayout upload_cover_bookBtn, question_layout,isbnlayout;
    private RelativeLayout cover_image_layout;
    private String filePath, selectedvideoPath;
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
    private TextView filname_view, verifyIsbnTv,upload_info, uploadcover_view, audioname_view, uploadMoreView, assignmentView ;
    private ProgressDialog progressdialog;
    private Button publishBtn, addQuestion ,saveForLater;
    private int categorytype;
    private EditText bookTitle, bookDesc, authorName, questionEdit,isbnTvview;
    private File coverimage, videofile;
    private File docfile, audioUrl;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    static final int REQUEST_PERMISSION_KEY = 9;
    private ImageButton recordbtn;
    private ImageView profile_image_user_upload,verifyImageview;
    private VideoView videoview;
    private int mCurrentPosition = 0;
    private ProgressBar upload_progress;
    Handler handler = new Handler();
    int status = 0;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    int id = 1;
    public int numberOfLines = 1;
    private List<String> questionList;
    List<EditText> allEds;
    EditText isbnTV;
    TextInputLayout bookIsbnLayout;
    private int textSize;
    private String isbnverifyValue="";
    private SpeechRecognizer speechRecognizer = null;
    // Tag for the instance state bundle.
    private static final String PLAYBACK_TIME = "play_time";
    private static final int ACTIVITY_CHOOSE_FILE = 33;
    private String speechString = " ";
    private BottomSheetDialog mBottomSheetDialog;
    String[] mimeTypes =
            {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                    "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                    "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                    "text/plain",
                    "application/pdf",
                    "application/zip"};

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__book__screen);
        back_btn_uploadbook = findViewById(R.id.back_btn_uploadbook);
        assignmentView = findViewById(R.id.asignmentView);
        uploadMoreView = findViewById(R.id.uploadMoreView);
        bookIsbnLayout=findViewById(R.id.bookIsbnLayout);
        isbnTvview = findViewById(R.id.isbn_numberTv);
        verifyIsbnTv=findViewById(R.id.verifyIsbnTv);
        verifyIsbnTv.setOnClickListener(this);
        verifyImageview=findViewById(R.id.verifyImageview);
        isbnlayout=findViewById(R.id.isbnlayout);
        fontsize();
        saveForLater=findViewById(R.id.saveForLater);
        saveForLater.setOnClickListener(this);
        assignmentView.setTextSize(textSize);
        uploadMoreView.setTextSize(textSize);
        questionList = new ArrayList<>();
        uploadcover_view = findViewById(R.id.uploadcover_view);
        videoview = findViewById(R.id.videoview);
        addQuestion = findViewById(R.id.add_question);
        question_layout = findViewById(R.id.question_layout);
        audioname_view = findViewById(R.id.audioname_view);
        addQuestion.setOnClickListener(this);
        cover_image_layout = findViewById(R.id.cover_image_layout);
        upload_progress = findViewById(R.id.upload_progress);
        bookTitle = findViewById(R.id.bookTitle_edit_text);
        bookTitle.setTextSize(textSize);
        bookDesc = findViewById(R.id.bookDesc_edit_text);
        recordbtn = findViewById(R.id.recordbtn);
        recordbtn.setOnClickListener(this);
        authorName = findViewById(R.id.authorName_edit_text);
        categoryName = new ArrayList<>();
        allEds = new ArrayList<EditText>();
        profile_image_user_upload = findViewById(R.id.profile_image_user_upload);
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
         if (new SessionManager(getApplicationContext()).getUserPublishType().equalsIgnoreCase("Publish House")){
             isbnlayout.setVisibility(View.VISIBLE);
             bookIsbnLayout.setVisibility(View.VISIBLE);
         }
         else {
             isbnlayout.setVisibility(View.GONE);
             bookIsbnLayout.setVisibility(View.GONE);
         }
        CreateProgressDialog();
        addQuestion.setTextSize(textSize);
        bookDesc.setTextSize(textSize);
        authorName.setTextSize(textSize);
        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(videoview);
        publishBtn.setTextSize(textSize);
        videoview.setMediaController(controller);
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }
        getBookCategory();
        String image = new SessionManager(getApplicationContext()).getUserimage();
        if (!image.isEmpty()) {
            GlideUtils.loadImage(this, "http://dnddemo.com/ebooks/api/v1/upload/" + image, profile_image_user_upload, R.drawable.user_default, R.drawable.user_default);
        } else {

            profile_image_user_upload.setImageResource(R.drawable.user_default);
        }
        Add_Line();


        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        SpannableString content = new SpannableString("Verify ISBN Number");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        verifyIsbnTv.setText(content);
    }

    private void openFile(int CODE) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "application/pdf", "application/vnd.ms-powerpoint", "text/csv"};
        i.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(i, CODE);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
            } catch (ActivityNotFoundException e)
            {
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

            openFile(111);

        }
        else if
        (view == publishBtn){
         if (isvalidate()){
             if (new SessionManager(getApplicationContext()).getUserPublishType().equalsIgnoreCase("Publish House")){
                if (isbnTvview.getText().toString().isEmpty()){
                    isbnTvview.setError("Enter Book ISBN Number");
                    isbnTvview.requestFocus();
                    isbnTvview.setEnabled(true);
                }
                else if(isbnverifyValue.equalsIgnoreCase("true")) {
                    uploadBook(isbnTvview.getText().toString());
                }
                else if (isbnverifyValue.equalsIgnoreCase("false")){
                    isbnTvview.setError("ISBN Rejected");
                    isbnTvview.requestFocus();
                    isbnTvview.setEnabled(true);
                }

             }
             else {
                 uploadBook("");
             }

         }
        } else if (view == cover_image_layout) {
            imagePreview(bitmap);
        } else if (view == filname_view) {


        } else if (view == addQuestion) {

            Add_Line();

        } else if (view == recordbtn) {
            startVoiceRecognitionActivity();
        }
        else if (view==saveForLater) {

        }
        else if (view==verifyIsbnTv)
        {
                 verifyISBN(isbnTvview.getText().toString());
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void verifyISBN(String isbn)
    {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforVerifyISBN( isbn, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                hideLoadingIndicator();
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final VerifyISBNPojo form = gson.fromJson(myResponse, VerifyISBNPojo.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                             if (form.getData().equalsIgnoreCase("true")){
                                 verifyImageview.setVisibility(View.VISIBLE);
                                 verifyImageview.setImageResource(R.drawable.verify);
                                 isbnverifyValue="true";
                             }
                             else {
                                 verifyImageview.setVisibility(View.VISIBLE);
                                 verifyImageview.setImageResource(R.drawable.reject);
                                 isbnverifyValue="false";
                             }
                    }
                });
            }
        });
    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    public void Add_Line() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newRowView = inflater.inflate(R.layout.questionitem, null);
        questionEdit = newRowView.findViewById(R.id.question_edit_text);
        questionEdit.setTextSize(textSize);
        allEds.add(questionEdit);
        questionEdit.setId(numberOfLines + 1);
        questionEdit.setHint("Question "+"  "+ numberOfLines );
        question_layout.addView(newRowView);
        numberOfLines++;

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

        if (file != null) {
            cover_image_layout.setVisibility(View.VISIBLE);
            cover_image_preview.setImageBitmap(file);
        } else {
            cover_image_layout.setVisibility(View.GONE);
        }
    }

    public void requestforUploadBook(final int type, final String userid, final String category_id, final String booktitle, final File profile_image, final String book_description, File docFile, final String author_name, String questiondata ,String isbn_number) {
        String url = null;
        url = " http://dnddemo.com/ebooks/api/v1/addNewBook";
        OkHttpClient client = new OkHttpClient();
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        RequestBody requestBody;
        switch (type) {
            case 1:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", userid)
                        .addFormDataPart("category_id", category_id)
                        .addFormDataPart("book_title", booktitle)
                        .addFormDataPart("book_description", book_description)
                        .addFormDataPart("author_name", author_name)
                        .addFormDataPart("pdf_url", docfile.getName(), RequestBody.create(MediaType.parse("text/csv"), docFile))
                        .addFormDataPart("thubm_image", profile_image.getName(), RequestBody.create(MEDIA_TYPE_PNG, profile_image))
                        .addFormDataPart("video_url", videofile.getName(), RequestBody.create(MediaType.parse("video/mp4"), videofile))
                        .addFormDataPart("audio_url", audioUrl.getName(), RequestBody.create(MediaType.parse("audio/*"), audioUrl))
                        .addFormDataPart("questiondata", questiondata)
                        .addFormDataPart("isbn_number",isbn_number)
                        .build();
                break;
            case 2:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", userid)
                        .addFormDataPart("category_id", category_id)
                        .addFormDataPart("book_title", booktitle)
                        .addFormDataPart("book_description", book_description)
                        .addFormDataPart("author_name", author_name)
                        .addFormDataPart("thubm_image", profile_image.getName(), RequestBody.create(MEDIA_TYPE_PNG, profile_image))
                        .addFormDataPart("audio_url", audioUrl.getName(), RequestBody.create(MediaType.parse("audio/*"), audioUrl))
                        .addFormDataPart("questiondata", questiondata)
                        .addFormDataPart("isbn_number",isbn_number)

                        .build();
                break;
            case 3:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", userid)
                        .addFormDataPart("category_id", category_id)
                        .addFormDataPart("book_title", booktitle)
                        .addFormDataPart("book_description", book_description)
                        .addFormDataPart("author_name", author_name)
                        .addFormDataPart("thubm_image", profile_image.getName(), RequestBody.create(MEDIA_TYPE_PNG, profile_image))
                        .addFormDataPart("video_url", videofile.getName(), RequestBody.create(MediaType.parse("video/mp4"), videofile))
                        .addFormDataPart("questiondata", questiondata)
                        .addFormDataPart("isbn_number",isbn_number)

                        .build();
                break;
            case 4:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", userid)
                        .addFormDataPart("category_id", category_id)
                        .addFormDataPart("book_title", booktitle)
                        .addFormDataPart("book_description", book_description)
                        .addFormDataPart("author_name", author_name)
                        .addFormDataPart("thubm_image", profile_image.getName(), RequestBody.create(MEDIA_TYPE_PNG, profile_image))
                        .addFormDataPart("questiondata", questiondata)
                        .addFormDataPart("isbn_number",isbn_number)

                        .build();
                break;
            case 5:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", userid)
                        .addFormDataPart("category_id", category_id)
                        .addFormDataPart("book_title", booktitle)
                        .addFormDataPart("book_description", book_description)
                        .addFormDataPart("author_name", author_name)
                        .addFormDataPart("thubm_image", profile_image.getName(), RequestBody.create(MEDIA_TYPE_PNG, profile_image))
                        .addFormDataPart("questiondata", questiondata)
                        .addFormDataPart("pdf_url", docfile.getName(), RequestBody.create(MediaType.parse("text/csv"), docFile))
                        .addFormDataPart("isbn_number",isbn_number)
                        .build();
                break;
            case 6:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", userid)
                        .addFormDataPart("category_id", category_id)
                        .addFormDataPart("book_title", booktitle)
                        .addFormDataPart("book_description", book_description)
                        .addFormDataPart("author_name", author_name)
                        .addFormDataPart("thubm_image", profile_image.getName(), RequestBody.create(MEDIA_TYPE_PNG, profile_image))
                        .addFormDataPart("questiondata", questiondata)
                        .addFormDataPart("pdf_url", docfile.getName(), RequestBody.create(MediaType.parse("text/csv"), docFile))
                        .addFormDataPart("audio_url", audioUrl.getName(), RequestBody.create(MediaType.parse("audio/*"), audioUrl))
                        .addFormDataPart("isbn_number",isbn_number)
                        .build();
                break;
            case 7:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", userid)
                        .addFormDataPart("category_id", category_id)
                        .addFormDataPart("book_title", booktitle)
                        .addFormDataPart("book_description", book_description)
                        .addFormDataPart("author_name", author_name)
                        .addFormDataPart("thubm_image", profile_image.getName(), RequestBody.create(MEDIA_TYPE_PNG, profile_image))
                        .addFormDataPart("questiondata", questiondata)
                        .addFormDataPart("pdf_url", docfile.getName(), RequestBody.create(MediaType.parse("text/csv"), docFile))
                        .addFormDataPart("video_url", videofile.getName(), RequestBody.create(MediaType.parse("video/mp4"), videofile))
                        .addFormDataPart("isbn_number",isbn_number)
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
                    docfile = FileUtil.from(Upload_Book_Screen.this, selectedfile);
                    filname_view.setVisibility(View.VISIBLE);
                    filname_view.setText(docfile.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Please Select Again", Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == 12) {

                Uri selectedaudio = data.getData();
                try {
                    String audioPathStr = FilePath.getPath(Upload_Book_Screen.this, selectedaudio);
                    audioUrl = new File(audioPathStr);
                    audioname_view.setVisibility(View.VISIBLE);
                    audioname_view.setText(audioUrl.getName());

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Please Select Again", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            speechString = bookDesc.getText().toString() + result.get(0);
            bookDesc.setText(speechString);
            bookDesc.setSelection(bookDesc.getText().length());
            bookDesc.requestFocus();
            bookDesc.setEnabled(true);
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
                }
                else {
                    Toast.makeText(this, "Please Upload Book Cover Image", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

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
                        } else {

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

    public void CreateProgressDialog() {

        progressdialog = new ProgressDialog(Upload_Book_Screen.this);

        progressdialog.setIndeterminate(false);

        progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressdialog.setCancelable(false);
        progressdialog.setTitle("Uploading Book");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setMax(100);
    }




    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {

        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        speechString = speechString  + matches.get(0);
        bookDesc.setText( speechString );
    }
    @Override
    public void onPartialResults(Bundle arg0) {


        ArrayList<String> matches = arg0
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);



    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }


    private void fontsize(){
        switch(new SessionManager(getApplicationContext()).getfontSize()) {
            case "smallest":
                textSize = 12;
                break;
            case "small":
                textSize = 14;
                break;
            case "normal":
                textSize = 16;
                break;
            case "large":
                textSize = 18;
                break;
            case "largest":
                textSize = 24;
                break;
        }
    }

    private void uploadBook(String isbnNumber){
        if (isvalidate()) {
            for (int i = 0; i < allEds.size(); i++) {
                if (!allEds.get(i).getText().toString().isEmpty()) {

                    questionList.add(allEds.get(i).getText().toString());
                }
            }
            String json = new Gson().toJson(questionList);

            if (audioUrl == null && videofile != null && docfile == null) {
                requestforUploadBook(3, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json,isbnNumber);
            } else if (audioUrl != null && videofile == null && docfile == null) {
                requestforUploadBook(2, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json,isbnNumber);

            } else if (audioUrl != null && videofile != null && docfile != null) {
                requestforUploadBook(1, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json,isbnNumber);
            } else if (audioUrl == null && videofile == null && docfile == null) {

                requestforUploadBook(4, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json,isbnNumber);

            } else if (audioUrl == null && videofile == null && docfile != null) {

                requestforUploadBook(5, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json,isbnNumber);

            } else if (audioUrl != null && videofile == null && docfile != null) {

                requestforUploadBook(6, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json,isbnNumber);

            } else if (audioUrl == null && videofile != null && docfile != null) {

                requestforUploadBook(7, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json,isbnNumber);

            }
        }

    }
    private void show_Dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.booknumberitem,null , false);
         isbnTV = viewInflated.findViewById(R.id.bookisbnTv);
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                     String val = isbnTV.getText().toString();
                     if (val.isEmpty()){
                              Toast.makeText(getApplicationContext(),"Enter ISBN Number to Upload Book",Toast.LENGTH_SHORT).show();
                     }
                     else {
                         dialog.dismiss();
                         uploadBook("");

                     }

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}













