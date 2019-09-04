package com.ue.uebook.DeatailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.ue.uebook.R;
import com.ue.uebook.ShareUtils;
import com.ue.uebook.WebviewScreen;

public class Book_Detail_Screen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton back_btn_Deatils, facebook_btn, google_btn, twitter_btn, whatsappshare_btn;
    private RecyclerView review_List;
    private Review_List_Adapter review_list_adapter;
    private ImageButton bookmark_btn;
    private Boolean isBookmark_book = false;
    private Button readFull_Book_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__detail__screen);
        back_btn_Deatils = findViewById(R.id.back_btn_Deatils);
        readFull_Book_btn = findViewById(R.id.readFull_Book_btn);
        bookmark_btn = findViewById(R.id.bookmark_btn);
        facebook_btn = findViewById(R.id.fbshare_btn);
        google_btn = findViewById(R.id.googleshare_btn);
        twitter_btn = findViewById(R.id.twittershare_btn);
        whatsappshare_btn = findViewById(R.id.whatsappshare_btn);
        facebook_btn.setOnClickListener(this);
        google_btn.setOnClickListener(this);
        twitter_btn.setOnClickListener(this);
        whatsappshare_btn.setOnClickListener(this);
        bookmark_btn.setOnClickListener(this);
        back_btn_Deatils.setOnClickListener(this);
        review_List = findViewById(R.id.review_List);
        LinearLayoutManager linearLayoutManagerList = new LinearLayoutManager(this);
        linearLayoutManagerList.setOrientation(LinearLayoutManager.VERTICAL);
        review_List.setLayoutManager(linearLayoutManagerList);
        review_list_adapter = new Review_List_Adapter();
        review_List.setAdapter(review_list_adapter);
        review_List.setNestedScrollingEnabled(false);
        readFull_Book_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == back_btn_Deatils) {
            finish();
        } else if (view == bookmark_btn) {

            if (!isBookmark_book) {

                bookmark_btn.setBackgroundResource(R.drawable.bookmark_active);
                isBookmark_book = true;
            } else {
                bookmark_btn.setBackgroundResource(R.drawable.bookmark_unactive);
                isBookmark_book = false;

            }
        } else if (view == facebook_btn) {

          ShareUtils.shareFacebook(this,"text","");

        } else if (view == google_btn) {
            ShareUtils.shareByGmail(this,"subject","Body");

        } else if (view == twitter_btn) {
            ShareUtils.shareTwitter(this,"text","","","");

        } else if (view == whatsappshare_btn) {
            ShareUtils.shareWhatsapp(this,"text","");


        }
        else if (view==readFull_Book_btn){
            gotoWebview();
        }

    }

    private  void  gotoWebview(){
        Intent intent = new Intent(this, WebviewScreen.class);
        startActivity(intent);
    }





}
