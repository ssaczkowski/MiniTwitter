package com.ssaczkowski.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.ssaczkowski.minitwitter.common.MyApp;
import com.ssaczkowski.minitwitter.retrofit.AuthTwitterClient;
import com.ssaczkowski.minitwitter.retrofit.AuthTwitterService;
import com.ssaczkowski.minitwitter.retrofit.request.RequestUserProfile;
import com.ssaczkowski.minitwitter.retrofit.response.ResponseUserProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    AuthTwitterService authTwitterService;
    AuthTwitterClient authTwitterClient;
    MutableLiveData<ResponseUserProfile> userProfile;

    public ProfileRepository() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        userProfile = getProfile();
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

}
