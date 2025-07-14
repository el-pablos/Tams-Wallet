package com.tamswallet.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tamswallet.app.R;

public class OnboardingActivity extends AppCompatActivity {
    
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private MaterialButton btnNext, btnSkip;
    
    // TODO: Create OnboardingAdapter for ViewPager2
    // private OnboardingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        initViews();
        setupViewPager();
        setupClickListeners();
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);
    }

    private void setupViewPager() {
        // TODO: Initialize adapter with onboarding slides
        // adapter = new OnboardingAdapter(this);
        // viewPager.setAdapter(adapter);
        
        // TODO: Connect TabLayout with ViewPager2 (after adapter is set)
        // new TabLayoutMediator(tabLayout, viewPager,
        //         (tab, position) -> {
        //             // Tab configuration will be handled by adapter
        //         }).attach();
    }

    private void setupClickListeners() {
        btnNext.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            // TODO: Check if this is the last slide
            if (currentItem < 2) { // Assuming 3 slides (0, 1, 2)
                viewPager.setCurrentItem(currentItem + 1);
            } else {
                goToLogin();
            }
        });

        btnSkip.setOnClickListener(v -> goToLogin());
    }

    private void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}