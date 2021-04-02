package com.ssaczkowski.minitwitter.data;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ssaczkowski.minitwitter.model.Tweet;
import com.ssaczkowski.minitwitter.ui.BottomModalTweetFragment;

import java.util.List;

public class TweetViewModel  extends AndroidViewModel {

    private TweetRepository tweetRepository;
    private MutableLiveData<List<Tweet>> tweets;
    private MutableLiveData<List<Tweet>> favTweets;

    public TweetViewModel(@NonNull Application application) {
        super(application);
        tweetRepository = new TweetRepository();
        tweets = tweetRepository.getAllTweets();
    }

    public void openDialogTweetMenu(Context ctx, int idTweet){
        BottomModalTweetFragment dialog = BottomModalTweetFragment.newInstance(idTweet);
        dialog.show(((AppCompatActivity) ctx).getSupportFragmentManager(),
                "BottomModalTweetFragment");
    }

    public LiveData<List<Tweet>> getTweets() {
        return tweets;
    }

    public LiveData<List<Tweet>> getFavTweets() {
        favTweets = tweetRepository.getFavsTweets();
        return favTweets;
    }

    public LiveData<List<Tweet>> getNewTweets() {
        tweets = tweetRepository.getAllTweets();
        return tweets;
    }

    public LiveData<List<Tweet>> getNewFavTweets() {
        getNewTweets();
        return getFavTweets();
    }

    public void insertTweet(String message) {
        tweetRepository.createTweet(message);
    }

    public void deleteTweet(int idTweet) {
        tweetRepository.deleteTweet(idTweet);
    }

    public void likeTweet(int idTweet) {
        tweetRepository.likeTweet(idTweet);
    }

}
