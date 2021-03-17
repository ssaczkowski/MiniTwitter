package com.ssaczkowski.minitwitter.retrofit;

import com.ssaczkowski.minitwitter.retrofit.model.RequestLogin;
import com.ssaczkowski.minitwitter.retrofit.model.RequestSignup;
import com.ssaczkowski.minitwitter.retrofit.model.ResponseAuth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MiniTwitterService {


    @POST("/auth/login")
    Call<ResponseAuth> doLogin(@Body RequestLogin requestLogin);

    @POST("/auth/signup")
    Call<ResponseAuth> doLogin(@Body RequestSignup requestSignup);

}
