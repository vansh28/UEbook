package com.ue.uebook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SessionManager {
    private static final String SHARED_PREFERENCE_NAME = "com.ue.uebook.sharedPreference";
    private static final String userimage = "userImage";
    private static final String Location = "location";
    private static final String Publish = "publish";
    private static final String LoginStatus = "login";
    private static final String UserId = "UserId";
    private static final String UserName = "UserName";
    private static final String UserEmail = "UserEmail";
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
    public void storeUserLocation(final String location) {
        editor.putString(Location, location);
        editor.commit();
    }
    public String getUserLocation() {
        return sharedPreference.getString(Location, "");
    }
    public void storeUserPublishtype(final String type) {
        editor.putString(Publish, type);
        editor.commit();
    }
    public String getUserPublishType() {
        return sharedPreference.getString(Publish, "");
    }

    public void storeUserLoginStatus(final int type) {
        editor.putInt(LoginStatus, type);
        editor.commit();
    }
    public int getLoginStatus() {
        return sharedPreference.getInt(LoginStatus, 0);
    }
    public void clearUserCredentials() {
        editor.clear();
        editor.commit();
    }
    public void storeUseruserID(final String userId) {
        editor.putString(UserId, userId);
        editor.commit();
    }
    public String getUserID() {
        return sharedPreference.getString(UserId, "");
    }
    public void storeUserName(final String userId) {
        editor.putString(UserName, userId);
        editor.commit();
    }
    public String getUserName() {
        return sharedPreference.getString(UserName, "");
    }
    public void storeUserEmail(final String userEmail) {
        editor.putString(UserEmail, userEmail);
        editor.commit();
    }
    public String getUserEmail() {
        return sharedPreference.getString(UserEmail, "");
    }
    public void saveArrayList(List<String> list, String key){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public List<String> getArrayList(String key){
        Gson gson = new Gson();
        String json = sharedPreference.getString(key, null);
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

}
