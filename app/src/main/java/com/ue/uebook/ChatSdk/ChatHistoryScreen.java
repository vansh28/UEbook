package com.ue.uebook.ChatSdk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.ue.uebook.ChatSdk.Adapter.MyAdapter;
import com.ue.uebook.HomeActivity.HomeScreen;
import com.ue.uebook.R;

public class ChatHistoryScreen extends AppCompatActivity implements ChatHistoryFrag.OnFragmentInteractionListener, GroupChatFrag.OnFragmentInteractionListener, StatusFragment.OnFragmentInteractionListener,View.OnClickListener ,CallLogFragment.OnFragmentInteractionListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    private ImageButton backbtnChat,optionMenu;
    private FloatingActionButton newChatbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history_screen);
        tabLayout = findViewById(R.id.tabLayout);
        backbtnChat=findViewById(R.id.backbtnChat);
        backbtnChat.setOnClickListener(this);
        newChatbtn= findViewById(R.id.newChatbtn);
        optionMenu = findViewById(R.id.option);
        optionMenu.setOnClickListener(this);
        newChatbtn.setOnClickListener(this);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Chat"));
        tabLayout.addTab(tabLayout.newTab().setText("Group Chat"));
        tabLayout.addTab(tabLayout.newTab().setText("Status"));
        tabLayout.addTab(tabLayout.newTab().setText("Calls"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final MyAdapter adapter = new MyAdapter(this,getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Log.e("poso",String.valueOf(tab.getPosition()));
                if (tab.getPosition()== 0){
                    newChatbtn.setVisibility(View.VISIBLE);
                }
                if (tab.getPosition()== 1){
                    newChatbtn.setVisibility(View.VISIBLE);
                }
                if (tab.getPosition()== 2){
                    newChatbtn.setVisibility(View.GONE);
                }
                if (tab.getPosition()== 3){
                    newChatbtn.setVisibility(View.GONE);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        if (v== backbtnChat){
            Intent intent = new Intent(this , HomeScreen.class);
            startActivity(intent);
            finish();
        }
       else if (v==newChatbtn){
            Intent intent = new Intent(this,ContactListScreen.class);
              startActivity(intent);
        }
       else if (v==optionMenu){
           showFilterPopup(v);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,HomeScreen.class);
        startActivity(intent);
        finish();
    }
    private void showFilterPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.userchatoptions);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Profile:
                        Intent intent = new Intent(ChatHistoryScreen.this,UserProfileChatScreen.class);
                        startActivity(intent);
                        return true;

                    case R.id.Privacy:
                    Intent intents = new Intent(ChatHistoryScreen.this,StatusPrivacyScreen.class);
                    startActivity(intents);
                    return  true;
                    case R.id.Starred:
                        Intent intentst = new Intent(ChatHistoryScreen.this,StarredMessageScreen.class);
                        startActivity(intentst);
                        return true;

                    case R.id.Broadcast:
                        Intent intentb = new Intent(ChatHistoryScreen.this,BroadCastUserList.class);
                        startActivity(intentb);
                        return  true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

}

