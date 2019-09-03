package com.ue.uebook.HomeActivity.HomeFragment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ue.uebook.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Home_recommended_Adapter extends RecyclerView.Adapter<Home_recommended_Adapter.MyViewHolder> {

    private RecommendedItemClick recommendedItemClick;

    public interface RecommendedItemClick {
        void onItemClick(int position);
    }

    public void setItemClickListener(RecommendedItemClick clickListener) {
        recommendedItemClick = clickListener;
    }

    @NonNull
    @Override
    public Home_recommended_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_home_item, parent, false);
// set the view's size, margins, paddings and layout parameters
        Home_recommended_Adapter.MyViewHolder vh = new Home_recommended_Adapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Home_recommended_Adapter.MyViewHolder holder, final int position) {
        holder.main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recommendedItemClick != null) {
                    recommendedItemClick.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout main_container;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            main_container=itemView.findViewById(R.id.container_book);
        }
    }
}
