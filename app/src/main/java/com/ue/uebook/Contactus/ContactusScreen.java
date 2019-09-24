package com.ue.uebook.Contactus;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class ContactusScreen extends BaseActivity implements View.OnClickListener {
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        if (view==backbtn){
            finish();
        }
        else if (view==send_Btn){

            if (isvalidate()){

                contactUs(new SessionManager(getApplicationContext()).getUserName(),email.getText().toString(),mobileNo.getText().toString(),message.getText().toString());

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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void contactUs(String name , String email_user,String phone,String contatMessage) {
        showLoadingIndicator();
        ApiRequest request = new ApiRequest();
        request.requestforContactUs(new SessionManager(getApplicationContext()).getUserID(),name,email_user,phone,contatMessage, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                hideLoadingIndicator();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(ContactusScreen.this,"Please Try Again",Toast.LENGTH_SHORT).show();
                    }
                });

            }
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String myResponse = response.body().string();
                hideLoadingIndicator();
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      email.setText("");
                      message.setText("");
                      mobileNo.setText("");
                      showMessage();
                  }
              });

            }
        });
    }


    private  void showMessage(){

//                            showDialogWithOkButton("Login Error", form.getMessage());
                final PrettyDialog pDialog=  new PrettyDialog(this);
                pDialog  .setTitle("Team UeBook");
                pDialog.setIcon(R.drawable.applogo);
                pDialog.setMessage("Thanks for contacting us , we will get back to you soon !");
                pDialog   .addButton(
                        "OK",					// button text
                        R.color.pdlg_color_white,		// button text color
                        R.color.colorPrimary,		// button background color
                        new PrettyDialogCallback() {		// button OnClick listener
                            @Override
                            public void onClick() {
                                pDialog.dismiss();
                            }
                        }
                ).show();

    }



}
