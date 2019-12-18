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

import com.ue.uebook.R;
import com.ue.uebook.UploadBook.Pojo.BookCategoryPojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookCategoryAdapter extends RecyclerView.Adapter<BookCategoryAdapter.MyViewHolder> {
    private List<BookCategoryPojo> response;
    private List<BookCategoryPojo> arraylist=null;

    private CategoryItemClick categoryItemClick;
    private AppCompatActivity mtx;
    String[] gridColor = {
            "#3c6169",
            "#30515d",
            "#284b5e",
            "#224156",
            "#86a6a1",
    };
    public BookCategoryAdapter(AppCompatActivity mtx ,List<BookCategoryPojo> response) {
        this.response=response;
        this.arraylist = new ArrayList<BookCategoryPojo>();
        this.arraylist.addAll(response);
        this.mtx=mtx;
    }

    public interface CategoryItemClick {
        void onItemClick(String CategoryId,String CategoryName);
    }

    public void setItemClickListener(CategoryItemClick clickListener) {
        categoryItemClick = clickListener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categoryitemview, parent, false);
        BookCategoryAdapter.MyViewHolder holder = new BookCategoryAdapter.MyViewHolder(view);
        return holder;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
         holder.categoryName.setText(response.get(position).getCategory_name());
         holder.categoryContqainer.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (categoryItemClick!=null){
                     categoryItemClick.onItemClick(String.valueOf(position+1) ,response.get(position).getCategory_name());
                 }
             }
         });
         if (response.get(position).getThum_image()!=null)
         {
//             GlideUtils.loadImage(mtx, "http://dnddemo.com/ebooks/uploads/category/" + response.get(position).getThum_image(), holder.categoryimage, R.drawable.bookbglist, R.drawable.bookbglist);
         }
//         holder.categoryimage.setImageResource(Color.parseColor(gridColor[position]));
    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;
        private LinearLayout categoryContqainer;
        private ImageView categoryimage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName=itemView.findViewById(R.id.categoryName);
            categoryContqainer=itemView.findViewById(R.id.categoryContqainer);
            categoryimage=itemView.findViewById(R.id.categoryimage);

        }
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        response.clear();
        if (charText.length() == 0) {
            response.addAll(arraylist);

        }
        else
        {
            for (BookCategoryPojo wp : arraylist) {
                if (wp.getCategory_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    response.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
