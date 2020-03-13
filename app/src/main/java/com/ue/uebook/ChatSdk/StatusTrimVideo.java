package com.ue.uebook.ChatSdk;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class StatusTrimVideo extends AppCompatActivity implements View.OnClickListener {
    private ImageView status_image ,backbtn;
    private Intent intent;
    private String type;
    private String file;
    private File imageFile;
    private RelativeLayout root_view;
    private ImageButton button_chat_emoji,button_status_send;
    private EmojiconEditText edit_chat_message;
    EmojIconActions emojIcon;
    final int PIC_CROP = 1;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_trim_video);
        status_image = findViewById(R.id.status_image);
        root_view = findViewById(R.id.root_view);
        button_chat_emoji= findViewById(R.id.button_chat_emoji);
        button_status_send = findViewById(R.id.button_chat_send);
        button_status_send.setOnClickListener(this);
        button_chat_emoji.setOnClickListener(this);
        edit_chat_message = findViewById(R.id.edit_chat_message);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(this);
        intent = getIntent();
        file = intent.getStringExtra("file");


        if(file!=null){
            bmp = (BitmapFactory.decodeFile(file));
            status_image.setImageBitmap(bmp);
        }

        type = intent.getStringExtra("type");
        emojIcon = new EmojIconActions(this, root_view, edit_chat_message, button_chat_emoji);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("fff", "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("fff", "Keyboard closed");
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v==backbtn){
            finish();
        }
        else if (v==button_status_send){

        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                status_image.setImageBitmap(thumbnail);
            }
        }
    }
}