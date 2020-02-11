package com.ue.uebook.ChatSdk;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.ChatSdk.Adapter.GroupMemberListAdapter;
import com.ue.uebook.ChatSdk.Adapter.MemberListDetails;
import com.ue.uebook.ChatSdk.Pojo.GroupMemberList;
import com.ue.uebook.ChatSdk.Pojo.MemberListResponse;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;
import java.util.List;

public class GroupDetailScreen extends BaseActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer ,addMember;
    private TextView mTitle ,groupname ,memberCount;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private RecyclerView memberList;
    private Intent intent;
    private GroupMemberListAdapter groupMemberListAdapter;
    private List<GroupMemberList>groupMemberLists;
    private MemberListDetails memberListDetails;
    private ImageButton backbtn;
    private ImageView  imageGroup;
    private String groupID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail_screen);
        bindActivity();
        mAppBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

    }
    private void bindActivity() {
        intent = getIntent();
        mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle          = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
        addMember=findViewById(R.id.addMember);
        addMember.setOnClickListener(this);
        imageGroup= findViewById(R.id.imageGroup);
        backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(this);
        memberList=findViewById(R.id.recyclerList);
        groupname=findViewById(R.id.groupname);
        groupname.setText(intent.getStringExtra("name"));
        mTitle.setText(intent.getStringExtra("name"));
        groupID=intent.getStringExtra("groupid");
        groupMemberLists= (List<GroupMemberList>) intent.getSerializableExtra("member");
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        memberList.setLayoutManager(linearLayoutManagerPopularList);
            memberCount=findViewById(R.id.memberCount);
            getGroupMember(new SessionManager(getApplicationContext()).getUserID(),groupID);

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
       v.startAnimation(alphaAnimation);
    }


    @Override
    public void onClick(View v) {
        if (v==backbtn){
            finish();
        }
        else if (v==addMember){

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getGroupMember(String user_id, String groupID) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        if (groupMemberLists.size()>0)
            groupMemberLists.clear();
        request.requestforgetGroupMember(user_id, groupID,"", new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                hideLoadingIndicator();

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final MemberListResponse form = gson.fromJson(myResponse, MemberListResponse.class);
                if (form.getError() == false && form.getUser_list() != null) {
//
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            groupMemberLists.addAll(form.getUser_list());
                            memberListDetails = new MemberListDetails(GroupDetailScreen.this,form.getUser_list());
                            memberList.setAdapter(memberListDetails);
                            memberCount.setText(String.valueOf(groupMemberLists.size())+" "+ "participants");

                        }
                    });
//
//
                } else {

                }
            }
        });
    }

}
