package com.ue.uebook.ChatSdk;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
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

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.ChatSdk.Adapter.GroupMemberListAdapter;
import com.ue.uebook.ChatSdk.Adapter.MemberListDetails;
import com.ue.uebook.ChatSdk.Pojo.DefaultPojo;
import com.ue.uebook.ChatSdk.Pojo.GroupMemberList;
import com.ue.uebook.ChatSdk.Pojo.GroupNameProfileResponse;
import com.ue.uebook.ChatSdk.Pojo.MemberListResponse;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.ImageUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupDetailScreen extends BaseActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener ,MemberListDetails.ItemClick ,ImageUtils.ImageAttachmentListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;
    private String filePath;
    private String fileName;
    private Bitmap bitmap;
    private LinearLayout mTitleContainer ,addMember ,exitGroup;
    private TextView mTitle ,group_name ,memberCount;
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
    private ImageUtils imageUtils;
    private ImageButton chnageGroupImageBtn;
    private File coverimage;
    private String groupImg="";
    private String groupName="";
    private EditText groupname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail_screen);
        bindActivity();
        memberListForAdmin = new ArrayList<>();
        memberListForAdminID = new ArrayList<>();
        mAppBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        imageUtils = new ImageUtils(this);
    }
    private void bindActivity() {
        intent = getIntent();
        mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle          = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
        chnageGroupImageBtn= findViewById(R.id.uploadgroupImage);
        chnageGroupImageBtn.setOnClickListener(this);

        exitGroup =findViewById(R.id.exitGroup);
        exitGroup.setOnClickListener(this);
        addMember=findViewById(R.id.addMember);
        addMember.setOnClickListener(this);
        imageGroup= findViewById(R.id.imageGroup);
        imageGroup .setOnClickListener(this);
        backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(this);
        memberList=findViewById(R.id.recyclerList);
        group_name=findViewById(R.id.groupname);
      //  group_name.setText(intent.getStringExtra("name"));
      //  mTitle.setText(intent.getStringExtra("name"));
        groupID=intent.getStringExtra("groupid");
      //  groupImg=intent.getStringExtra("groupimg");
        groupMemberLists= (List<GroupMemberList>) intent.getSerializableExtra("member");
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        memberList.setLayoutManager(linearLayoutManagerPopularList);
        memberCount=findViewById(R.id.memberCount);
       // GlideUtils.loadImage(GroupDetailScreen.this, "http:/dnddemo.com/ebooks/api/v1/" + groupImg, imageGroup, R.drawable.user_default, R.drawable.user_default);
        getGroupNameImage(groupID,new SessionManager(getApplicationContext()).getUserID());

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
        else if (v==chnageGroupImageBtn){

            showOption();

        }
        else if (v==imageGroup){
            Intent intent = new Intent (GroupDetailScreen.this,OponentUserDetailsScren.class);
            intent.putExtra("name",groupName);
            intent.putExtra("image","http://"+groupImg);
            intent.putExtra("id",1);
            startActivity(intent);

        }
    }

    private void showOption() {
        String [] options ={"Edit Name" , "Edit Profile" ,"Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                  if (options[item].equalsIgnoreCase("Edit Profile")){
                      imageUtils.imagepicker(1);
                      dialog.dismiss();
                  }
                  else   if (options[item].equalsIgnoreCase("Edit Name")){
                      showPopUp();
                   dialog.dismiss();
                  }
                  else {
                      dialog.dismiss();
                  }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showMemberToMakeAdmin(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a member for Admin");
        builder.setItems(memberListForAdmin.toArray(new String[memberListForAdmin.size()]), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
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
    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        this.bitmap = file;
        this.fileName = filename;
        String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageUtils.createImage(file, filename, path, false);
        filePath = getRealPathFromURI(uri.getPath());
        coverimage = (new File(imageUtils.getPath(uri)));
        imageGroup.setImageBitmap(file);
        Glide.with(this)
                .load(coverimage) // Uri of the picture
                .into(imageGroup);

        uploadGroupIcon(new SessionManager(getApplicationContext()).getUserID(),groupID,"",coverimage);
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = this.getApplicationContext().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void showPopUp() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.groupnameitem, null);
        groupname=customView.findViewById(R.id.groupname);
        Button okbtn=customView.findViewById(R.id.popupbtn);
        TextView charaterCount = customView.findViewById(R.id.charaterCount);
        groupname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                charaterCount.setText(String.valueOf(s.length()));
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        final android.app.AlertDialog alertDialog =builder.create();
//        builder.setPositiveButton("ok",new DialogInterface.OnClickListener() { // define the 'Cancel' button
//            public void onClick(DialogInterface dialog, int which) {
//
//
//                dialog.cancel();
//            }
//        });
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!groupname.getText().toString().isEmpty()){
                    editGroupName(new SessionManager(getApplicationContext()).getUserID(),groupID,groupname.getText().toString());
                    alertDialog.dismiss();

                }
                else {
                    groupname.setError("Enter group name");
                }
            }
        });
        alertDialog.setView(customView);
        alertDialog.show();

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void uploadGroupIcon(String user_id, String groupID  ,String action ,File group_image) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforuploadGroupIcon(user_id, groupID , action ,group_image, new okhttp3.Callback() {
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

                final DefaultPojo form = gson.fromJson(myResponse, DefaultPojo.class);
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         if (form.getError()==false){
                             getGroupNameImage(groupID,new SessionManager(getApplicationContext()).getUserID());

                             Toast.makeText(getApplicationContext(),"Successfully Updated",Toast.LENGTH_SHORT).show();
                         }
                         else {
                             Toast.makeText(getApplicationContext(),"Try Again",Toast.LENGTH_SHORT).show();

                         }
                     }
                 });


//                getGroupMember(new SessionManager(getApplicationContext()).getUserID(),groupID,"","");

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void editGroupName(String user_id, String groupID  ,String groupName ) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforEditGroupName(user_id, groupID , groupName, new okhttp3.Callback() {
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
                final DefaultPojo form = gson.fromJson(myResponse, DefaultPojo.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (form.getError()==false){
                 getGroupNameImage(groupID,new SessionManager(getApplicationContext()).getUserID());
                            Toast.makeText(getApplicationContext(),"Successfully Updated",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Try Again",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
//                getGroupMember(new SessionManager(getApplicationContext()).getUserID(),groupID,"","");

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getGroupNameImage( String groupID ,String user_id) {
        ApiRequest request = new ApiRequest();
        request.requestforGetGroupNameImage( groupID ,user_id, new okhttp3.Callback() {
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
                final GroupNameProfileResponse form = gson.fromJson(myResponse, GroupNameProfileResponse.class);
                if (form.getError() == false && form.getGroup_detail() != null) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GlideUtils.loadImage(GroupDetailScreen.this, "http://"+ form.getGroup_detail().getGroup_image(), imageGroup, R.drawable.user_default, R.drawable.user_default);
                            group_name.setText(form.getGroup_detail().getName());
                            mTitle.setText(form.getGroup_detail().getName());
                            groupName=form.getGroup_detail().getName();
                            groupImg=form.getGroup_detail().getGroup_image();
                        }
                    });
                }
            }
        });
    }



}
