package com.ue.uebook.ChatSdk;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.ChatSdk.Adapter.ContactListAdapter;
import com.ue.uebook.ChatSdk.Pojo.ContactListResponse;
import com.ue.uebook.ChatSdk.Pojo.OponentData;
import com.ue.uebook.ChatSdk.Pojo.UserData;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;
public class FriendListScreen extends BaseActivity implements View.OnClickListener  ,ContactListAdapter.ItemClick{
    private ImageButton back_btn;
    private ContactListAdapter contactListAdapter;
   private  RecyclerView contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list_screen2);
        back_btn =findViewById(R.id.backbtnContact);
        contactList=findViewById(R.id.contactList);
        back_btn.setOnClickListener(this);
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        contactList.setLayoutManager(linearLayoutManagerPopularList);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getContactList(new SessionManager(getApplication()).getUserID());
        }
    }
    @Override
    public void onClick(View v) {
        if (v== back_btn){
            finish();
        }
    }
    @Override
    public void onContactListItemClick(OponentData oponentData , UserData userData) {
        Intent intent = new Intent(this,MessageScreen.class);
        intent.putExtra("oponentdata",oponentData);
        intent.putExtra("userData",userData);
        intent.putExtra("id",2);
        startActivity(intent);
        finish();
    }
    @Override
    public void onProfileClick(String url) {
         imagePreview(url);
    }
    private void imagePreview(String file) {
        final Dialog previewDialog = new Dialog(this);
        previewDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        previewDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout ,null));
        ImageView imageView = previewDialog.findViewById(R.id.image_view);
        GlideUtils.loadImage(FriendListScreen.this, "http://dnddemo.com/ebooks/api/v1/upload/" + file, imageView, R.drawable.user_default, R.drawable.user_default);
        Button ok_Btn = previewDialog.findViewById(R.id.buton_ok);
        ok_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewDialog.dismiss();

            }
        });
        previewDialog.show();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contactListAdapter = new ContactListAdapter(FriendListScreen.this,form.getUserList(),form.getData());
                        contactList.setAdapter(contactListAdapter);
                        contactListAdapter.setItemClickListener(FriendListScreen.this);
                        contactListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

}
