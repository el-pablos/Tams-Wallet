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
import androidx.biometric.BiometricManager;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.button.MaterialButton;
import com.tamswallet.app.R;
import com.tamswallet.app.data.repository.TransactionRepository;
import com.tamswallet.app.data.repository.BudgetRepository;
import com.tamswallet.app.ui.auth.SplashActivity;
import com.tamswallet.app.utils.DataExportUtils;
import com.tamswallet.app.utils.SessionManager;

public class SettingsFragment extends Fragment {
    
    private CardView cardProfile, cardTheme, cardSecurity, cardBackup, cardExport;
    private Switch switchDarkMode, switchBiometric;
    private MaterialButton btnBackupCsv, btnBackupJson, btnExport, btnReset, btnLogout;
    private TextView tvUserName, tvUserEmail;
    private SessionManager sessionManager;
    private TransactionRepository transactionRepository;
    private BudgetRepository budgetRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        
        sessionManager = new SessionManager(getContext());
        transactionRepository = TransactionRepository.getInstance(getContext());
        budgetRepository = BudgetRepository.getInstance(getContext());

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
            handleBiometricToggle(isChecked);
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
                    androidx.fragment.app.FragmentActivity activity = getActivity();
                    activity.runOnUiThread(() -> {
                        new android.os.Handler().postDelayed(() -> {
                            // Check if activity is still valid before recreating
                            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                                activity.recreate();
                            }
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

    private void handleBiometricToggle(boolean isEnabled) {
        try {
            // Check if biometric authentication is available
            BiometricManager biometricManager = BiometricManager.from(getContext());

            if (isEnabled) {
                // User wants to enable biometric authentication
                switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
                    case BiometricManager.BIOMETRIC_SUCCESS:
                        // Biometric is available, save the setting
                        sessionManager.setBiometricEnabled(true);
                        Toast.makeText(getContext(), "Autentikasi biometrik diaktifkan", Toast.LENGTH_SHORT).show();
                        android.util.Log.d("SettingsFragment", "Biometric authentication enabled");
                        break;

                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                        // No biometric hardware available
                        switchBiometric.setChecked(false);
                        Toast.makeText(getContext(), "Perangkat tidak mendukung autentikasi biometrik", Toast.LENGTH_LONG).show();
                        android.util.Log.w("SettingsFragment", "No biometric hardware available");
                        break;

                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                        // Biometric hardware unavailable
                        switchBiometric.setChecked(false);
                        Toast.makeText(getContext(), "Autentikasi biometrik tidak tersedia saat ini", Toast.LENGTH_LONG).show();
                        android.util.Log.w("SettingsFragment", "Biometric hardware unavailable");
                        break;

                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                        // No biometric enrolled
                        switchBiometric.setChecked(false);
                        Toast.makeText(getContext(), "Belum ada biometrik yang terdaftar di perangkat", Toast.LENGTH_LONG).show();
                        android.util.Log.w("SettingsFragment", "No biometric enrolled");
                        break;

                    default:
                        // Other error
                        switchBiometric.setChecked(false);
                        Toast.makeText(getContext(), "Tidak dapat mengaktifkan autentikasi biometrik", Toast.LENGTH_LONG).show();
                        android.util.Log.e("SettingsFragment", "Unknown biometric error");
                        break;
                }
            } else {
                // User wants to disable biometric authentication
                sessionManager.setBiometricEnabled(false);
                Toast.makeText(getContext(), "Autentikasi biometrik dinonaktifkan", Toast.LENGTH_SHORT).show();
                android.util.Log.d("SettingsFragment", "Biometric authentication disabled");
            }

        } catch (Exception e) {
            android.util.Log.e("SettingsFragment", "Error handling biometric toggle: " + e.getMessage());
            switchBiometric.setChecked(false);
            sessionManager.setBiometricEnabled(false);
            Toast.makeText(getContext(), "Terjadi kesalahan saat mengatur biometrik", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportData(String format) {
        try {
            long userId = sessionManager.getUserId();
            if (userId == -1) {
                Toast.makeText(getContext(), "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            android.util.Log.d("SettingsFragment", "Starting export in format: " + format);

            if ("csv".equals(format)) {
                exportTransactionsToCSV();
            } else if ("json".equals(format)) {
                exportAllDataToJSON();
            } else {
                Toast.makeText(getContext(), "Format tidak didukung", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            android.util.Log.e("SettingsFragment", "Error exporting data: " + e.getMessage());
            Toast.makeText(getContext(), "Terjadi kesalahan saat mengekspor data", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportTransactionsToCSV() {
        long userId = sessionManager.getUserId();

        transactionRepository.getTransactionsByUserId(userId).observe(this, transactions -> {
            if (transactions != null && !transactions.isEmpty()) {
                DataExportUtils.exportTransactionsToCSV(getContext(), transactions, new DataExportUtils.ExportCallback() {
                    @Override
                    public void onSuccess(java.io.File file) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Transaksi berhasil diekspor ke CSV", Toast.LENGTH_SHORT).show();

                                // Show share dialog
                                new AlertDialog.Builder(getContext())
                                    .setTitle("Ekspor Berhasil")
                                    .setMessage("File CSV telah dibuat: " + file.getName() + "\n\nApakah Anda ingin membagikannya?")
                                    .setPositiveButton("Bagikan", (dialog, which) -> {
                                        DataExportUtils.shareFile(getContext(), file);
                                    })
                                    .setNegativeButton("OK", null)
                                    .show();
                            });
                        }
                    }

                    @Override
                    public void onError(String error) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Gagal mengekspor: " + error, Toast.LENGTH_LONG).show();
                            });
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), "Tidak ada transaksi untuk diekspor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void exportAllDataToJSON() {
        long userId = sessionManager.getUserId();

        transactionRepository.getTransactionsByUserId(userId).observe(this, transactions -> {
            budgetRepository.getBudgetsByUserId(userId).observe(this, budgets -> {
                if (transactions != null && budgets != null) {
                    DataExportUtils.exportAllDataToJSON(getContext(), transactions, budgets, new DataExportUtils.ExportCallback() {
                        @Override
                        public void onSuccess(java.io.File file) {
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    Toast.makeText(getContext(), "Data berhasil diekspor ke JSON", Toast.LENGTH_SHORT).show();

                                    // Show share dialog
                                    new AlertDialog.Builder(getContext())
                                        .setTitle("Ekspor Berhasil")
                                        .setMessage("File JSON telah dibuat: " + file.getName() + "\n\nApakah Anda ingin membagikannya?")
                                        .setPositiveButton("Bagikan", (dialog, which) -> {
                                            DataExportUtils.shareFile(getContext(), file);
                                        })
                                        .setNegativeButton("OK", null)
                                        .show();
                                });
                            }
                        }

                        @Override
                        public void onError(String error) {
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    Toast.makeText(getContext(), "Gagal mengekspor: " + error, Toast.LENGTH_LONG).show();
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Tidak ada data untuk diekspor", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void showExportDialog() {
        String[] options = {"Ekspor Transaksi (CSV)", "Ekspor Semua Data (JSON)"};

        new AlertDialog.Builder(getContext())
            .setTitle("Pilihan Ekspor")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        exportData("csv");
                        break;
                    case 1:
                        exportData("json");
                        break;
                }
            })
            .setNegativeButton("Batal", null)
            .show();
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