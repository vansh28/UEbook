package com.ue.uebook.AuthorProfileActivity;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.AuthorProfileActivity.pojo.AuthorBookList;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Author_BookListAdapter extends RecyclerView.Adapter<Author_BookListAdapter.MyViewHolder>{

   private BookItemClick bookItemClick;
    private List<AuthorBookList>Book_list;
    private AppCompatActivity mctx;
    private Integer id;
    private int textSize;
    public interface BookItemClick {
        void onItemClick_PopularBook(int position ,String book_id);
        void  OndeleteBook(String book_id);
    }

    public void setItemClickListener(BookItemClick clickListener) {
        bookItemClick = clickListener;
    }
    public Author_BookListAdapter(AppCompatActivity applicationContext, List<AuthorBookList> booklist, int id, int textSize) {
        this.Book_list=booklist;
        this.mctx=applicationContext;
        this.id=id;
        this.textSize=textSize;
    }


    @NonNull
    @Override
    public Author_BookListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popularlist_home_item, parent, false);
// set the view's size, margins, paddings and layout parameters
        Author_BookListAdapter.MyViewHolder vh = new Author_BookListAdapter.MyViewHolder(v); // pass the view to View Holder
        vh.authorName.setTextSize(textSize);
        vh.bookname.setTextSize(textSize);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final Author_BookListAdapter.MyViewHolder holder, final int position) {
          holder.button_menu.setVisibility(View.VISIBLE);
          if (id==1){
              holder.button_menu.setVisibility(View.VISIBLE);
          }
          else {
              holder.button_menu.setVisibility(View.GONE);

          }
        holder.book_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookItemClick != null) {
                    bookItemClick.onItemClick_PopularBook(position,Book_list.get(position).getId());
                }
            }
        });
        holder.authorName.setText(Book_list.get(position).getAuthor_name());
        holder.bookname.setText(Book_list.get(position).getBook_title());
        if (Book_list.get(position).getBook_description().length()>11){
            holder.bookDesc.setText(getFirst10Words(Book_list.get(position).getBook_description())+"...");
        }
        else {
            holder.bookDesc.setText(Book_list.get(position).getBook_description());
        }            GlideUtils.loadImage(mctx,"http://"+Book_list.get(position).getThubm_image(),holder.bookimage,R.drawable.noimage,R.drawable.noimage);
        if (Book_list.get(position).getRating()!=null){
            holder.ratingBar.setRating( Float.valueOf(Book_list.get(position).getRating()));
        }
      holder.button_menu.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              final PopupMenu popup = new PopupMenu(mctx, holder.button_menu);
              popup.getMenuInflater().inflate(R.menu.context_menu, popup.getMenu());
              popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                  public boolean onMenuItemClick(MenuItem item) {
                      int i = item.getItemId();
                      if (i == R.id.edit_menu) {
                          //do something
                          return true;
                      }
                      else if (i == R.id.delete_menu){
                          if (bookItemClick != null) {
                              bookItemClick.OndeleteBook(Book_list.get(position).getId());
                          }
                          return true;
                      }

                      else {
                          return onMenuItemClick(item);
                      }
                  }
              });

              popup.show();
          }
      });
    }
    public String getFirst10Words(String arg) {
        Pattern pattern = Pattern.compile("([\\S]+\\s*){1,10}");
        Matcher matcher = pattern.matcher(arg);
        matcher.find();
        return matcher.group();
    }
    @Override
    public int getItemCount() {
        return Book_list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout book_container;
        ImageView bookimage;
        TextView bookname,authorName,bookDesc;
        RatingBar ratingBar;
        ImageButton button_menu;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_container=itemView.findViewById(R.id.container);
            bookimage=itemView.findViewById(R.id.item_image);
            bookname=itemView.findViewById(R.id.bookname);
            authorName=itemView.findViewById(R.id.auhorname);
            bookDesc=itemView.findViewById(R.id.shortDesc);
            ratingBar =itemView.findViewById(R.id.myRatingBar);
            button_menu=itemView.findViewById(R.id.button_menu);
        }
    }}

