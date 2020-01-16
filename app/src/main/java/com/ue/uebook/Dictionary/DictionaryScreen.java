package com.ue.uebook.Dictionary;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.Dictionary.Pojo.DictionaryResponse;
import com.ue.uebook.R;

import java.io.IOException;

public class DictionaryScreen extends BaseActivity implements View.OnClickListener {
    private EditText word;
    private Button seachbtn;
    private TextView searchResult;
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_screen);
        word = findViewById(R.id.word);
        seachbtn = findViewById(R.id.searchbtn);
        searchResult = findViewById(R.id.output);
        back_btn=findViewById(R.id.back_dictionary);
        back_btn.setOnClickListener(this);
        seachbtn.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        if (view==back_btn){
            finish();
        }
        else if (view==seachbtn){

          getwordDetails(word.getText().toString());


        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getwordDetails(String word) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforWordDefination( word, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final DictionaryResponse form = gson.fromJson(myResponse, DictionaryResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchResult.setText("");
                        if(form.getResponse()!=null)
                        { searchResult.append(Html.fromHtml("<p><b>Source: Wikipedia</b></p>"));
                            for(int i = 0; i < form.getResponse().size(); i++)
                            {
                                searchResult.append(Html.fromHtml("<p>" + form.getResponse().get(i).getDefinition() + "</p>"));
                            }
                            searchResult.append(Html.fromHtml("<br/><br/>"));
                        }
                        else
                        {
                            searchResult.append("Word is not Found!");
                        }

                    }
                });
            }
        });
    }

}
