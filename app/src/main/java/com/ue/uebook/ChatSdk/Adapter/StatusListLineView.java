package com.ue.uebook.ChatSdk.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.ChatSdk.Pojo.StatusViewDetail;
import com.ue.uebook.R;

import java.util.List;

public class StatusListLineView  extends RecyclerView.Adapter<StatusListLineView.MyviewHolder>{
    private List<StatusViewDetail> statusmodelList;

    public StatusListLineView(List<StatusViewDetail> statusmodelList) {
        this.statusmodelList=statusmodelList;
    }

    @NonNull
    @Override
    public StatusListLineView.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.statuslineview, parent, false);
        StatusListLineView.MyviewHolder vh = new StatusListLineView.MyviewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StatusListLineView.MyviewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return statusmodelList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        private TextView friendName;
        private ImageView lastStatusImage;
        private RelativeLayout rootview;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);


        }
    }

}

