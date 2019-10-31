package com.ue.uebook.ChatSdk;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ue.uebook.ChatSdk.Adapter.ContactListAdapter;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;
public class FriendListScreen extends AppCompatActivity implements View.OnClickListener  ,ContactListAdapter.ItemClick{
    private ImageButton backbtn;
    private ContactListAdapter contactListAdapter;
   private  RecyclerView contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list_screen2);
        backbtn=findViewById(R.id.backbtnContact);
        contactList=findViewById(R.id.contactList);
        backbtn.setOnClickListener(this);
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        contactList.setLayoutManager(linearLayoutManagerPopularList);
        contactListAdapter = new ContactListAdapter();
        contactList.setAdapter(contactListAdapter);
        contactListAdapter.setItemClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v==backbtn){
            finish();
        }
    }
    @Override
    public void onContactListItemClick() {
        Intent intent = new Intent(this,MessageScreen.class);
        startActivity(intent);
    }
    @Override
    public void onProfileClick() {
         imagePreview("dshdjd");
    }
    private void imagePreview(String file) {
        final Dialog previewDialog = new Dialog(this);
        previewDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        previewDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout
                , null));
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
}
