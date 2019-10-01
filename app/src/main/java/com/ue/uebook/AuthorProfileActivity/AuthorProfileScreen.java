package com.ue.uebook.AuthorProfileActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
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
    private TextView author_name, author_desc, author_post_count, author_follower, author_following;
    private Button follow_To_author, emailToAuthor,editProfile;
    private Author_BookListAdapter author_postAdapter;
    private Intent intent;
    private String userID;
    private SwipeRefreshLayout pullTorfrsh;
    private LinearLayout editt_profile_view,sendRequest_view;


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
        int id = intent.getIntExtra("id",0);
        if (id==1){
            editt_profile_view.setVisibility(View.GONE);
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

            sendRequest(new SessionManager(getApplicationContext()).getUserID(),userID);



        } else if (view == emailToAuthor) {


        }

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
                        author_name.setText(form.getData().getUser_name());
                        author_desc.setText(form.getData().getAbout_me());
                        GlideUtils.loadImage(AuthorProfileScreen.this, "http://dnddemo.com/ebooks/api/v1/upload/" + form.getData().getUrl(), author_profile, R.drawable.user_default, R.drawable.user_default);
                        author_postAdapter = new Author_BookListAdapter(AuthorProfileScreen.this,form.getBooklist());
                        post_list.setAdapter(author_postAdapter);
                        author_postAdapter.setItemClickListener(AuthorProfileScreen.this);

                    }
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

}