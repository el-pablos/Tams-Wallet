package com.tamswallet.app.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.tamswallet.app.R;

public class SettingsFragment extends Fragment {
    
    private CardView cardProfile, cardTheme, cardSecurity, cardBackup, cardExport;
    private Switch switchDarkMode, switchBiometric;
    private MaterialButton btnBackupCsv, btnBackupJson, btnExport, btnReset;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        
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
    }

    private void setupClickListeners() {
        cardProfile.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ProfileActivity.class));
        });

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // TODO: Implement theme switching
            // Save preference and restart activity with new theme
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
    }

    private void loadSettings() {
        // TODO: Load saved settings from SharedPreferences
        // Set switch states based on saved preferences
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
}