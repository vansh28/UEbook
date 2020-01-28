package com.ue.uebook.DeatailActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.AuthorProfileActivity.AuthorProfileScreen;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.DeatailActivity.Pojo.Assignment;
import com.ue.uebook.DeatailActivity.Pojo.BookDetails;
import com.ue.uebook.DeatailActivity.Pojo.BookmarkResponse;
import com.ue.uebook.DeatailActivity.Pojo.DetailsResponse;
import com.ue.uebook.DeatailActivity.Pojo.user_answer;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.MySpannable;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;
import com.ue.uebook.ShareUtils;
import com.ue.uebook.WebviewScreen;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.text.Html.fromHtml;

public class Book_Detail_Screen extends BaseActivity implements View.OnClickListener {
    private ImageButton back_btn_Deatils, facebook_btn, google_btn, twitter_btn, whatsappshare_btn;
    private RecyclerView review_List;
    private Review_List_Adapter review_list_adapter;
    private ImageButton bookmark_btn;
    private Boolean isBookmark_book = false;
    private Button comment_Submit;
    private List<BookDetails> bookDetail;
    private ProgressDialog dialog;
    private ImageView book_coverTv, profile_user;
    private TextView reviewCountView, bookTitle, bookDesc, bookAuthor, averageRating, topreviewView, book_uploadBy, book_asignment, readFull_Book_btn;
    private Intent intent;
    private String book_Id, videourl, docurl, audiourl;
    private int position;
    private String bookdesc;
    private RatingBar commnetRating;
    private EditText user_comment;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String ulpoadByUserId;
    private List<Assignment> assignmentList;
    private List<user_answer> user_answers;
    private RatingBar myRatingBar;
    private int textSize;
    private Handler handler;
    String docbaseUrl = "http://docs.google.com/gview?embedded=true&url=";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__detail__screen);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        myRatingBar = findViewById(R.id.myRatingBar);
        reviewCountView = findViewById(R.id.reviewCountView);
        profile_user = findViewById(R.id.profile_user);
        book_uploadBy = findViewById(R.id.book_uploadBy);
        averageRating = findViewById(R.id.averageRating);
        topreviewView = findViewById(R.id.topreviewView);
        comment_Submit = findViewById(R.id.submit_comment);
        commnetRating = findViewById(R.id.myRatingBar_detail);
        user_comment = findViewById(R.id.comment_edit_text);
        book_asignment = findViewById(R.id.book_asignment);
        book_asignment.setOnClickListener(this);
        bookDetail = new ArrayList<>();
        assignmentList = new ArrayList<>();
        user_answers = new ArrayList<>();
        dialog = new ProgressDialog(this);
        intent = getIntent();
        book_Id = intent.getStringExtra("book_id");
        position = intent.getIntExtra("position", 0);
        back_btn_Deatils = findViewById(R.id.back_btn_Deatils);
        readFull_Book_btn = findViewById(R.id.readFull_Book_btn);
        readFull_Book_btn.setOnClickListener(this);
        bookmark_btn = findViewById(R.id.bookmark_btn);
        facebook_btn = findViewById(R.id.fbshare_btn);
        google_btn = findViewById(R.id.googleshare_btn);
        twitter_btn = findViewById(R.id.twittershare_btn);
        whatsappshare_btn = findViewById(R.id.whatsappshare_btn);
        bookTitle = findViewById(R.id.book_name_detail);
        bookDesc = findViewById(R.id.book_desc);
        bookAuthor = findViewById(R.id.bookauthor);
        book_uploadBy.setOnClickListener(this);
        book_coverTv = findViewById(R.id.bookcoverImage);
        comment_Submit.setOnClickListener(this);
        facebook_btn.setOnClickListener(this);
        google_btn.setOnClickListener(this);
        twitter_btn.setOnClickListener(this);
        whatsappshare_btn.setOnClickListener(this);
        bookmark_btn.setOnClickListener(this);
        back_btn_Deatils.setOnClickListener(this);
        review_List = findViewById(R.id.review_List);
        SpannableString content = new SpannableString(getResources().getString(R.string.click_for_book_assignment));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        book_asignment.setText(content);
        LinearLayoutManager linearLayoutManagerList = new LinearLayoutManager(this);
        linearLayoutManagerList.setOrientation(LinearLayoutManager.VERTICAL);
        review_List.setLayoutManager(linearLayoutManagerList);
        review_List.setNestedScrollingEnabled(false);
        getBookDetail(book_Id);
        pullTorefreshswipe();
        fontsize();
    }
    private void fontsize() {
        switch (new SessionManager(getApplicationContext()).getfontSize()) {
            case "smallest":
                textSize = 12;
                break;
            case "small":
                textSize = 14;
                break;
            case "normal":
                textSize = 16;
                break;
            case "large":
                textSize = 18;
                break;
            case "largest":
                textSize = 24;
                break;
        }
        bookDesc.setTextSize(textSize);
        bookTitle.setTextSize(textSize);
        bookAuthor.setTextSize(textSize);
        averageRating.setTextSize(textSize);
        topreviewView.setTextSize(textSize);
        book_uploadBy.setTextSize(textSize);
        book_asignment.setTextSize(textSize);
        readFull_Book_btn.setTextSize(textSize);
    }

    private void pullTorefreshswipe() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onRefresh() {
                getBookDetail(book_Id);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        if (view == back_btn_Deatils) {
            finish();
        } else if (view == bookmark_btn) {

            if (!isBookmark_book)
            {
                bookmark_btn.setBackgroundResource(R.drawable.bookmark_active);
                isBookmark_book = true;
                addBookToBookmark(book_Id, "1");
            }

            else {
                bookmark_btn.setBackgroundResource(R.drawable.bookmarkwhite);
                isBookmark_book = false;
                addBookToBookmark(book_Id, "0");
            }
        } else if (view == facebook_btn) {

            ShareUtils.shareFacebook(this, bookdesc, "");

        //    shareFacebook(bookdesc);

        } else if (view == google_btn) {

            ShareUtils.shareByGmail(this, "UeBook", bookdesc);

        } else if (view == twitter_btn) {
            ShareUtils.shareTwitter(this, bookdesc, "", "", "");

        } else if (view == whatsappshare_btn) {
            ShareUtils.shareWhatsapp(this, bookdesc, "");
        } else if (view == readFull_Book_btn) {
            showFilterPopup(view);
        } else if (view == comment_Submit) {
            if (isvalidate()) {
                addCommentToBook(user_comment.getText().toString(), String.valueOf(commnetRating.getRating()));
            }
        } else if (view == book_uploadBy) {

            if (ulpoadByUserId.equalsIgnoreCase(new SessionManager(getApplicationContext()).getUserID())) {
                Intent intent = new Intent(Book_Detail_Screen.this, AuthorProfileScreen.class);
                intent.putExtra("id", 1);
                intent.putExtra("userID", new SessionManager(getApplicationContext()).getUserID());
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, AuthorProfileScreen.class);
                intent.putExtra("userID", ulpoadByUserId);
                startActivity(intent);
            }
        }

        else if (view == book_asignment) {
            Intent intent = new Intent(this, Book_Assignment.class);
            intent.putExtra("QuestionListExtra", (Serializable) assignmentList);
            intent.putExtra("answer", (Serializable) user_answers);
            intent.putExtra("book_id", book_Id);
            startActivity(intent);

        }
    }

    private Boolean isvalidate() {
        String user_Comment = user_comment.getText().toString();
        Float userRating = commnetRating.getRating();
        if (!user_Comment.isEmpty()) {
            if (userRating != 0.0) {
                return true;
            } else {
                Toast.makeText(this, "Please Rate the Book", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {

            Toast.makeText(this, "Please Enter your Comment", Toast.LENGTH_LONG).show();
            return false;

        }
    }

    private void gotoWebview(String url) {
        Intent intent = new Intent(this, WebviewScreen.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getBookDetail(final String book_id) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        if (bookDetail.size() > 0)
            bookDetail.clear();

        request.requestforgetBookDetail(book_id, new SessionManager(getApplicationContext()).getUserID(), new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                hideLoadingIndicator();

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final DetailsResponse form = gson.fromJson(myResponse, DetailsResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        assignmentList = form.getAssignment();
                        user_answers = form.getUser_answer();
                        bookTitle.setText(form.getData().getBook_title());
                        bookAuthor.setText(form.getData().getAuthor_name());
                        ulpoadByUserId = form.getData().getUser_id();
                        if (form.getData().getUser_name() != null) {
//                            SpannableString content = new SpannableString(form.getData().getUser_name());
//                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            book_uploadBy.setText((form.getData().getUser_name()));
//                            GlideUtils.loadImage(Book_Detail_Screen.this,"http://"+form.getData().get,book_coverTv,R.drawable.noimage,R.drawable.noimage);
                        } else {
                            profile_user.setVisibility(View.GONE);
                        }
                        bookDesc.setText(form.getData().getBook_description());
                        videourl = form.getData().getVideo_url();
                        docurl = form.getData().getPdf_url();
                        audiourl = form.getData().getAudio_url();
                        bookdesc = form.getData().getBook_description();
                        averageRating.setText(form.getAveraVal());
                        myRatingBar.setRating(Float.parseFloat(form.getAveraVal()));
                        reviewCountView.setText("( "+form.getData().getMostView()+" )"+" Reviews");
                        if (form.getBookMark() != null) {
                            if (form.getBookMark().getBookmarkStatus().equals("1")) {
                                bookmark_btn.setBackgroundResource(R.drawable.bookmark_active);
                                isBookmark_book = true;
                            } else {
                                bookmark_btn.setBackgroundResource(R.drawable.bookmarkwhite);
                                isBookmark_book = false;
                            }
                        }
                        else {
                            bookmark_btn.setBackgroundResource(R.drawable.bookmarkwhite);
                            isBookmark_book = false;
                        }
                        if (form.getData().getBook_description().length() >= 50) {
                            makeTextViewResizable(bookDesc, 5, "See More", true);
                        }
                        GlideUtils.loadImage(Book_Detail_Screen.this, "http://" + form.getData().getThubm_image(), book_coverTv, R.drawable.noimage, R.drawable.noimage);

                        GlideUtils.loadImage(Book_Detail_Screen.this, "http://dnddemo.com/ebooks/api/v1/upload/" + form.getData().getProfile_pic(), profile_user, R.drawable.user_default, R.drawable.user_default);


                        if (form.getReview() != null) {
                            review_list_adapter = new Review_List_Adapter(Book_Detail_Screen.this, form.getReview());
                            review_List.setAdapter(review_list_adapter);
                            review_list_adapter.notifyDataSetChanged();
                            topreviewView.setVisibility(View.VISIBLE);
                        } else {
                            topreviewView.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addBookToBookmark(String book_id, String bookStatus) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforaddBookmark(book_id, bookStatus, new SessionManager(getApplicationContext()).getUserID(), new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                hideLoadingIndicator();
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final BookmarkResponse form = gson.fromJson(myResponse, BookmarkResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Book_Detail_Screen.this, form.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void showFilterPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.popup_filter);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.videotv:

                        if (videourl != null) {
                            gotoWebview("http://" + videourl);
                        } else {
                          //  Toast.makeText(Book_Detail_Screen.this, "No Video for this Book", Toast.LENGTH_SHORT).show();

                            dialog("ok","No Video for this Book");

                        }
                        return true;
                    case R.id.audiotv:
                        if (audiourl != null) {
                            gotoWebview("http://" + audiourl);
                        } else {

                           // Toast.makeText(Book_Detail_Screen.this, "No Audio for this Book", Toast.LENGTH_SHORT).show();
                            dialog("ok","No Audio for this Book");


                        }
                        return true;
                    case R.id.doctv:


                        if (docurl != null) {
                            gotoWebview(docbaseUrl + docurl);
                        } else {

                          //  Toast.makeText(Book_Detail_Screen.this, "No Document File", Toast.LENGTH_SHORT).show();
                            dialog("ok","No Document File for this Book");
                        }

                        return true;
                    default:
                        return false;
                }
            }
        });
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

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv, final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);
        if (str.contains(spanableText)) {
            ssb.setSpan(new MySpannable(false) {
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addCommentToBook(String comment, String rating) {
        showLoadingIndicator();
        ApiRequest request = new ApiRequest();
        request.requestforAddComment(new SessionManager(getApplicationContext()).getUserID(), book_Id, comment, rating, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                hideLoadingIndicator();

            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String myResponse = response.body().string();
                hideLoadingIndicator();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        user_comment.setText("");
                        commnetRating.setRating(0);
                        getBookDetail(book_Id);
                    }
                });
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onRestart() {
        super.onRestart();
        getBookDetail(book_Id);
    }

    public void shareFacebook(String text) {
        String fullUrl = "https://m.facebook.com/sharer.php?u=..";
        try {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setClassName("com.facebook.katana",
                    "com.facebook.katana.ShareLinkActivity");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(sharingIntent);
        } catch (Exception e) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(fullUrl));
            startActivity(i);
        }
    }
    public void dialog(final String value1,final String mesg ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mesg)
                .setCancelable(false)
                .setPositiveButton(value1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
