package com.ue.uebook.DeatailActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.PopularActivity.Adapter;
import com.ue.uebook.R;

public class Review_List_Adapter  extends RecyclerView.Adapter<Review_List_Adapter.MyViewHolder>{
    @NonNull
    @Override
    public Review_List_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_top_review_list_item, parent, false);
// set the view's size, margins, paddings and layout parameters
        Review_List_Adapter.MyViewHolder vh = new Review_List_Adapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Review_List_Adapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
