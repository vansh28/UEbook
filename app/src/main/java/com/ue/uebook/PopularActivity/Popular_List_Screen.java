package com.ue.uebook.PopularActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.DeatailActivity.Book_Detail_Screen;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListing;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListingResponse;
import com.ue.uebook.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Popular_List_Screen extends BaseActivity implements View.OnClickListener ,Adapter.PopularBook_ItemClick {
    private RecyclerView popularList;
    private Adapter popularList_adapter;
    private ImageButton backbtn;
    private List<HomeListing>popularListData;
    private ProgressDialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular__list__screen);
        popularList=findViewById(R.id.popularList);
        dialog= new ProgressDialog(this);
        popularListData= new ArrayList<>();
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        popularList.setLayoutManager(linearLayoutManagerPopularList);
        getPopularList();
        backbtn=findViewById(R.id.backbtn_popular);
        backbtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (view==backbtn){
            finish();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getPopularList() {
      showLoadingIndicator();
        ApiRequest request = new ApiRequest();
        request.requestforGetPopularBook(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", e.getLocalizedMessage());
                hideLoadingIndicator();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                hideLoadingIndicator();
                String myresponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final HomeListingResponse form = gson.fromJson(myresponse, HomeListingResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        popularList_adapter = new Adapter(Popular_List_Screen.this,form.getData());
                        popularList.setAdapter(popularList_adapter);
                         popularList_adapter.setItemClickListener(Popular_List_Screen.this);
                        popularList_adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void onItemClick_PopularBook(int position, String book_id) {
        Intent intent = new Intent(Popular_List_Screen.this, Book_Detail_Screen.class);
        intent.putExtra("book_id", book_id);
        intent.putExtra("position",position);
        startActivity(intent);
    }
}
