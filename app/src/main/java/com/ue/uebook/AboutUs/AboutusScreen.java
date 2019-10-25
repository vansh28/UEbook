package com.ue.uebook.AboutUs;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.R;

public class AboutusScreen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus_screen);
        backbtn = findViewById(R.id.back_abouttus);
        backbtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (view==backbtn){
            finish();
        }
    }

}
