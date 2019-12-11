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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class New_Book_Home_Adapter   extends RecyclerView.Adapter<New_Book_Home_Adapter.MyViewHolder> {

    private NewBookItemClick newBookItemClick;
    private AppCompatActivity cmtx;
    private List<HomeListing>newBookList;
    private List<HomeListing> arraylist=null;
    private int textsize;
    public New_Book_Home_Adapter(AppCompatActivity activity, List<HomeListing> newBookList, int textSize) {
        this.cmtx=activity;
        this.newBookList = newBookList;
        this.arraylist = new ArrayList<HomeListing>();
        this.arraylist.addAll(newBookList);
          this.textsize=textSize;

    }
    public interface NewBookItemClick {
        void onItemClick_NewBook(int position ,String book_id);
    }
    public void setItemClickListener(NewBookItemClick clickListener) {
        newBookItemClick = clickListener;
    }
    @NonNull
    @Override
    public New_Book_Home_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popularlist_home_item, parent, false);
// set the view's size, margins, paddings and layout parameters
        New_Book_Home_Adapter.MyViewHolder vh = new New_Book_Home_Adapter.MyViewHolder(v); // pass the view to View Holder
        vh.bookname.setTextSize(textsize);
        vh.authorName.setTextSize(textsize);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull New_Book_Home_Adapter.MyViewHolder holder, final int position) {
        holder.book_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newBookItemClick != null) {
                    newBookItemClick.onItemClick_NewBook(position,newBookList.get(position).getId());
                }
            }
        });
        holder.bookname.setText(newBookList.get(position).getBook_title());
        holder.authorName.setText(newBookList.get(position).getAuthor_name());
        GlideUtils.loadImage(cmtx,"http://"+newBookList.get(position).getThubm_image(),holder.bookimage,R.drawable.noimage,R.drawable.noimage);
    }
    @Override
    public int getItemCount() {
        return newBookList.size();
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
        newBookList.clear();
        if (charText.length() == 0) {
            newBookList.addAll(arraylist);
        }
        else
        {
            for (HomeListing wp : arraylist) {
                if (wp.getBook_title().toLowerCase(Locale.getDefault()).contains(charText)) {
                    newBookList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
