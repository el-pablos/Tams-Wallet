package com.tamswallet.app.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import com.tamswallet.app.data.model.Transaction;
import com.tamswallet.app.data.model.Budget;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataExportUtils {
    
    private static final String EXPORT_DIR = "exports";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    
    public static void exportTransactionsToCSV(Context context, List<Transaction> transactions, ExportCallback callback) {
        try {
            File exportDir = new File(context.getExternalFilesDir(null), EXPORT_DIR);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            
            String fileName = "transactions_" + System.currentTimeMillis() + ".csv";
            File file = new File(exportDir, fileName);
            
            FileWriter writer = new FileWriter(file);
            
            // Write CSV header
            writer.append("Date,Type,Category,Description,Amount\n");
            
            // Write transaction data
            for (Transaction transaction : transactions) {
                writer.append(dateFormat.format(new Date(transaction.getDate()))).append(",");
                writer.append(transaction.getType()).append(",");
                writer.append(transaction.getCategory()).append(",");
                writer.append("\"").append(transaction.getDescription()).append("\"").append(",");
                writer.append(String.valueOf(transaction.getAmount())).append("\n");
            }
            
            writer.close();
            
            if (callback != null) {
                callback.onSuccess(file);
            }
            
        } catch (IOException e) {
            if (callback != null) {
                callback.onError("Error exporting transactions: " + e.getMessage());
            }
        }
    }
    
    public static void exportBudgetsToCSV(Context context, List<Budget> budgets, ExportCallback callback) {
        try {
            File exportDir = new File(context.getExternalFilesDir(null), EXPORT_DIR);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            
            String fileName = "budgets_" + System.currentTimeMillis() + ".csv";
            File file = new File(exportDir, fileName);
            
            FileWriter writer = new FileWriter(file);
            
            // Write CSV header
            writer.append("Category,Period,Limit,Spent,Remaining,Percentage\n");
            
            // Write budget data
            for (Budget budget : budgets) {
                writer.append(budget.getCategory()).append(",");
                writer.append(budget.getPeriod()).append(",");
                writer.append(String.valueOf(budget.getLimit())).append(",");
                writer.append(String.valueOf(budget.getSpent())).append(",");
                writer.append(String.valueOf(budget.getRemainingBudget())).append(",");
                writer.append(String.valueOf(budget.getPercentageUsed())).append("\n");
            }
            
            writer.close();
            
            if (callback != null) {
                callback.onSuccess(file);
            }
            
        } catch (IOException e) {
            if (callback != null) {
                callback.onError("Error exporting budgets: " + e.getMessage());
            }
        }
    }
    
    public static void shareFile(Context context, File file) {
        try {
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/csv");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            
            context.startActivity(Intent.createChooser(shareIntent, "Share " + file.getName()));
            
        } catch (Exception e) {
            // Handle error
        }
    }
    
    public static void exportAllDataToJSON(Context context, List<Transaction> transactions, List<Budget> budgets, ExportCallback callback) {
        try {
            File exportDir = new File(context.getExternalFilesDir(null), EXPORT_DIR);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            
            String fileName = "tams_wallet_backup_" + System.currentTimeMillis() + ".json";
            File file = new File(exportDir, fileName);
            
            FileWriter writer = new FileWriter(file);
            
            // Write JSON structure (simplified)
            writer.append("{\n");
            writer.append("  \"exported_at\": \"").append(dateFormat.format(new Date())).append("\",\n");
            writer.append("  \"transactions\": [\n");
            
            // Write transactions
            for (int i = 0; i < transactions.size(); i++) {
                Transaction t = transactions.get(i);
                writer.append("    {\n");
                writer.append("      \"date\": \"").append(dateFormat.format(new Date(t.getDate()))).append("\",\n");
                writer.append("      \"type\": \"").append(t.getType()).append("\",\n");
                writer.append("      \"category\": \"").append(t.getCategory()).append("\",\n");
                writer.append("      \"description\": \"").append(t.getDescription()).append("\",\n");
                writer.append("      \"amount\": ").append(String.valueOf(t.getAmount())).append("\n");
                writer.append("    }");
                if (i < transactions.size() - 1) writer.append(",");
                writer.append("\n");
            }
            
            writer.append("  ],\n");
            writer.append("  \"budgets\": [\n");
            
            // Write budgets
            for (int i = 0; i < budgets.size(); i++) {
                Budget b = budgets.get(i);
                writer.append("    {\n");
                writer.append("      \"category\": \"").append(b.getCategory()).append("\",\n");
                writer.append("      \"period\": \"").append(b.getPeriod()).append("\",\n");
                writer.append("      \"limit\": ").append(String.valueOf(b.getLimit())).append(",\n");
                writer.append("      \"spent\": ").append(String.valueOf(b.getSpent())).append("\n");
                writer.append("    }");
                if (i < budgets.size() - 1) writer.append(",");
                writer.append("\n");
            }
            
            writer.append("  ]\n");
            writer.append("}\n");
            
            writer.close();
            
            if (callback != null) {
                callback.onSuccess(file);
            }
            
        } catch (IOException e) {
            if (callback != null) {
                callback.onError("Error exporting data: " + e.getMessage());
            }
        }
    }
    
    public interface ExportCallback {
        void onSuccess(File file);
        void onError(String error);
    }
}