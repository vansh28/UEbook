package com.ue.uebook.ChatSdk;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.ChatSdk.Adapter.GroupChatAdapter;
import com.ue.uebook.ChatSdk.Adapter.GroupListAdapter;
import com.ue.uebook.ChatSdk.Adapter.GroupMemberListAdapter;
import com.ue.uebook.ChatSdk.Pojo.GroupHistoryResponse;
import com.ue.uebook.ChatSdk.Pojo.GroupMemberList;
import com.ue.uebook.ChatSdk.Pojo.GroupNameProfileResponse;
import com.ue.uebook.ChatSdk.Pojo.Grouplist;
import com.ue.uebook.ChatSdk.Pojo.MemberListResponse;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.FilePath;
import com.ue.uebook.FileUtil;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.ImageUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;
import com.ue.uebook.WebviewScreen;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class GroupMessageScreen extends BaseActivity implements View.OnClickListener, ImageUtils.ImageAttachmentListener, GroupMemberListAdapter.GroupMemberItemClick, GroupListAdapter.ItemClick, GroupChatAdapter.ItemClick {
  private Intent intent;
  private static final int REQUEST_PICK_VIDEO = 12;
  private String groupID;
  private EmojiconEditText edit_chat_message;
  private ImageButton   emojiBtn,videobtncall, voicebtn, button_chat_send, backbtnMessage, button_chat_attachment, gallerybtn, audiobtn, videobtn, filebtn;
  private TextView group_name;
  private RecyclerView messageList;
  private BottomSheetDialog mBottomSheetDialog;
  private GroupChatAdapter groupChatAdapter;
  ImageUtils imageUtils;
  private ImageView previewImage, image_user_chat;
  private static final int MY_PERMISSIONS_REQUEST_CAMERA = 99;
  private File videofile, audioUrl, docfile;
  private int mCurrentPosition = 0;
  private String filePath;
  private String fileName;
  private File imageurl;
  private Bitmap bitmap;
  private int typevalue = 0;
  private VideoView videoview;
  private ImageButton callBtn, cancelBtn,morebtn;
  private ListView listView;
  private GroupMemberListAdapter groupMemberListAdapter;
  private List<String> memberForcall;
  private String userid = "";
  private String memberid = "";
  private String groupname = "";
  private List<GroupMemberList> groupMemberLists;
  private List<Grouplist> grouplists;
  private String groupImg = "";
  private String  channelID ="";
  private BroadcastReceiver mReceiver;
  EmojIconActions emojIcon;
  private RelativeLayout root_view;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_group_message_screen);
    button_chat_attachment = findViewById(R.id.button_chat_attachment);
    image_user_chat = findViewById(R.id.image_user_chat);
    morebtn=findViewById(R.id.morebtn);
    emojiBtn= findViewById(R.id.emojibtn);
    root_view=findViewById(R.id.root_view);
    emojiBtn.setOnClickListener(this);
    morebtn.setOnClickListener(this);
    image_user_chat.setOnClickListener(this);
    intent = getIntent();
    imageUtils = new ImageUtils(this);
    memberForcall = new ArrayList<>();
    groupMemberLists = new ArrayList<>();
    grouplists = new ArrayList<>();
    previewImage = findViewById(R.id.previewImage);
    videobtncall = findViewById(R.id.videobtn);
    videobtncall.setOnClickListener(this);
    voicebtn = findViewById(R.id.voicebtn);
    voicebtn.setOnClickListener(this);
    videoview = findViewById(R.id.videoview);
    groupID = intent.getStringExtra("groupid");
    channelID=intent.getStringExtra("channel_id");
    // groupImg = intent.getStringExtra("groupimg");
    backbtnMessage = findViewById(R.id.backbtnMessage);
    group_name = findViewById(R.id.group_name);
    group_name.setOnClickListener(this);
    messageList = findViewById(R.id.messageList);
    //  groupname=intent.getStringExtra("name");
    //   group_name.setText(intent.getStringExtra("name"));
    backbtnMessage.setOnClickListener(this);
    edit_chat_message = findViewById(R.id.edit_chat_message);
    button_chat_send = findViewById(R.id.button_chat_send);
    button_chat_send.setOnClickListener(this);
    button_chat_attachment.setOnClickListener(this);
    LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
    linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
    messageList.setLayoutManager(linearLayoutManagerPopularList);
    messageList.setNestedScrollingEnabled(false);
    if (channelID!=null){
      getGroupHistory(new SessionManager(getApplicationContext()).getUserID(), channelID);
      getGroupMember(new SessionManager(getApplicationContext()).getUserID(), channelID, "", "");
      getGroupNameImage(channelID, new SessionManager(getApplicationContext()).getUserID());
      groupID=channelID;
    }
    else {
      getGroupHistory(new SessionManager(getApplicationContext()).getUserID(), groupID);
      getGroupMember(new SessionManager(getApplicationContext()).getUserID(), groupID, "", "");
      getGroupNameImage(groupID, new SessionManager(getApplicationContext()).getUserID());
    }
    emojIcon = new EmojIconActions(this, root_view, edit_chat_message, emojiBtn);
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
    if (v == backbtnMessage) {
      finish();
    } else if (v == button_chat_send) {
      if (typevalue == 0) {

        if (edit_chat_message.getText().toString().isEmpty()) {
          edit_chat_message.setError("Enter your Message");
          edit_chat_message.requestFocus();
        } else {
          sendMesaage(groupID, new SessionManager(getApplicationContext()).getUserID(), "text", edit_chat_message.getText().toString(), 0);
        }

      } else if (typevalue == 1) {
        previewImage.setVisibility(View.GONE);
        sendMesaage(groupID, new SessionManager(getApplicationContext()).getUserID(), "image", edit_chat_message.getText().toString(), 1);
      } else if (typevalue == 2) {
        if (videofile != null) {
          videoview.setVisibility(View.GONE);
          sendMesaage(groupID, new SessionManager(getApplicationContext()).getUserID(), "video", edit_chat_message.getText().toString(), 2);
        } else {
          Toast.makeText(this, "Please Select Again", Toast.LENGTH_SHORT).show();
        }
      }
    } else if (v == button_chat_attachment) {
      showBottomSheet();
    } else if (v == gallerybtn) {

      PackageManager pm = this.getPackageManager();
      int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, this.getPackageName());
      if (hasPerm == PackageManager.PERMISSION_GRANTED) {
        typevalue = 1;
        mBottomSheetDialog.dismiss();
        imageUtils.imagepicker(1);
      } else {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS_REQUEST_CAMERA);
      }
    } else if (v == audiobtn) {

      typevalue = 3;
      getAudioFile();
      mBottomSheetDialog.dismiss();
    } else if (v == videobtn) {
      typevalue = 2;
      mBottomSheetDialog.dismiss();
      try {
        Intent mediaChooser = new Intent(Intent.ACTION_PICK);
        mediaChooser.setType("video/*");
        startActivityForResult(mediaChooser, REQUEST_PICK_VIDEO);
      } catch (ActivityNotFoundException e) {
        // Do nothing for now
      }
    } else if (v == filebtn) {
      typevalue = 4;
      openFile(111);
      mBottomSheetDialog.dismiss();

    } else if (v == voicebtn) {
      showBottomListSheet("audiocall");
    } else if (v == videobtncall) {
      showBottomListSheet("videocall");
    } else if (v == group_name) {
      Intent intent = new Intent(this, GroupDetailScreen.class);
      intent.putExtra("name", groupname);
      intent.putExtra("member", (Serializable) groupMemberLists);
      intent.putExtra("groupid", groupID);
      intent.putExtra("groupimg", groupImg);
      startActivity(intent);
    } else if (v == image_user_chat) {
      Intent intent = new Intent(this, GroupDetailScreen.class);
      intent.putExtra("name", groupname);
      intent.putExtra("member", (Serializable) groupMemberLists);
      intent.putExtra("groupid", groupID);
      intent.putExtra("groupimg", groupImg);
      startActivity(intent);
    }
    else if (v==morebtn){
      showPopupmenu();
    }
  }

  private void showBottomSheet() {
    final View bottomSheetLayout = getLayoutInflater().inflate(R.layout.bottomlayoutchat, null);
    mBottomSheetDialog = new BottomSheetDialog(this);
    mBottomSheetDialog.setContentView(bottomSheetLayout);
    gallerybtn = mBottomSheetDialog.findViewById(R.id.galleryBtn);
    filebtn = mBottomSheetDialog.findViewById(R.id.fileBtn);
    audiobtn = mBottomSheetDialog.findViewById(R.id.audioBtn);
    videobtn = mBottomSheetDialog.findViewById(R.id.videoBtn);
    gallerybtn.setOnClickListener(this);
    filebtn.setOnClickListener(this);
    audiobtn.setOnClickListener(this);
    videobtn.setOnClickListener(this);
    mBottomSheetDialog.show();
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private void sendMesaage(String group_id, String send_by, String message_type, String message, int typeval) {
    OkHttpClient client = new OkHttpClient();
    String url = ApiRequest.BaseUrl + "groupsChat";
    final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    showLoadingIndicator();
    RequestBody requestBody;
    switch (typeval) {
      case 0:
        requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(" group_id", group_id)
                .addFormDataPart(" send_by", send_by)
                .addFormDataPart(" message_type", message_type)
                .addFormDataPart(" message", message)
                .addFormDataPart(" sender_name", new SessionManager(getApplicationContext()).getUserName())
                .build();
        break;
      case 1:
        requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(" group_id", group_id)
                .addFormDataPart(" send_by", send_by)
                .addFormDataPart(" message_type", message_type)
                .addFormDataPart(" message", message)
                .addFormDataPart(" sender_name", new SessionManager(getApplicationContext()).getUserName())
                .addFormDataPart("image_file", imageurl.getName(), RequestBody.create(MEDIA_TYPE_PNG, imageurl))
                .build();
        break;
      case 2:
        requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(" group_id", group_id)
                .addFormDataPart(" send_by", send_by)
                .addFormDataPart(" message_type", message_type)
                .addFormDataPart(" message", message)
                .addFormDataPart(" sender_name", new SessionManager(getApplicationContext()).getUserName())
                .addFormDataPart("video_file", videofile.getName(), RequestBody.create(MediaType.parse("video/mp4"), videofile))
                .build();
        break;
      case 3:
        requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(" group_id", group_id)
                .addFormDataPart(" send_by", send_by)
                .addFormDataPart(" message_type", message_type)
                .addFormDataPart(" message", message)
                .addFormDataPart(" sender_name", new SessionManager(getApplicationContext()).getUserName())
                .addFormDataPart("audio_file", audioUrl.getName(), RequestBody.create(MediaType.parse("audio/*"), audioUrl))
                .build();
        break;
      case 4:
        requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(" group_id", group_id)
                .addFormDataPart(" send_by", send_by)
                .addFormDataPart(" message_type", message_type)
                .addFormDataPart(" message", message)
                .addFormDataPart(" sender_name", new SessionManager(getApplicationContext()).getUserName())
                .addFormDataPart("pdf_file", docfile.getName(), RequestBody.create(MediaType.parse("text/csv"), docfile))
                .build();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + 1);
    }
    Request request = new Request.Builder()
            .url(url)
            .post(requestBody)
            .build();
    client.newCall(request).enqueue(new Callback() {

      @Override
      public void onFailure(Call call, IOException e) {
        call.cancel();
      }

      @Override
      public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
        hideLoadingIndicator();
        final String myResponse = response.body().string();
        Gson gson = new GsonBuilder().create();
        Log.e("chatresponse", myResponse);
//                final ChatResponse form = gson.fromJson(myResponse, ChatResponse.class);
        runOnUiThread(new Runnable() {
          @RequiresApi(api = Build.VERSION_CODES.KITKAT)
          @Override
          public void run() {
            edit_chat_message.setText("");
            getGroupHistory(new SessionManager(getApplicationContext()).getUserID(), groupID);
//                        typevalue = 0;
//                        chanelID = form.getChat_list().get(0).getChannelId();
//                        getChatHistory(new SessionManager(getApplication()).getUserID(), sendToID, chanelID, "text");
          }
        });
      }
    });
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private void getGroupHistory(String user_id, String groupID) {
    ApiRequest request = new ApiRequest();
    request.requestforgetGroupMessage(user_id, groupID, new okhttp3.Callback() {
      @Override
      public void onFailure(okhttp3.Call call, IOException e) {
        Log.d("error", "error");
      }

      @Override
      public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
        final String myResponse = response.body().string();
        Gson gson = new GsonBuilder().create();
        final GroupHistoryResponse form = gson.fromJson(myResponse, GroupHistoryResponse.class);
        if (form.getError() == false && form.getData() != null) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              groupChatAdapter = new GroupChatAdapter(GroupMessageScreen.this, form.getData(), new SessionManager(getApplicationContext()).getUserID());
              messageList.setAdapter(groupChatAdapter);
              messageList.scrollToPosition(form.getData().size() - 1);
              groupChatAdapter.notifyDataSetChanged();
              groupChatAdapter.setItemClickListener(GroupMessageScreen.this);
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
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    imageUtils.request_permission_result(requestCode, permissions, grantResults);
  }

  public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
    this.bitmap = file;
    this.fileName = filename;
    String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
    imageUtils.createImage(file, filename, path, false);
    filePath = getRealPathFromURI(uri.getPath());
    imageurl = (new File(imageUtils.getPath(uri)));
    previewImage.setVisibility(View.VISIBLE);
    previewImage.setImageBitmap(file);
  }

  private void getAudioFile() {
    try {
      Intent audioIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
      startActivityForResult(audioIntent, 132);
    } catch (ActivityNotFoundException e) {

    }
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

  private void openFile(int CODE) {
    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
    i.setType("*/*");
    String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "application/pdf", "application/vnd.ms-powerpoint", "text/csv"};
    i.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
    startActivityForResult(i, CODE);
  }

  private void imagePreview(String file) {
    final Dialog previewDialog = new Dialog(this);
    previewDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    previewDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout
            , null));
    ImageView imageView = previewDialog.findViewById(R.id.image_view);
    GlideUtils.loadImage(GroupMessageScreen.this, ApiRequest.BaseUrl + "upload/" + file, imageView, R.drawable.user_default, R.drawable.user_default);
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
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    imageUtils.onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      if (requestCode == 132) {

        Uri selectedaudio = data.getData();
        try {
          String audioPathStr = FilePath.getPath(GroupMessageScreen.this, selectedaudio);
          audioUrl = new File(audioPathStr);
          sendMesaage(groupID, new SessionManager(getApplicationContext()).getUserID(), "audio", edit_chat_message.getText().toString(), 3);
        } catch (Exception e) {
          e.printStackTrace();
          Toast.makeText(this, "Please Select Again", Toast.LENGTH_SHORT).show();
        }
      } else if (requestCode == REQUEST_PICK_VIDEO) {
        Uri selectedVideo = data.getData();
        try {
          String videoPathStr = getPath(selectedVideo);
          videofile = new File(videoPathStr);
          initializePlayer(selectedVideo);
          videoview.setVisibility(View.VISIBLE);
        } catch (Exception e) {
          e.printStackTrace();
          Toast.makeText(this, "Please Select Again", Toast.LENGTH_SHORT).show();
        }
      } else if (requestCode == 111) {
        Uri selectedfile = data.getData();
        try {
          docfile = FileUtil.from(GroupMessageScreen.this, selectedfile);
          sendMesaage(groupID, new SessionManager(getApplicationContext()).getUserID(), "docfile", edit_chat_message.getText().toString(), 4);
        } catch (Exception e) {
          e.printStackTrace();
          Toast.makeText(this, "Please Select Again", Toast.LENGTH_SHORT).show();
        }
      }
    }
  }

  public String getPath(Uri uri) {
    String[] projection = {MediaStore.Video.Media.DATA};
    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
    if (cursor != null) {
      int column_index = cursor
              .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
      cursor.moveToFirst();
      return cursor.getString(column_index);
    } else
      return null;
  }

  private void initializePlayer(Uri uri) {
    if (uri != null) {
      videoview.setVideoURI(uri);
    }
    videoview.setOnPreparedListener(
            new MediaPlayer.OnPreparedListener() {
              @Override
              public void onPrepared(MediaPlayer mediaPlayer) {
                if (mCurrentPosition > 0) {
                  videoview.seekTo(mCurrentPosition);
                } else {
                  videoview.seekTo(1);
                }
                // Start playing!
                videoview.start();
              }
            });
  }

  private void releasePlayer() {
    videoview.stopPlayback();
  }

  private void gotoWebview(String url) {
    Intent intent = new Intent(this, WebviewScreen.class);
    intent.putExtra("url", url);
    startActivity(intent);
  }

  private void showBottomListSheet(String calltype) {
    final View bottomSheetLayout = getLayoutInflater().inflate(R.layout.bottomsheetgroup, null);
    mBottomSheetDialog = new BottomSheetDialog(this);
    mBottomSheetDialog.setContentView(bottomSheetLayout);
    listView = mBottomSheetDialog.findViewById(R.id.groupmemberList);
    callBtn = mBottomSheetDialog.findViewById(R.id.callBtn);
    cancelBtn = mBottomSheetDialog.findViewById(R.id.cancelBtn);
    if (calltype.equalsIgnoreCase("audiocall")) {
      callBtn.setBackgroundResource(R.drawable.phone);
    } else {
      callBtn.setBackgroundResource(R.drawable.videoc);
    }
    cancelBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mBottomSheetDialog.dismiss();
        memberForcall.clear();

      }
    });

    groupMemberListAdapter = new GroupMemberListAdapter(GroupMessageScreen.this, groupMemberLists);
    groupMemberListAdapter.setItemClickListener(GroupMessageScreen.this);
    listView.setAdapter(groupMemberListAdapter);

    callBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        for (String s : memberForcall) {
          if (memberid == "") {
            memberid = s;
          } else {
            memberid = memberid + "," + s;
          }
        }

        if (memberid != "") {
          if (calltype.equalsIgnoreCase("audiocall")) {
            callFunc(true, memberid);
          } else {
            callFunc(false, memberid);
          }
        } else {
          Toast.makeText(GroupMessageScreen.this, "Please Add member", Toast.LENGTH_SHORT).show();
        }

      }
    });

