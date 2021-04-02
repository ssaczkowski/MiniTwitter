package com.ssaczkowski.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.ssaczkowski.minitwitter.common.Constant;
import com.ssaczkowski.minitwitter.common.MyApp;
import com.ssaczkowski.minitwitter.common.SharedPreferencesManager;
import com.ssaczkowski.minitwitter.model.Like;
import com.ssaczkowski.minitwitter.model.Tweet;
import com.ssaczkowski.minitwitter.retrofit.AuthTwitterClient;
import com.ssaczkowski.minitwitter.retrofit.AuthTwitterService;
import com.ssaczkowski.minitwitter.retrofit.request.RequestCreateTweet;
import com.ssaczkowski.minitwitter.retrofit.response.TweetDeleted;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    AuthTwitterService authTwitterService;
    AuthTwitterClient authTwitterClient;
    MutableLiveData<List<Tweet>> allTweets;
    MutableLiveData<List<Tweet>> favTweets;
    String userName;

    public TweetRepository() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        allTweets = getAllTweets();
        userName = SharedPreferencesManager.getSomeStringValue(Constant.PREF_USERNAME);
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

        return allTweets;
    }

    public MutableLiveData<List<Tweet>> getFavsTweets(){
        if(favTweets == null){
            favTweets = new MutableLiveData<>();
        }
        List<Tweet> newFavList = new ArrayList<>();
        Iterator itTweets = allTweets.getValue().iterator();

        while (itTweets.hasNext()){
            Tweet current = (Tweet) itTweets.next();
            Iterator itLikes = current.getLikes().iterator();

            boolean finded = false;
            while (itLikes.hasNext() && !finded){
                Like like = (Like) itLikes.next();
                if( like.getUsername().equals(userName)){
                    finded = true;
                    newFavList.add(current);
                }
            }
        }

        favTweets.setValue(newFavList);

        return favTweets;
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

    public void likeTweet(int idTweet){

        Call<Tweet> call = authTwitterService.likeTweet(idTweet);

        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {

                if(response.isSuccessful()){
                    List<Tweet> clonedList = new ArrayList<>();

                    for(int i = 0 ; i < allTweets.getValue().size(); i++){
                        if(allTweets.getValue().get(i).getId() == response.body().getId())
                            clonedList.add(response.body());
                        else
                            clonedList.add(new Tweet(allTweets.getValue().get(i)));
                    }
                    allTweets.setValue(clonedList);
                    getFavsTweets();
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

    public void deleteTweet(final int idTweet){
        Call<TweetDeleted> call = authTwitterService.deleteTweet(idTweet);

        call.enqueue(new Callback<TweetDeleted>() {
            @Override
            public void onResponse(Call<TweetDeleted> call, Response<TweetDeleted> response) {

                if(response.isSuccessful()){

                    List<Tweet> clonedTweets = new ArrayList<>();
                    for(int i = 0 ; i < allTweets.getValue().size(); i++){
                        if(allTweets.getValue().get(i).getId() != idTweet)
                            clonedTweets.add(new Tweet(allTweets.getValue().get(i)));
                    }

                    allTweets.setValue(clonedTweets);
                    getFavsTweets();

                } else {
                    Toast.makeText(MyApp.getContext(), "There was an error, check your details.",
                            Toast.LENGTH_LONG);
                }

            }

            @Override
            public void onFailure(Call<TweetDeleted> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "We cannot assist you at this time, please try again later.",
                        Toast.LENGTH_LONG);
            }
        });
    }

}
