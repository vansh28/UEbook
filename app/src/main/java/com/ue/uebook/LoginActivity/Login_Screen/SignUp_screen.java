package com.ue.uebook.LoginActivity.Login_Screen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.Customview.CustomTextViewMedium;
import com.ue.uebook.R;

public class SignUp_screen extends AppCompatActivity implements View.OnClickListener {
    private CustomTextViewMedium haveAcountBtn;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_scren);
        haveAcountBtn=findViewById(R.id.haveAcountBtn);
        SpannableString blueSpannable = new SpannableString("not have an account? Sign Up");
        blueSpannable.setSpan(new ForegroundColorSpan(getColor(R.color.colorAccent)), 20,28, 0);
        haveAcountBtn.setText(blueSpannable);
        haveAcountBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==haveAcountBtn){
            Intent intent = new Intent(this,SignInScreen.class);
            startActivity(intent);
            finish();
        }
    }
}
