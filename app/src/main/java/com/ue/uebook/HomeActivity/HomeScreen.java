package com.ue.uebook.HomeActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.HomeActivity.HomeFragment.Bookmark_Fragment;
import com.ue.uebook.HomeActivity.HomeFragment.CompanyInfo_Fragment;
import com.ue.uebook.HomeActivity.HomeFragment.Home_Fragment;
import com.ue.uebook.HomeActivity.HomeFragment.NotepadFragment;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListing;
import com.ue.uebook.HomeActivity.HomeFragment.Search_Fragment;
import com.ue.uebook.HomeActivity.HomeFragment.UserMainFragment;
import com.ue.uebook.HomeActivity.HomeFragment.UserProfile_Fragment;
import com.ue.uebook.HomeActivity.HomeFragment.User_Fragment;
import com.ue.uebook.NotepadScreen;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class HomeScreen extends BaseActivity implements Home_Fragment.OnFragmentInteractionListener, Bookmark_Fragment.OnFragmentInteractionListener, User_Fragment.OnFragmentInteractionListener, Search_Fragment.OnFragmentInteractionListener, UserProfile_Fragment.OnFragmentInteractionListener, UserMainFragment.OnFragmentInteractionListener, CompanyInfo_Fragment.OnFragmentInteractionListener, NotepadFragment.OnFragmentInteractionListener, View.OnClickListener {
    private ActionBar toolbar;
    private List<HomeListing> recommendedList_book,newBookList,popularBook_List;
    private FloatingActionButton addnotes_fab;
    private CoordinatorLayout container;
    private Intent intent;
    Fragment fragment;
    private BottomSheetDialog mBottomSheetDialog;
    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        setLocale(new SessionManager(getApplicationContext()).getCurrentLanguage());
        addnotes_fab=findViewById(R.id.addnotes_fab);
        addnotes_fab.setVisibility(View.GONE);
        container=findViewById(R.id.container);
        addnotes_fab.setOnClickListener(this);
        recommendedList_book= new ArrayList<>();
        newBookList = new ArrayList<>();
        popularBook_List = new ArrayList<>();
        displayCurrentAddress();
        toolbar = getSupportActionBar();
        intent = getIntent();
        int loginid= intent.getIntExtra("login",0);
        int id = intent.getIntExtra("id",0);
        if (loginid==1){
            showmessage();
            showBottomSheet();
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new Home_Fragment());

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("RestrictedApi")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commitNowAllowingStateLoss();;

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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onStart() {
        super.onStart();
        if (!checkPermissions())
            PermissionRequest(34);
        else{

        }
            getCurrentLocation();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onClick(View view) {
        if (view==addnotes_fab){
            Intent intent  = new Intent(HomeScreen.this,NotepadScreen.class);
            startActivity(intent);
        }
    }
    private void showmessage(){
        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onFinish() {
                // TODO Auto-generated method stub

                mBottomSheetDialog.dismiss();
            }
        }.start();
    }
    private void showBottomSheet() {
        final View bottomSheetLayout = getLayoutInflater().inflate(R.layout.bottomsheetlogininfo, null);
        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(bottomSheetLayout);
        mBottomSheetDialog.show();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        }}
        return true;
    }
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}

