package com.ue.uebook.Data;

import com.ue.uebook.LoginActivity.Pojo.LoginBody;
import com.ue.uebook.LoginActivity.Pojo.LoginResponse;
import com.ue.uebook.LoginActivity.Pojo.RegistrationBody;
import com.ue.uebook.LoginActivity.Pojo.RegistrationResponse;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface NetworkAPI {
    @POST("createUser")
    Call<RegistrationResponse> userRegistration(@Body RegistrationBody registrationBody) ;
    @Multipart
    @POST("userLogin")
    Call<LoginResponse> userLogin( @Body LoginBody loginBody) ;



}
