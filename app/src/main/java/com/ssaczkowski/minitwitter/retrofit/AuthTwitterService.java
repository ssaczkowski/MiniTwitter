package com.ssaczkowski.minitwitter.retrofit;

import com.ssaczkowski.minitwitter.model.Tweet;
import com.ssaczkowski.minitwitter.retrofit.request.RequestCreateTweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthTwitterService {

    @GET("tweets/all")
    Call<List<Tweet>> getAllTweets();

    @POST("tweets/create")
    Call<Tweet> createTweet(RequestCreateTweet requestCreateTweet);

}
