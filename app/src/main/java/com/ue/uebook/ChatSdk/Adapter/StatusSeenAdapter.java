package com.ue.uebook.ChatSdk.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.R;

public class StatusSeenAdapter   extends RecyclerView.Adapter<StatusSeenAdapter.MyviewHolder>{

    @NonNull
    @Override
    public StatusSeenAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.statusitem, parent, false);
        StatusSeenAdapter.MyviewHolder vh = new StatusSeenAdapter.MyviewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StatusSeenAdapter.MyviewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 15;
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
