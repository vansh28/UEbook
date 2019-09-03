package com.ue.uebook.DeatailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.ue.uebook.PopularActivity.Adapter;
import com.ue.uebook.R;

public class Book_Detail_Screen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton back_btn_Deatils;
    private RecyclerView review_List;
    private Review_List_Adapter review_list_adapter;
    private ImageButton bookmark_btn;
    private  Boolean isBookmark_book=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__detail__screen);
        back_btn_Deatils = findViewById(R.id.back_btn_Deatils);
        bookmark_btn = findViewById(R.id.bookmark_btn);
        bookmark_btn.setOnClickListener(this);
        back_btn_Deatils.setOnClickListener(this);
        review_List=findViewById(R.id.review_List);
        LinearLayoutManager linearLayoutManagerList = new LinearLayoutManager(this);
        linearLayoutManagerList.setOrientation(LinearLayoutManager.VERTICAL);
        review_List.setLayoutManager(linearLayoutManagerList);
        review_list_adapter = new Review_List_Adapter();
        review_List.setAdapter(review_list_adapter);
    }

    @Override
    public void onClick(View view) {
        if (view == back_btn_Deatils){
            finish();
        }
        else if (view == bookmark_btn){

            if (!isBookmark_book){

                bookmark_btn.setBackgroundResource(R.drawable.bookmark_active);
                isBookmark_book=true;
            }
            else {
                bookmark_btn.setBackgroundResource(R.drawable.bookmark_unactive);
                isBookmark_book=false;

            }
        }

    }
}
