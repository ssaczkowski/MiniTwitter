package com.ssaczkowski.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.ssaczkowski.minitwitter.common.Constant;
import com.ssaczkowski.minitwitter.common.MyApp;
import com.ssaczkowski.minitwitter.common.SharedPreferencesManager;
import com.ssaczkowski.minitwitter.retrofit.AuthTwitterClient;
import com.ssaczkowski.minitwitter.retrofit.AuthTwitterService;
import com.ssaczkowski.minitwitter.retrofit.request.RequestUserProfile;
import com.ssaczkowski.minitwitter.retrofit.response.ResponseUploadPhoto;
import com.ssaczkowski.minitwitter.retrofit.response.ResponseUserProfile;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    AuthTwitterService authTwitterService;
    AuthTwitterClient authTwitterClient;
    MutableLiveData<ResponseUserProfile> userProfile;
    MutableLiveData<String> photoProfile;

    public ProfileRepository() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        userProfile = getProfile();
        if(photoProfile == null)
            photoProfile = new MutableLiveData<>();
    }

    public MutableLiveData<ResponseUserProfile> getProfile(){
        if(userProfile == null){
            userProfile = new MutableLiveData<>();
        }

        Call<ResponseUserProfile> call = authTwitterService.getProfile();
        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if(response.isSuccessful()){
                    userProfile.setValue(response.body());
                }else{
                    Toast.makeText(MyApp.getContext(), "There was an error, check your details.",
                            Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "We cannot assist you at this time, please try again later.",
                        Toast.LENGTH_LONG);
            }
        });

        return userProfile;
    }

    public void updateProfile(RequestUserProfile requestUserProfile){
        if(userProfile == null){
            userProfile = new MutableLiveData<>();
        }

        Call<ResponseUserProfile> call = authTwitterService.updateProfile(requestUserProfile);
        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if(response.isSuccessful()){
                    userProfile.setValue(response.body());
                }else{
                    Toast.makeText(MyApp.getContext(), "There was an error, check your details.",
                            Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "We cannot assist you at this time, please try again later.",
                        Toast.LENGTH_LONG);
            }
        });

    }

    public void uploadPhoto(String photoPath){
        File file = new File(photoPath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        Call<ResponseUploadPhoto> call = authTwitterService.uploadPhoto(requestBody);

        call.enqueue(new Callback<ResponseUploadPhoto>() {
            @Override
            public void onResponse(Call<ResponseUploadPhoto> call, Response<ResponseUploadPhoto> response) {
                if(response.isSuccessful()){
                    SharedPreferencesManager.setSomeStringValue(Constant.PREF_PHOTOURL,response.body().getFilename());
                    photoProfile.setValue(response.body().getFilename());
                }else{
                    Toast.makeText(MyApp.getContext(), "There was an error, check your details.",
                            Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<ResponseUploadPhoto> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "We cannot assist you at this time, please try again later.",
                        Toast.LENGTH_LONG);
            }
        });
    }

}
