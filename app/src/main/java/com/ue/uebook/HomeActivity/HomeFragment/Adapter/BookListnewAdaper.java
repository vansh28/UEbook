package com.ue.uebook.HomeActivity.HomeFragment.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.R;

public class BookListnewAdaper extends RecyclerView.Adapter<BookListnewAdaper.MyViewHolder> {
    private int imageList []={R.drawable.vvv,R.drawable.imgone,R.drawable.imgtwo,R.drawable.imgthree,R.drawable.imgfour,R.drawable.imgfive};
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

        holder.item_image.setImageResource(imageList[position]);
    }
    @Override
    public int getItemCount() {
        return 6;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;
        private LinearLayout categoryContqainer;
        private ImageView item_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image=itemView.findViewById(R.id.item_image);
        }
    }


}

