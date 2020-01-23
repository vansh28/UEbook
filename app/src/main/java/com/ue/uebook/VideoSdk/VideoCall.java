package com.ue.uebook.VideoSdk;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class VideoCall extends JitsiMeetActivity  {

    Intent intent;
    private String channeld=" ";
    private String  receiverid="";
    String RandomAudioFileName = "abcdefghjklmno";
    Random random;
    JitsiMeetView viewf ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);
        intent = getIntent();
        random = new Random();
        channeld=intent.getStringExtra("id");

        receiverid=intent.getStringExtra("receiverid");
           if (channeld.isEmpty()){
               channeld=CreateRandomAudioFileName(5);
           }
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
              //  .setAudioOnly(true)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);

        videocall(new SessionManager(getApplicationContext()).getUserID(),receiverid,channeld);
        if (channeld.length() > 0) {
            // Build options object for joining the conference. The SDK will merge the default
            // one we set earlier and this one when joining.
            JitsiMeetConferenceOptions options
                    = new JitsiMeetConferenceOptions.Builder()
                    .setRoom(channeld)
                    .build();
            // Launch the new activity with the given options. The launch() method takes care
            // of creating the required Intent and passing the options.
            JitsiMeetActivity.launch(this, options);
            finish();
        }

    }

    public void onButoonClick(View v) {

//        String text = editText.getText().toString();


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void videocall(String user_id ,String receiver ,String channeld) {
        ApiRequest request = new ApiRequest();
        request.requestforVideoCall(user_id, receiver ,channeld,"videoCall",new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }
    public String CreateRandomAudioFileName(int string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString();
    }






}
