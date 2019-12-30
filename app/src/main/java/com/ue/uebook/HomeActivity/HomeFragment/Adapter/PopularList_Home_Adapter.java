package com.ue.uebook.HomeActivity.HomeFragment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
    private int textsize;
    private int colorArray[]={R.drawable.cornercirclebg,R.drawable.cornercirclepink,R.drawable.cornercirclebgred,R.drawable.cornercirclebg};
    public PopularList_Home_Adapter(AppCompatActivity mctx, List<HomeListing> popularBook_list, int textSize) {
        this.popularBook_list=popularBook_list;
        this.mctx=mctx;
        this.textsize=textSize;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.topviewlayoyut, parent, false);
        PopularList_Home_Adapter.MyViewHolder vh = new PopularList_Home_Adapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PopularList_Home_Adapter.MyViewHolder holder, final int position) {
        holder.bookContainerTv.setBackgroundResource(colorArray[position]);
        holder.bookContainerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popularBookItemClick != null) {

                    popularBookItemClick.onItemClick_PopularBook(position, popularBook_list.get(position).getId());

                }
            }
        });
        holder.authorName.setText(popularBook_list.get(position).getAuthor_name());
        holder.bookname.setText(popularBook_list.get(position).getBook_title());
        holder.categoryNameTv.setText(popularBook_list.get(position).getCategory_name());
        GlideUtils.loadImage(mctx, "http://" + popularBook_list.get(position).getThubm_image(), holder.bookimage, R.drawable.noimage, R.drawable.noimage);
        if (popularBook_list.get(position).getRating() != null) {

            holder.ratingBar.setRating(Float.valueOf(popularBook_list.get(position).getRating()));

        }
    }
    @Override
    public int getItemCount() {
        return popularBook_list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout bookContainerTv;
        ImageView bookimage;
        TextView bookname,authorName,bookDesc,categoryNameTv;
        RatingBar ratingBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bookContainerTv=itemView.findViewById(R.id.bookContainerTv);
            bookimage=itemView.findViewById(R.id.bookimageTv);
            bookname=itemView.findViewById(R.id.bookTitleTv);
            authorName=itemView.findViewById(R.id.authorNameTv);
            ratingBar =itemView.findViewById(R.id.myRatingBar);
            categoryNameTv=itemView.findViewById(R.id.categoryNameTv);
        }
}}

