package com.ssaczkowski.minitwitter.ui.tweets;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.ssaczkowski.minitwitter.R;
import com.ssaczkowski.minitwitter.common.Constant;
import com.ssaczkowski.minitwitter.data.TweetViewModel;

public class BottomModalTweetFragment extends BottomSheetDialogFragment {

    private TweetViewModel mViewModel;
    private int mIdTweet = -1;

    public static BottomModalTweetFragment newInstance(int idTweet) {
        BottomModalTweetFragment f = new BottomModalTweetFragment();
        Bundle args =   new Bundle();
        args.putInt(Constant.ARG_TWEET_ID, idTweet);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            mIdTweet = getArguments().getInt(Constant.ARG_TWEET_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_modal_tweet_fragment, container, false);

        final NavigationView nav = view.findViewById(R.id.navigation_view_bottom_tweet);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.action_delete_tweet){
                    if(mIdTweet != -1) {
                        mViewModel.deleteTweet(mIdTweet);
                    }
                    getDialog().dismiss();
                }

                return false;
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);
    }

}