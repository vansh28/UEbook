package com.ue.uebook.LoginActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.HomeActivity.HomeScreen;
import com.ue.uebook.LoginActivity.Fragment.SignIn_Fragment;
import com.ue.uebook.LoginActivity.Fragment.SignUp_Fragment;
import com.ue.uebook.MainActivity;
import com.ue.uebook.NetworkUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.ue.uebook.NetworkUtils.getInstance;

public class LoginScreen extends BaseActivity implements View.OnClickListener, SignUp_Fragment.OnFragmentInteractionListener, SignIn_Fragment.OnFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 21;
    private Button signUp_btn, signIn_btn, continueToLogin;
    private LinearLayout google_login_btn, facebook_login_btn, login_screen;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        signIn_btn = findViewById(R.id.signIn_btn);
        signUp_btn = findViewById(R.id.signUp_btn);
        google_login_btn = findViewById(R.id.google_login_btn);
        facebook_login_btn = findViewById(R.id.facebook_login_btn);
        login_screen = findViewById(R.id.login_screen);
        facebook_login_btn.setOnClickListener(this);
        google_login_btn.setOnClickListener(this);
        signUp_btn.setOnClickListener(this);
        signIn_btn.setOnClickListener(this);
        replaceFragment(new SignUp_Fragment());

        callbackManager = CallbackManager.Factory.create();
        initializeGPlusSettings();

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

            replaceFragment(new SignUp_Fragment());
            signUp_btn.setBackgroundResource(R.drawable.active_signin_border);
            signIn_btn.setBackgroundResource(R.drawable.non_active_btn_signin);
            signUp_btn.setTextColor(Color.parseColor("#D31145"));
            signIn_btn.setTextColor(Color.parseColor("#000000"));
        } else if (view == signIn_btn) {

            replaceFragment(new SignIn_Fragment());
            signIn_btn.setBackgroundResource(R.drawable.active_signin_border);
            signUp_btn.setBackgroundResource(R.drawable.non_active_btn_signin);
            signIn_btn.setTextColor(Color.parseColor("#D31145"));
            signUp_btn.setTextColor(Color.parseColor("#000000"));

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
            signIn();

        }

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


                    }

                    @Override
                    public void onCancel() {
                        Log.d("error", "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("error", error.toString());
                    }
                });
    }

    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
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
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
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


    public void showDialog(String first_name, String last_name, String email, final String image) {
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
                new SessionManager(getApplicationContext()).storeUserImage(image);
                gotoHome();
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
        }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }

    }
    public void showDialogGP(String first_name, String last_name, String email, final Uri image) {
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
           Glide.with(getApplicationContext()).load(image).into(circleImageView);





        continueToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new SessionManager(getApplicationContext()).storeUserImage(image);
                gotoHome();
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
        }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }
    }
    public void gotoHome() {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleGPlusSignInResult(GoogleSignInResult result) {
        Log.d("", "handleSignInResult:" + result.isSuccess());
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

}
