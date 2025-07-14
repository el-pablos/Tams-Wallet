package com.tamswallet.app;

import android.app.Application;
import androidx.room.Room;
import com.tamswallet.app.data.database.TamsWalletDatabase;

public class TamsWalletApplication extends Application {
    private static TamsWalletApplication instance;
    private TamsWalletDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        
        // TODO: Initialize Room database
        database = Room.databaseBuilder(getApplicationContext(),
                TamsWalletDatabase.class, "tamswallet_database")
                .build();
    }

    public static TamsWalletApplication getInstance() {
        return instance;
    }

    public TamsWalletDatabase getDatabase() {
        return database;
    }
}