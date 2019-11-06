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
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder>{
    private ItemClick itemClick;
    private List<OponentData>oponentListdata;
    private UserData userData;
   private AppCompatActivity mctx;
    public ContactListAdapter(AppCompatActivity mctx,List<OponentData> userList, UserData data) {
        this.oponentListdata=userList;
        this.userData=data;
        this.mctx=mctx;
    }

    public interface ItemClick {
        void onContactListItemClick(OponentData oponentData,UserData userData);
        void onProfileClick(String url);
    }
    public void setItemClickListener(ItemClick clickListener) {
        itemClick = clickListener;
    }
    @NonNull
    @Override
    public ContactListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlis_item, parent, false);

        ContactListAdapter.MyViewHolder vh = new ContactListAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull ContactListAdapter.MyViewHolder holder, final int position) {
          holder.name.setText(oponentListdata.get(position).getName());
          holder.userchat.setText(oponentListdata.get(position).publisher_type);
          GlideUtils.loadImage(mctx, "http://dnddemo.com/ebooks/api/v1/upload/" + oponentListdata.get(position).getUrl(), holder.profile, R.drawable.user_default, R.drawable.user_default);

          holder.chatContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick!=null){
                    itemClick.onContactListItemClick(oponentListdata.get(position),userData);
                }
            }
        });
        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick!=null){
                    itemClick.onProfileClick(oponentListdata.get(position).url);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return oponentListdata.size();
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
