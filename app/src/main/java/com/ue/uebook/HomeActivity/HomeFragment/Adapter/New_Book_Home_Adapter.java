package com.ue.uebook.HomeActivity.HomeFragment.Adapter;

import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class New_Book_Home_Adapter   extends RecyclerView.Adapter<New_Book_Home_Adapter.MyViewHolder> {

    private NewBookItemClick newBookItemClick;
    private AppCompatActivity cmtx;
    private List<HomeListing>newBookList;
    private List<HomeListing> arraylist=null;
    public New_Book_Home_Adapter(AppCompatActivity activity, List<HomeListing> newBookList) {
        this.cmtx=activity;
        this.newBookList = newBookList;
        this.arraylist = new ArrayList<HomeListing>();
        this.arraylist.addAll(newBookList);
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
                    newBookItemClick.onItemClick_NewBook(position,newBookList.get(position).getId());
                }
            }
        });
        holder.book_name.setText(newBookList.get(position).getBook_title());
        holder.author_name.setText(newBookList.get(position).getAuthor_name());
        GlideUtils.loadImage(cmtx,"http://"+newBookList.get(position).getThubm_image(),holder.book_cover,R.drawable.noimage,R.drawable.noimage);
    }
    @Override
    public int getItemCount() {
        return newBookList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout main_container;
        private ImageView book_cover;
        private TextView book_name,author_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            main_container=itemView.findViewById(R.id.container_book);
            book_name=itemView.findViewById(R.id.book_name);
            author_name =itemView.findViewById(R.id.author_name);
            book_cover =itemView.findViewById(R.id.item_image);

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
