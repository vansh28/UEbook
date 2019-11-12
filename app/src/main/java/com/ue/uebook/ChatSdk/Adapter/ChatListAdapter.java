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

import com.ue.uebook.ChatSdk.Pojo.UserchatList;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;

import java.util.List;

public class ChatListAdapter  extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder>{
    private ItemClick itemClick;
    private AppCompatActivity mtx;
    private List<UserchatList>userchatLists;

    public ChatListAdapter(List<UserchatList> userList, AppCompatActivity mtx) {
        this.mtx=mtx;
        this.userchatLists=userList;
    }

    public interface ItemClick {
        void onUserChatClick(String channelID,String sendTo ,String name);
        void  onUserProfileClick();
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
       holder.chatContainer.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (itemClick!=null){
                   itemClick.onUserChatClick(userchatLists.get(position).getChannel_id(),userchatLists.get(position).getId(),userchatLists.get(position).getUser_name());
               }
           }
       });
        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick!=null){
                    itemClick.onUserProfileClick();
                }
            }
        });
       holder.name.setText(userchatLists.get(position).getUser_name());
       holder.userchat.setText(userchatLists.get(position).getMessage());
        GlideUtils.loadImage(mtx, "http://dnddemo.com/ebooks/api/v1/upload/" + userchatLists.get(position).getUser_pic(), holder.profile, R.drawable.user_default, R.drawable.user_default);

    }
    @Override
    public int getItemCount() {
        return userchatLists.size();
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

