package com.ue.uebook.ChatSdk.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.ChatSdk.Pojo.OponentData;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;

import java.util.List;

public class GroupItemListAdapter  extends RecyclerView.Adapter<GroupItemListAdapter.MyViewHolder>{

    private ItemClick itemClick;
    private List<OponentData> oponentListdata;
//    private UserData userData;
    private AppCompatActivity mctx;
    public GroupItemListAdapter(AppCompatActivity mctx, List<OponentData> userList) {
        this.oponentListdata=userList;
        this.mctx=mctx;
    }
    public interface ItemClick {
        void ongroupListItemClick(OponentData oponentData ,int position);
    }
    public void setItemClickListener(ItemClick clickListener) {
        itemClick = clickListener;
    }
    @NonNull
    @Override
    public GroupItemListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grouplistitem, parent, false);
        GroupItemListAdapter.MyViewHolder vh = new GroupItemListAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull GroupItemListAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(getFirstWord(oponentListdata.get(position).getName()));
        GlideUtils.loadImage(mctx, ApiRequest.BaseUrl+"upload/" + oponentListdata.get(position).getUrl(), holder.profile, R.drawable.user_default, R.drawable.user_default);
        holder.listContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (itemClick!=null){
                     itemClick.ongroupListItemClick(oponentListdata.get(position),position);
                 }
            }
        });

    }
    @Override
    public int getItemCount() {
        return oponentListdata.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profile,activeUnactiveTv;
        TextView name,userchat,unreadMszCounterTv,timeTv;
        LinearLayout listContainer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profile =itemView.findViewById(R.id.image_user);
            name=itemView.findViewById(R.id.name);
            listContainer=itemView.findViewById(R.id.container);
        }
    }

    private String getFirstWord(String text) {

        int index = text.indexOf(' ');

        if (index > -1) { // Check if there is more than one word.

            return text.substring(0, index).trim(); // Extract first word.

        } else {

            return text; // Text is the first word itself.
        }
    }

}
