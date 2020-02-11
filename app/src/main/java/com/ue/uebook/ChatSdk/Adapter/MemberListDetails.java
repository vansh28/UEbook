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

import com.ue.uebook.ChatSdk.Pojo.GroupMemberList;
import com.ue.uebook.ChatSdk.Pojo.UserData;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;

import java.util.List;

public class MemberListDetails  extends RecyclerView.Adapter<MemberListDetails.MyViewHolder>{
    private ContactListAdapter.ItemClick itemClick;
    private List<GroupMemberList> oponentListdata;
    private UserData userData;
    private AppCompatActivity mctx;
    public MemberListDetails(AppCompatActivity mctx, List<GroupMemberList> userList) {
        this.oponentListdata=userList;
        this.mctx=mctx;

    }

    @NonNull
    @Override
    public MemberListDetails.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlis_item, parent, false);

        MemberListDetails.MyViewHolder vh = new MemberListDetails.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull MemberListDetails.MyViewHolder holder, final int position) {
        holder.name.setText(oponentListdata.get(position).getUser_name());
        holder.timeTv.setVisibility(View.GONE);
        GlideUtils.loadImage(mctx, ApiRequest.BaseUrl+"upload/" + oponentListdata.get(position).getUrl(), holder.profile, R.drawable.user_default, R.drawable.user_default);

//        holder.chatContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (itemClick!=null){
//                    itemClick.onContactListItemClick(oponentListdata.get(position),userData);
//                }
//            }
//        });
//        holder.profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (itemClick!=null){
//                    itemClick.onProfileClick(oponentListdata.get(position).url);
//                }
//            }
//        });
//        holder.unreadMszCounterTv.setVisibility(View.GONE);
//        holder.timeTv.setVisibility(View.GONE);
//        holder.activeUnactiveTv.setVisibility(View.GONE);
    }
    @Override
    public int getItemCount() {
        return oponentListdata.size();
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
