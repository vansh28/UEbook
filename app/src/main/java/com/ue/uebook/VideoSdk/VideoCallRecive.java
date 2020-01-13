package com.ue.uebook.VideoSdk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ue.uebook.BackgroundSoundService;
import com.ue.uebook.R;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class VideoCallRecive extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    private String channeld=" ";
    private String  receiverid="";
    private Button answerbtn,cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call_recive);
        answerbtn=findViewById(R.id.answer);
        cancelBtn=findViewById(R.id.cancel);

        answerbtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        intent = getIntent();
        channeld=intent.getStringExtra("id");

        URL serverURL;

        try {
            serverURL = new URL("https://meet.jit.si");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }
        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);
    }

    @Override
    public void onClick(View v) {
        if (v==cancelBtn){
            stopService(new Intent(VideoCallRecive.this, BackgroundSoundService.class));
            finish();
        }
        else if (v==answerbtn){
            stopService(new Intent(VideoCallRecive.this, BackgroundSoundService.class));
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
    }
    @Override
    protected void onResume() {
        startService(new Intent(VideoCallRecive.this, BackgroundSoundService.class));
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(VideoCallRecive.this, BackgroundSoundService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(VideoCallRecive.this, BackgroundSoundService.class));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(VideoCallRecive.this, BackgroundSoundService.class));
    }

}
