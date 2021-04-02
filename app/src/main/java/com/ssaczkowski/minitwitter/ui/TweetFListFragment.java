package com.ssaczkowski.minitwitter.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssaczkowski.minitwitter.R;
import com.ssaczkowski.minitwitter.common.Constant;
import com.ssaczkowski.minitwitter.data.TweetViewModel;
import com.ssaczkowski.minitwitter.model.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetFListFragment extends Fragment {

    private int mTweetListType = 1;
    private RecyclerView mRecyclerView;
    private MyTweetRecyclerViewAdapter mAdapter;
    private List<Tweet> mItems = new ArrayList<>();
    private TweetViewModel tweetViewModel;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    public TweetFListFragment(List<Tweet> items) {
        mItems = items;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TweetFListFragment() {
    }


    @SuppressWarnings("unused")
    public static TweetFListFragment newInstance(int tweetListType) {
        TweetFListFragment fragment = new TweetFListFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.TWEET_LIST_TYPE, tweetListType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweetViewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);

        if (getArguments() != null) {
            mTweetListType = getArguments().getInt(Constant.TWEET_LIST_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_f_listragment_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        mRecyclerView = view.findViewById(R.id.list);
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue));

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mSwipeRefreshLayout.setRefreshing(true);
                        if (mTweetListType == Constant.TWEET_LIST_ALL) {
                            loadNewTweetData();
                        } else if(mTweetListType == Constant.TWEET_LIST_FAVS){
                            loadNewTweetFavData();
                        }
                    }
                }
        );

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));


        mAdapter = new MyTweetRecyclerViewAdapter(mItems, getContext());
        mRecyclerView.setAdapter(mAdapter);

        if (mTweetListType == Constant.TWEET_LIST_ALL) {
            loadTweetData();
        } else if(mTweetListType == Constant.TWEET_LIST_FAVS){
            loadTweetFavData();
        }
        return view;
    }

    private void loadNewTweetFavData() {
        tweetViewModel.getNewFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                mItems  = tweets;
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.setData(mItems);
                tweetViewModel.getNewFavTweets().removeObserver(this);
            }
        });
    }

    private void loadTweetFavData() {
        tweetViewModel.getFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                mItems  = tweets;
                mAdapter.setData(mItems);
            }
        });
    }

    private void loadTweetData() {
        tweetViewModel.getTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                mItems = tweets;
                mAdapter.setData(mItems);
            }
        });
    }

    private void loadNewTweetData() {
        tweetViewModel.getNewTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                mItems = tweets;
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.setData(mItems);
                tweetViewModel.getNewTweets().removeObserver(this);
            }
        });
    }
}