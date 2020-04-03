package com.ue.uebook.ChatSdk.Adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ue.uebook.ChatSdk.Pojo.Chathistory;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyviewHolder> {
   private List<Chathistory>chatData;
   private String userId;
   private AppCompatActivity mtx;
   private ChatImageFileClick chatImageFileClick;
   private List<Chathistory>historyAll;
    private MediaPlayer mediaPlayer =new MediaPlayer();
    private MediaPlayer omediaPlayer =new MediaPlayer();
    private int mediaFileLengthInMilliseconds; // this value contains the song
    private final Handler handler = new Handler();
    public MessageAdapter(AppCompatActivity mtx, List<Chathistory> chat_list, String userID) {
        this.chatData=chat_list;
        this.historyAll=chat_list;
        this.userId=userID;
        this.mtx=mtx;
        setHasStableIds(true);
    }
    public interface ChatImageFileClick {
        void onImageClick(String url ,String type ,String fileName);
        void onDownloadClick(String url ,String type ,String name);
        void onLongClickOnMessage(View view ,Chathistory chatid );
    }
    public void setItemClickListener(ChatImageFileClick clickListener) {
        chatImageFileClick = clickListener;
    }
    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.e("viewtype",String.valueOf(viewType));

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lefttext, parent, false);
        MessageAdapter.MyviewHolder vh = new MessageAdapter.MyviewHolder(v); // pass the view to View Holder
        return vh;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyviewHolder holder, final int position) {
        Log.d("sender",chatData.get(position).getSender());

        final long user = getItemId(position);
        if (chatData.get(position).getSender().equalsIgnoreCase(userId)) {
            if (chatData.get(position).getType().equalsIgnoreCase("image")){
                if (chatData.get(position).getFavorite()==1){
                    holder.oponentStarImage.setVisibility(View.GONE);
                    holder.senderStarImage.setVisibility(View.VISIBLE);
                }
                else {
                    holder.oponentStarImage.setVisibility(View.GONE);
                    holder.senderStarImage.setVisibility(View.GONE);
                }
                holder.oponentStartext.setVisibility(View.GONE);
                holder.senderStartext.setVisibility(View.GONE);
                holder.oponentStarImage.setVisibility(View.GONE);
                holder.senderStarImage.setVisibility(View.GONE);
                holder.progressbarOponent.setVisibility(View.GONE);
                holder.progressbarsender.setVisibility(View.VISIBLE);
                holder.commentSenderLayout.setVisibility(View.GONE);
                holder.commentOponentLayout.setVisibility(View.GONE);
                holder.senderlayoutimage.setVisibility(View.GONE);
                holder.playbtnOponent.setVisibility(View.GONE);
                holder.playbtnSender.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if ( Objects.nonNull(chatData.get(position).getChat_status_id())){
                        holder.progressbarsender.setVisibility(View.GONE);
                       holder.commentSenderLayout.setVisibility(View.VISIBLE);
                       holder.messageTypeSender.setText(chatData.get(position).getType());
                       holder.commentmszSender.setText(chatData.get(position).getChat_status_comm_msg());

                        Glide.with(mtx)
                                .load("http://dnddemo.com/ebooks/" + chatData.get(position).getMessage())

                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        holder.progressbarsender.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        holder.progressbarsender.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(holder.commentimageSender);
                    }
                     else
                     {
                         holder.senderlayoutimage.setVisibility(View.VISIBLE);
                         Glide.with(mtx)
                                 .load(ApiRequest.BaseUrl + chatData.get(position).getMessage())

                                 .listener(new RequestListener<Drawable>() {
                                     @Override
                                     public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                         holder.progressbarsender.setVisibility(View.GONE);
                                         return false;
                                     }

                                     @Override
                                     public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                         holder.progressbarsender.setVisibility(View.GONE);
                                         return false;
                                     }
                                 })
                                 .into(holder.senderimage);
                     }
                }
            }
            else if (chatData.get(position).getType().equalsIgnoreCase("text")) {

                if (chatData.get(position).getFavorite()==1){
                    holder.oponentStartext.setVisibility(View.GONE);
                    holder.senderStartext.setVisibility(View.VISIBLE);
                }
                else {
                    holder.oponentStartext.setVisibility(View.GONE);
                    holder.senderStartext.setVisibility(View.GONE);
                }

                holder.oponentStarImage.setVisibility(View.GONE);
                holder.senderStarImage.setVisibility(View.GONE);
                holder.commentSenderLayout.setVisibility(View.GONE);
                holder.commentOponentLayout.setVisibility(View.GONE);

                holder.progressbarOponent.setVisibility(View.GONE);
                holder.progressbarsender.setVisibility(View.GONE);
                holder.oponentlayout.setVisibility(View.GONE);
                holder.senderlayoutimage.setVisibility(View.GONE);
                holder.playbtnOponent.setVisibility(View.GONE);
                holder.playbtnSender.setVisibility(View.GONE);
                if ( Objects.nonNull(chatData.get(position).getChat_status_id())){
                    holder.progressbarsender.setVisibility(View.GONE);
                    holder.commentSenderLayout.setVisibility(View.VISIBLE);
                    holder.messageTypeSender.setText(chatData.get(position).getMessage());
                    holder.commentmszSender.setText(chatData.get(position).getChat_status_comm_msg());
                    holder.commentimageSender.setVisibility(View.GONE);

                }
                else {
                    holder.senderlayout.setVisibility(View.VISIBLE);
                    holder.sendermsz.setText(chatData.get(position).getMessage());
                }


            }
            else if (chatData.get(position).getType().equalsIgnoreCase("video")){
                holder.oponentStartext.setVisibility(View.GONE);
                holder.senderStartext.setVisibility(View.GONE);
                holder.oponentStarImage.setVisibility(View.GONE);
                holder.senderStarImage.setVisibility(View.GONE);
                holder.commentSenderLayout.setVisibility(View.GONE);
                holder.commentOponentLayout.setVisibility(View.GONE);
                holder.progressbarOponent.setVisibility(View.GONE);
                holder.progressbarsender.setVisibility(View.GONE);
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

                holder.playbtnOponent.setVisibility(View.GONE);
                holder.oponentlayoutimage.setVisibility(View.GONE);

                if ( Objects.nonNull(chatData.get(position).getChat_status_id())){
                    holder.commentSenderLayout.setVisibility(View.VISIBLE);
                    holder.messageTypeSender.setText(chatData.get(position).getType());
                    holder.commentmszSender.setText(chatData.get(position).getChat_status_comm_msg());
                    mediaMetadataRetriever .setDataSource("http://dnddemo.com/ebooks/" + chatData.get(position).getMessage(), new HashMap<String, String>());
                    Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(1000); //unit in microsecond
                    holder.commentimageSender.setImageBitmap(bmFrame);
                }
                else {
                    holder.senderlayoutimage.setVisibility(View.VISIBLE);
                    holder.playbtnSender.setVisibility(View.VISIBLE);
                    mediaMetadataRetriever .setDataSource(ApiRequest.BaseUrl + chatData.get(position).getMessage(), new HashMap<String, String>());
                    Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(1000); //unit in microsecond
                    holder.senderimage.setImageBitmap(bmFrame);

                }



            }
            else if (chatData.get(position).getType().equalsIgnoreCase("docfile")) {
                holder.oponentStartext.setVisibility(View.GONE);
                holder.senderStartext.setVisibility(View.GONE);
                holder.oponentStarImage.setVisibility(View.GONE);
                holder.senderStarImage.setVisibility(View.GONE);
                holder.commentSenderLayout.setVisibility(View.GONE);
                holder.commentOponentLayout.setVisibility(View.GONE);
                holder.progressbarOponent.setVisibility(View.GONE);
                holder.progressbarsender.setVisibility(View.GONE);
                     holder.fileviewSender.setVisibility(View.VISIBLE);
                     holder.fileviewoponent.setVisibility(View.GONE);
            }
            else if (chatData.get(position).getType().equalsIgnoreCase("audio")) {
                holder.oponentStartext.setVisibility(View.GONE);
                holder.senderStartext.setVisibility(View.GONE);
                holder.oponentStarImage.setVisibility(View.GONE);
                holder.senderStarImage.setVisibility(View.GONE);
                holder.commentSenderLayout.setVisibility(View.GONE);
                holder.commentOponentLayout.setVisibility(View.GONE);
                holder.progressbarOponent.setVisibility(View.GONE);
                holder.progressbarsender.setVisibility(View.GONE);
                holder.audioviewSender.setVisibility(View.VISIBLE);
                holder.audioviewoponent.setVisibility(View.GONE);
            }

        }
        else  {
            if (chatData.get(position).getType().equalsIgnoreCase("image")){
                holder.oponentStartext.setVisibility(View.GONE);
                holder.senderStartext.setVisibility(View.GONE);
                if (chatData.get(position).getFavorite()==1){
                    holder.oponentStarImage.setVisibility(View.GONE);
                    holder.senderStarImage.setVisibility(View.VISIBLE);
                }
                else {
                    holder.oponentStarImage.setVisibility(View.GONE);
                    holder.senderStarImage.setVisibility(View.GONE);
                }
                holder.oponentStarImage.setVisibility(View.GONE);
                holder.senderStarImage.setVisibility(View.GONE);
                holder.commentSenderLayout.setVisibility(View.GONE);
                holder.commentOponentLayout.setVisibility(View.GONE);
                holder.progressbarOponent.setVisibility(View.VISIBLE);
                holder.progressbarsender.setVisibility(View.GONE);
                holder.senderlayoutimage.setVisibility(View.GONE);

                holder.playbtnOponent.setVisibility(View.GONE);
                holder.playbtnSender.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if ( Objects.nonNull(chatData.get(position).getChat_status_id())){
                        holder.progressbarOponent.setVisibility(View.GONE);
                        holder.commentOponentLayout.setVisibility(View.VISIBLE);
                        holder.messageTypeOponent.setText(chatData.get(position).getType());
                        holder.commentmszOponent.setText(chatData.get(position).getChat_status_comm_msg());
                        Glide.with(mtx)
                                .load("http://dnddemo.com/ebooks/" + chatData.get(position).getMessage())

                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        holder.progressbarOponent.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        holder.progressbarOponent.setVisibility(View.GONE);

                                        return false;
                                    }
                                })
                                .into(holder.commnetimageOponent);
                    }
                    else
                    {
                        holder.oponentlayoutimage.setVisibility(View.VISIBLE);
                        Glide.with(mtx)
                                .load(ApiRequest.BaseUrl + chatData.get(position).getMessage())

                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        holder.progressbarOponent.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        holder.progressbarOponent.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(holder.oponentimage);
                    }
                }

            }
            else if (chatData.get(position).getType().equalsIgnoreCase("text")) {
                if (chatData.get(position).getFavorite()==1){
                    holder.oponentStartext.setVisibility(View.VISIBLE);
                    holder.senderStartext.setVisibility(View.GONE);
                }
                else {
                    holder.oponentStartext.setVisibility(View.GONE);
                    holder.senderStartext.setVisibility(View.GONE);
                }
                holder.oponentStarImage.setVisibility(View.GONE);
                holder.senderStarImage.setVisibility(View.GONE);
                holder.commentSenderLayout.setVisibility(View.GONE);
                holder.commentOponentLayout.setVisibility(View.GONE);
                holder.progressbarOponent.setVisibility(View.GONE);
                holder.progressbarsender.setVisibility(View.GONE);


                holder.oponentlayoutimage.setVisibility(View.GONE);
                holder.senderlayout.setVisibility(View.GONE);
                holder.playbtnOponent.setVisibility(View.VISIBLE);
                holder.playbtnSender.setVisibility(View.GONE);

                if ( Objects.nonNull(chatData.get(position).getChat_status_id())){
                    holder.progressbarOponent.setVisibility(View.GONE);
                    holder.commentOponentLayout.setVisibility(View.VISIBLE);
                    holder.messageTypeOponent.setText(chatData.get(position).getMessage());
                    holder.commentmszOponent.setText(chatData.get(position).getChat_status_comm_msg());
                    holder.commnetimageOponent.setVisibility(View.GONE);

                }
                else {
                    holder.oponentlayout.setVisibility(View.VISIBLE);
                    holder.oponentmsz.setText(chatData.get(position).getMessage());
                }


            }
            else if (chatData.get(position).getType().equalsIgnoreCase("video")){
                holder.oponentStartext.setVisibility(View.GONE);
                holder.senderStartext.setVisibility(View.GONE);
                holder.oponentStarImage.setVisibility(View.GONE);
                holder.senderStarImage.setVisibility(View.GONE);
                holder.commentSenderLayout.setVisibility(View.GONE);
                holder.commentOponentLayout.setVisibility(View.GONE);
                holder.progressbarOponent.setVisibility(View.GONE);
                holder.progressbarsender.setVisibility(View.GONE);
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                holder.senderlayoutimage.setVisibility(View.GONE);
                holder.playbtnSender.setVisibility(View.GONE);

                if ( Objects.nonNull(chatData.get(position).getChat_status_id())){
                    holder.progressbarOponent.setVisibility(View.GONE);
                    holder.commentOponentLayout.setVisibility(View.VISIBLE);
                    holder.messageTypeOponent.setText(chatData.get(position).getType());
                    holder.commentmszOponent.setText(chatData.get(position).getChat_status_comm_msg());
                    mediaMetadataRetriever .setDataSource("http://dnddemo.com/ebooks/" + chatData.get(position).getMessage(), new HashMap<String, String>());
                    Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(1000); //unit in microsecond
                    holder.commnetimageOponent.setImageBitmap(bmFrame);
                }
                else {
                    holder.playbtnOponent.setVisibility(View.VISIBLE);
                    holder.oponentlayoutimage.setVisibility(View.VISIBLE);
                    mediaMetadataRetriever .setDataSource(ApiRequest.BaseUrl + chatData.get(position).getMessage(), new HashMap<String, String>());
                    Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(1000); //unit in microsecond


                }



            }
            else if (chatData.get(position).getType().equalsIgnoreCase("docfile")) {
                holder.oponentStartext.setVisibility(View.GONE);
                holder.senderStartext.setVisibility(View.GONE);
                holder.oponentStarImage.setVisibility(View.GONE);
                holder.senderStarImage.setVisibility(View.GONE);
                holder.commentSenderLayout.setVisibility(View.GONE);
                holder.commentOponentLayout.setVisibility(View.GONE);
                holder.progressbarOponent.setVisibility(View.GONE);
                holder.progressbarsender.setVisibility(View.GONE);
                holder.fileviewoponent.setVisibility(View.VISIBLE);
                holder.fileviewSender.setVisibility(View.GONE);
            }
            else if (chatData.get(position).getType().equalsIgnoreCase("audio")) {
                holder.oponentStartext.setVisibility(View.GONE);
                holder.senderStartext.setVisibility(View.GONE);
                holder.oponentStarImage.setVisibility(View.GONE);
                holder.senderStarImage.setVisibility(View.GONE);
                holder.commentSenderLayout.setVisibility(View.GONE);
                holder.commentOponentLayout.setVisibility(View.GONE);
                holder.progressbarOponent.setVisibility(View.GONE);
                holder.progressbarsender.setVisibility(View.GONE);
                holder.audioviewoponent.setVisibility(View.VISIBLE);
                holder.audioviewSender.setVisibility(View.GONE);
            }

        }


        holder.playbtnSender.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (chatImageFileClick!=null){
                    chatImageFileClick.onLongClickOnMessage(v,chatData.get(position));
                }
                return false;
            }
        });
        holder.playbtnOponent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (chatImageFileClick!=null){
                    chatImageFileClick.onLongClickOnMessage(v,chatData.get(position));
                }
                return false;
            }
        });

        holder.senderimage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (chatData.get(position).getType().equalsIgnoreCase("image")){
                    if ( Objects.nonNull(chatData.get(position).getChat_status_id())){
                        chatImageFileClick.onImageClick("http://dnddemo.com/ebooks/" + chatData.get(position).getMessage(),"image",chatData.get(position).getMessage());

                    }
                    else {
                        chatImageFileClick.onImageClick(ApiRequest.BaseUrl + chatData.get(position).getMessage(),"image",chatData.get(position).getMessage());

                    }
                }
                else {
                    chatImageFileClick.onImageClick(ApiRequest.BaseUrl + chatData.get(position).getMessage(),"video",chatData.get(position).getMessage());

                }
            }
        });


        holder.oponentimage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (chatImageFileClick!=null){
                    chatImageFileClick.onLongClickOnMessage(v,chatData.get(position));
                }
                return false;
            }
        });
        holder.senderimage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (chatImageFileClick!=null){
                    chatImageFileClick.onLongClickOnMessage(v,chatData.get(position));
                }
                return false;
            }
        });


        holder.oponentimage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (chatData.get(position).getType().equalsIgnoreCase("image")) {
                    if ( Objects.nonNull(chatData.get(position).getChat_status_id())){
                        chatImageFileClick.onImageClick("http://dnddemo.com/ebooks/" + chatData.get(position).getMessage(),"image",chatData.get(position).getMessage());

                    }
                    else {
                        chatImageFileClick.onImageClick(ApiRequest.BaseUrl + chatData.get(position).getMessage(),"image",chatData.get(position).getMessage());

                    }

                }
                else {
                    chatImageFileClick.onImageClick(ApiRequest.BaseUrl + chatData.get(position).getMessage(),"video",chatData.get(position).getMessage());
                }
            }
        });
        holder.playbtnSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatData.get(position).getType().equalsIgnoreCase("image")){
                    chatImageFileClick.onImageClick(ApiRequest.BaseUrl+ chatData.get(position).getMessage(),"image",chatData.get(position).getMessage());

                }
                else {
                    chatImageFileClick.onImageClick(ApiRequest.BaseUrl + chatData.get(position).getMessage(),"video" ,chatData.get(position).getMessage());

                }
            }
        });
        holder.playbtnOponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatData.get(position).getType().equalsIgnoreCase("image")) {
                    chatImageFileClick.onImageClick(ApiRequest.BaseUrl + chatData.get(position).getMessage(),"image",chatData.get(position).getMessage());
                }
                else {
                    chatImageFileClick.onImageClick(ApiRequest.BaseUrl+ chatData.get(position).getMessage(),"video",chatData.get(position).getMessage());
                }
            }
        });
        holder.fileviewoponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatImageFileClick.onImageClick(ApiRequest.BaseUrl + chatData.get(position).getMessage(),"file",chatData.get(position).getMessage());

            }
        });
        holder.fileviewSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatImageFileClick.onImageClick(ApiRequest.BaseUrl + chatData.get(position).getMessage(),"file",chatData.get(position).getMessage());

            }
        });

        holder.senderlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (chatImageFileClick!=null){
                    chatImageFileClick.onLongClickOnMessage(v,chatData.get(position));
                }
                return false;
            }
        });
        holder.commentSenderLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (chatImageFileClick!=null){
                    chatImageFileClick.onLongClickOnMessage(v,chatData.get(position));
                }
                return false;
            }
        });
        holder.commentOponentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (chatImageFileClick!=null){
                    chatImageFileClick.onLongClickOnMessage(v,chatData.get(position));
                }
                return false;
            }
        });
        holder.oponentlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (chatImageFileClick!=null){
//                    if (historyAll.contains(user)){
//                        v.setBackgroundColor(Color.parseColor("#d3d3d3"));
//                    }
//                    else {
//                        v.setBackgroundColor(Color.parseColor("#ffffff"));
//                    }

                    chatImageFileClick.onLongClickOnMessage(v,chatData.get(position));
                }
                return false;
            }
        });

