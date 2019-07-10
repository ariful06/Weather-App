package com.weatherapp.www.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.weatherapp.www.R;
import com.weatherapp.www.model.Main;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        new Handler().postDelayed(() -> {
            startActivity(MainActivity.moveToMainActivity(SplashActivity.this));
            finish();
        }, SPLASH_SCREEN_TIME_OUT);
    }
}
