package com.ue.uebook.HomeActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.HomeActivity.HomeFragment.Adapter.Language_adapter;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;
import com.ue.uebook.SettingsActivity;

import java.util.Locale;

public class InerfaceLanguageScreen extends AppCompatActivity implements Language_adapter.LanguageItemClick, View.OnClickListener {

    private RecyclerView language_List;
    private Language_adapter language_adapter;
    private String[] language = {"English", "French" ,"German","Spanish"};
    private String[] language_name = {"en", "fr","de","es"};
    private ImageButton back_btn_language;
    private Button fontview;
    private int id= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inerface_language_screen);
        language_List = findViewById(R.id.language_list);
        back_btn_language = findViewById(R.id.back_btn_language);
        fontview=findViewById(R.id.fontview);
        fontview.setOnClickListener(this);

        back_btn_language.setOnClickListener(this);
        String lang = new SessionManager(getApplicationContext()).getCurrentLanguage();
        if (lang.equalsIgnoreCase("en")) {
            id=0;
        } else if (lang.equalsIgnoreCase("fr")) {
            id=1;
        } else if (lang.equalsIgnoreCase("de")) {
            id=2;
        } else if (lang.equalsIgnoreCase("es")) {
            id=3;
        }
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        language_List.setLayoutManager(linearLayoutManagerPopularList);
        language_adapter = new Language_adapter(getApplicationContext(), language, language_name ,id);
        language_List.setAdapter(language_adapter);
        language_adapter.setItemClickListener(this);

    }



    @Override
    public void onClick(View view) {
        if (view == back_btn_language) {
            finish();
        }
        else if (view==fontview){

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

    }

    private void confirmLanguageDialog(final String value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You sure, that you want Change Language")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new SessionManager(getApplicationContext()).setCurrentLanguage(value);
                        setLocale(value);

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

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, HomeScreen.class);
        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(refresh);
        finish();
    }

    @Override
    public void onItemClick(int position, String value) {

        confirmLanguageDialog(value);
    }
}