//        listView.setOnTouchListener(new ListView.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        // Disallow NestedScrollView to intercept touch events.
//                        v.getParent().requestDisallowInterceptTouchEvent(true);
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                        // Allow NestedScrollView to intercept touch events.
//                        v.getParent().requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//
//
//                // Handle ListView touch events.
//                v.onTouchEvent(event);
//                return true;
//            }
//        });


    mBottomSheetDialog.show();
  }


  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private void getGroupMember(String user_id, String groupID, String add_mem_id_in_group, String action) {
    ApiRequest request = new ApiRequest();
    if (groupMemberLists.size() > 0)
      groupMemberLists.clear();
    request.requestforgetGroupMember(user_id, groupID, add_mem_id_in_group, action, new okhttp3.Callback() {
      @Override
      public void onFailure(okhttp3.Call call, IOException e) {
        Log.d("error", "error");


      }

      @Override
      public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

        final String myResponse = response.body().string();
        Gson gson = new GsonBuilder().create();
        final MemberListResponse form = gson.fromJson(myResponse, MemberListResponse.class);
        if (form.getError() == false && form.getUser_list() != null) {
//
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              for (int i = 0; i < form.getUser_list().size(); i++) {
                if (form.getUser_list().get(i).getId().equalsIgnoreCase(new SessionManager(getApplicationContext()).getUserID())) {

                } else {
                  groupMemberLists.add(form.getUser_list().get(i));
                }
              }


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
  public void ontItemClick(GroupMemberList groupMemberList, int position, int id) {
    if (groupMemberList != null) {
      Log.e("pod", String.valueOf(position));
      if (id == 1) {
        memberForcall.add(groupMemberList.getId());


      } else if (id == 2) {
        if (memberForcall.size() > 0) {
          memberForcall.remove(groupMemberList.getId());
        }

      }

    }
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private void sendNotificationForCall(String user_id, String groupID, String type, String memberid) {
    ApiRequest request = new ApiRequest();
    request.requestforCallGroupMemberNotification(user_id, groupID, memberid, type, new okhttp3.Callback() {
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

      }
    });
  }

  private void callFunc(Boolean audio, String membersId) {
    URL serverURL;
    try {
      serverURL = new URL("https://meet.jit.si");
    } catch (MalformedURLException e) {
      e.printStackTrace();
      throw new RuntimeException("Invalid server URL!");
    }
    JitsiMeetConferenceOptions
            defaultOptions = new JitsiMeetConferenceOptions.Builder()
            .setServerURL(serverURL)
            .setWelcomePageEnabled(false)
            .setAudioOnly(audio)
            .build();
    JitsiMeet.setDefaultConferenceOptions(defaultOptions);

    if (audio == true) {
      sendNotificationForCall(new SessionManager(getApplicationContext()).getUserID(), groupID, "audioCall", membersId);
    } else {
      sendNotificationForCall(new SessionManager(getApplicationContext()).getUserID(), groupID, "videoCall", membersId);
    }
    if (memberForcall.size() > 0) {
      // Build options object for joining the conference. The SDK will merge the default
      // one we set earlier and this one when joining.
      JitsiMeetConferenceOptions options
              = new JitsiMeetConferenceOptions.Builder()
              .setRoom(groupID)
              .build();
      // Launch the new activity with the given options. The launch() method takes care
      // of creating the required Intent and passing the options.
      JitsiMeetActivity.launch(this, options);
      finish();
    }
  }



  @Override
  protected void onRestart() {
    super.onRestart();
    Log.d("lifecycle", "onRestart invoked");

    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
      @Override
      public void run() {
        //Do something here
        getGroupMember(new SessionManager(getApplicationContext()).getUserID(), groupID, "", "");
        getGroupNameImage(groupID, new SessionManager(getApplicationContext()).getUserID());
      }
    }, 1000);

  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private void getGroupNameImage(String groupID, String user_id) {
    ApiRequest request = new ApiRequest();
    request.requestforGetGroupNameImage(groupID, user_id, new okhttp3.Callback() {
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
              if (!GroupMessageScreen.this.isFinishing()) {
                GlideUtils.loadImage(GroupMessageScreen.this, "http://" + form.getGroup_detail().getGroup_image(), image_user_chat, R.drawable.user_default, R.drawable.user_default);
              }
              if (form.getGroup_detail().getName().length() > 10) {

                group_name.setText(form.getGroup_detail().getName().substring(0, 10) + "...");

              } else {
                group_name.setText(form.getGroup_detail().getName());
              }
              groupImg = "http://" + form.getGroup_detail().getGroup_image();
              groupname = form.getGroup_detail().getName();
            }
          });
        }
      }
    });
  }

  public String getFirst10Words(String arg) {
    Pattern pattern = Pattern.compile("([\\S]+\\s*){1,3}");
    Matcher matcher = pattern.matcher(arg);
    matcher.find();
    return matcher.group();
  }

  protected void onDestroy() {
    super.onDestroy();

  }
  @Override
  public void onResume() {
    super.onResume();
    IntentFilter intentFilter = new IntentFilter(
            "android.intent.action.MAIN");

    mReceiver = new BroadcastReceiver() {

      @RequiresApi(api = Build.VERSION_CODES.KITKAT)
      @Override
      public void onReceive(Context context, Intent intent) {
        //extract our message from intent
        String msg_for_me = intent.getStringExtra("some_msg");
        //log our message value
        Log.i("InchooTutorial", msg_for_me);
        if (channelID!=null){
          getGroupHistory(new SessionManager(getApplicationContext()).getUserID(), channelID);
        }
        else {
          getGroupHistory(new SessionManager(getApplicationContext()).getUserID(), groupID);
        }
      }
    };
    //registering our receiver
    this.registerReceiver(mReceiver, intentFilter);
  }

  @Override
  public void onPause() {
    // TODO Auto-generated method stub
    super.onPause();
    //unregister our receiver
    this.unregisterReceiver(this.mReceiver);
  }

  @Override
  public void ongroupListItemClick(Grouplist grouplist) {

  }

  @Override
  public void onGroupMessage(View view,String chatID, int position) {
    showFilterPopup(view ,chatID);

  }

  private void showFilterPopup(View v ,String chatID ) {
    PopupMenu popup = new PopupMenu(this, v);

    Log.e("chatid",chatID);
    Log.e("groupid",groupID);
    popup.inflate(R.menu.deletepopup);
    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
          case R.id.delete:
            Toast.makeText(GroupMessageScreen.this,"deleted",Toast.LENGTH_SHORT).show();

            if (channelID!=null){
              deleteMessage(channelID,new SessionManager(getApplicationContext()).getUserID(),chatID,"selected");
            }
            else {
              deleteMessage(groupID,new SessionManager(getApplicationContext()).getUserID(),chatID,"selected");

            }
          default:
            return false;
        }
      }
    });
    popup.show();
  }

  private void showPopupmenu() {
    PopupMenu popup = new PopupMenu(GroupMessageScreen.this, morebtn);
    popup.getMenuInflater()
            .inflate(R.menu.chatmoremenu, popup.getMenu());
    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.KITKAT)
      public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
          case R.id.clearChat:
            return true;

          default:
            return false;
        }
      }
    });
    popup.show();
  }
  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private void deleteMessage( String groupID ,String user_id , String chatId ,String action) {
    ApiRequest request = new ApiRequest();
    request.requestfordeleteMessage( groupID ,user_id, chatId,action ,new okhttp3.Callback() {
      @Override
      public void onFailure(okhttp3.Call call, IOException e) {
        Log.d("error", "error");
        hideLoadingIndicator();
      }

      @Override
      public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
        hideLoadingIndicator();
        final String myResponse = response.body().string();
        Log.e("response",myResponse);
        Gson gson = new GsonBuilder().create();
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            if (channelID!=null){
              getGroupHistory(new SessionManager(getApplicationContext()).getUserID(), channelID);
            }
            else {
              getGroupHistory(new SessionManager(getApplicationContext()).getUserID(), groupID);
            }
          }
        });

      }
    });
  }



}


//_listView.setOnTouchListener(new ListView.OnTouchListener() {
//@Override
//public boolean onTouch(View v, MotionEvent event) {
//        int action = event.getAction();
//        switch (action) {
//        case MotionEvent.ACTION_DOWN:
//        // Disallow NestedScrollView to intercept touch events.
//        v.getParent().requestDisallowInterceptTouchEvent(true);
//        break;
//
//        case MotionEvent.ACTION_UP:
//        // Allow NestedScrollView to intercept touch events.
//        v.getParent().requestDisallowInterceptTouchEvent(false);
//        break;
//        }
//
//        // Handle ListView touch events.
//        v.onTouchEvent(event);
//        return true;
//        }
//        });