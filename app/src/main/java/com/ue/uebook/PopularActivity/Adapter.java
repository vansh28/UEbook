package com.ue.uebook.PopularActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ue.uebook.HomeActivity.HomeFragment.Adapter.PopularList_Home_Adapter;
import com.ue.uebook.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter  extends RecyclerView.Adapter<Adapter.MyViewHolder>{
    @NonNull
    @Override
    public Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popularlist_home_item, parent, false);
// set the view's size, margins, paddings and layout parameters
        Adapter.MyViewHolder vh = new Adapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

