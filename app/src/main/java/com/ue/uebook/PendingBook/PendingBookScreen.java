package com.ue.uebook.PendingBook;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.R;
public class PendingBookScreen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton backbtn;
    private RecyclerView pending_bookTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_book_screen);
        backbtn=findViewById(R.id.back_pending);
        pending_bookTv=findViewById(R.id.pending_bookTv);
        backbtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v==backbtn){
            finish();
        }
    }
    private void getPendingBookList(){

    }
}
