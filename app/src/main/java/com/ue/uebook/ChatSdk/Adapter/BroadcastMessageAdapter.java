package com.ue.uebook.ChatSdk.Adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ue.uebook.ChatSdk.Pojo.Broadcastmessagepojo;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;

import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class BroadcastMessageAdapter  extends RecyclerView.Adapter<BroadcastMessageAdapter.MyviewHolder>  {
    List<Broadcastmessagepojo> broadcastmessageList;
    private AppCompatActivity mtx;

    public BroadcastMessageAdapter(List<Broadcastmessagepojo> broadcastmessageList , AppCompatActivity mtx) {
        this.broadcastmessageList=broadcastmessageList;
        this.mtx=mtx;
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
          if (broadcastmessageList.get(position).getType().equalsIgnoreCase("text")){
              holder.senderlayout.setVisibility(View.VISIBLE);
              holder.senderlayoutimage.setVisibility(View.GONE);
              holder.message.setText(broadcastmessageList.get(position).getMessage());
          }
          else if (broadcastmessageList.get(position).getType().equalsIgnoreCase("image")){
              holder.senderlayout.setVisibility(View.GONE);
              holder.senderlayoutimage.setVisibility(View.VISIBLE);
              Glide.with(mtx)
                      .load(ApiRequest.BaseUrl + broadcastmessageList.get(position).getMessage())

                      .listener(new RequestListener<Drawable>() {
                          @Override
                          public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                             // holder.progressbarsender.setVisibility(View.GONE);
                              return false;
                          }

                          @Override
                          public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                          //    holder.progressbarsender.setVisibility(View.GONE);
                              return false;
                          }
                      })
                      .into(holder.imageSender);

          }


    }

    @Override
    public int getItemCount() {
        return broadcastmessageList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        private EmojiconTextView message;
        private RelativeLayout senderlayout,senderlayoutimage;
        private ImageView imageSender;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.userMessage);
            senderlayout=itemView.findViewById(R.id.senderlayout);
            senderlayoutimage=itemView.findViewById(R.id.senderlayoutimage);
            imageSender = itemView.findViewById(R.id.imageSender);

        }
    }
}
