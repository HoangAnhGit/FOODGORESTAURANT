package com.example.foodgorestaurant.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodgorestaurant.Network.TokenManager;
import com.example.foodgorestaurant.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // 2 giây
    private ImageView logoImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        logoImage = findViewById(R.id.logoImage);


        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logoImage.startAnimation(fadeIn);


        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isUserLoggedIn()) {

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {

                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, SPLASH_DURATION);
    }

    private boolean isUserLoggedIn() {
        TokenManager tokenManager = TokenManager.getInstance(this);
        String token = tokenManager.getToken();
        return token != null && !token.isEmpty();
    }
}