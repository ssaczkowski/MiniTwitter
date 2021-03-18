package com.ssaczkowski.minitwitter.retrofit;

import com.ssaczkowski.minitwitter.common.Constant;
import com.ssaczkowski.minitwitter.common.SharedPreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = SharedPreferencesManager.getSomeStringValue(Constant.PREF_TOKEN);

        Request request = chain.request().newBuilder()
                .addHeader("Authorization",token).build();
        
        return chain.proceed(request);
    }
}
