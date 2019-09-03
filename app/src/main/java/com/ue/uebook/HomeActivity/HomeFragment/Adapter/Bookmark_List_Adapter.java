package com.ue.uebook.HomeActivity.HomeFragment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.R;

public class Bookmark_List_Adapter  extends RecyclerView.Adapter<Bookmark_List_Adapter.MyViewHolder>{



    private BookmarkBookItemClick bookmarkBookItemClick;

    public interface BookmarkBookItemClick {
        void onItemClick(int position);
    }

    public void setItemClickListener(BookmarkBookItemClick clickListener) {
        bookmarkBookItemClick = clickListener;
    }





    @NonNull
    @Override
    public Bookmark_List_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popularlist_home_item, parent, false);
// set the view's size, margins, paddings and layout parameters
        Bookmark_List_Adapter.MyViewHolder vh = new Bookmark_List_Adapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Bookmark_List_Adapter.MyViewHolder holder, final int position) {
        holder.book_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookmarkBookItemClick != null) {
                    bookmarkBookItemClick.onItemClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return 30;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout book_container;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_container=itemView.findViewById(R.id.container);
        }
    }
}
