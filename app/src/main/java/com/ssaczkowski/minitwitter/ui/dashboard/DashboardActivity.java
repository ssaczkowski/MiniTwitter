package com.ssaczkowski.minitwitter.ui.dashboard;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.ssaczkowski.minitwitter.data.ProfileViewModel;
import com.ssaczkowski.minitwitter.data.TweetViewModel;
import com.ssaczkowski.minitwitter.retrofit.response.ResponseUserProfile;
import com.ssaczkowski.minitwitter.ui.auth.SignUpActivity;
import com.ssaczkowski.minitwitter.ui.tweets.NewTweetDialogFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class DashboardActivity extends AppCompatActivity implements PermissionListener {

    private FloatingActionButton floatingActionButton;
    private ImageView ivAvatarToolbar;
    private NavController mNavController;
    private ProfileViewModel profileViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

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

        profileViewModel.photoProfile.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String photo) {
                Glide.with(DashboardActivity.this)
                        .load(Constant.API_MINITWITTER_FILES_URL + photo)
                        .dontAnimate()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(ivAvatarToolbar);
            }
        });

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
        Intent selectPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(selectPhoto,Constant.SELECT_PHOTO_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == Constant.SELECT_PHOTO_GALLERY) {
                if(data != null){
                    Uri photoSelected = data.getData();// content://gallery/photos/..
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(photoSelected, filePathColumn,
                            null,null,null);

                    if(cursor != null){
                        cursor.moveToFirst();
                        int photoIndex = cursor.getColumnIndex(filePathColumn[0]); // filename = filePathColumn[0]
                        String photoPath = cursor.getString(photoIndex);
                        profileViewModel.uploadPhoto(photoPath);
                        cursor.close();
                    }
                }
            }
        }

    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
        Toast.makeText(DashboardActivity.this, "Photo cannot be selected",Toast.LENGTH_LONG);
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

    }
}