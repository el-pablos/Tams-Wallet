package com.tamswallet.app.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.tamswallet.app.data.database.TamsWalletDatabase;
import com.tamswallet.app.data.model.Budget;
import com.tamswallet.app.data.model.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackupRestoreUtils {
    
    private static final String TAG = "BackupRestoreUtils";
    private static final String BACKUP_DIR = "backups";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);
    
    public static void createBackup(Context context, BackupCallback callback) {
        executor.execute(() -> {
            try {
                TamsWalletDatabase database = TamsWalletDatabase.getInstance(context);
                
                // Get all data
                List<Transaction> transactions = database.transactionDao().getAllTransactionsSync();
                List<Budget> budgets = database.budgetDao().getAllBudgetsSync();
                
                // Create backup file
                File backupDir = new File(context.getExternalFilesDir(null), BACKUP_DIR);
                if (!backupDir.exists()) {
                    backupDir.mkdirs();
                }
                
                String fileName = "tams_wallet_backup_" + System.currentTimeMillis() + ".json";
                File backupFile = new File(backupDir, fileName);
                
                // Create JSON backup
                JSONObject backup = new JSONObject();
                backup.put("version", "1.0");
                backup.put("created_at", dateFormat.format(new Date()));
                backup.put("device_info", android.os.Build.MODEL);
                
                // Add transactions
                JSONArray transactionsArray = new JSONArray();
                for (Transaction transaction : transactions) {
                    JSONObject transactionObj = new JSONObject();
                    transactionObj.put("id", transaction.getId());
                    transactionObj.put("type", transaction.getType());
                    transactionObj.put("category", transaction.getCategory());
                    transactionObj.put("description", transaction.getDescription());
                    transactionObj.put("amount", transaction.getAmount());
                    transactionObj.put("date", transaction.getDate());
                    transactionsArray.put(transactionObj);
                }
                backup.put("transactions", transactionsArray);
                
                // Add budgets
                JSONArray budgetsArray = new JSONArray();
                for (Budget budget : budgets) {
                    JSONObject budgetObj = new JSONObject();
                    budgetObj.put("id", budget.getId());
                    budgetObj.put("category", budget.getCategory());
                    budgetObj.put("limit", budget.getLimit());
                    budgetObj.put("spent", budget.getSpent());
                    budgetObj.put("period", budget.getPeriod());
                    budgetObj.put("created_at", budget.getCreatedAt());
                    budgetsArray.put(budgetObj);
                }
                backup.put("budgets", budgetsArray);
                
                // Write to file
                FileWriter writer = new FileWriter(backupFile);
                writer.write(backup.toString(4));
                writer.close();
                
                callback.onSuccess(backupFile, transactions.size() + budgets.size());
                
            } catch (Exception e) {
                Log.e(TAG, "Error creating backup", e);
                callback.onError("Failed to create backup: " + e.getMessage());
            }
        });
    }
    
    public static void restoreFromBackup(Context context, Uri backupUri, BackupCallback callback) {
        executor.execute(() -> {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(backupUri);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                reader.close();
                
                JSONObject backup = new JSONObject(jsonBuilder.toString());
                
                // Validate backup format
                if (!backup.has("version") || !backup.has("transactions") || !backup.has("budgets")) {
                    callback.onError("Invalid backup file format");
                    return;
                }
                
                TamsWalletDatabase database = TamsWalletDatabase.getInstance(context);
                
                // Clear existing data (optional - ask user first)
                database.transactionDao().deleteAllTransactions();
                database.budgetDao().deleteAllBudgets();
                
                int restoredCount = 0;
                
                // Restore transactions
                JSONArray transactionsArray = backup.getJSONArray("transactions");
                List<Transaction> transactions = new ArrayList<>();
                
                for (int i = 0; i < transactionsArray.length(); i++) {
                    JSONObject transactionObj = transactionsArray.getJSONObject(i);
                    Transaction transaction = new Transaction();
                    transaction.setType(transactionObj.getString("type"));
                    transaction.setCategory(transactionObj.getString("category"));
                    transaction.setDescription(transactionObj.getString("description"));
                    transaction.setAmount(transactionObj.getDouble("amount"));
                    transaction.setDate(transactionObj.getLong("date"));
                    transactions.add(transaction);
                }
                
                database.transactionDao().insertAllTransactions(transactions);
                restoredCount += transactions.size();
                
                // Restore budgets
                JSONArray budgetsArray = backup.getJSONArray("budgets");
                List<Budget> budgets = new ArrayList<>();
                
                for (int i = 0; i < budgetsArray.length(); i++) {
                    JSONObject budgetObj = budgetsArray.getJSONObject(i);
                    Budget budget = new Budget();
                    budget.setCategory(budgetObj.getString("category"));
                    budget.setLimit(budgetObj.getDouble("limit"));
                    budget.setSpent(budgetObj.getDouble("spent"));
                    budget.setPeriod(budgetObj.getString("period"));
                    budget.setCreatedAt(budgetObj.getLong("created_at"));
                    budgets.add(budget);
                }
                
                database.budgetDao().insertAllBudgets(budgets);
                restoredCount += budgets.size();
                
                callback.onSuccess(null, restoredCount);
                
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing backup file", e);
                callback.onError("Invalid backup file format: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Error restoring backup", e);
                callback.onError("Failed to restore backup: " + e.getMessage());
            }
        });
    }
    
    public static void getBackupFiles(Context context, BackupListCallback callback) {
        executor.execute(() -> {
            try {
                File backupDir = new File(context.getExternalFilesDir(null), BACKUP_DIR);
                List<BackupFileInfo> backupFiles = new ArrayList<>();
                
                if (backupDir.exists()) {
                    File[] files = backupDir.listFiles((dir, name) -> name.endsWith(".json"));
                    if (files != null) {
                        for (File file : files) {
                            BackupFileInfo info = new BackupFileInfo();
                            info.fileName = file.getName();
                            info.filePath = file.getAbsolutePath();
                            info.fileSize = file.length();
                            info.createdAt = file.lastModified();
                            backupFiles.add(info);
                        }
                    }
                }
                
                callback.onSuccess(backupFiles);
                
            } catch (Exception e) {
                Log.e(TAG, "Error listing backup files", e);
                callback.onError("Failed to list backup files: " + e.getMessage());
            }
        });
    }
    
    public static class BackupFileInfo {
        public String fileName;
        public String filePath;
        public long fileSize;
        public long createdAt;
        
        public String getFormattedSize() {
            if (fileSize < 1024) return fileSize + " B";
            if (fileSize < 1024 * 1024) return (fileSize / 1024) + " KB";
            return (fileSize / (1024 * 1024)) + " MB";
        }
        
        public String getFormattedDate() {
            return dateFormat.format(new Date(createdAt));
        }
    }
    
    public interface BackupCallback {
        void onSuccess(File backupFile, int itemCount);
        void onError(String error);
    }
    
    public interface BackupListCallback {
        void onSuccess(List<BackupFileInfo> backupFiles);
        void onError(String error);
    }
}