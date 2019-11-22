package com.ue.uebook.ChatSdk.Adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListAdapter  extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder>{
    private ItemClick itemClick;
    private AppCompatActivity mtx;
    private Data data;
    private List<UserList> userList=null;
    private String reciverName;
    private String userID;
    private List<UserList>dataList;

    public ChatListAdapter(Data data, List<UserList> userList, AppCompatActivity mtx ,String userID) {
        this.dataList=userList;
        this.mtx=mtx;
        this.userList = new ArrayList<UserList>();
        this.userList.addAll(userList);
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
        if (userID.equalsIgnoreCase(dataList.get(position).getSend_detail().getId())){
            reciverName=dataList.get(position).getRec_detail().getUser_name();
            holder.name.setText(dataList.get(position).getRec_detail().getUser_name());
            GlideUtils.loadImage(mtx, "http://dnddemo.com/ebooks/api/v1/upload/" + dataList.get(position).getRec_detail().getUrl(), holder.profile, R.drawable.user_default, R.drawable.user_default);

        }
        else {
            reciverName=dataList.get(position).getSend_detail().getUser_name();
            holder.name.setText(dataList.get(position).getSend_detail().getUser_name());
            GlideUtils.loadImage(mtx, "http://dnddemo.com/ebooks/api/v1/upload/" + dataList.get(position).getSend_detail().getUrl(), holder.profile, R.drawable.user_default, R.drawable.user_default);

        }

       holder.chatContainer.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (itemClick!=null){
                   if (userID.equalsIgnoreCase(dataList.get(position).getSend_detail().getId())){
                       itemClick.onUserChatClick(dataList.get(position).getChannel_id(),dataList.get(position).getRec_detail().getId(),userList.get(position).getRec_detail().getUser_name(),userList.get(position).getRec_detail().getUrl());

                   }
                   else {
                       itemClick.onUserChatClick(dataList.get(position).getChannel_id(),dataList.get(position).getSend_detail().getId(),userList.get(position).getSend_detail().getUser_name(),userList.get(position).getSend_detail().getUrl());

                   }
               }
           }
       });
        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick!=null){

                    if (userID.equalsIgnoreCase(dataList.get(position).getSend_detail().getId())){
                        itemClick.onUserProfileClick(dataList.get(position).getRec_detail().getUrl());
                    }
                    else {
                        itemClick.onUserProfileClick(dataList.get(position).getSend_detail().getUrl());
                    }

                }
            }
        });
        if (dataList.get(position).getType().equalsIgnoreCase("text"))
        {
            if (dataList.get(position).getRead_msg().equalsIgnoreCase("1")){
                holder.userchat.setText(getFirst4Words(dataList.get(position).getMessage()+".."));
            }
            else {
                holder.userchat.setTextColor(Color.parseColor("#000000"));
                holder.userchat.setTypeface( holder.userchat.getTypeface(), Typeface.BOLD);
                holder.userchat.setText(getFirst4Words(dataList.get(position).getMessage()+".."));
            }
        }
        else if (dataList.get(position).getType().equalsIgnoreCase("image")){
            holder.userchat.setText("Image");
        }
        else if (dataList.get(position).getType().equalsIgnoreCase("video")){
            holder.userchat.setText("Video");
        }
        else if (dataList.get(position).getType().equalsIgnoreCase("docfile")){
            holder.userchat.setText("File");
        }
        else if (dataList.get(position).getType().equalsIgnoreCase("audio")){
            holder.userchat.setText("Audio");
        }
        String str= dataList.get(position).getCreated();
        String[] arrOfStr = str.split(" ");
        Log.d("finsl",arrOfStr[1]);
        holder.timeTv.setText(arrOfStr[1]);
        if (Integer.valueOf(dataList.get(position).getMess_count().getTotalMessagecount())>0){
            holder.unreadMszCounterTv.setVisibility(View.VISIBLE);
            holder.unreadMszCounterTv.setText(dataList.get(position).getMess_count().getTotalMessagecount());
        }
        else {
            holder.unreadMszCounterTv.setVisibility(View.GONE);
        }
        }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name,userchat,timeTv,unreadMszCounterTv;
        RelativeLayout chatContainer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profile=itemView.findViewById(R.id.image_user);
            name=itemView.findViewById(R.id.name);
            userchat=itemView.findViewById(R.id.userchat);
            chatContainer =itemView.findViewById(R.id.chatContainer);
            timeTv=itemView.findViewById(R.id.timeTv);
            unreadMszCounterTv=itemView.findViewById(R.id.unreadMszCounterTv);
        }
    }
    public String getFirst4Words(String arg) {
        Pattern pattern = Pattern.compile("([\\S]+\\s*){1,3}");
        Matcher matcher = pattern.matcher(arg);
        matcher.find();
        return matcher.group();
    }
    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        dataList.clear();
        if (charText.length() == 0) {
              dataList.addAll(userList);
        }
        else
        {
            for (UserList wp : userList) {
                if (wp.getRec_detail().getUser_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}

