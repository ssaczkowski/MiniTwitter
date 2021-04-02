package com.ssaczkowski.minitwitter.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ssaczkowski.minitwitter.R;
import com.ssaczkowski.minitwitter.common.Constant;
import com.ssaczkowski.minitwitter.common.SharedPreferencesManager;
import com.ssaczkowski.minitwitter.retrofit.MiniTwitterClient;
import com.ssaczkowski.minitwitter.retrofit.MiniTwitterService;
import com.ssaczkowski.minitwitter.retrofit.request.RequestLogin;
import com.ssaczkowski.minitwitter.retrofit.response.ResponseAuth;
import com.ssaczkowski.minitwitter.ui.dashboard.DashboardActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private TextView textViewSignUp;
    private EditText etEmail, etPassword;
    private MiniTwitterClient miniTwitterClient;
    private MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        retrofitInit();

        findViews();

        events();

    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }


    private void findViews() {
        btnLogin = findViewById(R.id.buttonLogin);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        etEmail = findViewById(R.id.editTextTextEmailAddress);
        etPassword = findViewById(R.id.editTextPassword);
    }

    private void events() {
        btnLogin.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.buttonLogin:
                goToLogin();
                break;
            case R.id.textViewSignUp:
                goToSignUp();
                break;
        }
    }

    private void goToLogin() {

        String pass = etPassword.getText().toString();
        String email = etEmail.getText().toString();

        if(TextUtils.isEmpty(email)){
            etEmail.setError(getString(R.string.email_mandatory));
        } else if(TextUtils.isEmpty(pass)){
            etPassword.setError(getString(R.string.pass_mandatory));
        } else {
            RequestLogin requestLogin = new RequestLogin(email,pass);

            Call<ResponseAuth> call = miniTwitterService.doLogin(requestLogin);
            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Session successfully started.",
                                Toast.LENGTH_LONG);

                        SharedPreferencesManager
                                .setSomeStringValue(Constant.PREF_TOKEN,response.body().getToken());
                        SharedPreferencesManager
                                .setSomeStringValue(Constant.PREF_USERNAME,response.body().getUsername());
                        SharedPreferencesManager
                                .setSomeStringValue(Constant.PREF_EMAIL,response.body().getEmail());
                        SharedPreferencesManager
                                .setSomeStringValue(Constant.PREF_PHOTOURL,response.body().getPhotoUrl());
                        SharedPreferencesManager
                                .setSomeStringValue(Constant.PREF_CREATED,response.body().getCreated());
                        SharedPreferencesManager
                                .setSomeBooleanValue(Constant.PREF_ACTIVE,response.body().getActive());

                        Intent i = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "There was an error, check your details.",
                                Toast.LENGTH_LONG);
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "We cannot assist you at this time, please try again later.",
                            Toast.LENGTH_LONG);
                }
            });
        }

    }

    private void goToSignUp() {

        Intent i = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(i);
        finish();
    }

}