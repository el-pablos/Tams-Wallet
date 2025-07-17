package com.tamswallet.app;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Room;
import com.tamswallet.app.data.database.TamsWalletDatabase;
import com.tamswallet.app.data.repository.UserRepository;
import com.tamswallet.app.data.repository.TransactionRepository;
import com.tamswallet.app.data.repository.BudgetRepository;

public class TamsWalletApplication extends Application {
    private static TamsWalletApplication instance;
    private TamsWalletDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Apply saved theme preference
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        int nightMode = isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(nightMode);

        // Initialize Room database
        database = Room.databaseBuilder(getApplicationContext(),
                TamsWalletDatabase.class, "tamswallet_database")
                .build();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        // Shutdown all repository executors to prevent memory leaks
        try {
            UserRepository.getInstance(this).shutdown();
            TransactionRepository.getInstance(this).shutdown();
            BudgetRepository.getInstance(this).shutdown();
        } catch (Exception e) {
            // Log error but don't crash the app during shutdown
            android.util.Log.e("TamsWallet", "Error shutting down repositories", e);
        }

        // Close database
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // Handle low memory situations
        System.gc(); // Suggest garbage collection
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        // Handle different memory pressure levels
        switch (level) {
            case TRIM_MEMORY_RUNNING_MODERATE:
            case TRIM_MEMORY_RUNNING_LOW:
            case TRIM_MEMORY_RUNNING_CRITICAL:
                // App is running but system is low on memory
                System.gc();
                break;

            case TRIM_MEMORY_UI_HIDDEN:
                // App's UI is no longer visible
                break;

            case TRIM_MEMORY_BACKGROUND:
            case TRIM_MEMORY_MODERATE:
            case TRIM_MEMORY_COMPLETE:
                // App is in background and system needs memory
                break;
        }
    }

    public static TamsWalletApplication getInstance() {
        return instance;
    }

    public TamsWalletDatabase getDatabase() {
        return database;
    }
}