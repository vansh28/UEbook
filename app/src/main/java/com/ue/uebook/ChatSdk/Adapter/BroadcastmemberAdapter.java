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

import com.ue.uebook.ChatSdk.BroadcastDetailScreen;
import com.ue.uebook.ChatSdk.Pojo.GroupMemberList;
import com.ue.uebook.R;

import java.util.List;

public class BroadcastmemberAdapter extends  RecyclerView.Adapter<BroadcastmemberAdapter.MyViewHolder> {
    private AppCompatActivity mctx;
    private List<GroupMemberList> groupMemberList;
    public BroadcastmemberAdapter(BroadcastDetailScreen broadcastDetailScreen, List<GroupMemberList> groupMemberList) {


    }

    @NonNull
    @Override
    public BroadcastmemberAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlis_item, parent, false);

        BroadcastmemberAdapter.MyViewHolder vh = new BroadcastmemberAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BroadcastmemberAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profile,activeUnactiveTv;
        TextView name,userchat,unreadMszCounterTv,timeTv;
        RelativeLayout chatContainer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profile=itemView.findViewById(R.id.image_user);
            name=itemView.findViewById(R.id.name);
            userchat=itemView.findViewById(R.id.userchat);
            chatContainer =itemView.findViewById(R.id.chatContainer);
            unreadMszCounterTv=itemView.findViewById(R.id.unreadMszCounterTv);
            timeTv=itemView.findViewById(R.id.timeTv);
            activeUnactiveTv=itemView.findViewById(R.id.activeUnactiveTv);
        }
    }}
