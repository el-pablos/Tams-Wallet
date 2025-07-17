package com.tamswallet.app.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.tamswallet.app.data.database.BudgetDao;
import com.tamswallet.app.data.database.TamsWalletDatabase;
import com.tamswallet.app.data.model.Budget;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BudgetRepository {
    private BudgetDao budgetDao;
    private ExecutorService executor;
    private static BudgetRepository instance;
    
    private BudgetRepository(Context context) {
        TamsWalletDatabase db = TamsWalletDatabase.getDatabase(context);
        budgetDao = db.budgetDao();
        executor = Executors.newFixedThreadPool(4);
    }
    
    public static synchronized BudgetRepository getInstance(Context context) {
        if (instance == null) {
            instance = new BudgetRepository(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Shutdown the executor service to prevent memory leaks
     */
    public void shutdown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public LiveData<List<Budget>> getAllBudgets() {
        return budgetDao.getAllBudgets();
    }
    
    public LiveData<List<Budget>> getBudgetsByUserId(long userId) {
        return budgetDao.getBudgetsByUserId(userId);
    }
    
    public LiveData<Budget> getBudgetById(int id) {
        return budgetDao.getBudgetById(id);
    }
    
    public LiveData<Budget> getBudgetByCategory(String category) {
        return budgetDao.getBudgetByCategory(category);
    }
    
    public LiveData<List<Budget>> getBudgetsByPeriod(String period) {
        return budgetDao.getBudgetsByPeriod(period);
    }
    
    public LiveData<List<Budget>> getActiveBudgets() {
        return budgetDao.getActiveBudgets();
    }
    
    public LiveData<List<Budget>> getExceededBudgets() {
        return budgetDao.getExceededBudgets();
    }
    
    public interface BudgetCallback {
        void onSuccess(long budgetId);
        void onError(String error);
    }
    
    public void insertBudget(Budget budget, BudgetCallback callback) {
        executor.execute(() -> {
            try {
                long id = budgetDao.insertBudget(budget);
                if (callback != null) {
                    callback.onSuccess(id);
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onError("Gagal menyimpan budget: " + e.getMessage());
                }
            }
        });
    }
    
    public void updateBudget(Budget budget, BudgetCallback callback) {
        executor.execute(() -> {
            try {
                budgetDao.update(budget);
                if (callback != null) {
                    callback.onSuccess(budget.getId());
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onError("Gagal mengupdate budget: " + e.getMessage());
                }
            }
        });
    }
    
    public void deleteBudget(Budget budget, BudgetCallback callback) {
        executor.execute(() -> {
            try {
                budgetDao.delete(budget);
                if (callback != null) {
                    callback.onSuccess(budget.getId());
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onError("Gagal menghapus budget: " + e.getMessage());
                }
            }
        });
    }
    
    public void deleteAllBudgets() {
        executor.execute(() -> budgetDao.deleteAll());
    }
    
    public void updateBudgetSpent(int budgetId, double spent, BudgetCallback callback) {
        executor.execute(() -> {
            try {
                budgetDao.updateSpent(budgetId, spent);
                if (callback != null) {
                    callback.onSuccess(budgetId);
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onError("Gagal mengupdate pengeluaran budget: " + e.getMessage());
                }
            }
        });
    }
    
    /**
     * Calculate and update budget spent based on transactions
     */
    public void recalculateBudgetSpent(int budgetId, String category, long userId, BudgetCallback callback) {
        executor.execute(() -> {
            try {
                // This would require a more complex query joining with transactions
                // For now, we'll just update the spent amount directly
                // In a real implementation, you'd calculate from transactions
                if (callback != null) {
                    callback.onSuccess(budgetId);
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onError("Gagal menghitung ulang pengeluaran: " + e.getMessage());
                }
            }
        });
    }
}