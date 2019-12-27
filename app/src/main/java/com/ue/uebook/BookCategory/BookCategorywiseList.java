package com.ue.uebook.BookCategory;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.DeatailActivity.Book_Detail_Screen;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListingResponse;
import com.ue.uebook.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BookCategorywiseList extends BaseActivity implements View.OnClickListener ,CategoryBookAdater.OnbookClick_{
    private RecyclerView categoryBookList;
    private CategoryBookAdater categoryBookAdater;
    Intent intent;
    String cId,cName;
    private TextView categoryName;
    private ImageButton backbtn;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_categorywise_list);
        categoryName=findViewById(R.id.categoryName);
        backbtn=findViewById(R.id.backbtn_category);
        backbtn.setOnClickListener(this);
        intent=getIntent();
        cId=intent.getStringExtra("categoryId");
        cName=intent.getStringExtra("categoryName");
        categoryName.setText(cName);
        categoryBookList=findViewById(R.id.categoryBookList);
        LinearLayoutManager linearLayoutManagerBookmark = new LinearLayoutManager(this);
        linearLayoutManagerBookmark.setOrientation(LinearLayoutManager.VERTICAL);
        categoryBookList.setLayoutManager(linearLayoutManagerBookmark);
        getRecommenedBookList(cId);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getPopularList() {
        ApiRequest request = new ApiRequest();
        request.requestforGetPopularBook(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", e.getLocalizedMessage());
                hideLoadingIndicator();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myresponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final HomeListingResponse form = gson.fromJson(myresponse, HomeListingResponse.class);
                if (form.getData() != null) {
                    runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                categoryBookAdater = new CategoryBookAdater((AppCompatActivity) getApplicationContext(), form.getData(), 17);
                                categoryBookList.setAdapter(categoryBookAdater);
                                categoryBookAdater.notifyDataSetChanged();
                            }
                        });
                    }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getRecommenedBookList(String categoryId) {
        showLoadingIndicator();
        ApiRequest request = new ApiRequest();
        request.requestforgetBookList(categoryId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                hideLoadingIndicator();
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String myResponse = response.body().string();
                hideLoadingIndicator();
                Gson gson = new GsonBuilder().create();
                final HomeListingResponse form = gson.fromJson(myResponse, HomeListingResponse.class);
                if (form.getData() != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            categoryBookAdater = new CategoryBookAdater(BookCategorywiseList.this, form.getData(), 17);
                            categoryBookList.setAdapter(categoryBookAdater);
                            categoryBookAdater.setItemClickListener(BookCategorywiseList.this);
                        }
                    });
                }
                else {

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v==backbtn)
        {
            finish();
        }
    }
    @Override
        public void onItemClick(int position, String book_id) {
            Intent intent = new Intent(this, Book_Detail_Screen.class);
            intent.putExtra("book_id", book_id);
            intent.putExtra("position", position);
         startActivity(intent);
        }

}
