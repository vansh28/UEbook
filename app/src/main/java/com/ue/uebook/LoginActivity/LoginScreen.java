package com.ue.uebook.LoginActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.Data.NetworkAPI;
import com.ue.uebook.Data.NetworkService;
import com.ue.uebook.ForgotPassWord.ForgotPasswordScreen;
import com.ue.uebook.HomeActivity.HomeScreen;
import com.ue.uebook.LoginActivity.Fragment.SignIn_Fragment;
import com.ue.uebook.LoginActivity.Fragment.SignUp_Fragment;
import com.ue.uebook.LoginActivity.Pojo.RegistrationBody;
import com.ue.uebook.LoginActivity.Pojo.RegistrationResponse;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ue.uebook.NetworkUtils.getInstance;

public class LoginScreen extends BaseActivity implements View.OnClickListener, SignUp_Fragment.OnFragmentInteractionListener, SignIn_Fragment.OnFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 21;
    private Button signUp_btn, signIn_btn, continueToLogin;
    private LinearLayout google_login_btn, facebook_login_btn, login_screen;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private TextView have_Account_btn, forgotpasswordBtn;
    private Boolean issignup = true;
    private NetworkAPI networkAPI;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        networkAPI = NetworkService.getAPI().create(NetworkAPI.class);
        dialog= new ProgressDialog(this);
        signIn_btn = findViewById(R.id.signIn_btn);
        signUp_btn = findViewById(R.id.signUp_btn);
        google_login_btn = findViewById(R.id.google_login_btn);
        facebook_login_btn = findViewById(R.id.facebook_login_btn);
        login_screen = findViewById(R.id.login_screen);
        have_Account_btn = findViewById(R.id.have_Account_btn);
        forgotpasswordBtn = findViewById(R.id.forgotpasswordBtn);
        forgotpasswordBtn.setOnClickListener(this);
        facebook_login_btn.setOnClickListener(this);
        google_login_btn.setOnClickListener(this);
        signUp_btn.setOnClickListener(this);
        signIn_btn.setOnClickListener(this);
        replaceFragment(new SignUp_Fragment());
        callbackManager = CallbackManager.Factory.create();
        initializeGPlusSettings();
        have_Account_btn.setOnClickListener(this);
        hideLoadingIndicator();



    }

    private void initializeGPlusSettings() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, LoginScreen.this)
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
                showSnackBar(login_screen, "Login Error");
                hideLoadingIndicator();
            } else
                hideLoadingIndicator();
            loadUserProfile(currentAccessToken);
        }
    };

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_Fragment, fragment, fragment.getClass().getName());
        ft.commit();
    }
    @Override
    public void onClick(View view) {
        if (view == signUp_btn) {
            have_Account_btn.setText("I have an Account, Login Now");
            forgotpasswordBtn.setVisibility(View.GONE);
            replaceFragment(new SignUp_Fragment());
            signUp_btn.setBackgroundResource(R.drawable.active_signin_border);
            signIn_btn.setBackgroundResource(R.drawable.non_active_btn_signin);
            signUp_btn.setTextColor(Color.parseColor("#D31145"));
            signIn_btn.setTextColor(Color.parseColor("#000000"));
            issignup = true;
        } else if (view == signIn_btn) {
            have_Account_btn.setText("Create an Account");
            forgotpasswordBtn.setVisibility(View.VISIBLE);
            replaceFragment(new SignIn_Fragment());
            signIn_btn.setBackgroundResource(R.drawable.active_signin_border);
            signUp_btn.setBackgroundResource(R.drawable.non_active_btn_signin);
            signIn_btn.setTextColor(Color.parseColor("#D31145"));
            signUp_btn.setTextColor(Color.parseColor("#000000"));
            issignup = false;
        } else if (view == facebook_login_btn) {
            if (getInstance(this).isConnectingToInternet()) {
                Fblogin();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    showLoadingIndicator();
                }

            } else {

                showSnackBar(login_screen, getString(R.string.no_internet));
            }

        } else if (view == google_login_btn) {
            if (getInstance(this).isConnectingToInternet()) {
                signIn();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    showLoadingIndicator();
                }
            } else {
                showSnackBar(login_screen, getString(R.string.no_internet));
            }
        } else if (view == have_Account_btn) {
            if (issignup) {
                have_Account_btn.setText("Create an Account");
                forgotpasswordBtn.setVisibility(View.VISIBLE);
                replaceFragment(new SignIn_Fragment());
                signIn_btn.setBackgroundResource(R.drawable.active_signin_border);
                signUp_btn.setBackgroundResource(R.drawable.non_active_btn_signin);
                signIn_btn.setTextColor(Color.parseColor("#D31145"));
                signUp_btn.setTextColor(Color.parseColor("#000000"));
                issignup = false;
            } else {
                have_Account_btn.setText("I have an Account, Login Now");
                forgotpasswordBtn.setVisibility(View.GONE);
                replaceFragment(new SignUp_Fragment());
                signUp_btn.setBackgroundResource(R.drawable.active_signin_border);
                signIn_btn.setBackgroundResource(R.drawable.non_active_btn_signin);
                signUp_btn.setTextColor(Color.parseColor("#D31145"));
                signIn_btn.setTextColor(Color.parseColor("#000000"));
                issignup = true;
            }
        } else if (view == forgotpasswordBtn) {
              gotoForgotPassword();
        }
    }
    public void gotoForgotPassword() {
        Intent intent = new Intent(this, ForgotPasswordScreen.class);
        startActivity(intent);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
                            @Override
                            public void run() {
                                showDialog(first_name, last_name, email, image_url);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGPlusSignInResult(result);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void showDialog(final String first_name, String last_name, final String email, final String image) {
        hideLoadingIndicator();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.logindialog);


        TextView txtName = dialog.findViewById(R.id.profile_name);
        TextView txtEmail = dialog.findViewById(R.id.profile_email);
        ImageView circleImageView = dialog.findViewById(R.id.profile_pic);
        ImageButton cancel_dialog = dialog.findViewById(R.id.cancel_dialog);
        continueToLogin = dialog.findViewById(R.id.continueToLogin);
        txtEmail.setText(email);
        txtName.setText(first_name + " " + last_name);
        Glide.with(getApplicationContext()).load(image).into(circleImageView);
        continueToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registrationUser(first_name, " ", email, "Reader", "","");
                dialog.dismiss();
            }
        });

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
            //use a log message
        }

    }

    public void showDialogGP(final String first_name, String last_name, final String email, final Uri image) {
        hideLoadingIndicator();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.logindialog);


        TextView txtName = dialog.findViewById(R.id.profile_name);
        TextView txtEmail = dialog.findViewById(R.id.profile_email);
        ImageView circleImageView = dialog.findViewById(R.id.profile_pic);
        ImageButton cancel_dialog = dialog.findViewById(R.id.cancel_dialog);
        continueToLogin = dialog.findViewById(R.id.continueToLogin);
        txtEmail.setText(email);
        txtName.setText(first_name + " " + last_name);


