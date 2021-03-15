package com.ssaczkowski.minitwitter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private TextView textViewSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.buttonLogin);
        textViewSignUp = findViewById(R.id.textViewSignUp);

        btnLogin.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.buttonLogin:
                break;
            case R.id.textViewSignUp:
                goToSignUp();
                break;
        }
    }

    private void goToSignUp() {
        Intent i = new Intent(MainActivity.this,SignUpActivity.class);
        startActivity(i);
        finish();
    }
}