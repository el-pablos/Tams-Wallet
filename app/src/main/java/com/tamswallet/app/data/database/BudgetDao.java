package com.tamswallet.app.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.tamswallet.app.data.model.Budget;
import java.util.List;

@Dao
public interface BudgetDao {
    
    @Query("SELECT * FROM budgets ORDER BY category ASC")
    LiveData<List<Budget>> getAllBudgets();

    @Query("SELECT * FROM budgets WHERE category = :category")
    LiveData<Budget> getBudgetByCategory(String category);

    @Query("SELECT * FROM budgets WHERE id = :id")
    LiveData<Budget> getBudgetById(int id);

    @Insert
    void insert(Budget budget);

    @Update
    void update(Budget budget);

    @Delete
    void delete(Budget budget);

    @Query("DELETE FROM budgets WHERE id = :id")
    void deleteById(int id);

    // TODO: Add method to update spent amount when transactions are added/modified
    @Query("UPDATE budgets SET spent = :spent WHERE category = :category")
    void updateSpentAmount(String category, double spent);
    
    // Synchronous methods for background operations
    @Query("SELECT * FROM budgets ORDER BY category ASC")
    List<Budget> getAllBudgetsSync();
    
    @Query("DELETE FROM budgets")
    void deleteAllBudgets();
    
    @Insert
    void insertAllBudgets(List<Budget> budgets);
    
    // Additional methods needed by repositories
    @Query("SELECT * FROM budgets WHERE userId = :userId ORDER BY category ASC")
    LiveData<List<Budget>> getBudgetsByUserId(long userId);
    
    @Query("SELECT * FROM budgets WHERE period = :period ORDER BY category ASC")
    LiveData<List<Budget>> getBudgetsByPeriod(String period);
    
    @Query("SELECT * FROM budgets WHERE spent < `limit` ORDER BY category ASC")
    LiveData<List<Budget>> getActiveBudgets();
    
    @Query("SELECT * FROM budgets WHERE spent >= `limit` ORDER BY category ASC")
    LiveData<List<Budget>> getExceededBudgets();
    
    @Query("DELETE FROM budgets")
    void deleteAll();
    
    @Query("UPDATE budgets SET spent = :spent WHERE id = :budgetId")
    void updateSpent(int budgetId, double spent);
    
    @Insert
    long insertBudget(Budget budget);
}