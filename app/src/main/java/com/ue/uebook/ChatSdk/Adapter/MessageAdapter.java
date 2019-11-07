package com.ue.uebook.ChatSdk.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.ChatSdk.Pojo.Chathistory;
import com.ue.uebook.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyviewHolder> {

   private List<Chathistory>chatData;
   private String userId;
    public MessageAdapter(List<Chathistory> chat_list, String userID) {
        this.chatData=chat_list;
        this.userId=userID;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lefttext, parent, false);
        MessageAdapter.MyviewHolder vh = new MessageAdapter.MyviewHolder(v); // pass the view to View Holder
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

        Log.d("sender",chatData.get(position).getSender());
        if (chatData.get(position).getSender().equalsIgnoreCase(userId)) {
            holder.sendermsz.setText(chatData.get(position).getMessage());
            holder.oponentlayout.setVisibility(View.GONE);
            holder.senderlayout.setVisibility(View.VISIBLE);
        }
        else {
            holder.oponentmsz.setText(chatData.get(position).getMessage());
            holder.oponentlayout.setVisibility(View.VISIBLE);
            holder.senderlayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatData.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView sendermsz,oponentmsz;
        RelativeLayout senderlayout,oponentlayout;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            sendermsz=itemView.findViewById(R.id.userMessage);
            oponentmsz=itemView.findViewById(R.id.oPonentMessage);
            senderlayout=itemView.findViewById(R.id.senderlayout);
            oponentlayout=itemView.findViewById(R.id.oponentlayout);
        }
    }
}
