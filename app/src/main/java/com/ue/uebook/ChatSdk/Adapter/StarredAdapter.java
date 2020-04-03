package com.ue.uebook.ChatSdk.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ue.uebook.ChatSdk.Pojo.StarredMessageResponse;
import com.ue.uebook.ChatSdk.StarredMessageScreen;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class StarredAdapter extends RecyclerView.Adapter<StarredAdapter.MyviewHolder> {
    private AppCompatActivity context;
    private List<StarredMessageResponse>starredAllResponseList;
    private String userid;
    public StarredAdapter(StarredMessageScreen starredMessageScreen, List<StarredMessageResponse> data , String userid) {
              this.context=starredMessageScreen;
              this.starredAllResponseList=data;
              this.userid=userid;
    }


    @NonNull
    @Override
    public StarredAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.starred_list_item, parent, false);
        StarredAdapter.MyviewHolder vh = new StarredAdapter.MyviewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StarredAdapter.MyviewHolder holder, int position) {

        if (starredAllResponseList.get(position).getMessage_type().equalsIgnoreCase("text"))
        {
            holder.oPonentMessage.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
            holder.oPonentMessage.setText(starredAllResponseList.get(position).getMessage());
        }
        else if (starredAllResponseList.get(position).getMessage_type().equalsIgnoreCase("image"))
        {
            holder.oPonentMessage.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(starredAllResponseList.get(position).getMessage())
                    .placeholder(R.drawable.noimage)
                    .error(R.drawable.noimage)
                    .fit().centerInside()
                    .into(holder.imageView);
        }
          if (starredAllResponseList.get(position).getChat_info_sender().equalsIgnoreCase(userid)){
              holder.senderName.setText("You");
              holder.reciverName.setText(starredAllResponseList.get(position).getUser_name());
            //  GlideUtils.loadImage(context, ApiRequest.BaseUrl+"upload/" + new SessionManager(context).getUserimage(), holder.image_user, R.drawable.user_default, R.drawable.user_default);
              Picasso.get()
                      .load(ApiRequest.BaseUrl+"upload/"+new SessionManager(context).getUserimage())
                      .placeholder(R.drawable.user_default)
                      .error(R.drawable.user_default)
                      .fit().centerInside()
                      .into(holder.image_user);
          }
          else {
            Picasso.get()
                    .load(starredAllResponseList.get(position).getUrl())
                    .placeholder(R.drawable.user_default)
                    .error(R.drawable.user_default)
                    .fit().centerInside()
                    .into(holder.image_user);
              holder.reciverName.setText("You");
              holder.senderName.setText(starredAllResponseList.get(position).getUser_name());
          }
          holder.date.setText(starredAllResponseList.get(position).getCreated_at());

    }

    @Override
    public int getItemCount() {
        return starredAllResponseList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        EmojiconTextView oPonentMessage;
        ImageView image_user,imageView;
        TextView reciverName,senderName,date;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            oPonentMessage =itemView.findViewById(R.id.oPonentMessage);
            date = itemView.findViewById(R.id.date);
            image_user = itemView.findViewById(R.id.image_user);
            reciverName=itemView.findViewById(R.id.reciverName);
            senderName = itemView.findViewById(R.id.senderName);
            imageView = itemView.findViewById(R.id.imageview);
        }
    }
}
