package com.ue.uebook.BookCategory;

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

public class CategoryBookAdater extends RecyclerView.Adapter<CategoryBookAdater.Viewholder> {

    List<HomeListing> bookList;
    private AppCompatActivity mctx;
    // for loading main list
    private List<HomeListing> arraylist=null;
    private OnbookClick_ onbookClick_;
    private int textsize;// for loading  filter data

    public CategoryBookAdater(AppCompatActivity mctx, List<HomeListing> popularBook_list, int textSize) {
        this.bookList=popularBook_list;
        this.mctx=mctx;
        this.textsize=textSize;
    }
    interface OnbookClick_{
        void onItemClick(int position ,String book_id);

    }
    public void setItemClickListener(OnbookClick_ clickListener) {
        onbookClick_ = clickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.popularlist_home_item, parent, false);
        CategoryBookAdater.Viewholder holder = new CategoryBookAdater.Viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        holder.book_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (onbookClick_!=null){
                onbookClick_.onItemClick(position,bookList.get(position).getId());
            }
            }
        });
        holder.authorName.setText(bookList.get(position).getAuthor_name());
        holder.bookname.setText(bookList.get(position).getBook_title());
//        if (bookList.get(position).getBook_description().length()>11){
//            holder.bookDesc.setText(getFirst10Words(bookList.get(position).getBook_description())+"...");
//        }
//        else {
//            holder.bookDesc.setText(bookList.get(position).getBook_description());
//        }
        GlideUtils.loadImage(mctx,"http://"+bookList.get(position).getThubm_image(),holder.bookimage,R.drawable.noimage,R.drawable.noimage);
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

    public class Viewholder extends RecyclerView.ViewHolder {
        LinearLayout book_container;
        ImageView bookimage;
        TextView bookname,authorName,bookDesc;
        RatingBar ratingBar;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            book_container=itemView.findViewById(R.id.container);
            bookimage=itemView.findViewById(R.id.item_image);
            bookname=itemView.findViewById(R.id.bookname);
            authorName=itemView.findViewById(R.id.auhorname);
            bookDesc=itemView.findViewById(R.id.shortDesc);
            ratingBar =itemView.findViewById(R.id.myRatingBar);
        }
    }
}
