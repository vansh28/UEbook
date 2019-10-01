package com.ue.uebook.DeatailActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.R;

public class Book_Assignment extends AppCompatActivity implements View.OnClickListener {
    private ImageButton back_btn;
    private RecyclerView question_list;
    private AssignmentAdapter assignmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__assignment);
        question_list=findViewById(R.id.question_list);
        LinearLayoutManager linearLayoutManagerList = new LinearLayoutManager(this);
        linearLayoutManagerList.setOrientation(LinearLayoutManager.VERTICAL);
        question_list.setLayoutManager(linearLayoutManagerList);
        question_list.setNestedScrollingEnabled(false);
        assignmentAdapter = new AssignmentAdapter();
        question_list.setAdapter(assignmentAdapter);
        back_btn=findViewById(R.id.back_asignment);
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==back_btn){
            finish();
        }
    }
}
