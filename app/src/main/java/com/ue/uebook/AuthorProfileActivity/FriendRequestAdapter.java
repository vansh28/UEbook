package com.ue.uebook.AuthorProfileActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FriendRequestAdapter   extends RecyclerView.Adapter<FriendRequestAdapter.MyViewHolder>{

//    private Author_BookListAdapter.BookItemClick bookItemClick;
//    private List<AuthorBookList> Book_list;
//    private AppCompatActivity mctx;
//    public interface BookItemClick {
//        void onItemClick_PopularBook(int position ,String book_id);
//    }
//
//    public void setItemClickListener(Author_BookListAdapter.BookItemClick clickListener) {
//        bookItemClick = clickListener;
//    }
//    public Author_BookListAdapter(AppCompatActivity applicationContext, List<AuthorBookList> booklist) {
//        this.Book_list=booklist;
//        this.mctx=applicationContext;
//    }


    @NonNull
    @Override
    public FriendRequestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendrequestitem, parent, false);
// set the view's size, margins, paddings and layout parameters
        FriendRequestAdapter.MyViewHolder vh = new FriendRequestAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestAdapter.MyViewHolder holder, final int position) {

//        holder.book_container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (bookItemClick != null) {
//                    bookItemClick.onItemClick_PopularBook(position,Book_list.get(position).getId());
//                }
//            }
//        });
//        holder.authorName.setText(Book_list.get(position).getAuthor_name());
//        holder.bookname.setText(Book_list.get(position).getBook_title());
//        if (Book_list.get(position).getBook_description().length()>11){
//            holder.bookDesc.setText(getFirst10Words(Book_list.get(position).getBook_description())+"...");
//        }
//        else {
//            holder.bookDesc.setText(Book_list.get(position).getBook_description());
//        }            GlideUtils.loadImage(mctx,"http://"+Book_list.get(position).getThubm_image(),holder.bookimage,R.drawable.noimage,R.drawable.noimage);
//        if (Book_list.get(position).getRating()!=null){
//            holder.ratingBar.setRating( Float.valueOf(Book_list.get(position).getRating()));
//        }

    }
    public String getFirst10Words(String arg) {
        Pattern pattern = Pattern.compile("([\\S]+\\s*){1,10}");
        Matcher matcher = pattern.matcher(arg);
        matcher.find();
        return matcher.group();
    }
    @Override
    public int getItemCount() {
        return 5;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }}

