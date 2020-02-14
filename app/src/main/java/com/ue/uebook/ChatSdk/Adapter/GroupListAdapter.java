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

import com.ue.uebook.ChatSdk.Pojo.Grouplist;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupListAdapter  extends RecyclerView.Adapter<GroupListAdapter.MyViewHolder>{
    private List<Grouplist>grouplist;
    private AppCompatActivity mtx;
    private ItemClick itemClick;

    public GroupListAdapter(AppCompatActivity activity, List<Grouplist> group_details) {
        this.grouplist=group_details;
        this.mtx=activity;

    }

    public interface ItemClick {
        void ongroupListItemClick(Grouplist grouplist);
    }
    public void setItemClickListener(ItemClick clickListener) {
        itemClick = clickListener;
    }

    @NonNull
    @Override
    public GroupListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlis_item, parent, false);
        GroupListAdapter.MyViewHolder vh = new GroupListAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull GroupListAdapter.MyViewHolder holder, final int position) {
               holder.name.setText(grouplist.get(position).getName());
            GlideUtils.loadImage(mtx, "http:/dnddemo.com/ebooks/api/v1/" + grouplist.get(position).getGroup_image(),holder.profile, R.drawable.user_default, R.drawable.user_default);
             holder.chatContainer.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (itemClick!=null){
                       itemClick.ongroupListItemClick(grouplist.get(position));
                   }
               }
           });
    }


    @Override
    public int getItemCount() {
        return grouplist.size();
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

}

