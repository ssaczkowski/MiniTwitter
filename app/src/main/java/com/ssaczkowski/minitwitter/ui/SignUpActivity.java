package com.ssaczkowski.minitwitter.ui;

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
import com.ssaczkowski.minitwitter.retrofit.MiniTwitterClient;
import com.ssaczkowski.minitwitter.retrofit.MiniTwitterService;
import com.ssaczkowski.minitwitter.retrofit.request.RequestSignup;
import com.ssaczkowski.minitwitter.retrofit.response.ResponseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignUp;
    private TextView textViewLogin;
    private EditText etEmail, etPassword, etUsername;
    private MiniTwitterClient miniTwitterClient;
    private MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        retrofitInit();

        findViews();

        events();



    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    private void events() {
        btnSignUp.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);
    }

    private void findViews() {
        btnSignUp = findViewById(R.id.buttonSignUp);
        textViewLogin = findViewById(R.id.textViewGoToLogin);
        etEmail = findViewById(R.id.editTextTextEmailAddress);
        etPassword = findViewById(R.id.editTextPassword);
        etUsername = findViewById(R.id.editTextUserName);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.buttonSignUp:
                goToSignup();
                break;
            case R.id.textViewGoToLogin:
                goToLogin();
                break;
        }
    }

    private void goToSignup() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String email = etEmail.getText().toString();

        if(TextUtils.isEmpty(username)){
            etUsername.setError(getString(R.string.username_mandatory));
        }else if(TextUtils.isEmpty(password) || password.length() < 4){
            etPassword.setError(getString(R.string.pass_mandatory));
        }else if(TextUtils.isEmpty(email)){
            etEmail.setError(getString(R.string.email_mandatory));
        } else{
            RequestSignup requestSignup = new RequestSignup(username,email,password);
            Call<ResponseAuth> call = miniTwitterService.doSignUp(requestSignup);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){
                        Intent i = new Intent(SignUpActivity.this,DashboardActivity.class);
                        startActivity(i);
                        finish();
                    } else{
                        Toast.makeText(SignUpActivity.this, "There was an error, check your details.",Toast.LENGTH_LONG);
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "We cannot assist you at this time, please try again later.",
                            Toast.LENGTH_LONG);
                }
            });
        }

    }

    private void goToLogin() {
        Intent i = new Intent(SignUpActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }

}