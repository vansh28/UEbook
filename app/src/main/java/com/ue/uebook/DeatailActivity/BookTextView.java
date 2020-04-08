package com.ue.uebook.DeatailActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.R;

public class BookTextView extends AppCompatActivity implements View.OnClickListener {
          private ImageButton backbtn;
          private String Bookname;
          private TextView bookTextView,bookName;
          private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_text_view);
        backbtn = findViewById(R.id.backbtn);
        intent = getIntent();
        Bookname = intent.getStringExtra("name");
        bookName = findViewById(R.id.bookName);
        bookTextView = findViewById(R.id.bookTextView);
        bookName.setText(Bookname);
        backbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==backbtn){
            finish();
        }
    }
}
