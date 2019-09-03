package com.ue.uebook.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ue.uebook.LoginActivity.Fragment.SignIn_Fragment;
import com.ue.uebook.LoginActivity.Fragment.SignUp_Fragment;
import com.ue.uebook.R;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener ,SignUp_Fragment.OnFragmentInteractionListener,SignIn_Fragment.OnFragmentInteractionListener {
    private Button signUp_btn , signIn_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        signIn_btn = findViewById(R.id.signIn_btn);
        signUp_btn = findViewById(R.id.signUp_btn);
        signUp_btn.setOnClickListener(this);
        signIn_btn.setOnClickListener(this);
        replaceFragment(new SignUp_Fragment());

    }

    private  void  replaceFragment(Fragment fragment){
         FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_Fragment, fragment,fragment.getClass().getName());
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        if (view == signUp_btn){

            replaceFragment(new SignUp_Fragment());
            signUp_btn.setBackgroundResource(R.drawable.active_signin_border);
            signIn_btn.setBackgroundResource(R.drawable.non_active_btn_signin);
            signUp_btn.setTextColor(Color.parseColor("#D31145"));
            signIn_btn.setTextColor(Color.parseColor("#000000"));
        }

        else if (view==signIn_btn)
        {

            replaceFragment(new SignIn_Fragment());
            signIn_btn.setBackgroundResource(R.drawable.active_signin_border);
            signUp_btn.setBackgroundResource(R.drawable.non_active_btn_signin);
            signIn_btn.setTextColor(Color.parseColor("#D31145"));
            signUp_btn.setTextColor(Color.parseColor("#000000"));

        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
