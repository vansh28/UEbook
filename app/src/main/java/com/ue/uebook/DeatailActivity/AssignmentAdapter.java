package com.ue.uebook.DeatailActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.ue.uebook.DeatailActivity.Pojo.Assignment;
import com.ue.uebook.R;

import java.util.List;

public class AssignmentAdapter   extends RecyclerView.Adapter<AssignmentAdapter.MyViewHolder>{
    private List<Assignment> questionList;
    private SubmitAnswerClick submit_Answer_Click;
    public AssignmentAdapter(List<Assignment> questionList) {
        this.questionList=questionList;
    }

    public interface SubmitAnswerClick {
        void onItemClick(String id ,String answer );
    }
    public void setItemClickListener(SubmitAnswerClick clickListener) {
        submit_Answer_Click = clickListener;
    }

    @NonNull
    @Override
    public AssignmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_item, parent, false);
        AssignmentAdapter.MyViewHolder vh = new AssignmentAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final AssignmentAdapter.MyViewHolder holder, final int position) {
        if (questionList.get(position).getAnswer() != null) {
            holder.answer_btn.setText(questionList.get(position).getAnswer());
            holder.answer_view.setVisibility(View.GONE);
            holder.editanswer.setVisibility(View.VISIBLE);
        }
        holder.answer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.answer_view.setVisibility(View.VISIBLE);
            }
        });

        holder.question.setText(questionList.get(position).getQuestion());

        holder.editanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.answer_view.setVisibility(View.VISIBLE);
                holder.submit.setVisibility(View.VISIBLE);
                holder.editanswer.setVisibility(View.GONE);
                holder.answer_text_input.setVisibility(View.VISIBLE);
                holder.answer.setVisibility(View.VISIBLE);
                holder.answer_btn.setText("Answer");
                holder.answer.setText(questionList.get(position).getAnswer());
            }
        });
        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.answer.getText().toString().isEmpty()) {
                    if (submit_Answer_Click != null) {
                        holder.submit.setVisibility(View.GONE);
                        holder.answer_view.setVisibility(View.GONE);
                        holder.editanswer.setVisibility(View.VISIBLE);
                        holder.answer_btn.setText(holder.answer.getText().toString());
                        submit_Answer_Click.onItemClick(questionList.get(position).getId(), holder.answer.getText().toString());
                    } else {
                        holder.answer.setError("Enter the Answer");
                        holder.answer.requestFocus();
                        holder.answer.setEnabled(true);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout answer_view;
        TextView answer_btn,question;
        TextInputLayout answer_text_input;
        Button submit;
        EditText answer;
        ImageButton editanswer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            answer_btn=itemView.findViewById(R.id.answer_btn);
            answer_view=itemView.findViewById(R.id.answer_view);
            question =itemView.findViewById(R.id.question);
            submit=itemView.findViewById(R.id.submit_answer);
            answer=itemView.findViewById(R.id.answer_edit);
            editanswer=itemView.findViewById(R.id.editanswer);
            answer_text_input=itemView.findViewById(R.id.answer_text_input);
        }
    }
}
