package com.ue.uebook.ChatSdk.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.R;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder>{

    private ItemClick itemClick;

    public interface ItemClick {
        void onContactListItemClick();
        void onProfileClick();
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
          holder.userchat.setText("Reader");
        holder.chatContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick!=null){
                    itemClick.onContactListItemClick();
                }
            }
        });
        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick!=null){
                    itemClick.onProfileClick();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 30;
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
