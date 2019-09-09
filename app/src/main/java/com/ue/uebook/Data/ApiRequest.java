package com.ue.uebook.Data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.LoginActivity.Pojo.LoginResponse;
import com.ue.uebook.SessionManager;

import java.io.File;
import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiRequest {
    private static final String BaseUrl = "http://dnddemo.com/ebooks/api/v1/";

    public void requestforRegistration(final String full_name, final String password, final String email, final String publisher_type, final String gender, final String country, Callback callback) {
        String url = null;
        url = BaseUrl + "createUser";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_name", full_name)
                .addFormDataPart("password", password)
                .addFormDataPart("email", email)
                .addFormDataPart("publisher_type", publisher_type)
                .addFormDataPart("gender", gender)
                .addFormDataPart("country", country)

                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }
    public void requestforgetUserInfo( String user_id, Callback callback) {
        String url = null;
        url = BaseUrl + "getUserInfo";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id",user_id)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }


    public void requestforUpdateProfile(final String user_id , final String password, final String email, final String publisher_type, final  String country, Callback callback) {
        String url = null;
        url = BaseUrl + "userEdit";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", user_id)
                .addFormDataPart("password", password)
                .addFormDataPart("email", email)
                .addFormDataPart("publisher_type", publisher_type)
                .addFormDataPart("country", country)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public void requestforUpdateProfilePic(final String user_id ,File profile_image, Callback callback) {
        String url = null;
        url = BaseUrl + "UpdatePrfilePic";
        OkHttpClient client = new OkHttpClient();
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", user_id)
                .addFormDataPart("profile_image", profile_image.getName(), RequestBody.create(MEDIA_TYPE_PNG, profile_image))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public void requestforUploadBook(final String user_id ,File video, String category_id,String book_title,Callback callback) {
        String url = null;
        url = BaseUrl + "addNewBook";
        OkHttpClient client = new OkHttpClient();
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("*/*");
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", user_id)
                .addFormDataPart("category_id", category_id)
                .addFormDataPart("book_title", book_title)
                .addFormDataPart("video_url", video.getName(), RequestBody.create(MEDIA_TYPE_PNG, video))
                .build();

        Request request = new Request.Builder()
                .url("http://dnddemo.com/ebooks/api/v1/addNewBook")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }





}


