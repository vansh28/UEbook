package com.ue.uebook.PendingBook;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.PendingBook.Pojo.BookResponse;
import com.ue.uebook.PendingBook.Pojo.PendingBookdata;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;
import com.ue.uebook.UploadBook.Upload_Book_Screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PendingBookScreen extends BaseActivity implements View.OnClickListener ,PendingListAdapter.BookItemClick {
    private ImageButton backbtn;
    private RecyclerView pending_bookTv;
    private PendingListAdapter pendingListAdapter;
    private int textSize = 16;
    private SwipeRefreshLayout swipe_refresh_layout;
    private List<BookResponse>booklist;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_book_screen);
        backbtn=findViewById(R.id.back_pending);
        fontsize();

        booklist= new ArrayList<>();
        pending_bookTv=findViewById(R.id.pending_bookTv);
        backbtn.setOnClickListener(this);
        LinearLayoutManager linearLayoutManagerBookmark = new LinearLayoutManager(this);
        linearLayoutManagerBookmark.setOrientation(LinearLayoutManager.VERTICAL);
        pending_bookTv.setLayoutManager(linearLayoutManagerBookmark);
        getPendingBook(new SessionManager(getApplicationContext()).getUserID());

    }
    @Override
    public void onClick(View v) {
        if (v==backbtn){
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getPendingBook(String user_id) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        if (booklist.size()>0)
            booklist.clear();
        request.requestforPendingBook(user_id,new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                hideLoadingIndicator();
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final PendingBookdata form = gson.fromJson(myResponse, PendingBookdata.class);
                if (form.getData()!=null&&form.isError()==false){
                    booklist=form.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pendingListAdapter = new PendingListAdapter(PendingBookScreen.this,booklist,textSize);
                            pending_bookTv.setAdapter(pendingListAdapter);
                            pendingListAdapter.setItemClickListener(PendingBookScreen.this);
                            pending_bookTv.getRecycledViewPool().clear();
                            pendingListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }
    private void fontsize(){
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
    @Override
    public void onItemClick(int position, String bookid) {
        Intent intent = new Intent(this, Upload_Book_Screen.class);
        intent.putExtra("screenid",2);
        intent.putExtra("bookid",bookid);
        startActivity(intent);
    }
}
