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
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.BookmarkBookList;
import com.ue.uebook.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bookmark_List_Adapter  extends RecyclerView.Adapter<Bookmark_List_Adapter.MyViewHolder>{



    private BookmarkBookItemClick bookmarkBookItemClick;
    private AppCompatActivity context;
    private List<BookmarkBookList>bookList;


    public Bookmark_List_Adapter(AppCompatActivity context, List<BookmarkBookList> data) {
        this.bookList=data;
        this.context=context;
    }

    public interface BookmarkBookItemClick {
        void onItemClick(int position ,String bookid);
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
                    Log.d("ddd",bookList.get(position).getId());
                    bookmarkBookItemClick.onItemClick(position,bookList.get(position).getId());
                }
            }
        });
        holder.bookname.setText(bookList.get(position).getBook_title());
        holder.authorName.setText(bookList.get(position).getAuthor_name());
        GlideUtils.loadImage(context,"http://"+bookList.get(position).getThubm_image(),holder.bookimage,R.drawable.noimage,R.drawable.noimage);

        if (bookList.get(position).getBook_description().length()>11){
            holder.bookDesc.setText(getFirst10Words(bookList.get(position).getBook_description())+"...");
        }
        else {
            holder.bookDesc.setText(bookList.get(position).getBook_description());
        }
        if (bookList.get(position).getRating()!=null){
            holder.ratingBar.setRating( Float.valueOf(bookList.get(position).getRating()));

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
        return bookList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout book_container;
        ImageView bookimage;
        TextView bookname,authorName,bookDesc;
        RatingBar ratingBar ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_container=itemView.findViewById(R.id.container);
            bookimage=itemView.findViewById(R.id.item_image);
            bookname=itemView.findViewById(R.id.bookname);
            authorName=itemView.findViewById(R.id.auhorname);
            bookDesc=itemView.findViewById(R.id.shortDesc);
            ratingBar=itemView.findViewById(R.id.myRatingBar);
        }
    }
}
