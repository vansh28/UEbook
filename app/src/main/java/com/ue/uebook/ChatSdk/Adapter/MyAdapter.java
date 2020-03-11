package com.ue.uebook.ChatSdk.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ue.uebook.ChatSdk.ChatHistoryFrag;
import com.ue.uebook.ChatSdk.GroupChatFrag;
import com.ue.uebook.ChatSdk.StatusFragment;



public class MyAdapter   extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public MyAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ChatHistoryFrag chatHistoryFrag = new ChatHistoryFrag();
                return chatHistoryFrag;
            case 1:
                GroupChatFrag cricketFragment = new GroupChatFrag();
                return cricketFragment;
            case 2:
                StatusFragment statusFragment = new StatusFragment();
                return statusFragment;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}