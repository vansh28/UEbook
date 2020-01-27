package com.ue.uebook.ChatSdk.Adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.ChatSdk.Pojo.OponentData;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;

import java.util.List;

public class   CreategroupAdapter   extends BaseSelectableListAdapter<OponentData> {

//    private SelectedItemsCountsChangedListener selectedItemsCountChangedListener;
    private ItemClick itemClick;

    public CreategroupAdapter(AppCompatActivity context, List<OponentData> users) {
        super(context, users);
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {

        final CreategroupAdapter.ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chatlis_item, null);
            holder = new CreategroupAdapter.ViewHolder();
            holder.opponentIcon =  convertView.findViewById(R.id.checkboxSelcted);
            holder.opponentName =  convertView.findViewById(R.id.name);
            holder.selectimage=convertView.findViewById(R.id.image_user);
            holder.timeTv=convertView.findViewById(R.id.timeTv);

            convertView.setTag(holder);
        } else {
            holder = (CreategroupAdapter.ViewHolder) convertView.getTag();
        }

        final OponentData user = getItem(position);

        if (user != null) {
            holder.opponentName.setText(user.getName());
            GlideUtils.loadImage(context,user.getUrl(), holder.selectimage, R.drawable.user_default,  R.drawable.user_default);
            holder.timeTv.setVisibility(View.GONE);
            if (selectedItems.contains(user)) {
//                convertView.setBackgroundColor(Color.parseColor("#98A0EE"));
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
        TextView opponentName ,timeTv;
    }

//    public void setSelectedItemsCountsChangedListener(CreategroupAdapter.SelectedItemsCountsChangedListener selectedItemsCountsChanged) {
//        if (selectedItemsCountsChanged != null) {
//            this.selectedItemsCountChangedListener = selectedItemsCountsChanged;
//        }
//    }
//
//    public interface SelectedItemsCountsChangedListener {
//        void onCountSelectedItemsChanged(int count);
//    }
public interface ItemClick {
    void ontItemClick(OponentData oponentData, int position ,int id);
}
    public void setItemClickListener(ItemClick clickListener) {
        itemClick = clickListener;
    }
}
