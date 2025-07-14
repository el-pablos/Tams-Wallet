package com.tamswallet.app.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tamswallet.app.R;
import com.tamswallet.app.ui.transaction.AddTransactionActivity;
import com.tamswallet.app.ui.history.HistoryFragment;
import com.tamswallet.app.ui.budget.BudgetFragment;
import com.tamswallet.app.ui.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity {
    
    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupBottomNavigation();
        setupFab();
        
        // Load default fragment (Dashboard)
        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment());
        }
    }

    private void initViews() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_home) {
                fragment = new DashboardFragment();
            } else if (itemId == R.id.nav_history) {
                fragment = new HistoryFragment();
            } else if (itemId == R.id.nav_budget) {
                fragment = new BudgetFragment();
            } else if (itemId == R.id.nav_settings) {
                fragment = new SettingsFragment();
            }
            
            return loadFragment(fragment);
        });
    }

    private void setupFab() {
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, AddTransactionActivity.class));
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}