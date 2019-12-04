package com.ue.uebook.LoginActivity.Login_Screen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

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
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Customview.CustomTextViewMedium;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.HomeActivity.HomeScreen;
import com.ue.uebook.LoginActivity.Pojo.LoginResponse;
import com.ue.uebook.LoginActivity.Pojo.RegistrationResponse;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.ue.uebook.NetworkUtils.getInstance;

public class SignInScreen extends BaseActivity implements View.OnClickListener {
    private CustomTextViewMedium nothaveacountbtn;
    private Button signInBtn;
    private EditText emailTv,passwordTv;
    private ImageButton googleloginBtn,fbloginBtn;
    private CallbackManager callbackManager;
    private LinearLayout signinscreen;
    private static final int RC_SIGN_IN = 21;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);
        nothaveacountbtn=findViewById(R.id.nothaveacountbtn);
        signinscreen=findViewById(R.id.signinscreen);
        callbackManager = CallbackManager.Factory.create();
        emailTv=findViewById(R.id.loginEmailTv);
        passwordTv=findViewById(R.id.loginPasswordTv);
        signInBtn=findViewById(R.id.signInBtn);
        googleloginBtn=findViewById(R.id.googleloginBtn);
        fbloginBtn=findViewById(R.id.facebookloginBtn);
        signInBtn.setOnClickListener(this);
        nothaveacountbtn.setOnClickListener(this);
        googleloginBtn.setOnClickListener(this);
        fbloginBtn.setOnClickListener(this);
        SpannableString blueSpannable = new SpannableString("Do you have an account? Sign In");
        blueSpannable.setSpan(new ForegroundColorSpan(getColor(R.color.colorAccent)), 23,31, 0);
        nothaveacountbtn.setText(blueSpannable);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if (v==nothaveacountbtn){
            Intent intent = new Intent(SignInScreen.this,SignUp_screen.class);
            startActivity(intent);
        }
        else if (v==signInBtn){
            if (isvalidate())
            {
                String user = emailTv.getText().toString().trim();
                String userpass = passwordTv.getText().toString().trim();
                requestforLogin(user, userpass);
            }

        }
        else if (v==fbloginBtn)
        {
            if (getInstance(this).isConnectingToInternet()) {
                Fblogin();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    showLoadingIndicator();
                }

            } else {

                showSnackBar(signinscreen, getString(R.string.no_internet));
            }
        }
        else if (v==googleloginBtn)
        {


        }
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
                if (object!=null){
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
                                registrationUser(first_name, " ", email, "Reader", "","","");

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                handleGPlusSignInResult(result);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
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
                .addFormDataPart("device_type","android")
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
                Gson gson = new GsonBuilder().create();
                final LoginResponse form = gson.fromJson(myResponse, LoginResponse.class);
                if (form.getError() == false) {
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
                            final PrettyDialog pDialog=  new PrettyDialog(SignInScreen.this);
                            pDialog  .setTitle("Login Error");
                            pDialog.setIcon(R.drawable.cancel);
                            pDialog.setMessage(form.getMessage());
                            pDialog   .addButton(
                                    "OK",					// button text
                                    R.color.pdlg_color_white,		// button text color
                                    R.color.colorPrimary,		// button background color
                                    new PrettyDialogCallback() {		// button OnClick listener
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
        intent.putExtra("login",1);
      startActivity(intent);
        finish();
    }
    private void registrationUser(final String full_name, String password, String email, String publisher_type, String gender, String country,String device_token) {
        ApiRequest request = new ApiRequest();
        hideLoadingIndicator();
//        progressDialog.setMessage("Please wait ...");
//
//        try {
//            progressDialog.show();        }
//        catch (WindowManager.BadTokenException e) {
//            //use a log message
//        }
        request.requestforRegistration(full_name, password, email, publisher_type, gender,country,"" ,device_token,new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("error",e.getLocalizedMessage());


            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String myResponse = response.body().string();

                Gson gson = new GsonBuilder().create();
                final RegistrationResponse form = gson.fromJson(myResponse, RegistrationResponse.class);
                new SessionManager(getApplicationContext()).storeUseruserID(form.getUser_data().getId());
                if (form.getError().equalsIgnoreCase("false")&&form.getUser_data()!=null) {
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

}
