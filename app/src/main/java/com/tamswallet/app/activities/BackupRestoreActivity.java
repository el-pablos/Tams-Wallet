package com.tamswallet.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tamswallet.app.R;
import com.tamswallet.app.adapters.BackupFilesAdapter;
import com.tamswallet.app.utils.BackupRestoreUtils;
import com.tamswallet.app.utils.DataExportUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BackupRestoreActivity extends AppCompatActivity {
    
    private Button btnCreateBackup;
    private Button btnRestoreBackup;
    private Button btnExportData;
    private RecyclerView recyclerViewBackups;
    private BackupFilesAdapter backupFilesAdapter;
    
    private ActivityResultLauncher<Intent> restoreBackupLauncher;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_restore);
        
        initViews();
        setupRecyclerView();
        setupClickListeners();
        setupActivityResultLaunchers();
        loadBackupFiles();
    }
    
    private void initViews() {
        btnCreateBackup = findViewById(R.id.btnCreateBackup);
        btnRestoreBackup = findViewById(R.id.btnRestoreBackup);
        btnExportData = findViewById(R.id.btnExportData);
        recyclerViewBackups = findViewById(R.id.recyclerViewBackups);
    }
    
    private void setupRecyclerView() {
        backupFilesAdapter = new BackupFilesAdapter(new ArrayList<>());
        recyclerViewBackups.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBackups.setAdapter(backupFilesAdapter);
        
        backupFilesAdapter.setOnItemClickListener((backupFile, position) -> {
            showBackupOptionsDialog(backupFile);
        });
    }
    
    private void setupClickListeners() {
        btnCreateBackup.setOnClickListener(v -> createBackup());
        btnRestoreBackup.setOnClickListener(v -> selectBackupFile());
        btnExportData.setOnClickListener(v -> showExportOptions());
    }
    
    private void setupActivityResultLaunchers() {
        restoreBackupLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri backupUri = result.getData().getData();
                    if (backupUri != null) {
                        restoreFromBackup(backupUri);
                    }
                }
            }
        );
    }
    
    private void createBackup() {
        btnCreateBackup.setEnabled(false);
        btnCreateBackup.setText("Creating...");
        
        BackupRestoreUtils.createBackup(this, new BackupRestoreUtils.BackupCallback() {
            @Override
            public void onSuccess(File backupFile, int itemCount) {
                runOnUiThread(() -> {
                    btnCreateBackup.setEnabled(true);
                    btnCreateBackup.setText("Create Backup");
                    Toast.makeText(BackupRestoreActivity.this, 
                        "Backup created successfully! " + itemCount + " items backed up", 
                        Toast.LENGTH_SHORT).show();
                    loadBackupFiles();
                    
                    // Show share option
                    new AlertDialog.Builder(BackupRestoreActivity.this)
                        .setTitle("Backup Created")
                        .setMessage("Backup saved to: " + backupFile.getName() + "\n\nWould you like to share it?")
                        .setPositiveButton("Share", (dialog, which) -> {
                            DataExportUtils.shareFile(BackupRestoreActivity.this, backupFile);
                        })
                        .setNegativeButton("OK", null)
                        .show();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    btnCreateBackup.setEnabled(true);
                    btnCreateBackup.setText("Create Backup");
                    Toast.makeText(BackupRestoreActivity.this, 
                        "Error creating backup: " + error, 
                        Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    private void selectBackupFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        restoreBackupLauncher.launch(intent);
    }
    
    private void restoreFromBackup(Uri backupUri) {
        new AlertDialog.Builder(this)
            .setTitle("Restore Backup")
            .setMessage("This will replace all current data with the backup data. Are you sure?")
            .setPositiveButton("Restore", (dialog, which) -> {
                BackupRestoreUtils.restoreFromBackup(this, backupUri, new BackupRestoreUtils.BackupCallback() {
                    @Override
                    public void onSuccess(File backupFile, int itemCount) {
                        runOnUiThread(() -> {
                            Toast.makeText(BackupRestoreActivity.this, 
                                "Backup restored successfully! " + itemCount + " items restored", 
                                Toast.LENGTH_SHORT).show();
                        });
                    }
                    
                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            Toast.makeText(BackupRestoreActivity.this, 
                                "Error restoring backup: " + error, 
                                Toast.LENGTH_LONG).show();
                        });
                    }
                });
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
    
    private void showExportOptions() {
        String[] options = {"Export Transactions (CSV)", "Export Budgets (CSV)", "Export All Data (JSON)"};
        
        new AlertDialog.Builder(this)
            .setTitle("Export Options")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        exportTransactions();
                        break;
                    case 1:
                        exportBudgets();
                        break;
                    case 2:
                        exportAllData();
                        break;
                }
            })
            .show();
    }
    
    private void exportTransactions() {
        // Get transactions and export
        Toast.makeText(this, "Exporting transactions...", Toast.LENGTH_SHORT).show();
    }
    
    private void exportBudgets() {
        // Get budgets and export
        Toast.makeText(this, "Exporting budgets...", Toast.LENGTH_SHORT).show();
    }
    
    private void exportAllData() {
        // Export all data
        Toast.makeText(this, "Exporting all data...", Toast.LENGTH_SHORT).show();
    }
    
    private void loadBackupFiles() {
        BackupRestoreUtils.getBackupFiles(this, new BackupRestoreUtils.BackupListCallback() {
            @Override
            public void onSuccess(List<BackupRestoreUtils.BackupFileInfo> backupFiles) {
                runOnUiThread(() -> {
                    backupFilesAdapter.updateBackupFiles(backupFiles);
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(BackupRestoreActivity.this, 
                        "Error loading backup files: " + error, 
                        Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    private void showBackupOptionsDialog(BackupRestoreUtils.BackupFileInfo backupFile) {
        String[] options = {"Restore", "Share", "Delete"};
        
        new AlertDialog.Builder(this)
            .setTitle(backupFile.fileName)
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        restoreFromBackup(Uri.fromFile(new File(backupFile.filePath)));
                        break;
                    case 1:
                        DataExportUtils.shareFile(this, new File(backupFile.filePath));
                        break;
                    case 2:
                        deleteBackupFile(backupFile);
                        break;
                }
            })
            .show();
    }
    
    private void deleteBackupFile(BackupRestoreUtils.BackupFileInfo backupFile) {
        new AlertDialog.Builder(this)
            .setTitle("Delete Backup")
            .setMessage("Are you sure you want to delete this backup file?")
            .setPositiveButton("Delete", (dialog, which) -> {
                File file = new File(backupFile.filePath);
                if (file.delete()) {
                    Toast.makeText(this, "Backup deleted", Toast.LENGTH_SHORT).show();
                    loadBackupFiles();
                } else {
                    Toast.makeText(this, "Error deleting backup", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
}