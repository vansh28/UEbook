package com.ue.uebook.AuthorProfileActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.AuthorProfileActivity.pojo.RequestData;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FriendRequestAdapter   extends RecyclerView.Adapter<FriendRequestAdapter.MyViewHolder>{
    private List<RequestData>data;
    private AppCompatActivity mtx;
    private ItemClick itemClick;

    public FriendRequestAdapter(AppCompatActivity mtx, List<RequestData> data) {
        this.data=data;
        this.mtx=mtx;
    }
    public interface ItemClick {
        void onConfirmclick(int position ,String frndId);
        void ondeleteclick(int position ,String book_id);
    }

    public void setItemClickListener(ItemClick clickListener) {
        itemClick = clickListener;
    }



    @NonNull
    @Override
    public FriendRequestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendrequestitem, parent, false);

        FriendRequestAdapter.MyViewHolder vh = new FriendRequestAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestAdapter.MyViewHolder holder, final int position) {
        holder.usertype.setText(data.get(position).getPublisher_type());
        holder.name.setText(data.get(position).getUser_name());
        GlideUtils.loadImage(mtx,"http://"+data.get(position).getUrl(),holder.profile,R.drawable.user_default,R.drawable.user_default);
         holder.confirm.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (itemClick != null) {
                     itemClick.onConfirmclick(position,data.get(position).getId());
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
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name,usertype;
        Button confirm,delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profile=itemView.findViewById(R.id.image_user);
            name=itemView.findViewById(R.id.name);
            usertype=itemView.findViewById(R.id.usertype);
            confirm=itemView.findViewById(R.id.confirmbtn);
            delete=itemView.findViewById(R.id.deletebtn);

        }
    }}

