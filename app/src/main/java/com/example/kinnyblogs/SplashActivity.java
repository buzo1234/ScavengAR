package com.example.kinnyblogs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.*;

import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, AllGames.class));
            }
        }, 1000);

    }
}