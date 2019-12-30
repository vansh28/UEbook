package com.ue.uebook.LoginActivity.Login_Screen;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.facebook.CallbackManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Commonutils;
import com.ue.uebook.Customview.CustomTextViewMedium;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.LoginActivity.Pojo.RegistrationResponse;
import com.ue.uebook.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class SignUp_screen extends BaseActivity implements View.OnClickListener {
    private CustomTextViewMedium haveAcountBtn;
    private EditText usernameTv,passwordTv,emailTv,briefDescption;
    private Spinner actorType,genderSpinner;
    private String actorName,userGender;
    private Button signUpBtn;
    private String actorname="Select kind of actor";
    private String usergender="Select gender";
    private LinearLayout upload_faceDetect_image;
    private final int PICK_IMAGE_CAMERA = 121;
    private RelativeLayout faceimage;
     private ImageView faceimage_preview;
    private Bitmap bitmap;
    private CallbackManager callbackManager;
    private File faceFile;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_scren);
        haveAcountBtn=findViewById(R.id.haveAcountBtn);
        faceimage_preview=findViewById(R.id.faceimage_preview);
        faceimage=findViewById(R.id.faceimage);
        usernameTv=findViewById(R.id.usernameTv);
        passwordTv=findViewById(R.id.passwordTv);
        emailTv=findViewById(R.id.emailTv);
        signUpBtn=findViewById(R.id.signUpBtn);
        callbackManager = CallbackManager.Factory.create();
        briefDescption=findViewById(R.id.briefDescption);
        upload_faceDetect_image=findViewById(R.id.upload_faceDetect_image);
        upload_faceDetect_image.setOnClickListener(this);
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
                actorName = label;
                actorname=actorName;
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
                userGender = label;
                usergender=userGender;
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
                registrationUser(usernameTv.getText().toString(), passwordTv.getText().toString(), emailTv.getText().toString(), actorName, userGender, "",briefDescption.getText().toString(),"",faceFile);
            }
        }
        else if (view==upload_faceDetect_image){

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, PICK_IMAGE_CAMERA);
        }
    }

    private void gotoSignIn() {
        Intent intent = new Intent(this,SignInScreen.class);
        startActivity(intent);
        finish();
    }
    private void gotoHome() {
        Intent intent = new Intent(this, SignInScreen.class);
        startActivity(intent);
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void registrationUser(String full_name, String password, String email, String publisher_type, String gender, String country, String about_me , String device_token,File face_detect_image) {
        ApiRequest request = new ApiRequest();
          showLoadingIndicator();
        request.requestforRegistration(full_name, password, email, publisher_type, gender, country, about_me, "SDHDHDD",face_detect_image,new okhttp3.Callback() {
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
//                new SessionManager(getApplicationContext()).storeUseruserID(form.getUser_data().getId());
//                new SessionManager(getApplicationContext()).storeUserName(form.getUser_data().getUser_name());
//                new SessionManager(getApplicationContext()).storeUserImage(form.getUser_data().getUrl());
                if (form.getError().equalsIgnoreCase("false")) {
                    Log.d("user_id", form.getUser_data().getId());
//                    new SessionManager(getApplicationContext()).storeUserPublishtype(form.getUser_data().getPublisher_type());
//                    new SessionManager(getApplicationContext()).storeUserLoginStatus(1);
                  runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
//
                            Toast.makeText(SignUp_screen.this,"Successfully User Created",Toast.LENGTH_SHORT).show();
                            gotoHome();
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
                if (Commonutils.isValidEmail(user_Email)) {
                    if (!userpass.isEmpty()) {
                            if (usergender.equalsIgnoreCase("Select gender")){
                                TextView errorText = (TextView)genderSpinner.getSelectedView();
                                errorText.setError("");
                                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                                errorText.setText("Select your gender");
                                return false;
                            }
                            else {
                                if (actorname.equalsIgnoreCase("Select kind of actor")){
                                    TextView errorText = (TextView)actorType.getSelectedView();
                                    errorText.setError("");
                                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                                    errorText.setText("Select Actor type");
                                    return false;
                                }
                                else {
                                    return true;
                                }
                            }
                    }

                    else {
                        passwordTv.setError("Enter your Password");
                        passwordTv.requestFocus();
                        passwordTv.setEnabled(true);
                        return false;
                    }
                } else {
                    emailTv.setError("Enter your  Email ");
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CAMERA) {

            try {
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                final byte[] bitmapdata = bytes.toByteArray();
//                UpdateUser(new File(getPath(getImageUri(this,bitmap))));
//                imagePreview(bitmap,new File(getPath(getImageUri(this,bitmap))));
                   faceimage.setVisibility(View.VISIBLE);
                   faceimage_preview.setImageBitmap(bitmap);
//                loginUserByFace(new File(getPath(getImageUri(this,bitmap))));
                faceFile=new File(getPath(getImageUri(this,bitmap)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
}
