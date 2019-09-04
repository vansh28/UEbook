package com.ue.uebook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SessionManager {

    private static final String SHARED_PREFERENCE_NAME = "com.ue.uebook.sharedPreference";
    private static final String userimage = "userImage";
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context mCtx) {
        if (sharedPreference == null) {
            sharedPreference = mCtx.getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
            editor = sharedPreference.edit();
        }
    }

    public void storeUserImage(final String userImage) {
        editor.putString(userimage, userImage);
        editor.commit();
    }

    public String getUserimage() {
        return sharedPreference.getString(userimage, "");
    }

}
