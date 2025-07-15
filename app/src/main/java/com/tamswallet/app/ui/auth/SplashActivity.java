package com.tamswallet.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.tamswallet.app.R;
import com.tamswallet.app.ui.dashboard.MainActivity;
import com.tamswallet.app.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {
    
    private static final int SPLASH_DELAY = 2000; // 2 seconds
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this);
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if user is already logged in
                if (sessionManager.isLoggedIn()) {
                    // User is logged in, go to main activity
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    // User is not logged in, go to onboarding
                    startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
                }
                finish();
            }
        }, SPLASH_DELAY);
    }
}