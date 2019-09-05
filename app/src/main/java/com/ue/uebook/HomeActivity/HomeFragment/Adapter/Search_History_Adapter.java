package com.ue.uebook.HomeActivity.HomeFragment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.R;

public class Search_History_Adapter   extends RecyclerView.Adapter<Search_History_Adapter.MyViewHolder> {


    private SearchHistoryItemClick searchHistoryItemClick;
    public interface SearchHistoryItemClick {
        void onItemClick(int position);
    }

    public void setItemClickListener(SearchHistoryItemClick clickListener) {
        searchHistoryItemClick = clickListener;
    }


    @NonNull
    @Override
    public Search_History_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchhistory_item, parent, false);
// set the view's size, margins, paddings and layout parameters
        Search_History_Adapter.MyViewHolder vh = new Search_History_Adapter.MyViewHolder(v); // pass the view to View Holder


        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final Search_History_Adapter.MyViewHolder holder, final int position) {



    }

    @Override
    public int getItemCount() {
        return 30;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
