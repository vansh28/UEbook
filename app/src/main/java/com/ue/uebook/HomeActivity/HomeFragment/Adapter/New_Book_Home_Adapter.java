package com.ue.uebook.HomeActivity.HomeFragment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.R;

public class New_Book_Home_Adapter   extends RecyclerView.Adapter<New_Book_Home_Adapter.MyViewHolder> {

    private NewBookItemClick newBookItemClick;

    public interface NewBookItemClick {
        void onItemClick_NewBook(int position);
    }

    public void setItemClickListener(NewBookItemClick clickListener) {
        newBookItemClick = clickListener;
    }

    @NonNull
    @Override
    public New_Book_Home_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_home_item, parent, false);
// set the view's size, margins, paddings and layout parameters
        New_Book_Home_Adapter.MyViewHolder vh = new New_Book_Home_Adapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull New_Book_Home_Adapter.MyViewHolder holder, final int position) {
        holder.main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newBookItemClick != null) {
                    newBookItemClick.onItemClick_NewBook(position);
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
