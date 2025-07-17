package com.tamswallet.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.tamswallet.app.data.model.User;

public class SessionManager {
    private static final String PREF_NAME = "TamsWalletSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_BIOMETRIC_ENABLED = "biometricEnabled";
    private static final String KEY_TWO_FACTOR_ENABLED = "twoFactorEnabled";
    private static final String KEY_THEME_MODE = "themeMode";
    
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    
    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    
    public void createLoginSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putLong(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putBoolean(KEY_BIOMETRIC_ENABLED, user.isBiometricEnabled());
        editor.putBoolean(KEY_TWO_FACTOR_ENABLED, user.isTwoFactorEnabled());
        editor.apply();
    }
    
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    public long getUserId() {
        return sharedPreferences.getLong(KEY_USER_ID, -1);
    }
    
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }
    
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }
    
    public boolean isBiometricEnabled() {
        return sharedPreferences.getBoolean(KEY_BIOMETRIC_ENABLED, false);
    }
    
    public boolean isTwoFactorEnabled() {
        return sharedPreferences.getBoolean(KEY_TWO_FACTOR_ENABLED, false);
    }
    
    public void setBiometricEnabled(boolean enabled) {
        editor.putBoolean(KEY_BIOMETRIC_ENABLED, enabled);
        editor.apply();
    }
    
    public void setTwoFactorEnabled(boolean enabled) {
        editor.putBoolean(KEY_TWO_FACTOR_ENABLED, enabled);
        editor.apply();
    }
    
    public void logout() {
        editor.clear();
        editor.apply();
    }
    
    public void setThemeMode(int mode) {
        editor.putInt(KEY_THEME_MODE, mode);
        editor.apply();
    }
    
    public int getThemeMode() {
        return sharedPreferences.getInt(KEY_THEME_MODE, 0); // 0 = system default
    }
    
    public void updateUserProfile(String name, String email) {
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    public boolean hasStoredCredentials() {
        // Check if user has valid session and biometric is enabled
        return isLoggedIn() && isBiometricEnabled();
    }
}