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

import com.ue.uebook.ChatSdk.Pojo.Data;
import com.ue.uebook.ChatSdk.Pojo.UserList;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;

import java.util.List;
public class ChatListAdapter  extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder>{
    private ItemClick itemClick;
    private AppCompatActivity mtx;
    private Data data;
    private List<UserList> userList;
    private String reciverName;
    private String userID;

    public ChatListAdapter(Data data, List<UserList> userList, AppCompatActivity mtx ,String userID) {
        this.mtx=mtx;
        this.userList=userList;
        this.data=data;
        this.userID=userID;
    }
    public interface ItemClick {
        void onUserChatClick(String channelID,String sendTo ,String name ,String imageUrl);
        void  onUserProfileClick(String imageURl);
    }
    public void setItemClickListener(ItemClick clickListener) {
        itemClick = clickListener;
    }
    @NonNull
    @Override
    public ChatListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlis_item, parent, false);
        ChatListAdapter.MyViewHolder vh = new ChatListAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.MyViewHolder holder, final int position) {
        if (userID.equalsIgnoreCase(userList.get(position).getSend_detail().getId())){
            reciverName=userList.get(position).getRec_detail().getUser_name();
            holder.name.setText(userList.get(position).getRec_detail().getUser_name());
            GlideUtils.loadImage(mtx, "http://dnddemo.com/ebooks/api/v1/upload/" + userList.get(position).getRec_detail().getUrl(), holder.profile, R.drawable.user_default, R.drawable.user_default);

        }
        else {
            reciverName=userList.get(position).getSend_detail().getUser_name();
            holder.name.setText(userList.get(position).getSend_detail().getUser_name());
            GlideUtils.loadImage(mtx, "http://dnddemo.com/ebooks/api/v1/upload/" + userList.get(position).getSend_detail().getUrl(), holder.profile, R.drawable.user_default, R.drawable.user_default);

        }

       holder.chatContainer.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (itemClick!=null){
                   if (userID.equalsIgnoreCase(userList.get(position).getSend_detail().getId())){
                       itemClick.onUserChatClick(userList.get(position).getChannel_id(),userList.get(position).getRec_detail().getId(),userList.get(position).getRec_detail().getUser_name(),userList.get(position).getRec_detail().getUrl());

                   }
                   else {
                       itemClick.onUserChatClick(userList.get(position).getChannel_id(),userList.get(position).getSend_detail().getId(),userList.get(position).getSend_detail().getUser_name(),userList.get(position).getSend_detail().getUrl());

                   }
               }
           }
       });
        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick!=null){

                    if (userID.equalsIgnoreCase(userList.get(position).getSend_detail().getId())){
                        itemClick.onUserProfileClick(userList.get(position).getRec_detail().getUrl());
                    }
                    else {
                        itemClick.onUserProfileClick(userList.get(position).getSend_detail().getUrl());
                    }

                }
            }
        });

        if (userList.get(position).getType().equalsIgnoreCase("text")){
            holder.userchat.setText(userList.get(position).getMessage());
        }
        else if (userList.get(position).getType().equalsIgnoreCase("image")){
            holder.userchat.setText("image");
        }
        else if (userList.get(position).getType().equalsIgnoreCase("video")){
            holder.userchat.setText("video");
        }

    }
    @Override
    public int getItemCount() {
        return userList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name,userchat;
        RelativeLayout chatContainer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profile=itemView.findViewById(R.id.image_user);
            name=itemView.findViewById(R.id.name);
            userchat=itemView.findViewById(R.id.userchat);
            chatContainer =itemView.findViewById(R.id.chatContainer);
        }
    }}

