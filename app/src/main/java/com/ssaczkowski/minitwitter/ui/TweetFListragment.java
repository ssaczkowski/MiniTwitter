package com.ssaczkowski.minitwitter.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssaczkowski.minitwitter.R;
import com.ssaczkowski.minitwitter.model.Tweet;

import java.util.List;


public class TweetFListragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private RecyclerView mRecyclerView;
    private MyTweetRecyclerViewAdapter mAdapter;
    private List<Tweet> mItems;

    public TweetFListragment(List<Tweet> items) {
        mItems = items;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TweetFListragment() {
    }


    @SuppressWarnings("unused")
    public static TweetFListragment newInstance(int columnCount) {
        TweetFListragment fragment = new TweetFListragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_f_listragment_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            loadTweetData();
        }
        return view;
    }

    private void loadTweetData() {
        mAdapter = new MyTweetRecyclerViewAdapter(mItems,getContext());
        mRecyclerView.setAdapter(mAdapter);
    }
}