package com.ue.uebook.Data;

import com.ue.uebook.LoginActivity.Pojo.RegistrationBody;
import com.ue.uebook.LoginActivity.Pojo.RegistrationResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NetworkAPI {
    @POST("createUser")
    Call<RegistrationResponse> userRegistration(@Body RegistrationBody loginInfo) ;

}
