package com.ue.uebook.ChatSdk.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.R;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class StarredAdapter extends RecyclerView.Adapter<StarredAdapter.MyviewHolder> {
    @NonNull
    @Override
    public StarredAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.starred_list_item, parent, false);
        StarredAdapter.MyviewHolder vh = new StarredAdapter.MyviewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StarredAdapter.MyviewHolder holder, int position) {
      holder.oPonentMessage.setText("this is demo of starrwed message");
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        EmojiconTextView oPonentMessage;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            oPonentMessage =itemView.findViewById(R.id.oPonentMessage);
        }
    }
}
