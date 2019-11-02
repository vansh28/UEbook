package com.ue.uebook.DeatailActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.DeatailActivity.Pojo.ReviewPojo;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;

import java.util.List;

public class Review_List_Adapter  extends RecyclerView.Adapter<Review_List_Adapter.MyViewHolder>{
    private List<ReviewPojo>data;
    private AppCompatActivity mtc;
    public Review_List_Adapter(AppCompatActivity mtx ,List<ReviewPojo> review) {
        this.data=review;
        this.mtc=mtx;
    }
    @NonNull
    @Override
    public Review_List_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_top_review_list_item, parent, false);
        Review_List_Adapter.MyViewHolder vh = new Review_List_Adapter.MyViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull Review_List_Adapter.MyViewHolder holder, int position) {
        GlideUtils.loadImage(mtc,"http://"+data.get(position).getUrl(),holder.profileTv,R.drawable.user_default,R.drawable.user_default);
        holder.usernameTv.setText(data.get(position).getUser_name());
        holder.dateandtimeTv.setText(data.get(position).getCreated_at());
        holder.commentTv.setText(data.get(position).getComment());
        if (data.get(position).getRating()!=null){
            holder.ratingBar.setRating( Float.valueOf(data.get(position).getRating()));
        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView profileTv;
        private TextView usernameTv,dateandtimeTv,commentTv;
        private RatingBar ratingBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileTv=itemView.findViewById(R.id.profile_image_review);
            usernameTv=itemView.findViewById(R.id.reviewUsername);
            dateandtimeTv=itemView.findViewById(R.id.review_comment_time);
            commentTv=itemView.findViewById(R.id.review_comment);
            ratingBar=itemView.findViewById(R.id.review_RatingBar);

        }
    }
}
