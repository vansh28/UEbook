package com.ue.uebook.Payment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.ue.uebook.Config;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class StripePayment extends AppCompatActivity {
    Stripe stripe;
    float amount;
    String name;
    Card card;
    Token tok;
    String currency;
    String price;
    String mail_id;
    String bookid;
    Intent intent;
    private String bookName="";
    private String adminCommision="";



    int pro;
    private MaterialDialog progressDialog;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean validateEmail(String email) {
        if (email.length() > 0) {
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_payment);
        intent = getIntent();
        price = intent.getStringExtra("price");
        bookid = intent.getStringExtra("bookid");
        bookName = intent.getStringExtra("book_name");
        adminCommision=intent.getStringExtra("adminCommision");
        currency=intent.getStringExtra("currency");

        Log.e("pay",price);
        Log.e("cur",currency);

        ((EditText) findViewById(R.id.Mail)).setText(new SessionManager(getApplicationContext()).getUserEmail());
        stripe = new Stripe(this, Config.Publishable_key);
        amount = Float.parseFloat(price);
        findViewById(R.id.back_contactus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: replace with your own test key
                EditText cardNumberField = findViewById(R.id.cardNumber);
                EditText monthField = findViewById(R.id.month);
                EditText yearField = findViewById(R.id.year);
                EditText cvcField = findViewById(R.id.cvc);
                EditText Mail = findViewById(R.id.Mail);
                if (Mail.getText().toString().trim().isEmpty()) {
                    Mail.setError(getString(R.string.Enter_Your_Mail));
                } else if (cardNumberField.getText().toString().isEmpty()) {
                    cardNumberField.setError(getString(R.string.Enter_Card_Number));
                } else if (monthField.getText().toString().isEmpty()) {
                    monthField.setError(getString(R.string.Enter_Month));
                } else if (yearField.getText().toString().isEmpty()) {
                    yearField.setError(getString(R.string.Enter_Year));
                } else if (cvcField.getText().toString().isEmpty()) {
                    cvcField.setError(getString(R.string.Enter_CVC));
                } else {
                    if (validateEmail(Mail.getText().toString())) {
                        mail_id = Mail.getText().toString().trim();
                        cardDitail(cardNumberField.getText().toString(), Integer.valueOf(monthField.getText().toString()),
                                Integer.valueOf(yearField.getText().toString()),
                                cvcField.getText().toString());
                        progressDialog = new MaterialDialog.Builder(StripePayment.this)
                                .content(getResources().getString(R.string.please_wait))
                                .progress(true, 0)
                                .show();
                    } else {
                        Toast.makeText(StripePayment.this, getString(R.string.email_validation), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void cardDitail(String cardnumber, Integer integer, Integer valueOf, String toString) {

        card = new Card(cardnumber, integer, valueOf, toString);

        card.setCurrency(currency);
        //card.setName("yugal");
        //card.setAddressZip("1000");
        /*
        card.setNumber(4242424242424242);
        card.setExpMonth(12);
        card.setExpYear(19);
        card.setCVC("123");
        */

        stripe.createToken(card, Config.Publishable_key, new TokenCallback() {
            public void onSuccess(Token token) {
                // TODO: Send Token information to your backend to initiate a charge
                //Toast.makeText(getApplicationContext(), "Token created: " + token.getId(), Toast.LENGTH_LONG).show();
                Log.e("yugal: ", token.getId());
                tok = token;
                if (!token.getUsed()) {
                    // StrpePayment(token.getId(),currency,amount,mail_id);
                    new StripeCharge(token.getId(), currency, amount, mail_id).execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Token already  used ", Toast.LENGTH_LONG).show();
                }
                // new StripeCharge(token.getId(), currency, amount, token.getId()).execute();
                //

            }

            public void onError(Exception error) {
                progressDialog.dismiss();
                Log.d("Stripe", error.getLocalizedMessage());
            }
        });
    }

    public class StripeCharge extends AsyncTask<String, Void, String> {
        String token;
        String currency;
        float amount;
        String mail_id;

        public StripeCharge(String token, String currency, float amount, String mail_id) {
            this.token = token;
            this.currency = currency;
            this.amount = amount;
            this.mail_id = mail_id;
        }

        @Override
        protected String doInBackground(String... params) {

            return postData(token, currency, amount, mail_id, bookid ,bookName,adminCommision);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // Toast.makeText(StripePaymentActivity.this,"Payment Successful",Toast.LENGTH_SHORT).show();
            // <html><head><title>Slim Application Error</title><style>body{margin:0;padding:30px;font:12px/1.5 Helvetica,Arial,Verdana,sans-serif;}h1{margin:0;font-size:48px;font-weight:normal;line-height:48px;}strong{display:inline-block;width:65px;}</style></head><body><h1>Slim Application Error</h1><p>The application could not run because of the following error:</p><h2>Details</h2><div><strong>Type:</strong> Stripe_InvalidRequestError</div><div><strong>Message:</strong> No such token: tok_1EGXYxJ8yseMKr8EIohYuQ8v</div><div><strong>File:</strong> /home/dnddemo/public_html/red-listed/development/third_party/Stripe/lib/Stripe/ApiRequestor.php</div><div><strong>Line:</strong> 147</div><h2>Trace</h2><pre><div>#0 /home/dnddemo/public_html/red-listed/development/third_party/Stripe/lib/Stripe/ApiRequestor.php(268): Stripe_ApiRequestor-&gt;handleApiError('{\n  &quot;error&quot;: {\n...', 400, Array)</div><div>#1 /home/dnddemo/public_html/red-listed/development/third_party/Stripe/lib/Stripe/ApiRequestor.php(109): Stripe_ApiRequestor-&gt;_interpretResponse('{\n  &quot;error&quot;: {\n...', 400)</div><div>#2 /home/dnddemo/public_html/red-listed/development/third_party/Stripe/lib/Stripe/ApiResource.php(143): Stripe_ApiRequestor-&gt;request('post', '/v1/charges', Array, Array)</div><div>#3 /home/dnddemo/public_html/red-listed/development/third_party/Stripe/lib/Stripe/Charge.php(38): Stripe_ApiResource::_scopedCreate('Stripe_Charge', Array, NULL)</div><div>#4 /home/dnddemo/public_html/red-listed/api/v1/index.php(141): Stripe_Charge::create(Array)</div><div>#5 [internal function]: {closure}()</div><div>#6 /home/dnddemo/public_html/red-listed/api/libs/Slim/Route.php(468): call_user_func_array(Object(Closure), Array)</div><div>#7 /home/dnddemo/public_html/red-listed/api/libs/Slim/Slim.php(1369): Slim\Route-&gt;dispatch()</div><div>#8 /home/dnddemo/public_html/red-listed/api/libs/Slim/Middleware/Flash.php(85): Slim\Slim-&gt;call()</div><div>#9 /home/dnddemo/public_html/red-listed/api/libs/Slim/Middleware/MethodOverride.php(92): Slim\Middleware\Flash-&gt;call()</div><div>#10 /home/dnddemo/public_html/red-listed/api/libs/Slim/Middleware/PrettyExceptions.php(67): Slim\Middleware\MethodOverride-&gt;call()</div><div>#11 /home/dnddemo/public_html/red-listed/api/libs/Slim/Slim.php(1314): Slim\Middleware\PrettyExceptions-&gt;call()</div><div>#12 /home/dnddemo/public_html/red-listed/api/v1/index.php(1097): Slim\Slim-&gt;run()</div><div>#13 {main}</pre></body></html>
            Log.e("Result", s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equals("success")) {
//                    startActivity(new Intent(StripePayment.this, PaymentSuccess.class)
//                            .putExtra("PaymentDetails", s)
//                            .putExtra("PaymentAmount",String.valueOf (amount))
//                            .putExtra("tr_type","Stripe")
//                            .putExtra("currency",currency));
                } else {
                    Toast.makeText(StripePayment.this, object.getString("status"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String postData(String token, String currency, float amount, String mail_id, String book_id ,String  book_name ,String admin_commission) {
        // Create a new HttpClient and Post Header
        try {
            StringBuilder sb = new StringBuilder();
            URL url = new URL(ApiRequest.BaseUrl + "stripe_payment");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);





            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new NameValuePair("user_id", new SessionManager(getApplication()).getUserID()));
            params.add(new NameValuePair("email", mail_id));
            params.add(new NameValuePair("currency", currency));
            params.add(new NameValuePair("amount", String.valueOf(amount)));
            params.add(new NameValuePair("stripeToken", token));
            params.add(new NameValuePair("book_id", book_id));
            params.add(new NameValuePair("book_name", book_name));
            params.add(new NameValuePair("admin_commission", admin_commission));
            Log.e("Param:", "  " + params);
            OutputStream os = null;
            os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.write(getQuery(params));
            writer.flush();
            Log.e("response", "" + writer.toString());
            writer.close();
            os.close();
            progressDialog.dismiss();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String response;
                while ((response = br.readLine()) != null) {
                    sb.append(response);
                    Log.e("Response", "yugal        : " + response);
                }

                return sb.toString();
            } else {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }
        Log.e("Query", result.toString());
        return result.toString();
    }

    public class NameValuePair {
        String name, value;

        public NameValuePair(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
