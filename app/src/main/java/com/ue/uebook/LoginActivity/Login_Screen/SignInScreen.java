package com.ue.uebook.LoginActivity.Login_Screen;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Selection;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Customview.CustomTextViewBold;
import com.ue.uebook.Customview.CustomTextViewMedium;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.ForgotPassWord.ForgotPasswordScreen;
import com.ue.uebook.HomeActivity.HomeScreen;
import com.ue.uebook.LoginActivity.Pojo.LoginResponse;
import com.ue.uebook.LoginActivity.Pojo.RegistrationResponse;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.ue.uebook.NetworkUtils.getInstance;

public class SignInScreen extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private CustomTextViewMedium nothaveacountbtn;
    private Button signInBtn,loginbyFaceBtn;
    private EditText emailTv, passwordTv;
    private ImageButton googleloginBtn, fbloginBtn,showPasswordTv;
    private CallbackManager callbackManager;
    private LinearLayout signinscreen;
    private GoogleApiClient mGoogleApiClient;
    private CustomTextViewBold forgotPasswordBtn;
    private int id;
    private Intent intent;
    private Boolean ishown=false;
    private static final int RC_SIGN_IN = 21;
    public static final int RequestPermissionCode = 1;
    private final int PICK_IMAGE_CAMERA = 121;
    private Bitmap bitmap;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);
        intent = getIntent();
        if (!checkPermission()){
            requestPermission();
        }
        id = intent.getIntExtra("id", 0);
        if (id == 1)
        {
            GoogleSignInOptions gso = new GoogleSignInOptions.
                    Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                    build();
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
            googleSignInClient.signOut();
        }
        loginbyFaceBtn=findViewById(R.id.loginbyFaceBtn);
        loginbyFaceBtn.setOnClickListener(this);
        showPasswordTv=findViewById(R.id.showPasswordTv);
        showPasswordTv.setOnClickListener(this);
        forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn);
        forgotPasswordBtn.setOnClickListener(this);
        nothaveacountbtn = findViewById(R.id.nothaveacountbtn);
        signinscreen = findViewById(R.id.signinscreen);
        callbackManager = CallbackManager.Factory.create();
        emailTv = findViewById(R.id.loginEmailTv);
        passwordTv = findViewById(R.id.loginPasswordTv);
        signInBtn = findViewById(R.id.signInBtn);
        googleloginBtn = findViewById(R.id.googleloginBtn);
        fbloginBtn = findViewById(R.id.facebookloginBtn);
        signInBtn.setOnClickListener(this);
        nothaveacountbtn.setOnClickListener(this);
        googleloginBtn.setOnClickListener(this);
        fbloginBtn.setOnClickListener(this);
        SpannableString blueSpannable = new SpannableString("Do you have an account? Sign Up");
        blueSpannable.setSpan(new ForegroundColorSpan(getColor(R.color.colorAccent)), 23, 31, 0);
        nothaveacountbtn.setText(blueSpannable);
        initializeGPlusSettings();
        showPasswordTv.setBackgroundResource(R.drawable.eyec);
        forgotPasswordBtn.setPaintFlags(forgotPasswordBtn.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if (v == nothaveacountbtn) {
            Intent intent = new Intent(SignInScreen.this, SignUp_screen.class);
            startActivity(intent);
        } else if (v == signInBtn) {
            if (isvalidate()) {
                String user = emailTv.getText().toString().trim();
                String userpass = passwordTv.getText().toString().trim();
                requestforLogin(user, userpass);
            }

        } else if (v == fbloginBtn) {
            if (getInstance(this).isConnectingToInternet()) {
                Fblogin();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    showLoadingIndicator();
                }

            } else {

                showSnackBar(signinscreen, getString(R.string.no_internet));
            }
        } else if (v == googleloginBtn) {
            if (getInstance(this).isConnectingToInternet()) {
                signIn();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    showLoadingIndicator();
                }
            } else {
                showSnackBar(signinscreen, getString(R.string.no_internet));
            }

        } else if (v == forgotPasswordBtn)
        {
            gotoForgotPasswordScren();
        }
        else if (v==showPasswordTv){
            if (!ishown)
            {
                ishown=true;
                passwordTv.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                int position = passwordTv.getText().toString().length();
                Selection.setSelection(passwordTv.getText(), position);
                showPasswordTv.setBackgroundResource(R.drawable.eye);
            }
            else
                {
                ishown=false;
                passwordTv.setTransformationMethod(PasswordTransformationMethod.getInstance());
                int position = passwordTv.getText().toString().length();
                Selection.setSelection(passwordTv.getText(), position);
                showPasswordTv.setBackgroundResource(R.drawable.eyec);
            }
        }
        else if (v==loginbyFaceBtn){
            if (!checkPermission()){
                requestPermission();
            }
            else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, PICK_IMAGE_CAMERA);
            }
        }
    }
    private void gotoForgotPasswordScren() {
        Intent intent = new Intent(this, ForgotPasswordScreen.class);
        startActivity(intent);
    }
    private void initializeGPlusSettings() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, SignInScreen.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                showSnackBar(signinscreen, "Login Error");
                hideLoadingIndicator();
            } else
                hideLoadingIndicator();
            loadUserProfile(currentAccessToken);
        }
    };
    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object != null) {
                    try {
                        final String first_name = object.getString("first_name");
                        final String last_name = object.getString("last_name");
                        final String email = object.getString("email");
                        String id = object.getString("id");
                        final String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                        runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void run() {
//                                showDialog(first_name, last_name, email, image_url);
                                registrationUser(first_name, " ", email, "Reader", "", "", "");
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,gender,id,taggable_friends");
        request.setParameters(parameters);
        request.executeAsync();
    }
    private void Fblogin() {
        callbackManager = CallbackManager.Factory.create();
        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        hideLoadingIndicator();

                    }
                    @Override
                    public void onCancel() {
                        Log.d("error", "On cancel");
                        hideLoadingIndicator();
                    }
                    @Override
                    public void onError(FacebookException error) {
                        Log.d("error", error.toString());
                        hideLoadingIndicator();
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                handleGPlusSignInResult(result);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == PICK_IMAGE_CAMERA) {

            try {
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                final byte[] bitmapdata = bytes.toByteArray();
                Log.e("imageForLogin",getPath(getImageUri(this,bitmap)));
//                UpdateUser(new File(getPath(getImageUri(this,bitmap))));
//                imagePreview(bitmap,new File(getPath(getImageUri(this,bitmap))));
                loginUserByFace(new File(getPath(getImageUri(this,bitmap))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        hideLoadingIndicator();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleGPlusSignInResult(GoogleSignInResult result) {
        Log.d("", "handleSignInResult:" + result.isSuccess());
        hideLoadingIndicator();
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            //Fetch values
            String personName = acct.getDisplayName();
            String email = acct.getEmail();
            String familyName = acct.getFamilyName();
            Uri uri = acct.getPhotoUrl();
//            showDialogGP(personName, "", email, acct.getPhotoUrl());
            registrationUser(personName, " ", email, "Reader", "", "", "");
        }
    }
    private Boolean isvalidate() {
        String user_NAme = emailTv.getText().toString();
        String userpass = passwordTv.getText().toString();
        if (!user_NAme.isEmpty()) {
            if (!userpass.isEmpty()) {
                return true;
            } else {
                passwordTv.setError("Enter your Password");
                passwordTv.requestFocus();
                passwordTv.setEnabled(true);
                return false;
            }

        } else {
            emailTv.setError("Enter your Username");
            emailTv.requestFocus();
            emailTv.setEnabled(true);
            return false;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void requestforLogin(final String user_name, final String password) {
        String url = null;
        showLoadingIndicator();
        url = "http://dnddemo.com/ebooks/api/v1/userLogin";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_name", user_name)
                .addFormDataPart("password", password)
                .addFormDataPart("device_type", "android")
                .addFormDataPart("device_token", FirebaseInstanceId.getInstance().getToken())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                final String myResponse = e.getLocalizedMessage();
                hideLoadingIndicator();
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                final String myResponse = response.body().string();
                hideLoadingIndicator();
                Gson gson = new GsonBuilder().create();
                final LoginResponse form = gson.fromJson(myResponse, LoginResponse.class);
                if (form.getError() == false)
                {
                    new SessionManager(getApplicationContext()).storeUserEmail(form.getResponse().getEmail());
                    new SessionManager(getApplicationContext()).storeUserPublishtype(form.getResponse().getPublisher_type());
                    new SessionManager(getApplicationContext()).storeUseruserID(form.getResponse().getId());
                    new SessionManager(getApplicationContext()).storeUserName(form.getResponse().getUser_name());
                    runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
//                            Toast.makeText(getContext(), "Succesfully Login", Toast.LENGTH_SHORT).show();
                            {
                                new SessionManager(getApplicationContext()).storeUserLoginStatus(1);
                            }
                            gotoHome();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            showDialogWithOkButton("Login Error", form.getMessage());
                            final PrettyDialog pDialog = new PrettyDialog(SignInScreen.this);
                            pDialog.setTitle("Login Error");
                            pDialog.setIcon(R.drawable.cancel);
                            pDialog.setMessage(form.getMessage());
                            pDialog.addButton(
                                    "OK",                    // button text
                                    R.color.pdlg_color_white,        // button text color
                                    R.color.colorPrimary,        // button background color
                                    new PrettyDialogCallback() {        // button OnClick listener
                                        @Override
                                        public void onClick() {
                                            pDialog.dismiss();
                                            hideLoadingIndicator();
                                        }
                                    }
                            )
                                    .show();
                        }
                    });
                }
            }
        });
    }
    public void gotoHome() {
        Intent intent = new Intent(this, HomeScreen.class);
        intent.putExtra("login", 1);
        startActivity(intent);
        finish();
    }
    private void registrationUser(final String full_name, String password, String email, String publisher_type, String gender, String country, String device_token) {
        ApiRequest request = new ApiRequest();
        hideLoadingIndicator();
//        progressDialog.setMessage("Please wait ...");
//
//        try {
//            progressDialog.show();        }
//        catch (WindowManager.BadTokenException e) {
//            //use a log message
//        }
        request.requestforRegistrationfb(full_name, password, email, publisher_type, gender, country, "", device_token, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("error", e.getLocalizedMessage());


            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String myResponse = response.body().string();

                Gson gson = new GsonBuilder().create();
                final RegistrationResponse form = gson.fromJson(myResponse, RegistrationResponse.class);
                new SessionManager(getApplicationContext()).storeUseruserID(form.getUser_data().getId());
                if (form.getError().equalsIgnoreCase("false") && form.getUser_data() != null) {
                    new SessionManager(getApplicationContext()).storeUserName(form.getUser_data().getUser_name());
                    new SessionManager(getApplicationContext()).storeUserImage(form.getUser_data().getUrl());
                    new SessionManager(getApplicationContext()).storeUserEmail(form.getUser_data().getEmail());
                    new SessionManager(getApplicationContext()).storeUserPublishtype(form.getUser_data().getPublisher_type());
                    new SessionManager(getApplicationContext()).storeUserLoginStatus(1);
                    runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            gotoHome();
                        }
                    });
                }
            }
        });
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(SignInScreen.this, new
                String[]{
                WRITE_EXTERNAL_STORAGE, RECORD_AUDIO,CAMERA,READ_EXTERNAL_STORAGE,ACCESS_FINE_LOCATION
        }, RequestPermissionCode);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean CameraPermission = grantResults[2] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean ReadPermission = grantResults[3] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean LocationPermission = grantResults[4] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (StoragePermission && RecordPermission && CameraPermission && ReadPermission && LocationPermission) {
                        Toast.makeText(SignInScreen.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SignInScreen.this,"Permission Denied",Toast.LENGTH_LONG).show();
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
        int result2= ContextCompat.checkSelfPermission(getApplicationContext(),
                CAMERA);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(),
                READ_EXTERNAL_STORAGE);
        int result4= ContextCompat.checkSelfPermission(getApplicationContext(),
                ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED &&
                result2 == PackageManager.PERMISSION_GRANTED &&
                result3 == PackageManager.PERMISSION_GRANTED&&
                result4 == PackageManager.PERMISSION_GRANTED ;
    }
    private void imagePreview(final Bitmap file,final File userFile) {
        final Dialog previewDialog = new Dialog(this);
        previewDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        previewDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout
                , null));
        ImageView imageView = previewDialog.findViewById(R.id.image_view);
        imageView.setImageBitmap(file);
