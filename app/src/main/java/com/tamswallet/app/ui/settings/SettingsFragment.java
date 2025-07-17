package com.tamswallet.app.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.button.MaterialButton;
import com.tamswallet.app.R;
import com.tamswallet.app.ui.auth.SplashActivity;
import com.tamswallet.app.utils.SessionManager;

public class SettingsFragment extends Fragment {
    
    private CardView cardProfile, cardTheme, cardSecurity, cardBackup, cardExport;
    private Switch switchDarkMode, switchBiometric;
    private MaterialButton btnBackupCsv, btnBackupJson, btnExport, btnReset, btnLogout;
    private TextView tvUserName, tvUserEmail;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        
        sessionManager = new SessionManager(getContext());
        
        initViews(view);
        setupClickListeners();
        loadSettings();
        
        return view;
    }

    private void initViews(View view) {
        cardProfile = view.findViewById(R.id.cardProfile);
        cardTheme = view.findViewById(R.id.cardTheme);
        cardSecurity = view.findViewById(R.id.cardSecurity);
        cardBackup = view.findViewById(R.id.cardBackup);
        cardExport = view.findViewById(R.id.cardExport);
        
        switchDarkMode = view.findViewById(R.id.switchDarkMode);
        switchBiometric = view.findViewById(R.id.switchBiometric);
        
        btnBackupCsv = view.findViewById(R.id.btnBackupCsv);
        btnBackupJson = view.findViewById(R.id.btnBackupJson);
        btnExport = view.findViewById(R.id.btnExport);
        btnReset = view.findViewById(R.id.btnReset);
        btnLogout = view.findViewById(R.id.btnLogout);
        
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
    }

    private void setupClickListeners() {
        cardProfile.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ProfileActivity.class));
        });

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save dark mode preference
            SharedPreferences prefs = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            prefs.edit().putBoolean("dark_mode", isChecked).apply();

            // Apply theme change
            int nightMode = isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
            AppCompatDelegate.setDefaultNightMode(nightMode);

            // Restart activity to apply theme
            if (getActivity() != null) {
                getActivity().recreate();
            }
        });

        switchBiometric.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // TODO: Implement biometric setting
            // Enable/disable biometric authentication
        });

        btnBackupCsv.setOnClickListener(v -> {
            // TODO: Implement CSV backup functionality
            exportData("csv");
        });

        btnBackupJson.setOnClickListener(v -> {
            // TODO: Implement JSON backup functionality
            exportData("json");
        });

        btnExport.setOnClickListener(v -> {
            // TODO: Show export options dialog
            showExportDialog();
        });

        btnReset.setOnClickListener(v -> {
            // TODO: Show confirmation dialog for reset
            showResetConfirmationDialog();
        });
        
        btnLogout.setOnClickListener(v -> {
            showLogoutConfirmationDialog();
        });
    }

    private void loadSettings() {
        // Load user information
        tvUserName.setText(sessionManager.getUserName());
        tvUserEmail.setText(sessionManager.getUserEmail());

        // Load dark mode preference
        SharedPreferences prefs = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        switchDarkMode.setChecked(isDarkMode);

        // Load biometric setting
        switchBiometric.setChecked(sessionManager.isBiometricEnabled());
    }

    private void exportData(String format) {
        // TODO: Implement data export functionality
        // Use FileProvider to share exported files
    }

    private void showExportDialog() {
        // TODO: Create and show dialog with export options
    }

    private void showResetConfirmationDialog() {
        // TODO: Create and show confirmation dialog for data reset
    }
    
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Logout")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    performLogout();
                })
                .setNegativeButton("Batal", null)
                .show();
    }
    
    private void performLogout() {
        sessionManager.logout();
        
        // Navigate to splash screen
        Intent intent = new Intent(getContext(), SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}