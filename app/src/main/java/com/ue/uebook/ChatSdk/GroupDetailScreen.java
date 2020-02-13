package com.ue.uebook.ChatSdk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
import java.util.ArrayList;
import java.util.List;

public class GroupDetailScreen extends BaseActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener ,MemberListDetails.ItemClick {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer ,addMember ,exitGroup;
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
    private String  groupAdminID="";
    private  List<String> memberListForAdmin  ;
    private  List<String> memberListForAdminID  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail_screen);
        bindActivity();
        memberListForAdmin= new ArrayList<>();
        memberListForAdminID = new ArrayList<>();
        mAppBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
    }
    private void bindActivity() {
        intent = getIntent();
        mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle          = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
        exitGroup =findViewById(R.id.exitGroup);
        exitGroup.setOnClickListener(this);
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
        getGroupMember(new SessionManager(getApplicationContext()).getUserID(),groupID,"","");
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
            Intent intent = new Intent(this, AddMemberToGroupScreen.class);
            intent.putExtra("groupid",groupID);
            startActivity(intent);
        }
        else if (v==exitGroup)
        {
            if (groupAdminID.equalsIgnoreCase(new SessionManager(getApplicationContext()).getUserID())){
                showMemberToMakeAdmin();
            }
            else {
                confirmUser(new SessionManager(getApplicationContext()).getUserID());
            }
        }
    }

    private void showMemberToMakeAdmin(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a member for Admin");
        builder.setItems(memberListForAdmin.toArray(new String[memberListForAdmin.size()]), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(), String.valueOf(memberListForAdminID.get(item)), Toast.LENGTH_SHORT).show();
                exitGroup(new SessionManager(getApplicationContext()).getUserID(),groupID,memberListForAdminID.get(item));

            }
        });

        AlertDialog alert = builder.create();

        alert.show();
    }

    public void confirmUser(final String userId ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit this group")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        exitGroup(new SessionManager(getApplicationContext()).getUserID(),groupID,"");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getGroupMember(String user_id, String groupID,String memberid ,String action) {
        ApiRequest request = new ApiRequest();
        if (groupMemberLists.size()>0)
            groupMemberLists.clear();


//        if (memberListForAdmin.size()>0){
//            memberListForAdmin.clear();
//        }
//
//        if (memberListForAdminID.size()>0)
//        {
//            memberListForAdminID.clear();
//        }
        request.requestforgetGroupMember(user_id, groupID,memberid,action, new okhttp3.Callback() {
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
                             memberListDetails.setItemClickListener(GroupDetailScreen.this);
                            memberCount.setText(String.valueOf(groupMemberLists.size())+" "+ "participants");

                            for (int i = 0 ; i< groupMemberLists.size(); i++){
                                memberListForAdmin.add(groupMemberLists.get(i).getUser_name());
                                memberListForAdminID.add(groupMemberLists.get(i).getId());
                                if (groupMemberLists.get(i).getId().equalsIgnoreCase(new SessionManager(getApplicationContext()).getUserID())){
                                    if (groupMemberLists.get(i).getIs_admin().equalsIgnoreCase("yes")){
                                        addMember.setVisibility(View.VISIBLE);
                                        groupAdminID=groupMemberLists.get(i).getId();
                                        memberListForAdmin.remove(groupMemberLists.get(i).getUser_name());
                                        memberListForAdminID.remove(groupMemberLists.get(i).getId());
                                    }
                                    else {
                                        addMember.setVisibility(View.GONE);

                                    }
                                }

                            }
                            Log.e("sizxe",String.valueOf(memberListForAdmin.size()));


                        }
                    });
//
//
                } else {


                }
            }
        });
    }

    @Override
    public void onMemberItemClick(View v,String memberID ,String admin) {

                  if(memberID.equalsIgnoreCase(new SessionManager(getApplicationContext()).getUserID())){

                  }
                  else {
                      showFilterPopup(v,memberID);
                  }

    }


    private void showFilterPopup(View v , String memberID) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.groupdetail);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.removeFriend:
                        getGroupMember(new SessionManager(getApplicationContext()).getUserID(),groupID,memberID,"delete_member");
                        return true;
                    case R.id.makeGroupAdmin:


                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked");

    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle","onPause invoked");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle","onStop invoked");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle","onRestart invoked");

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something here
                getGroupMember(new SessionManager(getApplicationContext()).getUserID(),groupID,"","");

            }
        }, 1000);

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void exitGroup(String user_id, String groupID  ,String memberid) {
        ApiRequest request = new ApiRequest();
        request.requestforexitGroup(user_id, groupID , memberid, new okhttp3.Callback() {
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
//                getGroupMember(new SessionManager(getApplicationContext()).getUserID(),groupID,"","");


                Intent intent = new Intent(GroupDetailScreen.this , ChatHistoryScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }




}