//        GlideUtils.loadImage(SignInScreen.this, "http://dnddemo.com/ebooks/api/v1/upload/" + file, imageView, R.drawable.user_default, R.drawable.user_default);
        Button ok_Btn = previewDialog.findViewById(R.id.buton_ok);

        ok_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    requestforLogin("de", "123");


                }
                previewDialog.dismiss();
            }
        });
        previewDialog.show();
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loginUserByFace(File imagepath) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforLoginByFace( imagepath, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                hideLoadingIndicator();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SignInScreen.this,"Please retake the picture",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final LoginResponse form = gson.fromJson(myResponse, LoginResponse.class);
                if (form.getError() == false && form.getResponse()!=null)
                {
                    new SessionManager(getApplicationContext()).storeUserEmail(form.getResponse().getEmail());
                    new SessionManager(getApplicationContext()).storeUserPublishtype(form.getResponse().getPublisher_type());
                    new SessionManager(getApplicationContext()).storeUseruserID(form.getResponse().getId());
                    new SessionManager(getApplicationContext()).storeUserName(form.getResponse().getUser_name());
                    runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            {
                                new SessionManager(getApplicationContext()).storeUserLoginStatus(1);
                            }
                            gotoHome();
                        }
                    });
                }
                else {
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           final PrettyDialog pDialog = new PrettyDialog(SignInScreen.this);
                           pDialog.setTitle("Login Error");
                           pDialog.setIcon(R.drawable.cancel);
                           pDialog.setMessage("Invalid User");
                           pDialog.addButton(
                                   "OK",                    // button text
                                   R.color.pdlg_color_white,        // button text color
                                   R.color.colorPrimary,        // button background color
                                   new PrettyDialogCallback() {        // button OnClick listener
                                       @Override
                                       public void onClick() {
                                           pDialog.dismiss();
                                           hideLoadingIndicator();
                                       }
                                   }
                           )
                                   .show();
                       }
                   });
                }
            }
        });
    }

}
