package com.ue.uebook.ChatSdk;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.R;

public class TextStatusCreateScreen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton changeBackground;
    private EditText textStatus;
    private String colorArray []={"#4B0082","#FFA07A","#FFA500","#00FF00","#008B8B"};
    private int pos=0;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_status_create_screen);
        changeBackground=findViewById(R.id.changeBackground);
        changeBackground.setOnClickListener(this);
        layout = findViewById(R.id.layout);
        layout.setOnClickListener(this);
        textStatus = findViewById(R.id.textStatus);

    }

    @Override
    public void onClick(View v) {
        if (v==changeBackground){
            if (pos==colorArray.length-1){
                pos=0;
                layout.setBackgroundColor(Color.parseColor(colorArray[0]));

            }
            else {
                pos++;
                layout.setBackgroundColor(Color.parseColor(colorArray[pos]));

            }

        }

    }
}
