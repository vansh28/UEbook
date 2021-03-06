package com.ue.uebook.DeatailActivity;

import android.app.Activity;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.ue.uebook.AuthorProfileActivity.AuthorProfileScreen;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.ChatSdk.OponentUserDetailsScren;
import com.ue.uebook.Config;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.DeatailActivity.Pojo.Assignment;
import com.ue.uebook.DeatailActivity.Pojo.BookDetails;
import com.ue.uebook.DeatailActivity.Pojo.BookmarkResponse;
import com.ue.uebook.DeatailActivity.Pojo.DetailsResponse;
import com.ue.uebook.DeatailActivity.Pojo.user_answer;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.MySpannable;
import com.ue.uebook.Payment.StripePayment;
import com.ue.uebook.PaymentPojo.CheckPaymentDone;
import com.ue.uebook.PaymentPojo.PaymentResponse;
import com.ue.uebook.PaymentSuccess;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;
import com.ue.uebook.ShareUtils;
import com.ue.uebook.WebviewScreen;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import static android.text.Html.fromHtml;

public class Book_Detail_Screen extends BaseActivity implements View.OnClickListener {
    private ImageButton back_btn_Deatils, facebook_btn, google_btn, twitter_btn, whatsappshare_btn;
    private RecyclerView review_List;
    private Review_List_Adapter review_list_adapter;
    private ImageButton bookmark_btn;
    private Boolean isBookmark_book = false;
    private Button comment_Submit;
    private BookDetails bookDetail;
    private ProgressDialog dialog;
    private ImageView book_coverTv, profile_user;
    private TextView  payment, priceBook, reviewCountView, bookTitle, bookDesc, bookAuthor, averageRating, topreviewView, book_uploadBy, book_asignment, readFull_Book_btn;
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
    private String bookname = "";
    private String bookcover = "";
    private String username = "";
    private String userimage = "";
    private Handler handler;
    private String price, ispaid;
    private String admin_commission="0";
    public static final int Payrequescode = 111;
    private String paymentMethod="";
    private String currencyType="";
    private static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.clientID);
    private String euro = "\u20ac";
    private String transaction_id;
    private String intents,state;
    String docbaseUrl = "http://docs.google.com/gview?embedded=true&url=";
    private   RadioGroup radioGroupPaymentMethod,radioGroupCurrency;
    private String convertPrice="0";
    private String story_text;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__detail__screen);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);
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
        priceBook = findViewById(R.id.priceBook);
        book_asignment.setOnClickListener(this);
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
        book_coverTv.setOnClickListener(this);
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
        profile_user.setOnClickListener(this);
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

            if (!isBookmark_book) {
                bookmark_btn.setBackgroundResource(R.drawable.bookmark_active);
                isBookmark_book = true;
                addBookToBookmark(book_Id, "1");
            } else {
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

            if (new SessionManager(getApplicationContext()).getUserID().equalsIgnoreCase(ulpoadByUserId)) {
                showFilterPopup(view);
            } else {
                if (ispaid.equalsIgnoreCase("Yes")) {


                    checkPaymentDone(new SessionManager(getApplicationContext()).getUserID(), book_Id, view);


                } else {
                    showFilterPopup(view);
                }

            }


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
        } else if (view == book_asignment) {
            Intent intent = new Intent(this, Book_Assignment.class);
            intent.putExtra("QuestionListExtra", (Serializable) assignmentList);
            intent.putExtra("answer", (Serializable) user_answers);
            intent.putExtra("book_id", book_Id);
            startActivity(intent);
        } else if (view == book_coverTv) {
            Intent intent = new Intent(Book_Detail_Screen.this, OponentUserDetailsScren.class);
            intent.putExtra("name", bookname);
            intent.putExtra("image", bookcover);
            intent.putExtra("id", 1);
            startActivity(intent);
        } else if (view == profile_user) {
            Intent intent = new Intent(Book_Detail_Screen.this, OponentUserDetailsScren.class);
            intent.putExtra("name", username);
            intent.putExtra("image", userimage);
            intent.putExtra("id", 1);
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
    private void gotoTextview(String story_text) {
        Intent intent = new Intent(this, BookTextView.class);
        intent.putExtra("name",bookname);
        intent.putExtra("story_text",story_text);
        startActivity(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getBookDetail(final String book_id) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();

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
                        ispaid = form.getData().getIs_paid();
                        price = form.getData().getPrice();
                        if (form.getData().getUser_name() != null) {
//                       SpannableString content = new SpannableString(form.getData().getUser_name());
//                       content.setSpan(new UnderlineSpan(), 0, content.length(), 0)
                            book_uploadBy.setText((form.getData().getUser_name()));
                            username = form.getData().getUser_name();

                            userimage = "http://" + form.getData().getProfile_pic();
                            Log.e("imsge", userimage);
                            priceBook.setText(euro + price);
                            //          GlideUtils.loadImage(Book_Detail_Screen.this,"http://"+form.getData().getProfile_pic(),book_coverTv,R.drawable.user_default,R.drawable.user_default);
                        } else {
                            profile_user.setVisibility(View.GONE);
                        }
                        admin_commission = form.getData().getAdmin_commission();
                        bookDesc.setText(form.getData().getBook_description());
                        videourl = form.getData().getVideo_url();
                        story_text=form.getData().getStory_text();
                        docurl = form.getData().getPdf_url();
                        audiourl = form.getData().getAudio_url();
                        bookdesc = form.getData().getBook_description();
                        averageRating.setText(form.getAveraVal());
                        myRatingBar.setRating(Float.parseFloat(form.getAveraVal()));
                        reviewCountView.setText("( " + form.getData().getMostView() + " )" + " Reviews");
                        if (form.getBookMark() != null) {
                            if (form.getBookMark().getBookmarkStatus().equals("1")) {
                                bookmark_btn.setBackgroundResource(R.drawable.bookmark_active);
                                isBookmark_book = true;
                            } else {
                                bookmark_btn.setBackgroundResource(R.drawable.bookmarkwhite);
                                isBookmark_book = false;
                            }
                        } else {
                            bookmark_btn.setBackgroundResource(R.drawable.bookmarkwhite);
                            isBookmark_book = false;
                        }
                        if (form.getData().getBook_description().length() >= 50) {
                            makeTextViewResizable(bookDesc, 5, "See More", true);
                        }
                        bookcover = "http://" + form.getData().getThubm_image();
                        bookname = form.getData().getBook_title();

                        GlideUtils.loadImage(Book_Detail_Screen.this, "http://" + form.getData().getThubm_image(), book_coverTv, R.drawable.noimage, R.drawable.noimage);
                        GlideUtils.loadImage(Book_Detail_Screen.this, "http://" + form.getData().getProfile_pic(), profile_user, R.drawable.user_default, R.drawable.user_default);
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

                        if (!videourl.isEmpty()) {
                            gotoWebview("http://" + videourl);
                        } else {
                            //  Toast.makeText(Book_Detail_Screen.this, "No Video for this Book", Toast.LENGTH_SHORT).show();

                            dialog("ok", "No Video for this Book");

                        }
                        return true;
                    case R.id.audiotv:
                        if (!audiourl.isEmpty()) {
                            gotoWebview("http://" + audiourl);
                        } else {

                            // Toast.makeText(Book_Detail_Screen.this, "No Audio for this Book", Toast.LENGTH_SHORT).show();
                            dialog("ok", "No Audio for this Book");


                        }
                        return true;
                    case R.id.doctv:


                        if (!docurl.isEmpty()) {
                            gotoWebview(docbaseUrl + docurl);
                        } else {

                            //  Toast.makeText(Book_Detail_Screen.this, "No Document File", Toast.LENGTH_SHORT).show();
                            dialog("ok", "No Document File for this Book");
                        }

                        return true;

                    case R.id.texttv:
                        if (story_text!=null) {
                            gotoTextview(story_text);
                        } else {

                            //  Toast.makeText(Book_Detail_Screen.this, "No Document File", Toast.LENGTH_SHORT).show();
                            dialog("ok", "No Text for this Book");
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
                    public void run() {
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

    public void dialog(final String value1, final String mesg) {
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void paypal(String user_id, String amount, String currency, String trans_id, String email, String bookid, String intent, String state ,String adminCommission ,String book_name) {
        ApiRequest request = new ApiRequest();
        request.requestforPaymentPaypal(amount, currency, trans_id, user_id, email, bookid, intent, state, adminCommission,book_name,new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                final String myResponse = response.body().string();
                Log.e("response", myResponse);
                Gson gson = new GsonBuilder().create();
                final PaymentResponse form = gson.fromJson(myResponse, PaymentResponse.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                         if (form.getStatus().equalsIgnoreCase("success"))
//                         {
//                             Toast .makeText(Book_Detail_Screen.this,"Transaction Successfull",Toast.LENGTH_SHORT).show();
//                         }
//                         else {
//                             Toast .makeText(Book_Detail_Screen.this,"Transaction Failed",Toast.LENGTH_SHORT).show();
//                         }
                    }
                });


            }
        });
    }

    private void procssPayment() {
        // totalamount=amount.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(convertPrice), currencyType, "Book Payment", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, Payrequescode);

    }

    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        if (requestcode == Payrequescode) {
            if (resultcode == RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (configuration != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.d("payment6", paymentDetails);
                        try {
                            JSONObject jsonObject = new JSONObject(paymentDetails);
                            JSONObject object = jsonObject.getJSONObject("response");
                            transaction_id = (object.getString("id"));
                             intents = object.getString("intent");
                             state = object.getString("state");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        paypal(new SessionManager(getApplicationContext()).getUserID(), convertPrice, currencyType, transaction_id, new SessionManager(getApplicationContext()).getUserEmail(), book_Id, intents, state , admin_commission ,bookname);

                        startActivity(new Intent(this, PaymentSuccess.class)
                                .putExtra("transaction_id", transaction_id)
                                .putExtra("PaymentAmount", convertPrice)
                                .putExtra("tr_type", "PayPal")
                                .putExtra("bookId", book_Id)
                                .putExtra("currency", currencyType));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else if (resultcode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultcode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void checkPaymentDone(String user_id, String bookId, View view) {
        ApiRequest request = new ApiRequest();
        request.requestforcheckPaymentDone(user_id, bookId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                final String myResponse = response.body().string();
                Log.e("response", myResponse);
                Gson gson = new GsonBuilder().create();
                final CheckPaymentDone form = gson.fromJson(myResponse, CheckPaymentDone.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (form.getError().equals(false)) {
                            if (form.getUser_data().getIs_payment_done().equalsIgnoreCase("Yes") && form.getUser_data().getPayment_status().equalsIgnoreCase("success")) {
                                showFilterPopup(view);
                            } else {
                                new AlertDialog.Builder(Book_Detail_Screen.this)
                                        .setMessage("Do you want to purchase this Book")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                              //PaymentMethod();
                                                 showDialog(Book_Detail_Screen.this,"");
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();


                            }
                        }

                    }
                });


            }
        });
    }


    private void  PaymentMethod(){
        final CharSequence[] items = {"PayPal", "Master Card / Credit Card"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Payment Method");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                 if (items[item].equals("PayPal"))
                 {
                     procssPayment();
                 }
                 else {

                 }

            }

        });

        AlertDialog alert = builder.create();

        alert.show();
    }

    public void showDialog(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.paymentmethod);
          payment  = dialog.findViewById(R.id.payment);
        radioGroupPaymentMethod = dialog.findViewById(R.id.radioGroupPaymentMethod);
        radioGroupCurrency =dialog.findViewById(R.id.radioGroupCurrency);
        Button cancel = dialog.findViewById(R.id.cancel);
        Button ok = dialog.findViewById(R.id.ok);
        payment.setText(euro+price);
        radioGroupPaymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.paypalMethod:
                        paymentMethod="paypal";
                        break;
                    case R.id.StripeMethod:
                        paymentMethod="stripe";
                        break;
                }
            }
        });

        radioGroupCurrency.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.Euro:
                        currencyType="EUR";
                        convertPrice=price;
                        payment.setText(euro+price);
                        break;
                    case R.id.usd:
                       currencyType="USD";
                        DecimalFormat precision = new DecimalFormat("0.00");
                       // double p = Double.valueOf(price)*1.12;
                      //  convertPrice = precision.format(p);
                        convertCurrency(price,"euro","usd");

                        break;
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     dialog.dismiss();
                     currencyType="";
                     paymentMethod="";
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                     if (isValidate()){
                         dialog.dismiss();
                         if (paymentMethod.equalsIgnoreCase("paypal")){
                             Log.e("pay",paymentMethod);
                             Log.e("cur",currencyType);
                             procssPayment();

                         }
                         else if (paymentMethod.equalsIgnoreCase("stripe")){
                             Log.e("pay",paymentMethod);
                             Log.e("cur",currencyType);
                             Intent intent = new Intent(Book_Detail_Screen.this, StripePayment.class);
                             intent.putExtra("price",convertPrice);
                             intent.putExtra("bookid",book_Id);
                             intent.putExtra("adminCommision",admin_commission);
                             intent.putExtra("book_name",bookname);
                             intent.putExtra("currency",currencyType);
                             startActivity(intent);

                         }
                     }
                currencyType="";
                paymentMethod="";
            }
        });
        dialog.show();

    }

    private Boolean isValidate(){
         if (paymentMethod.isEmpty()){

             int lastChildPos=radioGroupPaymentMethod.getChildCount()-1;
             ((RadioButton)radioGroupPaymentMethod.getChildAt(lastChildPos)).setError("Select Method");
             return false;
         }
         else {
              if (currencyType.isEmpty()){
                  int lastChildPos=radioGroupCurrency.getChildCount()-1;
                  ((RadioButton)radioGroupCurrency.getChildAt(lastChildPos)).setError("Select Currency");
                  return false;
              }
              else {

                  return true;
              }
         }


    }

    public void convertCurrency(final String toCurr, final double euroVlaue) throws IOException {

        String url = "https://api.exchangeratesapi.io/latest";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .build();



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String mMessage = response.body().string();
              runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MainActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject obj = new JSONObject(mMessage);
                            JSONObject  b = obj.getJSONObject("rates");

                            String val = b.getString(toCurr);

                            double output = euroVlaue*Double.valueOf(val);


//                            convertPrice = String.valueOf(output);
//                            Log.e("USD",String.valueOf(output));
//                            payment.setText("$"+String.valueOf(output));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void convertCurrency(String amount, String convertFrom, String convertTO) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforConvertCurrency(amount, convertFrom, convertTO, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                final String myResponse = response.body().string();
                Log.e("response", myResponse);
                Gson gson = new GsonBuilder().create();
               // final CheckPaymentDone form = gson.fromJson(myResponse, CheckPaymentDone.class);
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      try {
                          JSONObject obj = new JSONObject(myResponse);
                          JSONObject  b = obj.getJSONObject("data");

                          String val = b.getString("converted_amount");

                          convertPrice = val;
                          Log.e("USD",val);
                          payment.setText("$"+val);

                      } catch (JSONException e) {
                          e.printStackTrace();
                      }
                  }
              });


            }
        });
    }



}
