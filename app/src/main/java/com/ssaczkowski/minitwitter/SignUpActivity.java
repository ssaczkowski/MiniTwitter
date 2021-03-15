package com.ssaczkowski.minitwitter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignUp;
    private TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSignUp = findViewById(R.id.buttonSignUp);
        textViewLogin = findViewById(R.id.textViewGoToLogin);

        btnSignUp.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.buttonSignUp:
                break;
            case R.id.textViewGoToLogin:
                goToLogin();
                break;
        }
    }

    private void goToLogin() {
        Intent i = new Intent(SignUpActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }

}