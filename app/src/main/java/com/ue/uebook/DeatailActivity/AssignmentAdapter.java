package com.ue.uebook.DeatailActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.DeatailActivity.Pojo.ReviewPojo;
import com.ue.uebook.R;

import java.util.List;

public class AssignmentAdapter   extends RecyclerView.Adapter<AssignmentAdapter.MyViewHolder>{
    private List<ReviewPojo> data;
    private AppCompatActivity mtc;
    private boolean isshow=false;

    @NonNull
    @Override
    public AssignmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_item, parent, false);
        AssignmentAdapter.MyViewHolder vh = new AssignmentAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final AssignmentAdapter.MyViewHolder holder, int position) {
               holder.answer_btn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       holder.answer_view.setVisibility(View.VISIBLE);
                   }
               });

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout answer_view;
        TextView answer_btn,question;
        Button submit;
        EditText answer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            answer_btn=itemView.findViewById(R.id.answer_btn);
            answer_view=itemView.findViewById(R.id.answer_view);
            question =itemView.findViewById(R.id.question);
            submit=itemView.findViewById(R.id.submit_answer);
            answer=itemView.findViewById(R.id.answer_edit);
        }
    }
}
