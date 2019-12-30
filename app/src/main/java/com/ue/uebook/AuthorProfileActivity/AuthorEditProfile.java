package com.ue.uebook.AuthorProfileActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.ImageUtils;
import com.ue.uebook.LoginActivity.Pojo.LoginResponse;
import com.ue.uebook.LoginActivity.Pojo.RegistrationResponse;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.File;
import java.io.IOException;

import static com.ue.uebook.R.id.country__userProfile;

public class AuthorEditProfile extends BaseActivity implements View.OnClickListener, ImageUtils.ImageAttachmentListener {
    private ImageButton backbtn;
    private ImageView profile_image_user;
    ImageUtils imageUtils;
    private File imagefile;
    private String filePath, selectedvideoPath;
    private String fileName;
    private String encodedString;
    private String uriData;
    private Bitmap bitmap;
    private ProgressDialog dialog;
    private String image;
    private EditText username, useremail, userpassword, country__user, about_me;
    private Spinner actor_Spinner;
    private Button update__userProfile;
    private String actortype;
    private TextView usernameview, address_user;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_edit_profile);
        backbtn = findViewById(R.id.back_profile);
        profile_image_user = findViewById(R.id.profile_image_user);
        profile_image_user.setOnClickListener(this);
        backbtn.setOnClickListener(this);
        dialog = new ProgressDialog(this);
        imageUtils = new ImageUtils(this);
        actor_Spinner = findViewById(R.id.actor_Spinner);
        username = findViewById(R.id.username_edit_text);
        useremail = findViewById(R.id.email_userProfile);
        userpassword = findViewById(R.id.password__userProfile);
        update__userProfile = findViewById(R.id.update__userProfile);
        about_me = findViewById(R.id.brief_desc);
        country__user = findViewById(country__userProfile);
        update__userProfile.setOnClickListener(this);
        image = new SessionManager(getApplicationContext()).getUserimage();
        usernameview = findViewById(R.id.username);
        address_user = findViewById(R.id.address_user);
        usernameview.setText(new SessionManager(getApplicationContext()).getUserName());
        String address = new SessionManager(getApplicationContext()).getUserLocation();
        if (address.length() > 0) {
            address_user.setText(address);
        }
        if (!image.isEmpty()) {
            GlideUtils.loadImage(AuthorEditProfile.this, "http://dnddemo.com/ebooks/api/v1/upload/" + image, profile_image_user, R.drawable.user_default, R.drawable.user_default);
        } else {
            profile_image_user.setImageResource(R.drawable.user_default);
        }
        UserInfo(new SessionManager(getApplicationContext()).getUserID());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        actor_Spinner.setAdapter(adapter);
        actor_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                String label = parent.getItemAtPosition(arg2).toString();
                // Showing selected spinner item
                actortype = label;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if (v == backbtn) {
            finish();
        } else if (v == profile_image_user) {
            imageUtils.imagepicker(1);
        }
        else if (v==update__userProfile){
            UpdateUser(userpassword.getText().toString(), useremail.getText().toString(), actortype, country__user.getText().toString(), about_me.getText().toString());
        }
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


    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        this.bitmap = file;
        this.fileName = filename;
        String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageUtils.createImage(file, filename, path, false);
        filePath = getRealPathFromURI(uri.getPath());
        imagefile = (new File(imageUtils.getPath(uri)));
        profile_image_user.setImageBitmap(bitmap);
        UpdateUser(imagefile);
//        requestforUploadBook(new File(imageUtils.getPath(uri)));
    }


    private void UpdateUser(File imagepath) {
        ApiRequest request = new ApiRequest();
        dialog.setMessage("please wait");
        dialog.show();
        request.requestforUpdateProfilePic(new SessionManager(getApplicationContext()).getUserID(), imagepath, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                dialog.dismiss();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                dialog.dismiss();
                String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final RegistrationResponse form = gson.fromJson(myResponse, RegistrationResponse.class);
                if (form.getError().equalsIgnoreCase("false") && form.getUser_data() != null) {
                    new SessionManager(getApplicationContext()).storeUserPublishtype(form.getUser_data().getPublisher_type());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new SessionManager(getApplicationContext()).storeUserImage(form.getUser_data().getUrl());
                        }
                    });

                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void UserInfo(String userID) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforgetUserInfo(userID, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                String myResponse = response.body().string();

                Gson gson = new GsonBuilder().create();
                final LoginResponse form = gson.fromJson(myResponse, LoginResponse.class);
                if (form.getError() == false) {
                   runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            username.setText(form.getResponse().getUser_name());
                            useremail.setText(form.getResponse().getEmail());
                            userpassword.setText(form.getResponse().getPassword());
                            country__user.setText(form.getResponse().getCountry());
                            about_me.setText(form.getResponse().getAbout_me());
                            if (form.getResponse().getPublisher_type().equalsIgnoreCase("Reader")) {
                                actor_Spinner.setSelection(1);
                            } else if (form.getResponse().getPublisher_type().equalsIgnoreCase("Writer")) {
                                actor_Spinner.setSelection(2);
                            } else if (form.getResponse().getPublisher_type().equalsIgnoreCase("Publish House")) {
                                actor_Spinner.setSelection(3);
                            } else if (form.getResponse().getPublisher_type().equalsIgnoreCase("Reader and Writer")) {
                                actor_Spinner.setSelection(4);
                            }
                        }
                    });
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void UpdateUser(String password, String email, String publisher_type, String country, String about_me) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforUpdateProfile(new SessionManager(getApplicationContext()).getUserID(), password, email, publisher_type, country, about_me, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                RegistrationResponse form = gson.fromJson(myResponse, RegistrationResponse.class);
                if (form.getError().equals("false") && form.getUser_data() != null) {
                    new SessionManager(getApplicationContext()).storeUserPublishtype(form.getUser_data().getPublisher_type());

                   runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UserInfo(new SessionManager(getApplicationContext()).getUserID());
                        }
                    });

                }
            }
        });
    }


}
