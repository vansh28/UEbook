package com.ue.uebook.HomeActivity.HomeFragment.Adapter;

import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home_recommended_Adapter extends RecyclerView.Adapter<Home_recommended_Adapter.MyViewHolder> {

    private RecommendedItemClick recommendedItemClick;
    List<HomeListing> recommendedList_book;
    private AppCompatActivity mctx;
     // for loading main list
    private List<HomeListing> arraylist=null;
    private int textsize;// for loading  filter data

    public Home_recommended_Adapter(AppCompatActivity mctx, List<HomeListing> recommendedList_book, int textSize) {
        this.recommendedList_book=recommendedList_book;
        this.mctx=mctx;
        this.arraylist = new ArrayList<HomeListing>();
        this.arraylist.addAll(recommendedList_book);
        this.textsize=textSize;
    }
    public interface RecommendedItemClick {
        void onItemClick(int position ,String book_id);
    }

    public void setItemClickListener(RecommendedItemClick clickListener) {
        recommendedItemClick = clickListener;
    }

    @NonNull
    @Override
    public Home_recommended_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popularlist_home_item, parent, false);
// set the view's size, margins, paddings and layout parameters

        Home_recommended_Adapter.MyViewHolder vh = new Home_recommended_Adapter.MyViewHolder(v);
        // pass the view to View Holder


        vh.bookname.setTextSize(textsize);
        vh.authorName.setTextSize(textsize);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Home_recommended_Adapter.MyViewHolder holder, final int position) {

        holder.book_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recommendedItemClick != null) {
                    recommendedItemClick.onItemClick(position,recommendedList_book.get(position).getId());
                }
            }
        });
        holder.bookname.setText(recommendedList_book.get(position).getBook_title());
        holder.authorName.setText(recommendedList_book.get(position).getAuthor_name());
        Log.e("image",recommendedList_book.get(position).getThubm_image());
       GlideUtils.loadImage(mctx,"http://"+recommendedList_book.get(position).getThubm_image(),holder.bookimage,R.drawable.noimage,R.drawable.noimage);
    }

    @Override
    public int getItemCount() {
        return recommendedList_book.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout book_container;
        ImageView bookimage;
        TextView bookname, authorName, bookDesc;
        RatingBar ratingBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_container = itemView.findViewById(R.id.container);
            bookimage = itemView.findViewById(R.id.item_image);
            bookname = itemView.findViewById(R.id.bookname);
            authorName = itemView.findViewById(R.id.auhorname);
            bookDesc = itemView.findViewById(R.id.shortDesc);
            ratingBar = itemView.findViewById(R.id.myRatingBar);

        }
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        recommendedList_book.clear();
        if (charText.length() == 0) {
            recommendedList_book.addAll(arraylist);
        }
        else
        {
            for (HomeListing wp : arraylist) {
                if (wp.getBook_title().toLowerCase(Locale.getDefault()).contains(charText)) {
                    recommendedList_book.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
