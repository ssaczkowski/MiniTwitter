package com.ssaczkowski.minitwitter.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ssaczkowski.minitwitter.R;
import com.ssaczkowski.minitwitter.common.Constant;
import com.ssaczkowski.minitwitter.common.SharedPreferencesManager;
import com.ssaczkowski.minitwitter.model.Like;
import com.ssaczkowski.minitwitter.model.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Tweet}.
 */
public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {

    private Context ctx;
    private List<Tweet> mValues;
    private String mUsername;

    public MyTweetRecyclerViewAdapter(List<Tweet> items,Context context) {
        mValues = items;
        ctx = context;
        mUsername = SharedPreferencesManager.getSomeStringValue(Constant.PREF_USERNAME);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tweet_f_listragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mValues != null){
            holder.mItem = mValues.get(position);
            holder.mTvUsername.setText("@" + holder.mItem.getUser().getUsername());
            holder.mTvMessage.setText(holder.mItem.getMensaje());
            holder.mTvLikesCount.setText(String.valueOf(holder.mItem.getLikes().size()));

            String photo = holder.mItem.getUser().getPhotoUrl();
            if(!photo.equals("")){
                Glide.with(ctx).load("https://www.minitwitter.com/apiv1/uploads/photos/" +
                        photo).into(holder.mIvAvatar);
            }

            Glide.with(ctx).load(R.drawable.ic_baseline_favorite_border_24_black).into(holder.mIvLike);
            holder.mTvLikesCount.setTextColor(ctx.getResources().getColor(R.color.black));
            holder.mTvLikesCount.setTypeface(null, Typeface.NORMAL);

            for(Like like: holder.mItem.getLikes()){
                if (like.getUsername().equals(mUsername)){
                    Glide.with(ctx).load(R.drawable.ic_like_pink).into(holder.mIvLike);
                    holder.mTvLikesCount.setTextColor(ctx.getResources().getColor(R.color.pink));
                    holder.mTvLikesCount.setTypeface(null, Typeface.BOLD);
                    break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if(mValues != null)
            return mValues.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mIvAvatar;
        public final ImageView mIvLike;
        public final TextView mTvUsername;
        public final TextView mTvMessage;
        public final TextView mTvLikesCount;
        public Tweet mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIvAvatar = (ImageView) view.findViewById(R.id.imageViewAvatar);
            mIvLike = (ImageView) view.findViewById(R.id.imageViewLike);
            mTvUsername = (TextView) view.findViewById(R.id.textViewUsername);
            mTvLikesCount = (TextView) view.findViewById(R.id.textViewLikes);
            mTvMessage = (TextView) view.findViewById(R.id.textViewMessage);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTvUsername.getText() + "'";
        }
    }

    public void setData(List<Tweet> tweetList){
        mValues = tweetList;
        notifyDataSetChanged();
    }

}