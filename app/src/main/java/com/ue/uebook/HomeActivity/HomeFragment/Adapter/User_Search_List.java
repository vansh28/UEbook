package com.ue.uebook.HomeActivity.HomeFragment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListing;
import com.ue.uebook.R;

import java.util.List;

public class User_Search_List  extends RecyclerView.Adapter<User_Search_List.MyViewHolder> {

    private SearchListItemClick searchListItemClick;
    private List<String> dataList;
    private List<HomeListing> arraylist=null;
    private AppCompatActivity mctx;
    public User_Search_List(List<String> list) {
        this.dataList=list;

    }
    public interface SearchListItemClick {
        void onItemClick(String book_id);
        void ondeleteItemClick(String position);
    }

    public void setItemClickListener(SearchListItemClick clickListener) {
        searchListItemClick = clickListener;
    }


    @NonNull
    @Override
    public User_Search_List.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchhistory_item, parent, false);
        User_Search_List.MyViewHolder vh = new User_Search_List.MyViewHolder(v); // pass the view to View Holder


        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final User_Search_List.MyViewHolder holder, final int position) {
        holder.search_item_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchListItemClick != null) {
                    searchListItemClick.onItemClick(dataList.get(position));
                }
            }
        });
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchListItemClick != null) {
                    searchListItemClick.ondeleteItemClick("1");
                }
            }
        });
        holder.search_title.setText(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout search_item_container;
        ImageView removeBtn;
        TextView search_title;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            search_item_container=itemView.findViewById(R.id.search_item_container);
            removeBtn=itemView.findViewById(R.id.removeBtn);
            search_title=itemView.findViewById(R.id.search_title);

        }
    }

}
