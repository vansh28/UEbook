package com.ue.uebook.HomeActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Constants;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.DeatailActivity.Book_Detail_Screen;
import com.ue.uebook.FontCache;
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.Home_recommended_Adapter;
import com.ue.uebook.HomeActivity.HomeFragment.HomeListingFragment;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListing;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListingResponse;
import com.ue.uebook.R;
import com.ue.uebook.UploadBook.Pojo.BookCategoryResponsePojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.ue.uebook.NetworkUtils.getInstance;

public class BookListing extends BaseActivity implements HomeListingFragment.OnFragmentInteractionListener, Home_recommended_Adapter.RecommendedItemClick, View.OnClickListener {
    private TabLayout tabLayout;
    ArrayList<String> categoryName;
    ArrayList<String> categoryID;
    private ViewPager viewPager;
    private List<HomeListing>recommendedList_book;
    private Home_recommended_Adapter home_recommended_adapter;
    private RecyclerView homelist;
    private ImageView back_iv,search_iv;
    private TextView noBookfoundText;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_listing);
        tabLayout = findViewById(R.id.tabs);
        viewPager =findViewById(R.id.viewpager);
        homelist = findViewById(R.id.homelist);
        back_iv=findViewById(R.id.back_iv);
        search_iv=findViewById(R.id.search_iv);
        noBookfoundText=findViewById(R.id.noBookfoundText);
        search_iv.setOnClickListener(this);
        back_iv.setOnClickListener(this);
        categoryName = new ArrayList<>();
        categoryID = new ArrayList<>();
        recommendedList_book= new ArrayList<>();
        tabLayout.setupWithViewPager(viewPager);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homelist.setLayoutManager(linearLayoutManager);
        getBookCategory();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int  position = tab.getPosition();
                if (getInstance(BookListing.this).isConnectingToInternet()) {
                    getRecommenedBookList(categoryID.get(position));
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void getBookCategory() {
        ApiRequest request = new ApiRequest();
        request.requestforGetbookCategory(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", e.getLocalizedMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myresponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final BookCategoryResponsePojo form = gson.fromJson(myresponse, BookCategoryResponsePojo.class);

                if (form != null) {
                    for (int i = 0; i < form.getResponse().size(); i++){
                        categoryName.add(form.getResponse().get(i).getCategory_name());
                        categoryID.add(form.getResponse().get(i).getId());
                    }
                }
                runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            addTabs(viewPager);
                           //changeTabsFont(tabLayout);
                        }
                    });
            }
        });
    }

    private void changeTabsFont(TabLayout tabLayout) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(FontCache.getTypeface(Constants.FONT_BOLD, this));
                    ((TextView) tabViewChild).setTextColor(getResources().getColor(R.color.white));
//                    ((TextView) tabViewChild).setTextSize(getResources().getDimension(R.dimen.size_20));
//                    ((TextView) tabViewChild).setTextSize(50);
                }
            }
        }
    }
    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i=0;i<categoryName.size();i++){
            adapter.addFrag(new HomeListingFragment(), categoryName.get(i));
            viewPager.setAdapter(adapter);
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void onItemClick(int position, String book_id) {
        Intent intent = new Intent(this, Book_Detail_Screen.class);
        intent.putExtra("book_id", book_id);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v==back_iv){
                  finish();
        }
        else if (v==search_iv){

        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getRecommenedBookList(String categoryId) {
        ApiRequest request = new ApiRequest();
        if (recommendedList_book.size() > 0)
            recommendedList_book.clear();

        request.requestforgetBookList(categoryId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String myResponse = response.body().string();
                hideLoadingIndicator();
                Gson gson = new GsonBuilder().create();
                final HomeListingResponse form = gson.fromJson(myResponse, HomeListingResponse.class);
                if (form.getData() != null) {
                    recommendedList_book.addAll(form.getData());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                homelist.setVisibility(View.VISIBLE);
                                noBookfoundText.setVisibility(View.GONE);
                                home_recommended_adapter = new Home_recommended_Adapter(BookListing.this , form.getData(), 16);
                                homelist.setAdapter(home_recommended_adapter);
                                home_recommended_adapter.setItemClickListener(BookListing.this);
                                home_recommended_adapter.notifyDataSetChanged();
                                homelist.setNestedScrollingEnabled(false);
                            }
                        });
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            homelist.setVisibility(View.GONE);
                            noBookfoundText.setVisibility(View.VISIBLE);
                        }
                    });

                }

            }
        });
    }

}
