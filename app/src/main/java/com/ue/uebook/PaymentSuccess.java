package com.ue.uebook;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentSuccess extends AppCompatActivity {
    private Button finish;
    String euro = "\u20ac";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        TextView transaction_id_tv= findViewById(R.id.transaction_id_tv);
        TextView amount_tv= findViewById(R.id.amount_tv);
        TextView date_time_tv= findViewById(R.id.date_time_tv);
        TextView print_tv= findViewById(R.id.print_tv);
        finish = findViewById(R.id.finish_tv);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        Bundle extras = getIntent().getExtras();
         String bookID = extras.getString("bookId");
        Log.e("bookId",bookID);
        amount_tv.setText(euro +extras.getString("PaymentAmount"));

        transaction_id_tv.setText("Transaction ID" +" "+extras.getString("transaction_id"));

//            String  response = extras.getString("PaymentDetails");
//            try {
//                JSONObject jsonObject=new JSONObject(response);
//
//                transaction_id_tv.setText("Transaction ID" +" "+jsonObject.getString("transaction_id"));
//                //date_time_tv.setText(jsonObject.getString("date"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }

    }




