package com.ue.uebook.HomeActivity.HomeFragment.Adapter;

import android.annotation.SuppressLint;
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

public class BookListnewAdaper extends RecyclerView.Adapter<BookListnewAdaper.MyViewHolder> {
    private int imageList []={R.drawable.vvv,R.drawable.imgone,R.drawable.imgtwo,R.drawable.imgthree,R.drawable.imgfour,R.drawable.imgfive};
    List<HomeListing> popularBook_list;
    private AppCompatActivity mctx;
    private PopularBookItemClick popularBookItemClick;

    public BookListnewAdaper(AppCompatActivity activity, List<HomeListing> popularBook_list, int textSize) {
        this.popularBook_list=popularBook_list;
        this.mctx =activity;
    }
    public interface PopularBookItemClick {
        void onItemClick_PopularBook(int position ,String book_id);
    }
    public void setItemClickListener(PopularBookItemClick clickListener) {
        popularBookItemClick = clickListener;
    }
    @NonNull
    @Override
    public BookListnewAdaper.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.writerbookitem, parent, false);
        BookListnewAdaper.MyViewHolder holder = new BookListnewAdaper.MyViewHolder(view);
        return holder;
    }
    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull BookListnewAdaper.MyViewHolder holder, final int position) {

//        holder.item_image.setImageResource(imageList[position]);
         holder.bookName.setText(popularBook_list.get(position).getBook_title());
          holder.authorName.setText(popularBook_list.get(position).getAuthor_name());
        GlideUtils.loadImage(mctx, "http://" + popularBook_list.get(position).getThubm_image(), holder.item_image, R.drawable.noimage, R.drawable.noimage);

        holder.item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popularBookItemClick != null) {
                    popularBookItemClick.onItemClick_PopularBook(position, popularBook_list.get(position).getId());
                }
            }
        });




    }
    @Override
    public int getItemCount() {
        return popularBook_list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView bookName,authorName;
        private LinearLayout categoryContqainer;
        private ImageView item_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image=itemView.findViewById(R.id.item_image);
            bookName=itemView.findViewById(R.id.book_name);
            authorName=itemView.findViewById(R.id.author_name);
        }
    }


}

