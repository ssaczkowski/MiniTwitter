package com.ssaczkowski.minitwitter.retrofit;

import com.ssaczkowski.minitwitter.model.Tweet;
import com.ssaczkowski.minitwitter.retrofit.request.RequestCreateTweet;
import com.ssaczkowski.minitwitter.retrofit.response.TweetDeleted;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthTwitterService {

    @GET("tweets/all")
    Call<List<Tweet>> getAllTweets();

    @POST("tweets/create")
    Call<Tweet> createTweet(@Body RequestCreateTweet requestCreateTweet);

    @POST("tweets/like/{idTweet}")
    Call<Tweet> likeTweet(@Path("idTweet") int idTweet);

    @DELETE("tweets/{idTweet}")
    Call<TweetDeleted> deleteTweet(@Path("idTweet") int idTweet);

}
