package com.ue.uebook.ChatSdk;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.ChatSdk.Adapter.CreategroupAdapter;
import com.ue.uebook.ChatSdk.Adapter.GroupAdapter;
import com.ue.uebook.ChatSdk.Adapter.GroupItemListAdapter;
import com.ue.uebook.ChatSdk.Pojo.ContactListResponse;
import com.ue.uebook.ChatSdk.Pojo.OponentData;
import com.ue.uebook.ChatSdk.Pojo.UserData;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupFriendList extends BaseActivity implements View.OnClickListener, GroupAdapter.ItemClick, GroupItemListAdapter.ItemClick ,CreategroupAdapter.ItemClick{
    private ImageButton backbtngroup;
    private RecyclerView recyclerView,groupList;
    private GroupAdapter groupAdapter;
    private GroupItemListAdapter groupItemListAdapter;
    private List<OponentData>groupPopleList;
    private FloatingActionButton createGroup;
    private ListView userlist;
    private CreategroupAdapter contactList;
    private EditText groupname;
    private  TextWatcher mTextEditorWatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_friend_list);
        backbtngroup = findViewById(R.id.backbtngroup);
        backbtngroup.setOnClickListener(this);
        recyclerView=findViewById(R.id.contactList);
        groupList=findViewById(R.id.groupList);
        createGroup=findViewById(R.id.createGroup);
        userlist = findViewById(R.id.userlist);
        createGroup.setOnClickListener(this);
        groupPopleList= new ArrayList<>();
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManagerPopularList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        groupList.setLayoutManager(linearLayoutManager);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getContactList(new SessionManager(getApplication()).getUserID());
        }


    }

    @Override
    public void onClick(View v) {
        if (v==backbtngroup){
            finish();
        }
        else if (v==createGroup){
          showPopUp();
         // Toast.makeText(GroupFriendList.this,"Coming Soon!",Toast.LENGTH_SHORT).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getContactList(String  user_id) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforgetContactList( user_id,new okhttp3.Callback() {
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
                final ContactListResponse form = gson.fromJson(myResponse, ContactListResponse.class);

                if (form.error==false && form.getUserList()!=null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

//                            groupAdapter = new GroupAdapter(GroupFriendList.this,form.getUserList(),form.getData());
//                            recyclerView.setAdapter(groupAdapter);
//                            groupAdapter.setItemClickListener(GroupFriendList.this);
//                            groupAdapter.notifyDataSetChanged();


                            contactList = new CreategroupAdapter(GroupFriendList.this,form.getUserList());
                             contactList.setItemClickListener(GroupFriendList.this);
                            userlist.setAdapter(contactList);

                        }
                    });
                }
            }
        });
    }


    @Override
    public void onContactListItemClick(OponentData oponentData, UserData userData) {

    }
    @SuppressLint("RestrictedApi")
    private void addKist(List<OponentData>oponentData){
        if (oponentData.size()>0){
            groupList.setVisibility(View.VISIBLE);
            createGroup.setVisibility(View.VISIBLE);
            groupItemListAdapter= new GroupItemListAdapter(GroupFriendList.this,oponentData);
            groupList.setAdapter(groupItemListAdapter);
            groupItemListAdapter.setItemClickListener(GroupFriendList.this);
            groupItemListAdapter.notifyDataSetChanged();
        }
        else {
            createGroup.setVisibility(View.GONE);
            groupList.setVisibility(View.GONE);
        }

    }

    @Override
    public void ongroupListItemClick(OponentData oponentData, int position) {
         if (oponentData!=null){
//             if (groupPopleList.size()>0){
//                 groupPopleList.remove(position);
//                 addKist(groupPopleList);
//             }
         }

    }

    @Override
    public void ontItemClick(OponentData oponentData, int position ,int id) {
        if (oponentData!=null){
            Log.e("pod",String.valueOf(position));
            if (id==1){
                groupPopleList.add(oponentData);
                addKist(groupPopleList);
            }
            else if (id==2){
                if (groupPopleList.size()>0){
                    groupPopleList.remove(oponentData);
                    addKist(groupPopleList);
                }

            }

        }
    }

    public void showPopUp() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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


        final AlertDialog alertDialog =builder.create();
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
    private void disableEditText(EditText editText) {
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

}
