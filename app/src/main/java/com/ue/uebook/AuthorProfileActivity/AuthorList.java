package com.ue.uebook.AuthorProfileActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.ue.uebook.R;

public class AuthorList extends AppCompatActivity implements View.OnClickListener {
    private ImageButton back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_list);
        back_btn=findViewById(R.id.back_author_list);
        back_btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (view==back_btn){
            finish();
        }
    }
}
