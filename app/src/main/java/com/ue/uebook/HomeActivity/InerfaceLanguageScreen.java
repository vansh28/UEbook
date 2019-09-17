package com.ue.uebook.HomeActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.HomeActivity.HomeFragment.Adapter.Language_adapter;
import com.ue.uebook.R;

import java.util.Locale;

public class InerfaceLanguageScreen extends AppCompatActivity implements Language_adapter.LanguageItemClick, View.OnClickListener {

    private RecyclerView language_List;
    private Language_adapter language_adapter;
    private String[] language = {"English", "Espanol", "Portugues", "Pyccknn"};
    private String[] language_name = {"English", "Spanish", "Portuguese", "Russian"};
    private ImageButton back_btn_language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inerface_language_screen);
        language_List = findViewById(R.id.language_list);
        back_btn_language = findViewById(R.id.back_btn_language);
        back_btn_language.setOnClickListener(this);
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        language_List.setLayoutManager(linearLayoutManagerPopularList);
        language_adapter = new Language_adapter(getApplicationContext(), language, language_name);
        language_List.setAdapter(language_adapter);
        language_adapter.setItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
//      confirmLanguageDialog();
    }

    @Override
    public void onClick(View view) {
        if (view == back_btn_language) {
            finish();
        }

    }

    private void confirmLanguageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You sure, that you want Change Language")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void setLanguage(String language){

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);
        Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
        startActivity(intent);
        finish();

    }
}
