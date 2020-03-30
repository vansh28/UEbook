package com.ue.uebook.ChatSdk.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.ChatSdk.Pojo.Statusmodel;
import com.ue.uebook.ChatSdk.StatusViewScreen;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;

import java.util.List;

public class StatusSeenAdapter   extends RecyclerView.Adapter<StatusSeenAdapter.MyviewHolder>{
     private AppCompatActivity activity;
     private List<Statusmodel>statusmodellist;
    public StatusSeenAdapter(StatusViewScreen statusViewScreen, List<Statusmodel> seenViewList) {

        this.activity=statusViewScreen;
        this.statusmodellist=seenViewList;
    }

    @NonNull
    @Override
    public StatusSeenAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.statusitem, parent, false);
        StatusSeenAdapter.MyviewHolder vh = new StatusSeenAdapter.MyviewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StatusSeenAdapter.MyviewHolder holder, int position) {
        GlideUtils.loadImage(activity, ApiRequest.BaseUrl+"upload/" + statusmodellist.get(position).getUrl(), holder.lastStatusImage, R.drawable.user_default, R.drawable.user_default);
       holder.friendName.setText(statusmodellist.get(position).getUser_name());
    }

    @Override
    public int getItemCount() {
        return statusmodellist.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        private TextView friendName;
        private ImageView lastStatusImage;
        private RelativeLayout rootview;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
          lastStatusImage = itemView.findViewById(R.id.lastStatusImage);
          friendName=itemView.findViewById(R.id.friendName);

        }
    }

}
