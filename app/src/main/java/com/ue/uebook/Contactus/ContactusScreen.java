package com.ue.uebook.Contactus;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.R;

public class ContactusScreen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton backbtn;
    private EditText email,message,mobileNo;
    private Button send_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus_screen);
        backbtn = findViewById(R.id.back_contactus);
        email=findViewById(R.id.email_contact);
        message=findViewById(R.id.body_contact);
        mobileNo=findViewById(R.id.mobile_contact);
        send_Btn=findViewById(R.id.send_btn);
        send_Btn.setOnClickListener(this);
        backbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==backbtn){
            finish();
        }
        else if (view==send_Btn){

            if (isvalidate()){
                email.setText("");
                message.setText("");
                mobileNo.setText("");
                Toast.makeText(this,"Successfully Send",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private Boolean isvalidate(){
        String eMail=email.getText().toString();
        String msg=message.getText().toString();
        String mobile = mobileNo.getText().toString();
        if (!eMail.isEmpty()){
            if (!mobile.isEmpty()){
                if (!msg.isEmpty()){
                    return true;
                }
                else {
                    message.setError("Enter your message");
                    message.requestFocus();
                    message.setEnabled(true);
                    return  false;
                }
            }
            else {
                mobileNo.setError("Enter your Contact Number");
                mobileNo.requestFocus();
                mobileNo.setEnabled(true);
                return  false;
            }
        }
        else {

            email.setError("Enter your Email");
            email.requestFocus();
            email.setEnabled(true);
            return  false;
        }
    }
}
