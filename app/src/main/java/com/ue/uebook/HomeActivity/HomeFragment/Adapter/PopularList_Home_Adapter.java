package com.ue.uebook.HomeActivity.HomeFragment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.GlideUtils;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListing;
import com.ue.uebook.R;

import java.util.List;

public class PopularList_Home_Adapter extends RecyclerView.Adapter<PopularList_Home_Adapter.MyViewHolder>{

    private PopularBookItemClick popularBookItemClick;
    private List<HomeListing> popularBook_list;
    private AppCompatActivity mctx;
    public PopularList_Home_Adapter(AppCompatActivity mctx ,List<HomeListing> popularBook_list) {
        this.popularBook_list=popularBook_list;
        this.mctx=mctx;

    }

    public interface PopularBookItemClick {
        void onItemClick_PopularBook(int position ,String book_id);
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
        holder.book_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popularBookItemClick != null) {
                    popularBookItemClick.onItemClick_PopularBook(position,popularBook_list.get(position).getId());
                }
            }
        });
            holder.authorName.setText(popularBook_list.get(position).getAuthor_name());
            holder.bookname.setText(popularBook_list.get(position).getBook_title());
            GlideUtils.loadImage(mctx,"http://"+popularBook_list.get(position).getThubm_image(),holder.bookimage,R.drawable.noimage,R.drawable.noimage);

    }
    @Override
    public int getItemCount() {
        return popularBook_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout book_container;
        ImageView bookimage;
        TextView bookname,authorName,bookDesc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_container=itemView.findViewById(R.id.container);
            bookimage=itemView.findViewById(R.id.item_image);
            bookname=itemView.findViewById(R.id.bookname);
            authorName=itemView.findViewById(R.id.auhorname);
            bookDesc=itemView.findViewById(R.id.shortDesc);
        }
}}

