package com.ue.uebook.VoiceCall;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.R;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class VoiceCallReceive extends AppCompatActivity implements View.OnClickListener{
    Intent intent;
    private String channeld=" ";
    private String  receiverid="";
    private ImageView videoCall_receive ,videoCall_cancel;
    private MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call_receive);
        videoCall_receive=findViewById(R.id.videoCall_receive);
        videoCall_cancel=findViewById(R.id.videoCall_cancel);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mp = MediaPlayer.create(getApplicationContext(), notification);
        mp.start();
        videoCall_cancel.setOnClickListener(this);
        videoCall_receive.setOnClickListener(this);
        intent = getIntent();
        channeld=intent.getStringExtra("id");

        URL serverURL;
        try {

            serverURL = new URL("https://meet.jit.si");

        } catch (MalformedURLException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }
        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setWelcomePageEnabled(false)
                .setAudioOnly(true)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);
    }
    @Override
    public void onClick(View v) {
        if (v==videoCall_cancel){
            mp.stop();
            finish();
        }
        else if (v==videoCall_receive){
            mp.stop();
            if (channeld.length() > 0) {
                // Build options object for joining the conference. The SDK will merge the default
                // one we set earlier and this one when joining.
                JitsiMeetConferenceOptions options
                        = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(channeld)
                        .build();
                // Launch the new activity with the given options. The launch() method takes care
                //                // of creating the required Intent and passing the options.
                JitsiMeetActivity.launch(this, options);
                finish();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mp.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mp.stop();


    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();

    }

}
