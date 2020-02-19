package com.ue.uebook.ChatSdk.Adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.ChatSdk.Pojo.GroupMemberList;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.util.List;

public class GroupMemberListAdapter  extends BaseSelectableListAdapter<GroupMemberList> {

    //
    private GroupMemberItemClick itemClick;

    public GroupMemberListAdapter(AppCompatActivity context, List<GroupMemberList> users) {
        super(context, users);
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {

        final CreategroupAdapter.ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.bottomgroupitem, null);
            holder = new CreategroupAdapter.ViewHolder();


            holder.opponentIcon =  convertView.findViewById(R.id.checkboxSelcted);
           holder.opponentName =  convertView.findViewById(R.id.name);
            holder.selectimage=convertView.findViewById(R.id.image_user);
//            holder.timeTv=convertView.findViewById(R.id.timeTv);

            convertView.setTag(holder);
        } else {
            holder = (CreategroupAdapter.ViewHolder) convertView.getTag();
        }

        final GroupMemberList user = getItem(position);

       if (user != null) {
           if (!user.getId().equalsIgnoreCase(new SessionManager(context).getUserID())){
               holder.opponentName.setVisibility(View.VISIBLE);
               holder.selectimage.setVisibility(View.VISIBLE);
           holder.opponentName.setText(user.getUser_name());
            GlideUtils.loadImage(context, ApiRequest.BaseUrl+"upload/"+user.getUrl(), holder.selectimage, R.drawable.user_default,  R.drawable.user_default);
           }
           else {
               holder.opponentName.setVisibility(View.GONE);
               holder.selectimage.setVisibility(View.GONE);
           }
            //            holder.timeTv.setVisibility(View.GONE);
           if (selectedItems.contains(user)) {
               //    convertView.setBackgroundColor(Color.parseColor("#98A0EE"));
               // holder.opponentIcon.setImageResource(R.drawable.ic_checkmark);
               holder.opponentIcon.setVisibility(View.VISIBLE);
               convertView.setBackgroundColor(Color.parseColor("#d3d3d3"));
           } else {
               convertView.setBackgroundColor(Color.parseColor("#ffffff"));
               holder.opponentIcon.setVisibility(View.GONE);

           }
       }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSelection(position);
                if (itemClick!=null){

                    if (selectedItems.contains(user)) {
                        itemClick.ontItemClick(user,position,1 );

                    } else {
                        itemClick.ontItemClick(user,position,2 );
                    }

                }


            }
        });

        return convertView;
    }

    public static class ViewHolder {
        ImageView opponentIcon,selectimage;
        TextView opponentName ,timeTv ,lastmsz;
    }
    public interface GroupMemberItemClick {
        void ontItemClick(GroupMemberList groupMemberList, int position , int id);
    }
    public void setItemClickListener(GroupMemberItemClick clickListener) {
        itemClick = clickListener;
    }

}
