package com.ssaczkowski.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ssaczkowski.minitwitter.common.MyApp;
import com.ssaczkowski.minitwitter.model.Tweet;
import com.ssaczkowski.minitwitter.retrofit.AuthTwitterClient;
import com.ssaczkowski.minitwitter.retrofit.AuthTwitterService;
import com.ssaczkowski.minitwitter.retrofit.request.RequestCreateTweet;
import com.ssaczkowski.minitwitter.ui.MyTweetRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    AuthTwitterService authTwitterService;
    AuthTwitterClient authTwitterClient;
    MutableLiveData<List<Tweet>> allTweets;

    public TweetRepository() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        allTweets = getAllTweets();
    }

    public MutableLiveData<List<Tweet>> getAllTweets(){
        if(allTweets == null){
            allTweets = new MutableLiveData<>();
        }

        Call<List<Tweet>> call = authTwitterService.getAllTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if(response.isSuccessful()){
                    getAllTweets().setValue(response.body());

                }else{
                    Toast.makeText(MyApp.getContext(), "There was an error, check your details.",
                            Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "We cannot assist you at this time, please try again later.",
                        Toast.LENGTH_LONG);
            }
        });

        return getAllTweets();
    }

    public void createTweet(String message){
        RequestCreateTweet requestCreateTweet = new RequestCreateTweet(message);
        Call<Tweet> call = authTwitterService.createTweet(requestCreateTweet);

        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {

                if(response.isSuccessful()){
                    List<Tweet> clonedList = new ArrayList<>();
                    clonedList.add(response.body());
                    for(int i = 0 ; i < allTweets.getValue().size(); i++){
                        clonedList.add(new Tweet(allTweets.getValue().get(i)));
                    }
                    allTweets.setValue(clonedList);
                } else {
                    Toast.makeText(MyApp.getContext(), "There was an error, check your details.",
                            Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "We cannot assist you at this time, please try again later.",
                        Toast.LENGTH_LONG);
            }
        });

    }

}
