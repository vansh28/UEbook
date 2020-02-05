package com.ue.uebook.ChatSdk;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ue.uebook.ChatSdk.Adapter.ConactListTabChanger;
import com.ue.uebook.R;

public class ContactListScreen extends AppCompatActivity implements View.OnClickListener ,TelephonebookFrag.OnFragmentInteractionListener,FriendListFrag.OnFragmentInteractionListener{
    TabLayout tabLayout;
    ViewPager viewPager;
    private ImageButton backbtnChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list_screen);


        tabLayout = findViewById(R.id.tabLayout);
        backbtnChat=findViewById(R.id.backbtnChat);
        backbtnChat.setOnClickListener(this);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Friend List"));
        tabLayout.addTab(tabLayout.newTab().setText("Telephone Book"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ConactListTabChanger adapter = new ConactListTabChanger(this,getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });
    }



    @Override
    public void onClick(View v) {
        if (v== backbtnChat){

            finish();
        }
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

