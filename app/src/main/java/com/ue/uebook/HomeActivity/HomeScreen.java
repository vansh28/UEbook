package com.ue.uebook.HomeActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ue.uebook.HomeActivity.HomeFragment.Bookmark_Fragment;
import com.ue.uebook.HomeActivity.HomeFragment.CompanyInfo_Fragment;
import com.ue.uebook.HomeActivity.HomeFragment.Home_Fragment;
import com.ue.uebook.HomeActivity.HomeFragment.Search_Fragment;
import com.ue.uebook.HomeActivity.HomeFragment.UserMainFragment;
import com.ue.uebook.HomeActivity.HomeFragment.UserProfile_Fragment;
import com.ue.uebook.HomeActivity.HomeFragment.User_Fragment;
import com.ue.uebook.ImageUtils;
import com.ue.uebook.LoginActivity.Fragment.SignIn_Fragment;
import com.ue.uebook.LoginActivity.Fragment.SignUp_Fragment;
import com.ue.uebook.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeScreen extends AppCompatActivity implements Home_Fragment.OnFragmentInteractionListener, Bookmark_Fragment.OnFragmentInteractionListener, User_Fragment.OnFragmentInteractionListener, Search_Fragment.OnFragmentInteractionListener, UserProfile_Fragment.OnFragmentInteractionListener , UserMainFragment.OnFragmentInteractionListener , CompanyInfo_Fragment.OnFragmentInteractionListener {
    private ActionBar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        toolbar = getSupportActionBar();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new Home_Fragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.home_bottom:
                    fragment = new Home_Fragment();
                    loadFragment(fragment);
                    return true;
                case R.id.search_bottom:
                    fragment = new Search_Fragment();
                    loadFragment(fragment);
                    return true;
                case R.id.bookmark_bottom:
                    fragment = new Bookmark_Fragment();
                    loadFragment(fragment);
                    return true;
                case R.id.user_bottom:
                    fragment = new User_Fragment();
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
}

