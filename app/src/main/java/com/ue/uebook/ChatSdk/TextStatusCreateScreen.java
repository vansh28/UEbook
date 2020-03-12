package com.ue.uebook.ChatSdk;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.R;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class TextStatusCreateScreen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton changeBackground ,button_chat_emoji ,textStyleChanges;
    private EmojiconEditText textStatus;
    private String colorArray []={"#4B0082","#FFA07A","#FFA500","#00FF00","#008B8B"};
    private int pos=0;
    private RelativeLayout layout;
    EmojIconActions emojIcon;
    private int fontPos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_status_create_screen);
        changeBackground=findViewById(R.id.changeBackground);
        button_chat_emoji = findViewById(R.id.button_chat_emoji);
        textStyleChanges = findViewById(R.id.textStyleChanges);
        textStyleChanges.setOnClickListener(this);
        changeBackground.setOnClickListener(this);
        layout = findViewById(R.id.layout);
        layout.setOnClickListener(this);
        textStatus = findViewById(R.id.textStatus);
        emojIcon = new EmojIconActions(this, layout, textStatus, button_chat_emoji);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("fff", "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("fff", "Keyboard closed");
            }
        });

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
        else if (v==textStyleChanges){
            if (fontPos==0){
                textStatus.setTypeface(null, Typeface.BOLD);
                fontPos++;
            }
            else if (fontPos==1){
                textStatus.setTypeface(null, Typeface.ITALIC);
                fontPos++;
            }
            else if (fontPos==2){
                textStatus.setTypeface(null, Typeface.BOLD_ITALIC);
                fontPos++;
            }
            else if (fontPos==3){
                textStatus.setTypeface(null, Typeface.NORMAL);
                fontPos=0;
            }
        }

    }
}
