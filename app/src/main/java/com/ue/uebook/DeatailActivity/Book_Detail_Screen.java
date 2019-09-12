package com.ue.uebook.DeatailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.DeatailActivity.Pojo.BookDetails;
import com.ue.uebook.DeatailActivity.Pojo.BookmarkResponse;
import com.ue.uebook.DeatailActivity.Pojo.DetailsResponse;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.Home_recommended_Adapter;
import com.ue.uebook.HomeActivity.HomeFragment.Home_Fragment;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListingResponse;
import com.ue.uebook.MySpannable;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;
import com.ue.uebook.ShareUtils;
import com.ue.uebook.WebviewScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.text.Html.fromHtml;

public class Book_Detail_Screen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton back_btn_Deatils, facebook_btn, google_btn, twitter_btn, whatsappshare_btn;
    private RecyclerView review_List;
    private Review_List_Adapter review_list_adapter;
    private ImageButton bookmark_btn;
    private Boolean isBookmark_book = false;
    private Button readFull_Book_btn;
    private List<BookDetails>bookDetail;
    private ProgressDialog dialog;
    private ImageView book_cover;
    private TextView bookTitle,bookDesc,bookAuthor;
    private Intent intent;
    private String book_id,videourl,docurl,audiourl;
    private int position;
    String docbaseUrl="http://docs.google.com/gview?embedded=true&url=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__detail__screen);
        bookDetail= new ArrayList<>();
        dialog = new ProgressDialog(this);
        intent = getIntent();
         book_id = intent.getStringExtra("book_id");
        position =intent.getIntExtra("position",0);
        back_btn_Deatils = findViewById(R.id.back_btn_Deatils);
        readFull_Book_btn = findViewById(R.id.readFull_Book_btn);
        readFull_Book_btn.setOnClickListener(this);
        bookmark_btn = findViewById(R.id.bookmark_btn);
        facebook_btn = findViewById(R.id.fbshare_btn);
        google_btn = findViewById(R.id.googleshare_btn);
        twitter_btn = findViewById(R.id.twittershare_btn);
        whatsappshare_btn = findViewById(R.id.whatsappshare_btn);
        bookTitle = findViewById(R.id.book_name_detail);
        bookDesc=findViewById(R.id.book_desc);
        bookAuthor=findViewById(R.id.bookauthor);
        book_cover=findViewById(R.id.bookcoverImage);
        facebook_btn.setOnClickListener(this);
        google_btn.setOnClickListener(this);
        twitter_btn.setOnClickListener(this);
        whatsappshare_btn.setOnClickListener(this);
        bookmark_btn.setOnClickListener(this);
        back_btn_Deatils.setOnClickListener(this);
        review_List = findViewById(R.id.review_List);
        LinearLayoutManager linearLayoutManagerList = new LinearLayoutManager(this);
        linearLayoutManagerList.setOrientation(LinearLayoutManager.VERTICAL);
        review_List.setLayoutManager(linearLayoutManagerList);
        review_list_adapter = new Review_List_Adapter();
        review_List.setAdapter(review_list_adapter);
        review_List.setNestedScrollingEnabled(false);
        getBookDetail(book_id);


    }

    @Override
    public void onClick(View view) {
        if (view == back_btn_Deatils) {
            finish();
        } else if (view == bookmark_btn) {

            if (!isBookmark_book) {

                bookmark_btn.setBackgroundResource(R.drawable.bookmark_active);
                isBookmark_book = true;
                addBookToBookmark(book_id,"1");
            } else {
                bookmark_btn.setBackgroundResource(R.drawable.bookmark_unactive);
                isBookmark_book = false;
                addBookToBookmark(book_id,"0");

            }
        } else if (view == facebook_btn) {

          ShareUtils.shareFacebook(this,"text","");

        } else if (view == google_btn) {
            ShareUtils.shareByGmail(this,"subject","Body");

        } else if (view == twitter_btn) {
            ShareUtils.shareTwitter(this,"text","","","");

        } else if (view == whatsappshare_btn) {
            ShareUtils.shareWhatsapp(this,"text","");


        }
        else if (view==readFull_Book_btn){
           showFilterPopup(view);
        }

    }

    private  void  gotoWebview(String url){
        Intent intent = new Intent(this, WebviewScreen.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }

    private void getBookDetail(String book_id) {
        ApiRequest request = new ApiRequest();
        dialog.setTitle("Loading");
        dialog.show();
        if (bookDetail.size()>0)
            bookDetail.clear();

        request.requestforgetBookDetail(book_id, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                dialog.dismiss();

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                 dialog.dismiss();
                String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final DetailsResponse form = gson.fromJson(myResponse, DetailsResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bookTitle.setText(form.getData().getBook_title());
                        bookDesc.setText(form.getData().getBook_description());
                        bookAuthor.setText(form.getData().getAuthor_name());
                        videourl=form.getData().getVideo_url();
                        docurl=form.getData().getPdf_url();
                        audiourl=form.getData().getAudio_url();
                        if (form.getData().getBook_description().length()>=50){
                            makeTextViewResizable(bookDesc, 5, "See More", true);
                        }

                        GlideUtils.loadImage(Book_Detail_Screen.this,"http://"+form.getData().getThubm_image(),book_cover,R.drawable.noimage,R.drawable.noimage);

                    }
                });

            }
        });
    }
    private void addBookToBookmark(String book_id , String bookStatus) {
        ApiRequest request = new ApiRequest();
        dialog.setTitle("Please Wait");
        dialog.show();
        request.requestforaddBookmark(book_id, bookStatus,new SessionManager(getApplicationContext()).getUserID(),new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                dialog.dismiss();

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
              dialog.dismiss();
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final BookmarkResponse form = gson.fromJson(myResponse, BookmarkResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Book_Detail_Screen.this,form.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }

    private void showFilterPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        // Inflate the menu from xml
        popup.inflate(R.menu.popup_filter);
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.video:
                        if (videourl!=null)
                        {
                            gotoWebview("http://"+videourl);
                        }
                        else {

                            Toast.makeText(Book_Detail_Screen.this, "No Video for this Book", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case R.id.audio:
                        if (audiourl!=null)
                        {
                            gotoWebview("http://"+audiourl);
                        }
                        else {

                            Toast.makeText(Book_Detail_Screen.this, "No Audio for this Book", Toast.LENGTH_SHORT).show();
                        }                         return true;
                    case R.id.doc:


                        if (docurl!=null)
                        {
                            gotoWebview(docbaseUrl+docurl);
                        }
                        else {

                            Toast.makeText(Book_Detail_Screen.this, "No Document File", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    default:
                        return false;
                }
            }
        });
        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show();
    }
    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false){
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, ".. See More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

}
