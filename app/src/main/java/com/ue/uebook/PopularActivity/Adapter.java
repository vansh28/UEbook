package com.ue.uebook.PopularActivity;

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

public class Adapter  extends RecyclerView.Adapter<Adapter.MyViewHolder>{
    private PopularBook_ItemClick popularBookItemClick;
    private List<HomeListing>data;
    private AppCompatActivity mctx;
    public Adapter(AppCompatActivity mctx,List<HomeListing> data) {
        this.data=data;
        this.mctx=mctx;
    }
    public interface PopularBook_ItemClick {
        void onItemClick_PopularBook(int position ,String book_id);
    }

    public void setItemClickListener(PopularBook_ItemClick clickListener) {
        popularBookItemClick = clickListener;
    }


    @NonNull
    @Override
    public Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popularlist_home_item, parent, false);
// set the view's size, margins, paddings and layout parameters
        Adapter.MyViewHolder vh = new Adapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, final int position) {
        holder.book_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popularBookItemClick != null) {
                    popularBookItemClick.onItemClick_PopularBook(position,data.get(position).getId());
                }
            }
        });
        holder.authorName.setText(data.get(position).getAuthor_name());
        holder.bookname.setText(data.get(position).getBook_title());
        GlideUtils.loadImage(mctx,"http://"+data.get(position).getThubm_image(),holder.bookimage,R.drawable.noimage,R.drawable.noimage);

    }

    @Override
    public int getItemCount() {
        return data.size();
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
    }
}

