package com.ue.uebook.LoginActivity.Login_Screen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Customview.CustomTextViewMedium;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.HomeActivity.HomeScreen;
import com.ue.uebook.LoginActivity.Pojo.RegistrationResponse;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;

public class SignUp_screen extends BaseActivity implements View.OnClickListener {
    private CustomTextViewMedium haveAcountBtn;
    private EditText usernameTv,passwordTv,emailTv,briefDescption;
    private Spinner actorType,genderSpinner;
    private String actorName,userGender;
    private Button signUpBtn;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_scren);
        haveAcountBtn=findViewById(R.id.haveAcountBtn);
        usernameTv=findViewById(R.id.usernameTv);
        passwordTv=findViewById(R.id.passwordTv);
        emailTv=findViewById(R.id.emailTv);
        signUpBtn=findViewById(R.id.signUpBtn);
        briefDescption=findViewById(R.id.briefDescption);
        signUpBtn.setOnClickListener(this);
        actorType = findViewById(R.id.actor_Spinner_signup);
        genderSpinner=findViewById(R.id.genderSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actorType.setSelection(0);
        actorType.setAdapter(adapter);
        ArrayAdapter<CharSequence> genderadapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        genderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setSelection(0);
        genderSpinner.setAdapter(genderadapter);
        SpannableString blueSpannable = new SpannableString("not have an account? Sign In");
        blueSpannable.setSpan(new ForegroundColorSpan(getColor(R.color.colorAccent)), 20,28, 0);
        haveAcountBtn.setText(blueSpannable);
        haveAcountBtn.setOnClickListener(this);
        actorType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                String label = parent.getItemAtPosition(arg2).toString();
                // Showing selected spinner item
                actorName = label;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                String label = parent.getItemAtPosition(arg2).toString();
                // Showing selected spinner item
                userGender = label;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        if (view==haveAcountBtn){
        gotoSignIn();
        }
        else if (view==signUpBtn){
            if (isvalidate())
            {
                registrationUser(usernameTv.getText().toString(), passwordTv.getText().toString(), emailTv.getText().toString(), actorName, userGender, "",briefDescption.getText().toString(),"");
            }
        }
    }

    private void gotoSignIn() {
        Intent intent = new Intent(this,SignInScreen.class);
        startActivity(intent);
        finish();
    }
    private void gotoHome() {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void registrationUser(String full_name, String password, String email, String publisher_type, String gender, String country, String about_me , String device_token) {
        ApiRequest request = new ApiRequest();
          showLoadingIndicator();
        request.requestforRegistration(full_name, password, email, publisher_type, gender, country, about_me, "SDHDHDD",new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("error", e.getLocalizedMessage());
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final RegistrationResponse form = gson.fromJson(myResponse, RegistrationResponse.class);
                new SessionManager(getApplicationContext()).storeUseruserID(form.getUser_data().getId());
                new SessionManager(getApplicationContext()).storeUserName(form.getUser_data().getUser_name());
                new SessionManager(getApplicationContext()).storeUserImage(form.getUser_data().getUrl());
                if (form.getError().equalsIgnoreCase("false")) {
                    Log.d("user_id", form.getUser_data().getId());
                    new SessionManager(getApplicationContext()).storeUserPublishtype(form.getUser_data().getPublisher_type());
                    new SessionManager(getApplicationContext()).storeUserLoginStatus(1);
                  runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
//
                            gotoHome();
//                            Toast.makeText(getContext(), "Succesfully Login", Toast.LENGTH_SHORT).show();
                        }


                  });
                }

            }
        });
    }
    private Boolean isvalidate() {
        String userNAme = usernameTv.getText().toString();
        String user_Email = emailTv.getText().toString();
        String userpass = passwordTv.getText().toString();
        String brief_des = briefDescption.getText().toString();


        if (!userNAme.isEmpty()) {
            if (!brief_des.isEmpty()) {
                if (!user_Email.isEmpty()) {
                    if (!userpass.isEmpty()) {
                        return true;
                    }
                            else {
                        passwordTv.setError("Enter your Password");
                        passwordTv.requestFocus();
                        passwordTv.setEnabled(true);
                        return false;
                    }
                } else {
                    emailTv.setError("Enter your Email ");
                    emailTv.requestFocus();
                    emailTv.setEnabled(true);
                    return false;
                }
            } else {
                briefDescption.setError("Enter your Brief Description");
                briefDescption.requestFocus();
                briefDescption.setEnabled(true);
                return false;
            }

        } else {
            usernameTv.setError("Enter your Username");
            usernameTv.requestFocus();
            usernameTv.setEnabled(true);
            return false;
        }
    }

}
