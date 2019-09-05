package com.ue.uebook.Data;

import com.ue.uebook.LoginActivity.Pojo.RegistrationBody;
import com.ue.uebook.LoginActivity.Pojo.RegistrationResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;



public class NetworkService {
    private static final String BASE_URL = "http://dnddemo.com/ebooks/api/v1/";
    private static Retrofit retrofit = null;

    public static Retrofit getAPI() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}