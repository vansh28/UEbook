package com.ue.uebook.ChatSdk.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ue.uebook.ChatSdk.FriendListFrag;
import com.ue.uebook.ChatSdk.TelephonebookFrag;

public class ConactListTabChanger  extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public ConactListTabChanger(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FriendListFrag chatHistoryFrag = new FriendListFrag();
                return chatHistoryFrag;
            case 1:
                TelephonebookFrag cricketFragment = new TelephonebookFrag();
                return cricketFragment;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}