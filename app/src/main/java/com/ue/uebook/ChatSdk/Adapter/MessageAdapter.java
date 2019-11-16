package com.ue.uebook.ChatSdk.Adapter;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.ChatSdk.Pojo.Chathistory;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;

import java.util.HashMap;
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
        void onImageClick(String url ,String type);

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
                holder.playbtnOponent.setVisibility(View.GONE);
                holder.playbtnSender.setVisibility(View.GONE);
                GlideUtils.loadImage(mtx, "http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage(), holder.senderimage, R.drawable.noimage, R.drawable.noimage);
            }
            else if (chatData.get(position).getType().equalsIgnoreCase("text")) {
                holder.sendermsz.setText(chatData.get(position).getMessage());
                holder.oponentlayout.setVisibility(View.GONE);
                holder.senderlayout.setVisibility(View.VISIBLE);
                holder.senderlayoutimage.setVisibility(View.GONE);
                holder.playbtnOponent.setVisibility(View.GONE);
                holder.playbtnSender.setVisibility(View.GONE);
            }
            else if (chatData.get(position).getType().equalsIgnoreCase("video")){
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                holder.senderlayoutimage.setVisibility(View.VISIBLE);
                holder.playbtnOponent.setVisibility(View.GONE);
                holder.oponentlayoutimage.setVisibility(View.GONE);
                holder.playbtnSender.setVisibility(View.VISIBLE);
                mediaMetadataRetriever .setDataSource("http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage(), new HashMap<String, String>());
                Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(1000); //unit in microsecond
                holder.senderimage.setImageBitmap(bmFrame);
            }

        }
        else  {
            if (chatData.get(position).getType().equalsIgnoreCase("image")){
                holder.senderlayoutimage.setVisibility(View.GONE);
                holder.oponentlayoutimage.setVisibility(View.VISIBLE);
                holder.playbtnOponent.setVisibility(View.GONE);
                holder.playbtnSender.setVisibility(View.GONE);
                GlideUtils.loadImage(mtx, "http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage(), holder.oponentimage, R.drawable.noimage, R.drawable.noimage);

            }
            else if (chatData.get(position).getType().equalsIgnoreCase("text")) {
                holder.oponentmsz.setText(chatData.get(position).getMessage());
                holder.oponentlayout.setVisibility(View.VISIBLE);
                holder.oponentlayoutimage.setVisibility(View.GONE);
                holder.senderlayout.setVisibility(View.GONE);
                holder.playbtnOponent.setVisibility(View.GONE);
                holder.playbtnSender.setVisibility(View.GONE);
            }
            else if (chatData.get(position).getType().equalsIgnoreCase("video")){
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                holder.senderlayoutimage.setVisibility(View.GONE);
                holder.playbtnOponent.setVisibility(View.VISIBLE);
                holder.oponentlayoutimage.setVisibility(View.VISIBLE);
                holder.playbtnSender.setVisibility(View.GONE);
                mediaMetadataRetriever .setDataSource("http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage(), new HashMap<String, String>());
                Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(1000); //unit in microsecond
                holder.oponentimage.setImageBitmap(bmFrame);
            }

        }
        holder.senderimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatData.get(position).getType().equalsIgnoreCase("image")){
                    chatImageFileClick.onImageClick("http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage(),"image");

                }
                else {
                    chatImageFileClick.onImageClick("http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage(),"video");

                }
            }
        });
        holder.oponentimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatData.get(position).getType().equalsIgnoreCase("image")) {
                    chatImageFileClick.onImageClick("http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage(),"image");
                }
                else {
                    chatImageFileClick.onImageClick("http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage(),"video");
                }
            }
        });
        holder.playbtnSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatData.get(position).getType().equalsIgnoreCase("image")){
                    chatImageFileClick.onImageClick("http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage(),"image");

                }
                else {
                    chatImageFileClick.onImageClick("http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage(),"video");

                }
            }
        });
        holder.playbtnOponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatData.get(position).getType().equalsIgnoreCase("image")) {
                    chatImageFileClick.onImageClick("http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage(),"image");
                }
                else {
                    chatImageFileClick.onImageClick("http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage(),"video");
                }
            }
        });


    }
    @Override
    public int getItemCount() {
        return chatData.size();
    }
    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView sendermsz,oponentmsz;
        ImageView senderimage,oponentimage ,videoviewSender,VideoViewOponent;

        RelativeLayout videoConainersender,videoConainerOpopnent;
        RelativeLayout senderlayout,oponentlayout,senderlayoutimage,oponentlayoutimage;
        ImageButton playbtnOponent,playbtnSender;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            sendermsz=itemView.findViewById(R.id.userMessage);
            oponentmsz=itemView.findViewById(R.id.oPonentMessage);
            senderlayout=itemView.findViewById(R.id.senderlayout);
            oponentlayout=itemView.findViewById(R.id.oponentlayout);
            senderlayoutimage=itemView.findViewById(R.id.senderlayoutimage);
            senderimage=itemView.findViewById(R.id.imageSender);
            oponentimage=itemView.findViewById(R.id.imageoponent);
            videoConainersender=itemView.findViewById(R.id.videoConainersender);
            videoConainerOpopnent=itemView.findViewById(R.id.videoConainerOponet);
            videoviewSender=itemView.findViewById(R.id.videoviewsender);
            VideoViewOponent=itemView.findViewById(R.id.videoviewOpoenent);
            playbtnOponent=itemView.findViewById(R.id.playbtnOponent);
            playbtnSender=itemView.findViewById(R.id.playbtnsender);
            oponentlayoutimage=itemView.findViewById(R.id.oponentlayoutimage);

        }
    }
}
