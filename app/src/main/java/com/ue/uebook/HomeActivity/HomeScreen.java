package com.ue.uebook.HomeActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.Home_recommended_Adapter;
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.New_Book_Home_Adapter;
import com.ue.uebook.HomeActivity.HomeFragment.Bookmark_Fragment;
import com.ue.uebook.HomeActivity.HomeFragment.CompanyInfo_Fragment;
import com.ue.uebook.HomeActivity.HomeFragment.Home_Fragment;
import com.ue.uebook.HomeActivity.HomeFragment.NotepadFragment;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListing;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListingResponse;
import com.ue.uebook.HomeActivity.HomeFragment.Search_Fragment;
import com.ue.uebook.HomeActivity.HomeFragment.UserMainFragment;
import com.ue.uebook.HomeActivity.HomeFragment.UserProfile_Fragment;
import com.ue.uebook.HomeActivity.HomeFragment.User_Fragment;
import com.ue.uebook.NotepadScreen;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ue.uebook.NetworkUtils.getInstance;


public class HomeScreen extends BaseActivity implements Home_Fragment.OnFragmentInteractionListener, Bookmark_Fragment.OnFragmentInteractionListener, User_Fragment.OnFragmentInteractionListener, Search_Fragment.OnFragmentInteractionListener, UserProfile_Fragment.OnFragmentInteractionListener, UserMainFragment.OnFragmentInteractionListener, CompanyInfo_Fragment.OnFragmentInteractionListener, NotepadFragment.OnFragmentInteractionListener, View.OnClickListener {
    private ActionBar toolbar;
    private List<HomeListing> recommendedList_book,newBookList;
    private FloatingActionButton addnotes_fab;
    private CoordinatorLayout container;



    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        addnotes_fab=findViewById(R.id.addnotes_fab);
        addnotes_fab.setVisibility(View.GONE);
        container=findViewById(R.id.container);
        addnotes_fab.setOnClickListener(this);
         recommendedList_book= new ArrayList<>();
         newBookList = new ArrayList<>();
        displayCurrentAddress();
        toolbar = getSupportActionBar();

        if (getInstance(this).isConnectingToInternet()){
            getRecommenedBookList("1");

            getnewBookList("2");

        }
        else {

            showSnackBar(container, getString(R.string.no_internet));
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new Home_Fragment());

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("RestrictedApi")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.home_bottom:
                    fragment = new Home_Fragment();
                    loadFragment(fragment);
                    addnotes_fab.setVisibility(View.GONE);
                    return true;
                case R.id.search_bottom:
                    addnotes_fab.setVisibility(View.GONE);
                    fragment = new Search_Fragment();
                    loadFragment(fragment);

                    return true;
                case R.id.bookmark_bottom:
                    addnotes_fab.setVisibility(View.GONE);
                    fragment = new Bookmark_Fragment();
                    loadFragment(fragment);
                    return true;
                case R.id.user_bottom:
                    addnotes_fab.setVisibility(View.GONE);
                    fragment = new User_Fragment();
                    loadFragment(fragment);
                    return true;
                case R.id.notepad_bottom:
                    addnotes_fab.setVisibility(View.VISIBLE);
                    fragment = new NotepadFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void displayCurrentAddress() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        if (updateLocationUI() != null && !(updateLocationUI().isEmpty())) {
                            Thread.sleep(1000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new SessionManager(getApplicationContext()).storeUserLocation(updateLocationUI());
                                }
                            });
                            break;
                        } else
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException ignored) {
                }
            }
        };
        t.start();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!checkPermissions())
            PermissionRequest(34);
        else
            getCurrentLocation();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getRecommenedBookList(String categoryId) {
        ApiRequest request = new ApiRequest();
        if (recommendedList_book.size()>0)
            recommendedList_book.clear();

        request.requestforgetBookList(categoryId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");

            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final HomeListingResponse form = gson.fromJson(myResponse, HomeListingResponse.class);
                if (form.getError().equalsIgnoreCase("false") && form.getData() != null) {
                    recommendedList_book.addAll(form.getData());
                }
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getnewBookList(String categoryId) {
        ApiRequest request = new ApiRequest();
        if (newBookList.size()>0)
            newBookList.clear();

        request.requestforgetBookList(categoryId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");


            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final HomeListingResponse form = gson.fromJson(myResponse, HomeListingResponse.class);
                if (form.getError().equalsIgnoreCase("false") && form.getData() != null) {
                    newBookList.addAll(form.getData());
                }

            }
        });
    }

    public List<HomeListing> getRecommendedListBookData() {
        return recommendedList_book;
    }

    public List<HomeListing> getnewBookData() {
        return newBookList;
    }

    @Override
    public void onClick(View view) {
        if (view==addnotes_fab){
            Intent intent  = new Intent(HomeScreen.this,NotepadScreen.class);
            startActivity(intent);
        }
    }
}