//        GlideUtils.loadImage(this,image,circleImageView,R.drawable.user,R.drawable.user);

//      if (image.)
        if (image != null) {
            Glide.with(getApplicationContext()).load(image).into(circleImageView);
        } else {

            circleImageView.setImageResource(R.drawable.user_default);
        }

        continueToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new SessionManager(getApplicationContext()).storeUserImage(image);
                registrationUser(first_name, " ", email, "Reader", "","");
                dialog.dismiss();
            }
        });

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
            //use a log message
        }
    }

    public void gotoHome() {
        Intent intent = new Intent(this, HomeScreen.class);
        intent.putExtra("login",1);
         startActivity(intent);
        finish();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        hideLoadingIndicator();

    }

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
            showDialogGP(personName, "", email, acct.getPhotoUrl());

        }
    }

    public void fetchUserCredentials(final String user_name, final String password, final String email, final String publisher_type, String gender) {
        Call<RegistrationResponse> registrationResponseCall = networkAPI.userRegistration(new RegistrationBody(user_name, password, email, publisher_type, gender));
        registrationResponseCall.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegistrationResponse> call, @NonNull Response<RegistrationResponse> response) {
                RegistrationResponse registrationResponse = response.body();
                if (registrationResponse != null) {

                    Log.d("pub", registrationResponse.getUser_data().getPublisher_type());
                    new SessionManager(getApplicationContext()).storeUserPublishtype(registrationResponse.getUser_data().getPublisher_type());


                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call<RegistrationResponse> call, @NonNull Throwable t) {
                Log.d("error", "error");
            }
        });
    }




    private void registrationUser(String full_name, String password, String email, String publisher_type, String gender,String country) {
        ApiRequest request = new ApiRequest();
        dialog.setMessage("please wait");
        dialog.show();
        request.requestforRegistration(full_name, password, email, publisher_type, gender,country,"" ,new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("error",e.getLocalizedMessage());
                dialog.dismiss();

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                dialog.dismiss();
                String myResponse = response.body().string();

                Gson gson = new GsonBuilder().create();
                RegistrationResponse form = gson.fromJson(myResponse, RegistrationResponse.class);
                new SessionManager(getApplicationContext()).storeUseruserID(form.getUser_data().getId());
                if (form.getError().equalsIgnoreCase("false")&&form.getUser_data()!=null) {
                    new SessionManager(getApplicationContext()).storeUserName(form.getUser_data().getUser_name());
                    new SessionManager(getApplicationContext()).storeUserImage(form.getUser_data().getUrl());

                    new SessionManager(getApplicationContext()).storeUserPublishtype(form.getUser_data().getPublisher_type());
                    new SessionManager(getApplicationContext()).storeUserLoginStatus(1);
                runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(LoginScreen.this, "Succesfully Login", Toast.LENGTH_SHORT).show();
                        }
                    });
                    gotoHome();
                }
            }
        });
    }





}
