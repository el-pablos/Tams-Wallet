package com.tamswallet.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.tamswallet.app.R;
import com.tamswallet.app.ui.dashboard.MainActivity;

public class SplashActivity extends AppCompatActivity {
    
    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // TODO: Check if user is already logged in
        // TODO: Check if onboarding has been completed
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // For now, always go to onboarding
                // TODO: Implement proper flow based on user state
                startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
                finish();
            }
        }, SPLASH_DELAY);
    }
}