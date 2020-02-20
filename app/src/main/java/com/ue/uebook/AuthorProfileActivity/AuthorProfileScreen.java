package com.ue.uebook.AuthorProfileActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.ue.uebook.AuthorProfileActivity.pojo.StatusPojo;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.ChatSdk.MessageScreen;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.DeatailActivity.Book_Detail_Screen;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthorProfileScreen extends BaseActivity implements View.OnClickListener ,Author_BookListAdapter.BookItemClick{
    private ImageView backbtn, author_profile;
    private RecyclerView post_list;
    private TextView author_name, postView,author_desc, author_post_count, author_follower, author_following,publisher_type;
    private Button follow_To_author, emailToAuthor,editProfile;
    private Author_BookListAdapter author_postAdapter;
    private Intent intent;
    private String userID;
    private SwipeRefreshLayout pullTorfrsh;
    private LinearLayout editt_profile_view,sendRequest_view;
    private int id;
    private String authorEmail=" ";
    private int textSize;
    private int followStatusvalue = 0;
    private List<StatusPojo>detailList;
    private List<AuthorData>authorDataList;





    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_profile_screen);
        intent = getIntent();
        fontsize();
        detailList= new ArrayList<>();
        authorDataList = new ArrayList<>();
        userID=intent.getStringExtra("userID");
        editt_profile_view=findViewById(R.id.editt_profile_view);
        sendRequest_view=findViewById(R.id.sendRequest_view);
        editProfile=findViewById(R.id.editt_profile);
        editProfile.setOnClickListener(this);
        postView=findViewById(R.id.postView);
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
        editProfile.setTextSize(textSize);
        author_desc.setTextSize(textSize);
        author_name.setTextSize(textSize);
        postView.setTextSize(textSize);
        emailToAuthor.setTextSize(textSize);

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
       // follow_To_author.setText("Message");
        emailToAuthor.setOnClickListener(this);
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        post_list.setLayoutManager(linearLayoutManagerPopularList);
       pullTorefreshswipe();
        post_list.setNestedScrollingEnabled(false);
        AuthorInfo(userID);
          checkStatus(new SessionManager(getApplicationContext()).getUserID(),userID);

    }
    private void pullTorefreshswipe(){
        pullTorfrsh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onRefresh() {
                AuthorInfo(userID);
                checkStatus(new SessionManager(getApplicationContext()).getUserID(),userID);
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
                if (followStatusvalue==1){
                    Intent intent = new Intent(this, MessageScreen.class);
                    intent.putExtra("sendTo",userID);
                    intent.putExtra("channelID",detailList.get(0).getChannelId());
                    intent.putExtra("name",authorDataList.get(0).getData().getUser_name());
                    intent.putExtra("imageUrl",authorDataList.get(0).getData().getUrl());
                    intent.putExtra("id",1);
                    startActivity(intent);
                    finish();
                }
                else if (followStatusvalue==0){
                    sendRequest(new SessionManager(getApplicationContext()).getUserID(),userID);
                }


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
                            authorDataList.add(form);
                        author_name.setText(form.getData().getUser_name());
                        author_desc.setText(form.getData().getAbout_me());
                        publisher_type.setText(form.getData().getPublisher_type());
                        GlideUtils.loadImage(AuthorProfileScreen.this, "http://dnddemo.com/ebooks/api/v1/upload/" + form.getData().getUrl(), author_profile, R.drawable.user_default, R.drawable.user_default);
                        authorEmail=form.getData().getEmail();
                        if (form.getBooklist()!=null){
                            post_list.setVisibility(View.VISIBLE);
                            author_postAdapter = new Author_BookListAdapter(AuthorProfileScreen.this,form.getBooklist(),id,textSize);
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
                Gson gson = new GsonBuilder().create();
                Log.d("djhfkj",myResponse);
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         follow_To_author.setText("Pending");
                     }
                 });
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
    private void fontsize(){
        SharedPreferences pref = getSharedPreferences(getPackageName() + "_preferences", Context.MODE_PRIVATE);
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void checkStatus(String user_id ,String frndID ) {
        ApiRequest request = new ApiRequest();
        request.requestforCheckFollowStatus(user_id, frndID ,new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final StatusPojo form = gson.fromJson(myResponse, StatusPojo.class);
               if (form.getError()==false && form.getData()!=null){
                   detailList.add(form);
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                          if (form.getData().get(0).getStatus().equalsIgnoreCase("1")){
                              follow_To_author.setText("Message");
                              followStatusvalue=1;
                          }
                          else  if (form.getData().get(0).getStatus().equalsIgnoreCase("0")){
                              follow_To_author.setText("Pending");
                              followStatusvalue=0;
                          }

                       }
                   });


               }
            }
        });
    }








}