package com.ssaczkowski.minitwitter.ui.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.ssaczkowski.minitwitter.R;
import com.ssaczkowski.minitwitter.common.Constant;
import com.ssaczkowski.minitwitter.common.SharedPreferencesManager;
import com.ssaczkowski.minitwitter.ui.auth.SignUpActivity;
import com.ssaczkowski.minitwitter.ui.tweets.NewTweetDialogFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class DashboardActivity extends AppCompatActivity implements PermissionListener {

    private FloatingActionButton floatingActionButton;
    private ImageView ivAvatarToolbar;
    private NavController mNavController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if(getIntent() != null){
            String action = getIntent().getAction();
            try {
                if(action != null && action.equals(Intent.ACTION_VIEW)){
                    Uri data = getIntent().getData();
                    if (data != null){
                        data.getLastPathSegment();
                    }
                }
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }

        ivAvatarToolbar = findViewById(R.id.imageViewToolbarPhoto);
        floatingActionButton = findViewById(R.id.fab);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_tweets_like, R.id.navigation_profile)
                .build();
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, appBarConfiguration);

        NavigationUI.setupWithNavController(navView, mNavController);

        navView.setOnNavigationItemSelectedListener(item -> checkItem(item));

        getSupportActionBar().hide();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewTweetDialogFragment dialog = new NewTweetDialogFragment();
                dialog.show(getSupportFragmentManager(),"NewTweetDialogFragment");
            }
        });

        String photoUrl = SharedPreferencesManager.getSomeStringValue(Constant.PREF_PHOTOURL);
        if(!photoUrl.isEmpty()) {
            Glide.with(this)
                    .load(Constant.API_MINITWITTER_FILES_URL + photoUrl)
                    .dontAnimate()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivAvatarToolbar);
        }

    }

    private boolean checkItem(MenuItem item) {
        if(item.getItemId() == R.id.navigation_home){
            Bundle args = new Bundle();
            args.putInt(Constant.TWEET_LIST_TYPE,Constant.TWEET_LIST_ALL);
            mNavController.navigate(item.getItemId(), args);
            floatingActionButton.show();

        } else if(item.getItemId() == R.id.navigation_tweets_like){
            Bundle args = new Bundle();
            args.putInt(Constant.TWEET_LIST_TYPE,Constant.TWEET_LIST_FAVS);
            mNavController.navigate(item.getItemId(), args);
            floatingActionButton.hide();
        } else if(item.getItemId() == R.id.navigation_profile){
            mNavController.navigate(item.getItemId());
            floatingActionButton.hide();
        }

        return false;
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
        //We invoke the photo selection from the gallery

    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
        Toast.makeText(DashboardActivity.this, "Photo cannot be selected",Toast.LENGTH_LONG);
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

    }
}