//        holder.audioviewoponent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chatImageFileClick.onImageClick("http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage(),"audio");
//
//            }
//        });
//        holder.audioviewSender.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chatImageFileClick.onImageClick("http://dnddemo.com/ebooks/api/v1/" + chatData.get(position).getMessage(),"audio");
//            }
//        });

        holder.downloadfileoponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str= chatData.get(position).getMessage();
                String[] arrOfStr = str.split("/");
                Log.d("finsl",arrOfStr[3]);
                chatImageFileClick.onDownloadClick(ApiRequest.BaseUrl + chatData.get(position).getMessage(),"file",arrOfStr[3]);

            }
        });
//        holder.downloadimageoponent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                  String str= chatData.get(position).getMessage();
//                String[] arrOfStr = str.split("/");
//                Log.d("finsl",arrOfStr[3]);
//                chatImageFileClick.onDownloadClick(ApiRequest.BaseUrl + chatData.get(position).getMessage(),"image",arrOfStr[3]);
//            }
//        });
        holder.downloadaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatImageFileClick.onDownloadClick(ApiRequest.BaseUrl + chatData.get(position).getMessage(),"audio",chatData.get(position).getMessage());

            }
        });

        holder.senderButtonTestPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url=ApiRequest.BaseUrl + chatData.get(position).getMessage();
                    Log.d("urllll",url);
                    mediaPlayer.setDataSource(url); // setup
                    mediaPlayer.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mediaFileLengthInMilliseconds = mediaPlayer.getDuration();

                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    holder.senderButtonTestPlayPause.setImageResource(R.drawable.pause);

                } else {
                    mediaPlayer.pause();
                    holder.senderButtonTestPlayPause.setImageResource(R.drawable.play);
                }
                primarySeekBarProgressUpdater(holder.senderSeekBarTestPlay);

            }


        });
        holder.oponentButtonTestPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer.setDataSource(ApiRequest.BaseUrl + chatData.get(position).getMessage()); // setup
                    mediaPlayer.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mediaFileLengthInMilliseconds = mediaPlayer.getDuration();
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    holder.oponentButtonTestPlayPause.setImageResource(R.drawable.pause);

                } else {
                    mediaPlayer.pause();
                    holder.oponentButtonTestPlayPause.setImageResource(R.drawable.play);
                }
                primarySeekBarProgressUpdater(holder.oponentsenderSeekBarTestPlay);
            }

        });


    }

    private void primarySeekBarProgressUpdater(final SeekBar SeekBarTestPlay) {
        SeekBarTestPlay.setProgress((int) (((float) mediaPlayer
                .getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater(SeekBarTestPlay);
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }
    @Override
    public int getItemCount() {
        return chatData.size();
    }

    private void clearMediaPlayer(MediaPlayer mp) {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }
    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView nameUser ,messageTypeOponent,messageTypeSender ;
        EmojiconTextView sendermsz,oponentmsz ,commentmszOponent,commentmszSender;
        ImageView senderimage,oponentimage ,videoviewSender,VideoViewOponent,commentimageSender,commnetimageOponent;
        SeekBar senderSeekBarTestPlay,oponentsenderSeekBarTestPlay;
         ProgressBar progressbarsender,progressbarOponent;
        RelativeLayout videoConainersender,videoConainerOpopnent ,fileviewSender,fileviewoponent ,audioviewoponent,audioviewSender;
        RelativeLayout senderlayout,oponentlayout,senderlayoutimage,oponentlayoutimage;
        ImageButton playbtnOponent,playbtnSender,downloadimageoponent,downloadfileoponent,downloadaudio,senderButtonTestPlayPause,oponentButtonTestPlayPause;
        LinearLayout  commentSenderLayout,commentOponentLayout;

          ImageView oponentStartext,senderStartext,senderStarImage,oponentStarImage;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            oponentStartext=itemView.findViewById(R.id.oponentStartext);
            senderStartext = itemView.findViewById(R.id.senderStartext);
            senderStarImage = itemView.findViewById(R.id.senderStarImage);
            oponentStarImage =itemView.findViewById(R.id.oponentStarImage);

            commentSenderLayout=itemView.findViewById(R.id.SendertCommentLAyout);
            commentOponentLayout = itemView.findViewById(R.id.oponentCommentLAyout);
            messageTypeOponent = itemView.findViewById(R.id.messageTypeOponent);
            messageTypeSender =itemView.findViewById(R.id.messageTypeSender);
            commentmszOponent =itemView.findViewById(R.id.commentMszOponent);
            commentmszSender =itemView.findViewById(R.id.commentMszSender);
            commentimageSender =itemView.findViewById(R.id.imageCommentSender);
            commnetimageOponent = itemView.findViewById(R.id.imageCommentOponent);

            oponentsenderSeekBarTestPlay=itemView.findViewById(R.id.oponentSeekBarTestPlay);
            senderSeekBarTestPlay=itemView.findViewById(R.id.SeekBarTestPlay);
            senderButtonTestPlayPause=itemView.findViewById(R.id.ButtonTestPlayPause);
            progressbarOponent = itemView.findViewById(R.id.progressbarOpopnent);
            progressbarsender = itemView.findViewById(R.id.progressbarsender);
            oponentButtonTestPlayPause=itemView.findViewById(R.id.oponentButtonTestPlayPause);
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
            fileviewSender=itemView.findViewById(R.id.fileviewSender);
            fileviewoponent=itemView.findViewById(R.id.fileviewoponent);
            audioviewoponent=itemView.findViewById(R.id.audioviewoponent);
            audioviewSender=itemView.findViewById(R.id.audioviewSender);
           // downloadimageoponent=itemView.findViewById(R.id.downloadimageoponent);
            downloadfileoponent=itemView.findViewById(R.id.downloadfile);
            downloadaudio=itemView.findViewById(R.id.downloadaudio);

        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
