package com.ue.uebook.Help;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.Contactus.ContactusScreen;
import com.ue.uebook.R;

public class HelpCenterScreen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton backbtn;
    private Button contact_us;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center_screen);
        backbtn = findViewById(R.id.back_help);
        backbtn.setOnClickListener(this);
        contact_us=findViewById(R.id.contact_us);
        contact_us.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==backbtn){
            finish();
        }
        else if (view==contact_us)
        {
            Intent intent = new Intent(HelpCenterScreen.this, ContactusScreen.class);
            startActivity(intent);
        }
    }


}
