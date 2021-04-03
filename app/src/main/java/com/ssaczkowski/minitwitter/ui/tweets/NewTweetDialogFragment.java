package com.ssaczkowski.minitwitter.ui.tweets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ssaczkowski.minitwitter.R;
import com.ssaczkowski.minitwitter.common.Constant;
import com.ssaczkowski.minitwitter.common.SharedPreferencesManager;
import com.ssaczkowski.minitwitter.data.TweetViewModel;


public class NewTweetDialogFragment extends DialogFragment implements View.OnClickListener {

    private ImageView ivClose,ivAvatar;
    private Button btnTwittear;
    private EditText etMessage;
    private Dialog newTweetDialog;

    public NewTweetDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_tweet_dialog, container, false);

        ivClose = view.findViewById(R.id.imageViewClose);
        ivAvatar = view.findViewById(R.id.imageViewAvatar);
        btnTwittear = view.findViewById(R.id.buttonTwittear);
        etMessage = view.findViewById(R.id.editTextMultiLineMessage);

        btnTwittear.setOnClickListener(this);
        ivClose.setOnClickListener(this);

        String photoUrl = SharedPreferencesManager.getSomeStringValue(Constant.PREF_PHOTOURL);
        if(!photoUrl.isEmpty()) {
            Glide.with(getActivity())
                    .load(Constant.API_MINITWITTER_FILES_URL + photoUrl)
                    .dontAnimate()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivAvatar);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String message = etMessage.getText().toString();

        if(id == R.id.buttonTwittear){

            if( message.isEmpty()){
                Toast.makeText(getActivity(), R.string.you_must_write,Toast.LENGTH_LONG);
            }else{
                TweetViewModel tweetViewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);
                tweetViewModel.insertTweet(message);
                getDialog().dismiss();
            }


        }else if(id == R.id.imageViewClose){
            showDialogConfirm();
        }
    }

    private void showDialogConfirm() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.delete_tweet_question)
                .setTitle(R.string.cancel_tweet);

        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                getDialog().dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}