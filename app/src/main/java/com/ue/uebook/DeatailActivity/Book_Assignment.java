package com.ue.uebook.DeatailActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.DeatailActivity.Pojo.Assignment;
import com.ue.uebook.DeatailActivity.Pojo.user_answer;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Book_Assignment extends BaseActivity implements View.OnClickListener, AssignmentAdapter.SubmitAnswerClick {
    private ImageButton back_btn;
    private RecyclerView question_list;
    private AssignmentAdapter assignmentAdapter;
    private Intent intent;
    private List<Assignment> questionList;
    private TextView viewfornoBook;
    private String book_id;
    private LinearLayout question_container;
    private EditText questionEdit;
    List<EditText> allEds;
    private Button submit_assignment;
    private List<String>answerlist;
    private int numberOfLines=1;
    private List<user_answer>user_answers;
    private int textSize;
    private SwipeRefreshLayout swipe_refresh_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__assignment);
        swipe_refresh_layout=findViewById(R.id.swipe_refresh_layout);
        question_list = findViewById(R.id.question_list);
        font_size();
        question_container = findViewById(R.id.question_container);
        viewfornoBook = findViewById(R.id.viewfornoBook);
        submit_assignment = findViewById(R.id.submit_assignment);
        submit_assignment.setOnClickListener(this);
        submit_assignment.setTextSize(textSize);
        questionList = new ArrayList<>();
        allEds = new ArrayList<>();
        answerlist=new ArrayList<>();
        user_answers= new ArrayList<>();
        intent = getIntent();
        questionList = (List<Assignment>)intent.getSerializableExtra("QuestionListExtra");
        user_answers = (List<user_answer>)intent.getSerializableExtra("answer");
        book_id = intent.getStringExtra("book_id");
        if (questionList != null) {
            viewfornoBook.setVisibility(View.GONE);
            question_list.setVisibility(View.GONE);
            Add_Line();
        }
        else
            {
            viewfornoBook.setVisibility(View.VISIBLE);
            question_list.setVisibility(View.GONE);
            submit_assignment.setVisibility(View.INVISIBLE);
        }
        LinearLayoutManager linearLayoutManagerList = new LinearLayoutManager(this);
        linearLayoutManagerList.setOrientation(LinearLayoutManager.VERTICAL);
        question_list.setLayoutManager(linearLayoutManagerList);
        question_list.setNestedScrollingEnabled(false);
        assignmentAdapter = new AssignmentAdapter(questionList);
        question_list.setAdapter(assignmentAdapter);
        assignmentAdapter.setItemClickListener(Book_Assignment.this);
        back_btn = findViewById(R.id.back_asignment);
        back_btn.setOnClickListener(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        if (view == back_btn) {
            finish();
        }
        else if (view == submit_assignment) {
            JSONArray ja = new JSONArray();
            for(int i = 0; i < allEds.size(); i++){
                JSONObject jo = new JSONObject();
                try {
                    jo.put("books_id", book_id);
                    jo.put("assignment_id", questionList.get(i).getId());
                    jo.put("answer", allEds.get(i).getText().toString());
                    jo.put("answered_by", new SessionManager(getApplicationContext()).getUserID());
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                ja.put(jo);
            }

            sendAnswer(String.valueOf(ja));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemClick(String id, String answer) {

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendAnswer(String answer) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforsubmitAssignmentAnswer( answer, new okhttp3.Callback() {
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
    public void Add_Line() {
        for (int i = 0; i < questionList.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View newRowView = inflater.inflate(R.layout.assignment_item, question_container, false);
            questionEdit = newRowView.findViewById(R.id.answer_edit);
            TextView question = newRowView.findViewById(R.id.question);
            question.setText(questionList.get(i).getQuestion());
            question.setTextSize(textSize);
            if (user_answers!=null){
                questionEdit.setText(user_answers.get(i).getAnswer());
                questionEdit.setTextSize(textSize);
            }
            allEds.add(questionEdit);
            questionEdit.setId(numberOfLines + 1);
            question_container.addView(newRowView);
            numberOfLines++;
        }
    }
    private void font_size(){
        switch(new SessionManager(getApplicationContext()).getfontSize()) {
            case "smallest":
                textSize = 12;
                break;
            case "small":
                textSize = 14;
                break;
            case "normal":
                textSize = 16;
                break;
            case "large":
                textSize = 18;
                break;
            case "largest":
                textSize = 24;
                break;
        }
    }
}
