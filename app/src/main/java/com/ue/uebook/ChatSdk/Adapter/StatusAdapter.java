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

import com.ue.uebook.ChatSdk.Pojo.Statusmodel;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;

import java.util.List;

public class StatusAdapter  extends RecyclerView.Adapter<StatusAdapter.MyviewHolder>{
    private List<Statusmodel>statusmodelList;
    private AppCompatActivity context;
    private ItemClick itemClick;

    public StatusAdapter(AppCompatActivity statusFragment, List<Statusmodel> statusmodelList) {
        this.context=statusFragment;
        this.statusmodelList=statusmodelList;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.statusitem, parent, false);
        StatusAdapter.MyviewHolder vh = new StatusAdapter.MyviewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        holder.friendName.setText(statusmodelList.get(position).getUser_name());
        GlideUtils.loadImage(context, ApiRequest.BaseUrl+"upload/" + statusmodelList.get(position).getUrl(), holder.lastStatusImage, R.drawable.user_default, R.drawable.user_default);
        holder.rootview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if (itemClick!=null){
                            itemClick.ontItemClick(statusmodelList.get(position),position);
                        }
            }
        });
    }

    @Override
    public int getItemCount() {
        return statusmodelList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
              private TextView friendName;
              private ImageView lastStatusImage;
              private RelativeLayout rootview;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friendName);
            lastStatusImage =itemView.findViewById(R.id.lastStatusImage);
            rootview =itemView.findViewById(R.id.rootview);

        }
    }
    public interface ItemClick {
        void ontItemClick(Statusmodel oponentData, int position );
    }
    public void setItemClickListener(ItemClick clickListener) {
        itemClick = clickListener;
    }
}
