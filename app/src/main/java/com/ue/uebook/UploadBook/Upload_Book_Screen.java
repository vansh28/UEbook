package com.ue.uebook.UploadBook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.AudioRecordActivity;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.FileUtil;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.HomeActivity.HomeScreen;
import com.ue.uebook.ImageUtils;
import com.ue.uebook.PendingBook.Pojo.BookResponse;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;
import com.ue.uebook.UploadBook.Pojo.BookCategoryResponsePojo;
import com.ue.uebook.UploadBook.Pojo.PendingData;
import com.ue.uebook.UploadBook.Pojo.VerifyISBNPojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Upload_Book_Screen extends BaseActivity implements View.OnClickListener, ImageUtils.ImageAttachmentListener, AdapterView.OnItemSelectedListener, RecognitionListener {
    private static final int REQUEST_PICK_VIDEO = 4;
    int RQS_RECORDING = 19;
    private static final String CHANNEL_ID = "channelID";
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 122;
    private ImageButton back_btn_uploadbook;
    private LinearLayout upload_cover_bookBtn, question_layout, isbnlayout ,paidView;
    private RelativeLayout cover_image_layout;
    private String filePath;
    private String fileName;
    private Bitmap bitmap;
    private CountDownTimer countDownTimer;
    private long currentMillis=10;
    private String coverimageurl="";
    ImageUtils imageUtils;
    private Spinner book_category,payemntInfo;
    private TextInputLayout paymemt_text_input;
    ArrayList<String> categoryName;
    private ImageView camera_btn, video_btn, audio_btn, documents_btn, cover_image_preview;
    private TextView    commisionView,timerTv,messageTv,filname_view, verifyIsbnTv, upload_info, uploadcover_view, audioname_view, uploadMoreView, assignmentView;
    private ProgressDialog progressdialog;
    private Button    checkCommisionBtn,publishBtn, addQuestion, saveForLater ,startvoiceRecord,pausevoiceRecord,stopvoiceRecord;
    private String categorytype;
    private EditText bookTitle, bookDesc, authorName, questionEdit, isbnTvview;
    private File coverimage, videofile;
    private File docfile, audioUrl;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private ImageButton recordbtn;
    private ImageView profile_image_user_upload, verifyImageview;
    private VideoView videoview;
    private int mCurrentPosition = 0;
    private List<String>categoryId;
    private String paidType="";
    Handler handler = new Handler();
    int status = 0;
    private NotificationCompat.Builder mBuilder;
    int id = 1;
    public int numberOfLines = 1;
    private List<String> questionList;
    List<EditText> allEds;
    EditText isbnTV;
    TextInputLayout bookIsbnLayout;
    private int textSize;
    private String isbnverifyValue = "";
    private SpeechRecognizer speechRecognizer = null;
    private static final String PLAYBACK_TIME = "play_time";
    private static final int ACTIVITY_CHOOSE_FILE = 33;
    private String speechString = " ";
    private BottomSheetDialog mBottomSheetDialog;
    Intent pendigbookIntent;
    private String bookid_pending;
    private String bookid=" ";
    private List<BookResponse>pendingBookdata;
    private   int pendingbookID,pendingbookPosition;
    private String uploadurl=ApiRequest.BaseUrl+"addNewBook";
    private String savelaterUrl;
    private int typevalue=0;  //
    private Handler customHandler = new Handler();
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private TextView questionNumber;
    private TextInputEditText payment_edit_text;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    private Uri audioUri;
    private String isPaid="No";
    private String coverimaqgeURL=" ";
    private MaterialDialog progressDialog;
    String admin_Commision="0";

    public static final int RequestPermissionCode = 1;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__book__screen);
        categoryId= new ArrayList<>();
        pendigbookIntent= getIntent();
        pendingBookdata= new ArrayList<>();
        random = new Random();

        payemntInfo=findViewById(R.id.payemntInfo);
        commisionView =findViewById(R.id.commisionView);
        paidView = findViewById(R.id.paidView);
        checkCommisionBtn = findViewById(R.id.checkCommisionBtn);
        checkCommisionBtn.setOnClickListener(this);
        paymemt_text_input = findViewById(R.id.paymemt_text_input);
        pendingbookID = pendigbookIntent.getIntExtra("screenid",0);
        payment_edit_text = findViewById(R.id.payment_edit_text);
        bookid_pending = pendigbookIntent.getStringExtra("bookid");
        back_btn_uploadbook = findViewById(R.id.back_btn_uploadbook);
        assignmentView = findViewById(R.id.asignmentView);
        uploadMoreView = findViewById(R.id.uploadMoreView);
        bookIsbnLayout = findViewById(R.id.bookIsbnLayout);
        isbnTvview = findViewById(R.id.isbn_numberTv);
        verifyIsbnTv = findViewById(R.id.verifyIsbnTv);
        verifyIsbnTv.setOnClickListener(this);
        verifyImageview = findViewById(R.id.verifyImageview);
        isbnlayout = findViewById(R.id.isbnlayout);
        fontsize();
        saveForLater = findViewById(R.id.saveForLater);
        saveForLater.setOnClickListener(this);
        assignmentView.setTextSize(textSize);
        uploadMoreView.setTextSize(textSize);
        questionList = new ArrayList<>();
        uploadcover_view = findViewById(R.id.uploadcover_view);
        videoview = findViewById(R.id.videoview);
        addQuestion = findViewById(R.id.add_question);
        question_layout = findViewById(R.id.question_layout);
        audioname_view = findViewById(R.id.audioname_view);
        audioname_view.setOnClickListener(this);
        addQuestion.setOnClickListener(this);
        cover_image_layout = findViewById(R.id.cover_image_layout);

        bookTitle = findViewById(R.id.bookTitle_edit_text);
        bookTitle.setTextSize(textSize);
        bookDesc = findViewById(R.id.bookDesc_edit_text);
        recordbtn = findViewById(R.id.recordbtnTv);
        recordbtn.setOnClickListener(this);
        authorName = findViewById(R.id.authorName_edit_text);
        categoryName = new ArrayList<>();
        cover_image_preview = findViewById(R.id.cover_image_preview);
        allEds = new ArrayList<EditText>();
        profile_image_user_upload = findViewById(R.id.profile_image_user_upload);
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
        payemntInfo.setOnItemSelectedListener(this);
        if (new SessionManager(getApplicationContext()).getUserPublishType().equalsIgnoreCase("Publish House")) {
            isbnlayout.setVisibility(View.VISIBLE);
            bookIsbnLayout.setVisibility(View.VISIBLE);
        } else {
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
            GlideUtils.loadImage(this, ApiRequest.BaseUrl+"upload/" + image, profile_image_user_upload, R.drawable.user_default, R.drawable.user_default);
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
        if (pendingbookID==2){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getPendingBookDetails(bookid_pending);
            }
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.paymentInfo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payemntInfo.setAdapter(adapter);
        payemntInfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                String label = parent.getItemAtPosition(arg2).toString();
                     if (label.equalsIgnoreCase("paid")){
                         paidType = label;
                         isPaid ="Yes";
                         paidView.setVisibility(View.VISIBLE);
                     }
                     else {
                         paidType = label;
                         isPaid ="No";
                         paidView.setVisibility(View.GONE);
                     }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



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
            openFile(111);
        } else if
        (view == publishBtn) {

            Log.e("paidType",paidType);

            Log.e("isPaid",isPaid.toString());
            Log.e("isPaid",isPaid.toString());

            if (isvalidate()) {

                if (paidType.equalsIgnoreCase("paid")){
                    if (!payment_edit_text.getText().toString().isEmpty()){
                    if (admin_Commision.equalsIgnoreCase("0")){
                         checkCommisionBtn.setError("check Commision");
                        checkCommisionBtn.setEnabled(true);
                        checkCommisionBtn.requestFocus();

                    }
                    else {
                        if (new SessionManager(getApplicationContext()).getUserPublishType().equalsIgnoreCase("Publish House")) {
                            if (isbnTvview.getText().toString().isEmpty()) {
                                isbnTvview.setError("Enter Book ISBN Number");
                                isbnTvview.requestFocus();
                                isbnTvview.setEnabled(true);
                            } else if (isbnverifyValue.equalsIgnoreCase("true")) {
                                uploadBook(isbnTvview.getText().toString(), "1",bookid ,payment_edit_text.getText().toString(),isPaid);
                            } else if (isbnverifyValue.equalsIgnoreCase("false")) {
                                isbnTvview.setError("ISBN Rejected");
                                isbnTvview.requestFocus();
                                isbnTvview.setEnabled(true);
                            }
                        } else {
                            uploadBook("", "1",bookid ,payment_edit_text.getText().toString(),isPaid);
                        }

                    }

                    }
                    else {
                        payment_edit_text.setError("Enter price of book");
                        payment_edit_text.requestFocus();
                        payment_edit_text.setEnabled(true);
                    }

                }
                else {
                    if (new SessionManager(getApplicationContext()).getUserPublishType().equalsIgnoreCase("Publish House")) {
                        if (isbnTvview.getText().toString().isEmpty()) {
                            isbnTvview.setError("Enter Book ISBN Number");
                            isbnTvview.requestFocus();
                            isbnTvview.setEnabled(true);
                        } else if (isbnverifyValue.equalsIgnoreCase("true")) {
                            uploadBook(isbnTvview.getText().toString(), "1",bookid ,payment_edit_text.getText().toString(),isPaid);
                        } else if (isbnverifyValue.equalsIgnoreCase("false")) {
                            isbnTvview.setError("ISBN Rejected");
                            isbnTvview.requestFocus();
                            isbnTvview.setEnabled(true);
                        }
                    } else {
                        uploadBook("", "1",bookid ,payment_edit_text.getText().toString(),isPaid);
                    }
                }

            }
        } else if (view == cover_image_layout) {
            if (pendingbookID==2){
                imagePreview(bitmap,2,coverimageurl);
            }
            else {
                imagePreview(bitmap,1,coverimageurl);
            }
        } else if (view == filname_view) {


        }
        else if (view == addQuestion) {
            Add_Line();

        } else if (view == recordbtn) {
            Intent intent = new Intent(Upload_Book_Screen.this , AudioRecordActivity.class);
            startActivityForResult(intent, RQS_RECORDING);


        } else if (view == saveForLater) {
            saveForlater(isbnTvview.getText().toString(), "0",bookid,payment_edit_text.getText().toString(),isPaid.toString());
        } else if (view == verifyIsbnTv) {
            verifyISBN(isbnTvview.getText().toString());
        }
        else if (view==startvoiceRecord){
            startvoiceRecord.setVisibility(View.GONE);
            pausevoiceRecord.setVisibility(View.VISIBLE);
            stopvoiceRecord.setVisibility(View.VISIBLE);
            messageTv.setText("Recording...");
            if(checkPermission()) {
                AudioSavePathInDevice =
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                CreateRandomAudioFileName(5) + "UebookRecord.3gp";
                MediaRecorderReady();
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);

            } else {
                requestPermission();
            }

        }
        else if (view ==pausevoiceRecord){
            stopvoiceRecord.setVisibility(View.VISIBLE);
            pausevoiceRecord.setVisibility(View.GONE);
            startvoiceRecord.setVisibility(View.VISIBLE);
            messageTv.setText("Paused...");
            timeSwapBuff += timeInMilliseconds;
            customHandler.removeCallbacks(updateTimerThread);

        }
        else if (view==stopvoiceRecord){
            mediaRecorder.stop();
            timeSwapBuff=0L;
            customHandler.removeCallbacks(updateTimerThread);
            Toast.makeText(Upload_Book_Screen.this, "Recording Completed",
                    Toast.LENGTH_LONG).show();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result",AudioSavePathInDevice);
            setResult(Activity.RESULT_OK,returnIntent);
            mBottomSheetDialog.dismiss();
        } else if (view == audioname_view) {
//             Log.e("audiourl",audioUrl.toString());


        }
        else  if (view==checkCommisionBtn){
                if (payment_edit_text.getText().toString().isEmpty()){
                     payment_edit_text.setEnabled(true);
                     payment_edit_text.requestFocus();
                     payment_edit_text.setError("Enter amount");
                }
                else {
                    checkComission(payment_edit_text.getText().toString());
                }
        }

    }
    private void stopRecording() {
        try{
            mediaRecorder.release();
            mediaRecorder.stop();
        }
        catch(RuntimeException stopException)
        {
            //handle cleanup here
        }

    }
    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerTv.setText(String.format("%02d", mins) + ":" + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void verifyISBN(String isbn) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforVerifyISBN(isbn, new okhttp3.Callback() {
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
                        if (form.getData().equalsIgnoreCase("true")) {
                            verifyImageview.setVisibility(View.VISIBLE);
                            verifyImageview.setImageResource(R.drawable.verify);
                            isbnverifyValue = "true";
                        } else {
                            verifyImageview.setVisibility(View.VISIBLE);
                            verifyImageview.setImageResource(R.drawable.reject);
                            isbnverifyValue = "false";
                        }
                    }
                });
            }
        });
    }
    public void Add_Line() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newRowView = inflater.inflate(R.layout.questionitem, null);
        questionEdit = newRowView.findViewById(R.id.question_edit_text);
        questionNumber=newRowView.findViewById(R.id.questionNumber);

        questionEdit.setTextSize(textSize);
        allEds.add(questionEdit);
        questionEdit.setId(numberOfLines + 1);
        questionNumber.setText("Question "+  numberOfLines);
        question_layout.addView(newRowView);
        numberOfLines++;

    }
    private void imagePreview(Bitmap file ,int id ,String coverimageurl) {
        final Dialog previewDialog = new Dialog(this);
        previewDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        previewDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout , null));
        ImageView imageView = previewDialog.findViewById(R.id.image_view);
        if (file==null) {
            GlideUtils.loadImage(this, ApiRequest.BaseUrl+"upload/books/" + coverimageurl, imageView, R.drawable.noimage, R.drawable.noimage);
        }
        else {
            imageView.setImageBitmap(file);
        }

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
    public void requestforUploadBook(String url,final int type, final String userid, final String category_id, final String booktitle, final File profile_image, final String book_description, File docFile, final String author_name, String questiondata, String isbn_number, String statusbook ,String book_id ,String price ,String ispaid ,String admin_commision) {
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
                        .addFormDataPart("isbn_number", isbn_number)
                        .addFormDataPart("status", statusbook)
                        .addFormDataPart("book_id", book_id)
                        .addFormDataPart("cover_url",coverimaqgeURL)
                        .addFormDataPart("price", price)
                        .addFormDataPart("is_paid",ispaid)
                        .addFormDataPart("admin_commission",admin_commision)





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
                        .addFormDataPart("isbn_number", isbn_number)
                        .addFormDataPart("status", statusbook)
                        .addFormDataPart("book_id", book_id)
                        .addFormDataPart("cover_url",coverimaqgeURL)
                        .addFormDataPart("price", price)
                        .addFormDataPart("is_paid",ispaid)
                        .addFormDataPart("admin_commission",admin_commision)

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
                        .addFormDataPart("isbn_number", isbn_number)
                        .addFormDataPart("status", statusbook)
                        .addFormDataPart("book_id", book_id)
                        .addFormDataPart("cover_url",coverimaqgeURL)
                        .addFormDataPart("price", price)
                        .addFormDataPart("is_paid",ispaid)
                        .addFormDataPart("admin_commission",admin_commision)
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
                        .addFormDataPart("isbn_number", isbn_number)
                        .addFormDataPart("status", statusbook)
                        .addFormDataPart("book_id", book_id)
                        .addFormDataPart("cover_url",coverimaqgeURL)
                        .addFormDataPart("price", price)
                        .addFormDataPart("is_paid",ispaid)
                        .addFormDataPart("admin_commission",admin_commision)

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
                        .addFormDataPart("isbn_number", isbn_number)
                        .addFormDataPart("status", statusbook)
                        .addFormDataPart("book_id", book_id)
                        .addFormDataPart("cover_url",coverimaqgeURL)
                        .addFormDataPart("price", price)
                        .addFormDataPart("is_paid",ispaid)
                        .addFormDataPart("admin_commission",admin_commision)

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
                        .addFormDataPart("isbn_number", isbn_number)
                        .addFormDataPart("status", statusbook)
                        .addFormDataPart("book_id", book_id)
                        .addFormDataPart("cover_url",coverimaqgeURL)
                        .addFormDataPart("price", price)
                        .addFormDataPart("is_paid",ispaid)
                        .addFormDataPart("admin_commission",admin_commision)

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
                        .addFormDataPart("isbn_number", isbn_number)
                        .addFormDataPart("status", statusbook)
                        .addFormDataPart("book_id", book_id)
                        .addFormDataPart("cover_url",coverimaqgeURL)
                        .addFormDataPart("price", price)
                        .addFormDataPart("is_paid",ispaid)
                        .addFormDataPart("admin_commission",admin_commision)

                        .build();
                break;
            case 8:
                requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", userid)
                        .addFormDataPart("category_id", category_id)
                        .addFormDataPart("book_title", booktitle)
                        .addFormDataPart("book_description", book_description)
                        .addFormDataPart("author_name", author_name)
                        .addFormDataPart("questiondata", questiondata)
                        .addFormDataPart("isbn_number", isbn_number)
                        .addFormDataPart("status", statusbook)
                        .addFormDataPart("book_id", book_id)
                        .addFormDataPart("cover_url",coverimaqgeURL)
                        .addFormDataPart("price", price)
                        .addFormDataPart("is_paid",ispaid)
                        .addFormDataPart("admin_commission",admin_commision)

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
                progressdialog.show();
                progressdialog.setProgress((int) (100 * percent));
            }
            @Override
            public void onUIProgressFinish() {
                super.onUIProgressFinish();
                progressdialog.dismiss();
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
                Log.e("response",res);
              //  final UploadPojo form = gson.fromJson(res, UploadPojo.class);
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


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Please Select Again", Toast.LENGTH_SHORT).show();
                }
            }
            if(requestCode == RQS_RECORDING){

                if(resultCode == Activity.RESULT_OK){
                    // Great! User has recorded and saved the audio file
                    String result=data.getStringExtra("result");
                    Toast.makeText(Upload_Book_Screen.this,
                            "Saved: " + result,
                            Toast.LENGTH_LONG).show();
//                    String audioPathStr = FilePath.getPath(Upload_Book_Screen.this, data.getData());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        try {
//                            audioToText(result);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }
                    audioUri=data.getData();
                    audioUrl = new File(result);
                    audioname_view.setVisibility(View.VISIBLE);
                    audioname_view.setText(audioUrl.getName());
                    Log.d("debugvoice" , "Saved Path::" + result);


                }
        }
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            speechString = bookDesc.getText().toString() + result.get(0);
            bookDesc.setText(speechString);
            bookDesc.setSelection(bookDesc.getText().length());
            bookDesc.requestFocus();
            bookDesc.setEnabled(true);
        }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageUtils.request_permission_result(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean CameraPermission = grantResults[2] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean ReadPermission = grantResults[2] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (StoragePermission && RecordPermission && CameraPermission && ReadPermission) {
                        Toast.makeText(Upload_Book_Screen.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Upload_Book_Screen.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;

        }
    }
    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
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

                    for (int i = 0; i < form.getResponse().size(); i++){
                        categoryName.add(form.getResponse().get(i).getCategory_name());
                         categoryId.add(form.getResponse().get(i).getId());
                }}
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
        categorytype = categoryId.get(i);
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
        if (!booktitle.isEmpty()) {
            if (!bookdesc.isEmpty()) {
                return  true;
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

    public void CreateProgressDialog() {
        progressdialog = new ProgressDialog(Upload_Book_Screen.this);
        progressdialog.setIndeterminate(true);
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
        speechString = speechString + matches.get(0);
        bookDesc.setText(speechString);
    }

    @Override
    public void onPartialResults(Bundle arg0) {


        ArrayList<String> matches = arg0.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);


    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
    private void fontsize() {
        switch (new SessionManager(getApplicationContext()).getfontSize()) {
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
    private void uploadBook(String isbnNumber, String status ,String book_id ,String price ,String ispaid) {
        if (isvalidate()) {
            for (int i = 0; i < allEds.size(); i++) {
                if (!allEds.get(i).getText().toString().isEmpty()) {
                    questionList.add(allEds.get(i).getText().toString());
                }
            }
            String json = new Gson().toJson(questionList);

            if (audioUrl == null && videofile != null && docfile == null) {
                requestforUploadBook(uploadurl,3, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json, isbnNumber, status,book_id ,price ,ispaid ,admin_Commision);
            } else if (audioUrl != null && videofile == null && docfile == null) {
                requestforUploadBook(uploadurl,2, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json, isbnNumber, status,book_id ,price ,ispaid ,admin_Commision);

            } else if (audioUrl != null && videofile != null && docfile != null) {
                requestforUploadBook(uploadurl,1, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json, isbnNumber, status,book_id ,price ,ispaid,admin_Commision);
            }
            else if (audioUrl == null && videofile == null && docfile == null && coverimage==null) {
                requestforUploadBook(uploadurl,8, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json, isbnNumber, status,book_id,price ,ispaid,admin_Commision);
            }
            else if (audioUrl == null && videofile == null && docfile == null) {
                requestforUploadBook(uploadurl,4, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json, isbnNumber, status,book_id,price ,ispaid,admin_Commision);
            } else if (audioUrl == null && videofile == null && docfile != null) {
                requestforUploadBook(uploadurl,5, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json, isbnNumber, status,book_id,price ,ispaid,admin_Commision);
            } else if (audioUrl != null && videofile == null && docfile != null) {
                requestforUploadBook(uploadurl,6, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json, isbnNumber, status,book_id,price ,ispaid,admin_Commision);
            } else if (audioUrl == null && videofile != null && docfile != null) {
                requestforUploadBook(uploadurl,7, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json, isbnNumber, status,book_id,price ,ispaid,admin_Commision);
            }
        }

    }
    private void saveForlater(String isbnNumber, String status,String book_id ,String price ,String ispaid) {
        if (pendingbookID==2){
            savelaterUrl=ApiRequest.BaseUrl+"updateBookByid";
        }
        else {
            savelaterUrl=uploadurl;
        }
        for (int i = 0; i < allEds.size(); i++) {
            if (!allEds.get(i).getText().toString().isEmpty()) {
                questionList.add(allEds.get(i).getText().toString());
            }
        }
        String json = new Gson().toJson(questionList);
        if (audioUrl == null && videofile != null && docfile == null) {
            requestforUploadBook(savelaterUrl,3, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json, isbnNumber, status,book_id ,price ,ispaid ,admin_Commision);
        } else if (audioUrl != null && videofile == null && docfile == null) {
            requestforUploadBook(savelaterUrl,2, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json, isbnNumber, status,book_id ,price ,ispaid,admin_Commision);

        } else if (audioUrl != null && videofile != null && docfile != null) {
            requestforUploadBook(savelaterUrl,1, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json, isbnNumber, status,book_id ,price ,ispaid,admin_Commision);
        }

        else if (audioUrl == null && videofile == null && docfile == null && coverimage==null) {

            requestforUploadBook(savelaterUrl,8, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json, isbnNumber, status,book_id ,price ,ispaid,admin_Commision);

        }else if (audioUrl == null && videofile == null && docfile == null) {

            requestforUploadBook(savelaterUrl,4, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json, isbnNumber, status,book_id ,price ,ispaid,admin_Commision);

        } else if (audioUrl == null && videofile == null && docfile != null) {

            requestforUploadBook(savelaterUrl,5, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json, isbnNumber, status,book_id ,price ,ispaid,admin_Commision);

        } else if (audioUrl != null && videofile == null && docfile != null) {

            requestforUploadBook(savelaterUrl,6, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json, isbnNumber, status,book_id ,price ,ispaid,admin_Commision);

        } else if (audioUrl == null && videofile != null && docfile != null) {

            requestforUploadBook(savelaterUrl,7, new SessionManager(getApplicationContext()).getUserID(), String.valueOf(categorytype), bookTitle.getText().toString(), coverimage, bookDesc.getText().toString(), docfile, authorName.getText().toString(), json, isbnNumber, status,book_id ,price ,ispaid,admin_Commision);

        }

    }

    private void show_Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.booknumberitem, null, false);
        isbnTV = viewInflated.findViewById(R.id.bookisbnTv);
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String val = isbnTV.getText().toString();
                if (val.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter ISBN Number to Upload Book", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    uploadBook("", "1","",payment_edit_text.getText().toString(),isPaid.toString());

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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getPendingBookDetails(String book_id) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforgetPendingBookDetail(book_id,new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                hideLoadingIndicator();
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final PendingData form = gson.fromJson(myResponse, PendingData.class);
                if (form.getResponse()!=null&&form.isError()==false){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            coverimageurl=form.getResponse().getThubm_image();
                            bookid=form.getResponse().getId();
                            bookTitle.setText(form.getResponse().getBook_title());
                            authorName.setText(form.getResponse().getAuthor_name());
                            bookDesc.setText(form.getResponse().getBook_description());
                            if (coverimageurl!=null){
                                cover_image_layout.setVisibility(View.VISIBLE);
                                coverimaqgeURL=coverimageurl;
//                                try {
//                                    persistImage(getBitmapFromURL("http://dnddemo.com/ebooks/api/v1/upload/books/" + coverimageurl),coverimageurl);
////                                    coverimage=toFile(new URL("http://dnddemo.com/ebooks/api/v1/upload/books/" + coverimageurl));
//                                } catch (MalformedURLException e) {
//                                    e.printStackTrace();
//                                }
                                GlideUtils.loadImage(Upload_Book_Screen.this, "http://dnddemo.com/ebooks/api/v1/upload/books/" + coverimageurl, cover_image_preview, R.drawable.noimage, R.drawable.noimage);
                            }
                            else {
                                cover_image_layout.setVisibility(View.GONE);
                            }
                            if (form.getResponse().getCategory_id() == "1") {

                            book_category.setSelection(0);
                            }
                            else {
                                book_category.setSelection(1);
                            }

                        }
                    });
                }
            }


        });
    }
    private void showBottomSheet() {
        final View bottomSheetLayout = getLayoutInflater().inflate(R.layout.voicerecognitionview, null);
        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setCanceledOnTouchOutside(false);
        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.setContentView(bottomSheetLayout);
        startvoiceRecord = mBottomSheetDialog.findViewById(R.id.startVoicerecord);
        pausevoiceRecord = mBottomSheetDialog.findViewById(R.id.pauseVoiceRecording);
        stopvoiceRecord = mBottomSheetDialog.findViewById(R.id.stopVoiceRecording);
        messageTv=mBottomSheetDialog.findViewById(R.id.messageTv);
        timerTv=mBottomSheetDialog.findViewById(R.id.timerTv);
        startvoiceRecord.setOnClickListener(this);
        pausevoiceRecord.setOnClickListener(this);
        stopvoiceRecord.setOnClickListener(this);
        mBottomSheetDialog.show();
    }
    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Upload_Book_Screen.this, new
                String[]{
                        WRITE_EXTERNAL_STORAGE, RECORD_AUDIO,CAMERA,READ_EXTERNAL_STORAGE
        }, RequestPermissionCode);
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void  audioToText(String patha) throws IOException {
//        SpeechClient speech = SpeechClient.create();
//
//        // The path to the audio file to transcribe
//        String fileName = "./resources/audio.raw";
//
//        // Reads the audio file into memory
//        Path path = Paths.get(patha);
//        byte[] data = Files.readAllBytes(path);
//        ByteString audioBytes = ByteString.copyFrom(data);
//
//        // Builds the sync recognize request
//        RecognitionConfig config = RecognitionConfig.newBuilder()
//                .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
//                .setSampleRateHertz(16000)
//                .setLanguageCode("en-US")
//                .build();
//        RecognitionAudio audio = RecognitionAudio.newBuilder()
//                .setContent(audioBytes)
//                .build();
//
//        // Performs speech recognition on the audio file
//        RecognizeResponse response = speech.recognize(config, audio);
//        List<SpeechRecognitionResult> results = response.getResultsList();
//
//        for (SpeechRecognitionResult result: results) {
//            // There can be several alternative transcripts for a given chunk of speech. Just use the
//            // first (most likely) one here.
//            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
//            System.out.printf("Transcription: %s%n", alternative.getTranscript());
//            bookDesc.setText(alternative.getTranscript().toString());
//
//        }
//        try {
//            speech.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private File toFile(final URL url) throws MalformedURLException {
        // only correct way to convert the URL to a File object, also see issue #16
        // Do not use URLDecoder
        try {
            return new File(url.toURI());
        } catch (URISyntaxException ex) {
            throw new MalformedURLException(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            try {
                return new File(URLDecoder.decode(url.getFile(), "UTF-8"));
            } catch (Exception ex2) {
                throw new MalformedURLException(ex.getMessage());
            }
        }
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void audioPlayer(String path, String fileName){
        //set up MediaPlayer
        MediaPlayer mp = new MediaPlayer();

        try {
            mp.setDataSource(path + File.separator + fileName);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkComission(final String amount) {
        progressDialog = new MaterialDialog.Builder(Upload_Book_Screen.this)
                .content(getResources().getString(R.string.please_wait))
                .progress(true, 0)
                .show();

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,ApiRequest.BaseUrl+"admin_percentage",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("response", response);
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("false")) {
                             Log.e("percentage",jsonObject.getString("percentage"));
                                admin_Commision=jsonObject.getString("percentage");
                                 commisionView.setText( "You have to pay "+ admin_Commision +" % commision to UEBook" );
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> arguments = new HashMap<String, String>();
                arguments.put("amount", amount);
                return arguments;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


}













