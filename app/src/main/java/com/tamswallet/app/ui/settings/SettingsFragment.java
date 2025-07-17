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
import android.widget.Toast;
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

        // Dark mode listener is set up in setupDarkModeListener() method

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
        try {
            // Load user information
            String userName = sessionManager.getUserName();
            String userEmail = sessionManager.getUserEmail();

            tvUserName.setText(userName != null ? userName : "User");
            tvUserEmail.setText(userEmail != null ? userEmail : "user@example.com");

            // Load dark mode preference with error handling
            SharedPreferences prefs = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            boolean isDarkMode = prefs.getBoolean("dark_mode", false);

            // Set switch state without triggering listener
            switchDarkMode.setOnCheckedChangeListener(null);
            switchDarkMode.setChecked(isDarkMode);

            // Re-attach listener after setting state
            setupDarkModeListener();

            // Load biometric setting
            switchBiometric.setChecked(sessionManager.isBiometricEnabled());

            android.util.Log.d("SettingsFragment", "Settings loaded - Dark mode: " + isDarkMode);

        } catch (Exception e) {
            android.util.Log.e("SettingsFragment", "Error loading settings: " + e.getMessage());
            // Set default values on error
            tvUserName.setText("User");
            tvUserEmail.setText("user@example.com");
            switchDarkMode.setChecked(false);
            switchBiometric.setChecked(false);
        }
    }

    private void setupDarkModeListener() {
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            try {
                // Save dark mode preference
                SharedPreferences prefs = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("dark_mode", isChecked);
                editor.apply();

                // Log theme change for debugging
                android.util.Log.d("SettingsFragment", "Dark mode changed to: " + isChecked);

                // Apply theme change
                int nightMode = isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
                AppCompatDelegate.setDefaultNightMode(nightMode);

                // Show feedback to user
                String message = isChecked ? "Mode gelap diaktifkan" : "Mode terang diaktifkan";
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                // Restart activity to apply theme with delay to ensure preference is saved
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        new android.os.Handler().postDelayed(() -> {
                            getActivity().recreate();
                        }, 100);
                    });
                }
            } catch (Exception e) {
                android.util.Log.e("SettingsFragment", "Error changing theme: " + e.getMessage());
                Toast.makeText(getContext(), "Gagal mengubah tema", Toast.LENGTH_SHORT).show();
                // Revert switch state
                buttonView.setChecked(!isChecked);
            }
        });
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