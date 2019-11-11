package com.ue.uebook.ChatSdk.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.ChatSdk.Pojo.Chathistory;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;

import java.util.List;
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyviewHolder> {
   private List<Chathistory>chatData;
   private String userId;
   private AppCompatActivity mtx;
   private ChatImageFileClick chatImageFileClick;

    public MessageAdapter(AppCompatActivity mtx, List<Chathistory> chat_list, String userID) {
        this.chatData=chat_list;
        this.userId=userID;
        this.mtx=mtx;
    }
    public interface ChatImageFileClick {
        void onImageClick(String url);

    }
    public void setItemClickListener(ChatImageFileClick clickListener) {
        chatImageFileClick = clickListener;
    }
    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lefttext, parent, false);
        MessageAdapter.MyviewHolder vh = new MessageAdapter.MyviewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, final int position) {
        Log.d("sender",chatData.get(position).getSender());
        if (chatData.get(position).getSender().equalsIgnoreCase(userId)) {
            if (chatData.get(position).getType().equalsIgnoreCase("image")){
                holder.senderlayoutimage.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(mtx, "http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage(), holder.senderimage, R.drawable.noimage, R.drawable.noimage);

            }
            else {
                holder.sendermsz.setText(chatData.get(position).getMessage());
                holder.oponentlayout.setVisibility(View.GONE);
                holder.senderlayout.setVisibility(View.VISIBLE);
                holder.senderlayoutimage.setVisibility(View.GONE);
            }
        }
        else  {
            if (chatData.get(position).getType().equalsIgnoreCase("image")){
                holder.senderlayoutimage.setVisibility(View.GONE);
                holder.oponentlayoutimage.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(mtx, "http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage(), holder.oponentimage, R.drawable.noimage, R.drawable.noimage);

            }
            else {
                holder.oponentmsz.setText(chatData.get(position).getMessage());
                holder.oponentlayout.setVisibility(View.VISIBLE);
                holder.oponentlayoutimage.setVisibility(View.GONE);
                holder.senderlayout.setVisibility(View.GONE);
            }

        }
        holder.senderimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               chatImageFileClick.onImageClick("http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage());
            }
        });
        holder.oponentimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatImageFileClick.onImageClick("http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage());
            }
        });

    }
    @Override
    public int getItemCount() {
        return chatData.size();
    }
    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView sendermsz,oponentmsz;
        ImageView senderimage,oponentimage;
        RelativeLayout senderlayout,oponentlayout,senderlayoutimage,oponentlayoutimage;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            sendermsz=itemView.findViewById(R.id.userMessage);
            oponentmsz=itemView.findViewById(R.id.oPonentMessage);
            senderlayout=itemView.findViewById(R.id.senderlayout);
            oponentlayout=itemView.findViewById(R.id.oponentlayout);
            senderlayoutimage=itemView.findViewById(R.id.senderlayoutimage);
            senderimage=itemView.findViewById(R.id.imageSender);
            oponentimage=itemView.findViewById(R.id.imageoponent);
            oponentlayoutimage=itemView.findViewById(R.id.oponentlayoutimage);

        }
    }
}
