package com.ue.uebook.Dictionary;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.R;

import java.util.ArrayList;

public class DictionaryScreen extends AppCompatActivity implements View.OnClickListener {
    private EditText word;
    private Button seachbtn;
    private TextView searchResult;
    private TranslationDataSource translation_data_source;
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_screen);
        word = findViewById(R.id.word);
        seachbtn = findViewById(R.id.searchbtn);
        searchResult = findViewById(R.id.output);
        back_btn=findViewById(R.id.back_dictionary);
        translation_data_source = new TranslationDataSource(this);
        back_btn.setOnClickListener(this);
        seachbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchResult.setText("");
                ArrayList<Translation> translation_array_long = translation_data_source.getEngEngTranslation(word.getText().toString());
                if(translation_array_long.size() > 0)
                {
                    searchResult.append(Html.fromHtml("<p><b>Source: Wikipedia</b></p>"));
                    for(int i = 0; i < translation_array_long.size(); i++)
                    {
                        searchResult.append(Html.fromHtml("<p>" + translation_array_long.get(i).getTranslation() + "</p>"));
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

    @Override
    public void onClick(View view) {
        if (view==back_btn){
            finish();
        }
    }
}
