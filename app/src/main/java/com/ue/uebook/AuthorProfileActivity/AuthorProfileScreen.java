package com.ue.uebook.AuthorProfileActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.AuthorProfileActivity.pojo.AuthorData;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.DeatailActivity.Book_Detail_Screen;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;

public class AuthorProfileScreen extends BaseActivity implements View.OnClickListener ,Author_BookListAdapter.BookItemClick{
    private ImageView backbtn, author_profile;
    private RecyclerView post_list;
    private TextView author_name, author_desc, author_post_count, author_follower, author_following,publisher_type;
    private Button follow_To_author, emailToAuthor,editProfile;
    private Author_BookListAdapter author_postAdapter;
    private Intent intent;
    private String userID;
    private SwipeRefreshLayout pullTorfrsh;
    private LinearLayout editt_profile_view,sendRequest_view;
    private int id;
    private String authorEmail=" ";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_profile_screen);
        intent = getIntent();
        userID=intent.getStringExtra("userID");
        editt_profile_view=findViewById(R.id.editt_profile_view);
        sendRequest_view=findViewById(R.id.sendRequest_view);
        editProfile=findViewById(R.id.editt_profile);
        editProfile.setOnClickListener(this);
        backbtn = findViewById(R.id.back_author_profile);
        author_profile = findViewById(R.id.profile_author);
        pullTorfrsh=findViewById(R.id.swipe_refresh_layout);
        post_list = findViewById(R.id.author_post_list);
        author_name = findViewById(R.id.author_name);
        author_desc = findViewById(R.id.author_desc);
        author_post_count = findViewById(R.id.author_postview);
        author_follower = findViewById(R.id.author_followers);
        author_following = findViewById(R.id.author_following);
        follow_To_author = findViewById(R.id.follow_btn);
        emailToAuthor = findViewById(R.id.email_btn);
        publisher_type=findViewById(R.id.publisher_type);


         id = intent.getIntExtra("id",0);
        if (id==1){
            editt_profile_view.setVisibility(View.VISIBLE);
            sendRequest_view.setVisibility(View.GONE);
        }
        else {
            editt_profile_view.setVisibility(View.GONE);
            sendRequest_view.setVisibility(View.VISIBLE);
        }

        backbtn.setOnClickListener(this);
        follow_To_author.setOnClickListener(this);
        emailToAuthor.setOnClickListener(this);
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        post_list.setLayoutManager(linearLayoutManagerPopularList);
       pullTorefreshswipe();
        post_list.setNestedScrollingEnabled(false);
        AuthorInfo(userID);


    }
    private void pullTorefreshswipe(){
        pullTorfrsh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onRefresh() {
                AuthorInfo(userID);
                pullTorfrsh.setRefreshing(false);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        if (view == backbtn) {
            finish();
        } else if (view == follow_To_author) {

//            sendRequest(new SessionManager(getApplicationContext()).getUserID(),userID);



        } else if (view == emailToAuthor) {

            showEmailDialog();
        }
        else if (view== editProfile){
         Intent intent = new Intent(this,AuthorEditProfile.class);
         startActivity(intent);


        }

    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void AuthorInfo(String userID) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforgetUploadByInfo(userID, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                String myResponse = response.body().string();

                Gson gson = new GsonBuilder().create();
                final AuthorData form = gson.fromJson(myResponse, AuthorData.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (form.getData()!=null){
                        author_name.setText(form.getData().getUser_name());
                        author_desc.setText(form.getData().getAbout_me());
                        publisher_type.setText(form.getData().getPublisher_type());
                        GlideUtils.loadImage(AuthorProfileScreen.this, "http://dnddemo.com/ebooks/api/v1/upload/" + form.getData().getUrl(), author_profile, R.drawable.user_default, R.drawable.user_default);
                        authorEmail=form.getData().getEmail();
                        if (form.getBooklist()!=null){
                            post_list.setVisibility(View.VISIBLE);
                            author_postAdapter = new Author_BookListAdapter(AuthorProfileScreen.this,form.getBooklist(),id);
                            post_list.setAdapter(author_postAdapter);
                            author_postAdapter.setItemClickListener(AuthorProfileScreen.this);
                            author_post_count.setText(String.valueOf(form.getBooklist().size()));

                        }
                        else {
                            post_list.setVisibility(View.GONE);
                            author_post_count.setText("0");
                        }

                    }}
                });

            }

        });
    }

    @Override
    public void onItemClick_PopularBook(int position, String book_id) {
        Intent intent = new Intent(AuthorProfileScreen.this, Book_Detail_Screen.class);
        intent.putExtra("book_id", book_id);
        intent.putExtra("position",position);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void OndeleteBook(String book_id) {
        deleteBook(book_id);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendRequest(String userID,String friendId) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforSendFriendRequest(userID, friendId,new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                String myResponse = response.body().string();

            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void deleteBook(String book_id) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforDeleteAuthorBook( book_id, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                String myResponse = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AuthorInfo(userID);
                    }
                });
            }
        });
    }


    private void showDialog(){

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.emaildialog, null);
        Button btn= alertLayout.findViewById(R.id.send_btn);
        final EditText subject = alertLayout.findViewById(R.id.subjectemail);
        final EditText body = alertLayout.findViewById(R.id.body_contact);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"dfdf",Toast.LENGTH_SHORT);

            }
        });
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
      alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        });
        AlertDialog dialog = alert.create();

        dialog.show();



    }
    private void sendEmail(){

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + "recipient@example.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My email's subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "My email's body");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AuthorProfileScreen.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendMail(String from,String mailto,String subject,String msg) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforSendMail( from,mailto,subject,msg, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                String myResponse = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AuthorProfileScreen.this,"Successfully Send",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showEmailDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.emaildialog, null, false);

        final EditText subject = viewInflated.findViewById(R.id.subjectemail);
        final EditText body = viewInflated.findViewById(R.id.body_contact);// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

// Set up the buttons
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String subjectEmail=subject.getText().toString();
                String bodyEmail=body.getText().toString();

                    sendMail(new SessionManager(getApplicationContext()).getUserEmail(),authorEmail,subjectEmail,bodyEmail);
                      dialog.cancel();


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