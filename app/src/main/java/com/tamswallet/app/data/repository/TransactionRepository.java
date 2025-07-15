package com.tamswallet.app.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.tamswallet.app.data.database.TamsWalletDatabase;
import com.tamswallet.app.data.database.TransactionDao;
import com.tamswallet.app.data.model.Transaction;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionRepository {
    private TransactionDao transactionDao;
    private ExecutorService executor;
    private static TransactionRepository instance;
    
    private TransactionRepository(Context context) {
        TamsWalletDatabase db = TamsWalletDatabase.getDatabase(context);
        transactionDao = db.transactionDao();
        executor = Executors.newFixedThreadPool(4);
    }
    
    public static synchronized TransactionRepository getInstance(Context context) {
        if (instance == null) {
            instance = new TransactionRepository(context.getApplicationContext());
        }
        return instance;
    }
    
    public LiveData<List<Transaction>> getAllTransactions() {
        return transactionDao.getAllTransactions();
    }
    
    public LiveData<List<Transaction>> getTransactionsByUserId(long userId) {
        return transactionDao.getTransactionsByUserId(userId);
    }
    
    public LiveData<List<Transaction>> getTransactionsByType(String type) {
        return transactionDao.getTransactionsByType(type);
    }
    
    public LiveData<List<Transaction>> getTransactionsByCategory(String category) {
        return transactionDao.getTransactionsByCategory(category);
    }
    
    public LiveData<List<Transaction>> getTransactionsByDateRange(long startDate, long endDate) {
        return transactionDao.getTransactionsByDateRange(startDate, endDate);
    }
    
    public LiveData<List<Transaction>> getRecentTransactions(int limit) {
        return transactionDao.getRecentTransactions(limit);
    }
    
    public LiveData<Double> getTotalBalance() {
        return transactionDao.getTotalBalance();
    }
    
    public LiveData<Double> getTotalIncomeByUserId(long userId) {
        return transactionDao.getTotalIncomeByUserId(userId);
    }
    
    public LiveData<Double> getTotalExpenseByUserId(long userId) {
        return transactionDao.getTotalExpenseByUserId(userId);
    }
    
    public LiveData<Double> getTodayIncomeByUserId(long userId) {
        return transactionDao.getTodayIncomeByUserId(userId);
    }
    
    public LiveData<Double> getTodayExpenseByUserId(long userId) {
        return transactionDao.getTodayExpenseByUserId(userId);
    }
    
    public LiveData<Double> getMonthlyIncomeByUserId(long userId, int month, int year) {
        return transactionDao.getMonthlyIncomeByUserId(userId, month, year);
    }
    
    public LiveData<Double> getMonthlyExpenseByUserId(long userId, int month, int year) {
        return transactionDao.getMonthlyExpenseByUserId(userId, month, year);
    }
    
    public interface TransactionCallback {
        void onSuccess(long transactionId);
        void onError(String error);
    }
    
    public void insertTransaction(Transaction transaction, TransactionCallback callback) {
        executor.execute(() -> {
            try {
                long id = transactionDao.insert(transaction);
                if (callback != null) {
                    callback.onSuccess(id);
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onError("Gagal menyimpan transaksi: " + e.getMessage());
                }
            }
        });
    }
    
    public void updateTransaction(Transaction transaction, TransactionCallback callback) {
        executor.execute(() -> {
            try {
                transactionDao.update(transaction);
                if (callback != null) {
                    callback.onSuccess(transaction.getId());
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onError("Gagal mengupdate transaksi: " + e.getMessage());
                }
            }
        });
    }
    
    public void deleteTransaction(Transaction transaction, TransactionCallback callback) {
        executor.execute(() -> {
            try {
                transactionDao.delete(transaction);
                if (callback != null) {
                    callback.onSuccess(transaction.getId());
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onError("Gagal menghapus transaksi: " + e.getMessage());
                }
            }
        });
    }
    
    public void deleteAllTransactions() {
        executor.execute(() -> transactionDao.deleteAll());
    }
    
    public LiveData<List<Transaction>> searchTransactions(String query) {
        return transactionDao.searchTransactions("%" + query + "%");
    }
}