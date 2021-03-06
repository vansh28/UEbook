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

import com.ue.uebook.ChatSdk.Pojo.OponentData;
import com.ue.uebook.ChatSdk.Pojo.UserData;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;

import java.util.List;
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder>{
    private ItemClick itemClick;
    private List<OponentData> oponentListdata;
    private UserData userData;
    private AppCompatActivity mctx;
    private int selected_position = -1;
    public GroupAdapter(AppCompatActivity mctx,List<OponentData> userList, UserData data) {
        this.oponentListdata=userList;
        this.userData=data;
        this.mctx=mctx;
    }

    public interface ItemClick {
        void onContactListItemClick(OponentData oponentData,UserData userData);
    }
    public void setItemClickListener(ItemClick clickListener) {
        itemClick = clickListener;
    }
    @NonNull
    @Override
    public GroupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlis_item, parent, false);
        GroupAdapter.MyViewHolder vh = new GroupAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(oponentListdata.get(position).getName());
        holder.userchat.setText(oponentListdata.get(position).publisher_type);
        GlideUtils.loadImage(mctx, ApiRequest.BaseUrl+"upload/" + oponentListdata.get(position).getUrl(), holder.profile, R.drawable.user_default, R.drawable.user_default);


        holder.chatContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick!=null) {
                    itemClick.onContactListItemClick(oponentListdata.get(position), userData);

                }
            }
        });
        holder.unreadMszCounterTv.setVisibility(View.GONE);
        holder.timeTv.setVisibility(View.GONE);
        holder.activeUnactiveTv.setVisibility(View.GONE);
    }
    @Override
    public int getItemCount() {
        return oponentListdata.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profile,activeUnactiveTv,selectedUser;
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
            selectedUser=itemView.findViewById(R.id.checkboxSelcted);
        }
    }}
