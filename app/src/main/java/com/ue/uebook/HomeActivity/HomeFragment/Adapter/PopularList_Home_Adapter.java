package com.ue.uebook.HomeActivity.HomeFragment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ue.uebook.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PopularList_Home_Adapter extends RecyclerView.Adapter<PopularList_Home_Adapter.MyViewHolder>{

    private PopularBookItemClick popularBookItemClick;

    public interface PopularBookItemClick {
        void onItemClick_PopularBook(int position);
    }

    public void setItemClickListener(PopularBookItemClick clickListener) {
        popularBookItemClick = clickListener;
    }




    @NonNull
    @Override
    public PopularList_Home_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popularlist_home_item, parent, false);
// set the view's size, margins, paddings and layout parameters
        PopularList_Home_Adapter.MyViewHolder vh = new PopularList_Home_Adapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PopularList_Home_Adapter.MyViewHolder holder, final int position) {
        holder.book_Conatiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popularBookItemClick != null) {
                    popularBookItemClick.onItemClick_PopularBook(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout book_Conatiner;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_Conatiner=itemView.findViewById(R.id.container);
        }
    }
}

