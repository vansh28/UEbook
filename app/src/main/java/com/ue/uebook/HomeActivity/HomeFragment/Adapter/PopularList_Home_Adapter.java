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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if (popularBook_list.get(position).getBook_description().length()>11){
            holder.bookDesc.setText(getFirst10Words(popularBook_list.get(position).getBook_description())+"...");
        }
        else {
            holder.bookDesc.setText(popularBook_list.get(position).getBook_description());
        }            GlideUtils.loadImage(mctx,"http://"+popularBook_list.get(position).getThubm_image(),holder.bookimage,R.drawable.noimage,R.drawable.noimage);
        if (popularBook_list.get(position).getRating()!=null){
            holder.ratingBar.setRating( Float.valueOf(popularBook_list.get(position).getRating()));
        }
    }
    public String getFirst10Words(String arg) {
        Pattern pattern = Pattern.compile("([\\S]+\\s*){1,10}");
        Matcher matcher = pattern.matcher(arg);
        matcher.find();
        return matcher.group();
    }
    @Override
    public int getItemCount() {
        return popularBook_list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout book_container;
        ImageView bookimage;
        TextView bookname,authorName,bookDesc;
        RatingBar ratingBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_container=itemView.findViewById(R.id.container);
            bookimage=itemView.findViewById(R.id.item_image);
            bookname=itemView.findViewById(R.id.bookname);
            authorName=itemView.findViewById(R.id.auhorname);
            bookDesc=itemView.findViewById(R.id.shortDesc);
            ratingBar =itemView.findViewById(R.id.myRatingBar);
        }
}}

