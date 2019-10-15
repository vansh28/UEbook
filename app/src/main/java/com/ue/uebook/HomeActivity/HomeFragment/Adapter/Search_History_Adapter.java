package com.ue.uebook.HomeActivity.HomeFragment.Adapter;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Search_History_Adapter   extends RecyclerView.Adapter<Search_History_Adapter.MyViewHolder> {


    private SearchHistoryItemClick searchHistoryItemClick;
    private List<HomeListing>dataList;
    private List<HomeListing> arraylist=null;
    private AppCompatActivity mctx;
    private int textsize;
    public Search_History_Adapter(AppCompatActivity mctx, List<HomeListing> data, int textSize) {
        this.dataList=data;
        this.arraylist = new ArrayList<HomeListing>();
        this.arraylist.addAll(data);
        this.textsize=textSize;
        this.mctx=mctx;
    }

    public interface SearchHistoryItemClick {
        void onItemClick(int position,String book_id);
    }

    public void setItemClickListener(SearchHistoryItemClick clickListener) {
        searchHistoryItemClick = clickListener;
    }


    @NonNull
    @Override
    public Search_History_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popularlist_home_item, parent, false);
        Search_History_Adapter.MyViewHolder vh = new Search_History_Adapter.MyViewHolder(v); // pass the view to View Holder
        vh.authorName.setTextSize(textsize);
        vh.authorName.setTextSize(textsize);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final Search_History_Adapter.MyViewHolder holder, final int position) {
        holder.book_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchHistoryItemClick != null) {
                    searchHistoryItemClick.onItemClick(position,dataList.get(position).getId());
                }
            }
        });
        holder.bookname.setText(dataList.get(position).getBook_title());
        holder.authorName.setText(dataList.get(position).getAuthor_name());
        GlideUtils.loadImage(mctx,"http://"+dataList.get(position).getThubm_image(),holder.bookimage,R.drawable.noimage,R.drawable.noimage);


        if (dataList.get(position).getBook_description().length()>11){
            holder.bookDesc.setText(getFirst10Words(dataList.get(position).getBook_description())+"...");
        }
        else {
            holder.bookDesc.setText(dataList.get(position).getBook_description());
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
        return dataList.size();
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
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        dataList.clear();
        if (charText.length() == 0) {

        }
        else
        {
            for (HomeListing wp : arraylist) {
                if (wp.getBook_title().toLowerCase(Locale.getDefault()).contains(charText)||wp.getAuthor_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
