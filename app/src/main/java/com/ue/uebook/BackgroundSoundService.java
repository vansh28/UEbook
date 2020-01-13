package com.ue.uebook;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class BackgroundSoundService extends Service {
    private static final String TAG = "BackgroundSoundService";
    Context context;
    MediaPlayer player;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = context;
        player = MediaPlayer.create(this, R.raw.oneplus_tune);
        player.setLooping(true);
        player.setVolume(100,100);
    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int flags, int startId) {

        player.start();
        return 1;

    }

    public void onStart(Intent intent, int startId) {

        Log.e("Star", "Music is start now");
        // TO DO
    }
    public IBinder onUnBind(Intent arg0) {

        return null;
    }

    public void onStop() {
        player.stop();
        Log.e("Stop", "Music is Stop now");

    }
    public void onPause() {
        player.pause();

        Log.e("Pause", "Music is Pause now");
    }
    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() {
        Log.e("onLowMemory", "Music is onLowMemory");

    }
}
