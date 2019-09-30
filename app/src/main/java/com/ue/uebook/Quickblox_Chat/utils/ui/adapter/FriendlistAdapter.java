package com.ue.uebook.Quickblox_Chat.utils.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.Quickblox_Chat.utils.ui.FriendData;
import com.ue.uebook.R;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FriendlistAdapter extends RecyclerView.Adapter<FriendlistAdapter.MyViewHolder> {
    private List<FriendData> data;
    private AppCompatActivity mtx;
    private ItemClick itemClick;
    private Set<FriendData> selectedUsers;

    public FriendlistAdapter(AppCompatActivity mtx, List<FriendData> data) {
        this.data=data;
    }
    public interface ItemClick {
        void onuserclick(int position ,String chatid);

    }

    public void setItemClickListener(ItemClick clickListener) {
        itemClick = clickListener;
    }


    @NonNull
    @Override
    public FriendlistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlistitem, parent, false);
        FriendlistAdapter.MyViewHolder vh = new FriendlistAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendlistAdapter.MyViewHolder holder, final int position) {

//        holder.usertype.setText(data.get(position).getPublisher_type());
//        holder.name.setText(data.get(position).getUser_name());
//              GlideUtils.loadImage(mtx,"http://"+data.get(position).getUrl(),holder.profile,R.drawable.user_default,R.drawable.user_default);
        holder.name.setText(data.get(position).getUser_name());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClick != null) {

                    itemClick.onuserclick(position,data.get(position).getChat_id());
                }
            }
        });

    }

    public String getFirst10Words(String arg) {
        Pattern pattern = Pattern.compile("([\\S]+\\s*){1,10}");
        Matcher matcher = pattern.matcher(arg);
        matcher.find();
        return matcher.group();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public Set<FriendData> getSelectedUsers() {
        return selectedUsers;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name, usertype;
        Button confirm, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);


        }
    }
}

