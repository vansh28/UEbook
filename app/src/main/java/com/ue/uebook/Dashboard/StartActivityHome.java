package com.ue.uebook.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.Customview.CustomTextViewNormal;
import com.ue.uebook.LoginActivity.Login_Screen.SignUp_screen;
import com.ue.uebook.R;

public class StartActivityHome extends AppCompatActivity implements View.OnClickListener {
    private CustomTextViewNormal title,voiceText,typingText,chatText,bookText;
    private RelativeLayout signUp_btn_new;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_home);
        title=(CustomTextViewNormal)findViewById(R.id.title);
        voiceText=(CustomTextViewNormal)findViewById(R.id.voiceText);
        typingText=(CustomTextViewNormal)findViewById(R.id.keyboardText);
        chatText=(CustomTextViewNormal)findViewById(R.id.chatText);
        bookText=(CustomTextViewNormal)findViewById(R.id.readBookText);

        signUp_btn_new=findViewById(R.id.signUp_btn_new);
        signUp_btn_new.setOnClickListener(this);

        String s= "Write your Book \n With UEbook";

        String s1= "Write with\nVOICE";
        String s2= "Write with\nTYPING";
        String s3= "Start\nCHATING";
        String s4= "Read The\n BOOK";


        SpannableString ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(2f), 16,s.length(), 0); // set size
//        ss1.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 5, 0);// set color
        ss1.setSpan(new StyleSpan( android.graphics.Typeface.BOLD),16,s.length(),0);
        title.setText(ss1);

        SpannableString ss2=  new SpannableString(s1);
        ss2.setSpan(new RelativeSizeSpan(2f), 11,s1.length(), 0); // set size
//        ss1.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 5, 0);// set color
        ss2.setSpan(new StyleSpan( android.graphics.Typeface.BOLD),11,s1.length(),0);
        voiceText.setText(ss2);

        SpannableString ss3=  new SpannableString(s2);
        ss3.setSpan(new RelativeSizeSpan(2f), 11,s2.length(), 0); // set size
//        ss1.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 5, 0);// set color
        ss3.setSpan(new StyleSpan( android.graphics.Typeface.BOLD),11,s2.length(),0);
        typingText.setText(ss3);

        SpannableString ss4=  new SpannableString(s3);
        ss4.setSpan(new RelativeSizeSpan(2f), 6,s3.length(), 0); // set size
//        ss1.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 5, 0);// set color
        ss4.setSpan(new StyleSpan( android.graphics.Typeface.BOLD),6,s3.length(),0);
        chatText.setText(ss4);

        SpannableString ss5=  new SpannableString(s4);
        ss5.setSpan(new RelativeSizeSpan(2f), 9,s4.length(), 0); // set size
//        ss1.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 5, 0);// set color
        ss5.setSpan(new StyleSpan( android.graphics.Typeface.BOLD),9,s4.length(),0);
        bookText.setText(ss5);
    }

    @Override
    public void onClick(View v) {
        if (v==signUp_btn_new){
            Intent intent = new Intent(this, SignUp_screen.class);
            startActivity(intent);
        }
    }
}
