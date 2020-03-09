package com.ue.uebook.ChatSdk.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.ChatSdk.GroupMessageScreen;
import com.ue.uebook.ChatSdk.Pojo.GroupMessageLIst;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.MkPlayer.VideoPlayer;
import com.ue.uebook.R;

import java.util.HashMap;
import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.MyviewHolder> {
    private List<GroupMessageLIst> groupMessageLIsts;
    private String userid;
    private AppCompatActivity mtx;
    private ItemClick itemClick;


    public GroupChatAdapter(GroupMessageScreen groupMessageScreen, List<GroupMessageLIst> data, String userid) {
        this.groupMessageLIsts = data;
        this.userid = userid;
        this.mtx = groupMessageScreen;

    }

    public interface ItemClick {
        void onGroupMessage(View v, String chatID, int position);
    }

    public void setItemClickListener(ItemClick clickListener) {
        itemClick = clickListener;
    }

    @NonNull
    @Override
    public GroupChatAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.groupmessagelistitem, parent, false);
        GroupChatAdapter.MyviewHolder vh = new GroupChatAdapter.MyviewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupChatAdapter.MyviewHolder holder, final int position) {
        if (groupMessageLIsts.get(position).getUserid().equalsIgnoreCase(userid)) {
            if (groupMessageLIsts.get(position).getMessagetype().equalsIgnoreCase("text")) {
                holder.oponentlayout.setVisibility(View.GONE);
                holder.senderlayout.setVisibility(View.VISIBLE);
                holder.senderlayoutimage.setVisibility(View.GONE);
                holder.oponentlayoutimage.setVisibility(View.GONE);
                holder.audioviewoponent.setVisibility(View.GONE);
                holder.videoConainersender.setVisibility(View.GONE);
                holder.videoConainerOpopnent.setVisibility(View.GONE);
                holder.audioviewSender.setVisibility(View.GONE);
                holder.sendermsz.setText(groupMessageLIsts.get(position).getMessage());
            } else if (groupMessageLIsts.get(position).getMessagetype().equalsIgnoreCase("image")) {
                holder.oponentlayout.setVisibility(View.GONE);
                holder.senderlayout.setVisibility(View.GONE);
                holder.senderlayoutimage.setVisibility(View.VISIBLE);
                holder.oponentlayoutimage.setVisibility(View.GONE);
                holder.audioviewoponent.setVisibility(View.GONE);
                holder.videoConainersender.setVisibility(View.GONE);
                holder.videoConainerOpopnent.setVisibility(View.GONE);
                holder.audioviewSender.setVisibility(View.GONE);
                GlideUtils.loadImage(mtx, ApiRequest.BaseUrl + groupMessageLIsts.get(position).getMessage(), holder.senderimage, R.drawable.noimage, R.drawable.noimage);
            } else if (groupMessageLIsts.get(position).getMessagetype().equalsIgnoreCase("audio")) {
                holder.oponentlayout.setVisibility(View.GONE);
                holder.senderlayout.setVisibility(View.GONE);
                holder.senderlayoutimage.setVisibility(View.GONE);
                holder.oponentlayoutimage.setVisibility(View.GONE);
                holder.audioviewoponent.setVisibility(View.GONE);
                holder.videoConainersender.setVisibility(View.GONE);
                holder.videoConainerOpopnent.setVisibility(View.GONE);
                holder.audioviewSender.setVisibility(View.VISIBLE);
            } else if (groupMessageLIsts.get(position).getMessagetype().equalsIgnoreCase("video")) {
                try {
                    holder.videoviewSender.setImageBitmap(retriveVideoFrameFromVideo("http://dnddemo.com/ebooks/api/v1/" + groupMessageLIsts.get(position).getMessage()));
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                holder.oponentlayout.setVisibility(View.GONE);
                holder.senderlayout.setVisibility(View.GONE);
                holder.senderlayoutimage.setVisibility(View.GONE);
                holder.oponentlayoutimage.setVisibility(View.GONE);
                holder.audioviewoponent.setVisibility(View.GONE);
                holder.videoConainersender.setVisibility(View.VISIBLE);
                holder.videoConainerOpopnent.setVisibility(View.GONE);
                holder.audioviewSender.setVisibility(View.GONE);
            }
        } else {
            if (groupMessageLIsts.get(position).getMessagetype().equalsIgnoreCase("text")) {
                holder.oponentlayout.setVisibility(View.VISIBLE);
                holder.senderlayout.setVisibility(View.GONE);
                holder.senderlayoutimage.setVisibility(View.GONE);
                holder.oponentlayoutimage.setVisibility(View.GONE);
                holder.audioviewoponent.setVisibility(View.GONE);
                holder.videoConainersender.setVisibility(View.GONE);
                holder.videoConainerOpopnent.setVisibility(View.GONE);
                holder.audioviewSender.setVisibility(View.GONE);
                holder.nameUser.setText(groupMessageLIsts.get(position).getSendername());
                holder.oponentmsz.setText(groupMessageLIsts.get(position).getMessage());
                holder.sendermsz.setText(groupMessageLIsts.get(position).getMessage());
            } else if (groupMessageLIsts.get(position).getMessagetype().equalsIgnoreCase("image")) {
                holder.oponentlayout.setVisibility(View.GONE);
                holder.senderlayout.setVisibility(View.GONE);
                holder.senderlayoutimage.setVisibility(View.GONE);
                holder.oponentlayoutimage.setVisibility(View.VISIBLE);
                holder.audioviewoponent.setVisibility(View.GONE);
                holder.videoConainersender.setVisibility(View.GONE);
                holder.videoConainerOpopnent.setVisibility(View.GONE);
                holder.audioviewSender.setVisibility(View.GONE);
                holder.oponentlayoutimage.setVisibility(View.VISIBLE);
                holder.nameImageSender.setText(groupMessageLIsts.get(position).getSendername());
                GlideUtils.loadImage(mtx, ApiRequest.BaseUrl + groupMessageLIsts.get(position).getMessage(), holder.oponentimage, R.drawable.noimage, R.drawable.noimage);
            } else if (groupMessageLIsts.get(position).getMessagetype().equalsIgnoreCase("audio")) {
                holder.oponentlayout.setVisibility(View.GONE);
                holder.senderlayout.setVisibility(View.GONE);
                holder.senderlayoutimage.setVisibility(View.GONE);
                holder.oponentlayoutimage.setVisibility(View.GONE);
                holder.audioviewoponent.setVisibility(View.VISIBLE);
                holder.videoConainersender.setVisibility(View.GONE);
                holder.videoConainerOpopnent.setVisibility(View.GONE);
                holder.audioviewSender.setVisibility(View.GONE);
            } else if (groupMessageLIsts.get(position).getMessagetype().equalsIgnoreCase("video")) {
                try {
                    holder.VideoViewOponent.setImageBitmap(retriveVideoFrameFromVideo("http://dnddemo.com/ebooks/api/v1/" + groupMessageLIsts.get(position).getMessage()));
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }


                holder.oponentlayout.setVisibility(View.GONE);
                holder.senderlayout.setVisibility(View.GONE);
                holder.senderlayoutimage.setVisibility(View.GONE);
                holder.oponentlayoutimage.setVisibility(View.GONE);
                holder.audioviewoponent.setVisibility(View.GONE);
                holder.videoConainersender.setVisibility(View.GONE);
                holder.videoConainerOpopnent.setVisibility(View.VISIBLE);
                holder.audioviewSender.setVisibility(View.GONE);
            }
        }

        holder.oponentlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (itemClick != null) {
                    itemClick.onGroupMessage(v, groupMessageLIsts.get(position).getUschid(), 3);
                }
                return false;
            }
        });
        holder.senderlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (itemClick != null) {
                    itemClick.onGroupMessage(v, groupMessageLIsts.get(position).getUschid(), 3);
                }
                return false;
            }
        });
        holder.video_start_oponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mtx, VideoPlayer.class);
                Log.e("url","http://dnddemo.com/ebooks/api/v1/" + groupMessageLIsts.get(position).getMessage());
                intent.putExtra("url","http://dnddemo.com/ebooks/api/v1/" + groupMessageLIsts.get(position).getMessage());
                mtx.startActivity(intent);
            }
        });
        holder.video_start_sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mtx, VideoPlayer.class);
                intent.putExtra("url","http://dnddemo.com/ebooks/api/v1/" + groupMessageLIsts.get(position).getMessage());
                mtx.startActivity(intent);
            }
        });
        holder.videoConainersender.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (itemClick != null) {
                    itemClick.onGroupMessage(v, groupMessageLIsts.get(position).getUschid(), 3);
                }
                return false;
            }
        });
        holder.videoConainerOpopnent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (itemClick != null) {
                    itemClick.onGroupMessage(v, groupMessageLIsts.get(position).getUschid(), 3);
                }
                return false;
            }
        });
        holder.oponentlayoutimage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (itemClick != null) {
                    itemClick.onGroupMessage(v, groupMessageLIsts.get(position).getUschid(), 3);
                }
                return false;
            }
        });
        holder.senderlayoutimage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (itemClick != null) {
                    itemClick.onGroupMessage(v, groupMessageLIsts.get(position).getUschid(), 3);
                }
                return false;
            }
        });
        holder.audioviewoponent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (itemClick != null) {
                    itemClick.onGroupMessage(v, groupMessageLIsts.get(position).getUschid(), 3);
                }
                return false;
            }
        });
        holder.audioviewSender.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (itemClick != null) {
                    itemClick.onGroupMessage(v, groupMessageLIsts.get(position).getUschid(), 3);
                }
                return false;
            }
        });
        holder.fileviewSender.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (itemClick != null) {
                    itemClick.onGroupMessage(v, groupMessageLIsts.get(position).getUschid(), 3);
                }
                return false;
            }
        });
        holder.fileviewoponent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (itemClick != null) {
                    itemClick.onGroupMessage(v, groupMessageLIsts.get(position).getUschid(), 3);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupMessageLIsts.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView  nameUser, nameImageSender;
        EmojiconTextView sendermsz, oponentmsz;
        ImageView senderimage, oponentimage, videoviewSender, VideoViewOponent;
        SeekBar senderSeekBarTestPlay, oponentsenderSeekBarTestPlay;

        RelativeLayout videoConainersender, videoConainerOpopnent, fileviewSender, fileviewoponent, audioviewoponent, audioviewSender;
        RelativeLayout senderlayout, oponentlayout, senderlayoutimage, oponentlayoutimage;
        ImageButton video_start_sender, video_start_oponent, playbtnOponent, playbtnSender, downloadimageoponent, downloadfileoponent, downloadaudio, senderButtonTestPlayPause, oponentButtonTestPlayPause;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            video_start_sender = itemView.findViewById(R.id.video_start_sender);
            video_start_oponent = itemView.findViewById(R.id.video_start_oponent);
            oponentsenderSeekBarTestPlay = itemView.findViewById(R.id.oponentSeekBarTestPlay);
            senderSeekBarTestPlay = itemView.findViewById(R.id.SeekBarTestPlay);
            senderButtonTestPlayPause = itemView.findViewById(R.id.ButtonTestPlayPause);
            nameUser = itemView.findViewById(R.id.nameUser);
            nameImageSender = itemView.findViewById(R.id.nameSender);
            oponentButtonTestPlayPause = itemView.findViewById(R.id.oponentButtonTestPlayPause);
            sendermsz = itemView.findViewById(R.id.userMessage);
            oponentmsz = itemView.findViewById(R.id.oPonentMessage);
            senderlayout = itemView.findViewById(R.id.senderlayout);
            oponentlayout = itemView.findViewById(R.id.oponentlayout);
            senderlayoutimage = itemView.findViewById(R.id.senderlayoutimage);
            senderimage = itemView.findViewById(R.id.imageSender);
            oponentimage = itemView.findViewById(R.id.imageoponent);
            videoConainersender = itemView.findViewById(R.id.videoConainersender);
            videoConainerOpopnent = itemView.findViewById(R.id.videoConainerOponet);
            videoviewSender = itemView.findViewById(R.id.videoviewsender);
            VideoViewOponent = itemView.findViewById(R.id.videoviewOpoenent);
            playbtnOponent = itemView.findViewById(R.id.playbtnOponent);
            playbtnSender = itemView.findViewById(R.id.playbtnsender);
            oponentlayoutimage = itemView.findViewById(R.id.oponentlayoutimage);
            fileviewSender = itemView.findViewById(R.id.fileviewSender);
            fileviewoponent = itemView.findViewById(R.id.fileviewoponent);
            audioviewoponent = itemView.findViewById(R.id.audioviewoponent);
            audioviewSender = itemView.findViewById(R.id.audioviewSender);
            downloadimageoponent = itemView.findViewById(R.id.downloadimageoponent);
            downloadfileoponent = itemView.findViewById(R.id.downloadfile);
            downloadaudio = itemView.findViewById(R.id.downloadaudio);

        }
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
