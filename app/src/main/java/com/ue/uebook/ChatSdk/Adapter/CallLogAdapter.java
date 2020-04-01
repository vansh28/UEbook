package com.ue.uebook.ChatSdk.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.ChatSdk.Pojo.callResponse;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;

import java.util.List;

public class CallLogAdapter  extends RecyclerView.Adapter<CallLogAdapter.MyviewHolder>{

    private  List<callResponse> callResponseList;
    private AppCompatActivity context;
    private String userid;

    public CallLogAdapter(AppCompatActivity callLogFragment, List<callResponse> form , String userid) {
        this.context = callLogFragment;
        this.callResponseList=form;
        this.userid = userid;
    }

    @NonNull
    @Override
    public CallLogAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_log_item, parent, false);
        CallLogAdapter.MyviewHolder vh = new CallLogAdapter.MyviewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CallLogAdapter.MyviewHolder holder, int position) {
          if (callResponseList.get(position).getType().equalsIgnoreCase("audio")){
            holder.calltype.setBackgroundResource(R.drawable.phone);
          }
          else {
              holder.calltype.setBackgroundResource(R.drawable.videoc);
          }
          if (callResponseList.get(position).getSender_info().getId().equalsIgnoreCase(userid)){
              holder.userName.setText(callResponseList.get(position).getReceiver_info().getUser_name());
          }
          else {
              holder.userName.setText(callResponseList.get(position).getSender_info().getUser_name());
          }
        if (callResponseList.get(position).getSender_info().getId().equalsIgnoreCase(userid)){
            GlideUtils.loadImage(context, ApiRequest.BaseUrl+"upload/" +callResponseList.get(position).getReceiver_info().getUrl(), holder.imageuser, R.drawable.user_default, R.drawable.user_default);

        }
        else {

            GlideUtils.loadImage(context, ApiRequest.BaseUrl+"upload/" + callResponseList.get(position).getSender_info().getUrl(), holder.imageuser, R.drawable.user_default, R.drawable.user_default);

        }
        if (callResponseList.get(position).getSender().equalsIgnoreCase(userid)){
            holder.CallTime.setText(callResponseList.get(position).getCreated());
            holder.CallTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.upcall, 0, 0, 0);

        }
        else {
            holder.CallTime.setText(callResponseList.get(position).getCreated());
            holder.CallTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diagonalarrow, 0, 0, 0);
        }

    }

    @Override
    public int getItemCount() {
        return callResponseList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        private ImageView calltype,imageuser;
        private TextView CallTime,userName;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            calltype = itemView .findViewById(R.id.calltype);
            imageuser = itemView .findViewById(R.id.image_user);
            CallTime = itemView .findViewById(R.id.time);
            userName = itemView .findViewById(R.id.username);



        }
    }
}
