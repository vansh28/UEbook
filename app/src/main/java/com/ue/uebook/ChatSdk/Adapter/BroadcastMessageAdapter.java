package com.ue.uebook.ChatSdk.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.ChatSdk.Pojo.Broadcastmessagepojo;
import com.ue.uebook.R;

import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class BroadcastMessageAdapter  extends RecyclerView.Adapter<BroadcastMessageAdapter.MyviewHolder>  {
    List<Broadcastmessagepojo> broadcastmessageList;

    public BroadcastMessageAdapter(List<Broadcastmessagepojo> broadcastmessageList) {
        this.broadcastmessageList=broadcastmessageList;
    }

    @NonNull
    @Override
    public BroadcastMessageAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.broadcastmessageitem, parent, false);
        BroadcastMessageAdapter.MyviewHolder vh = new BroadcastMessageAdapter.MyviewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BroadcastMessageAdapter.MyviewHolder holder, int position) {

       holder.message.setText(broadcastmessageList.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return broadcastmessageList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        private EmojiconTextView message;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.userMessage);
        }
    }
}
