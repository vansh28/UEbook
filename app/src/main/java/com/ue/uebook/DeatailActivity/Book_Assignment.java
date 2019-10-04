package com.ue.uebook.DeatailActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.DeatailActivity.Pojo.Assignment;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Book_Assignment extends BaseActivity implements View.OnClickListener ,AssignmentAdapter.SubmitAnswerClick {
    private ImageButton back_btn;
    private RecyclerView question_list;
    private AssignmentAdapter assignmentAdapter;
    private Intent intent;
    private List<Assignment>questionList;
    private TextView viewfornoBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__assignment);
        question_list=findViewById(R.id.question_list);
        viewfornoBook=findViewById(R.id.viewfornoBook);
        questionList= new ArrayList<>();
        intent = getIntent();
        questionList= (List<Assignment>) intent.getSerializableExtra("QuestionListExtra");
        if (questionList!=null){
            viewfornoBook.setVisibility(View.GONE);
            question_list.setVisibility(View.VISIBLE);
        }
        else {
            viewfornoBook.setVisibility(View.VISIBLE);
            question_list.setVisibility(View.GONE);
        }
        LinearLayoutManager linearLayoutManagerList = new LinearLayoutManager(this);
        linearLayoutManagerList.setOrientation(LinearLayoutManager.VERTICAL);
        question_list.setLayoutManager(linearLayoutManagerList);
        question_list.setNestedScrollingEnabled(false);
        assignmentAdapter = new AssignmentAdapter(questionList);
        question_list.setAdapter(assignmentAdapter);
        assignmentAdapter.setItemClickListener(Book_Assignment.this);
        back_btn=findViewById(R.id.back_asignment);
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==back_btn){
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemClick(String id, String answer) {
                    sendAnswer(new SessionManager(getApplicationContext()).getUserID(),id,answer);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendAnswer(String userid,String assignmentid,String answer) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforsubmitAssignmentAnswer(userid,assignmentid,answer, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                String myResponse = response.body().string();

            }

        });
    }

}
