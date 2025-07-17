package com.tamswallet.app.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.tamswallet.app.data.model.Transaction;
import java.util.List;

@Dao
public interface TransactionDao {
    
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT :limit OFFSET :offset")
    LiveData<List<Transaction>> getTransactionsPaginated(int limit, int offset);

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<Transaction>> getTransactionsByDateRange(long startDate, long endDate);

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC LIMIT :limit OFFSET :offset")
    LiveData<List<Transaction>> getTransactionsByDateRangePaginated(long startDate, long endDate, int limit, int offset);

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    LiveData<List<Transaction>> getTransactionsByType(String type);

    @Query("SELECT * FROM transactions WHERE category = :category ORDER BY date DESC")
    LiveData<List<Transaction>> getTransactionsByCategory(String category);

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'income' AND date BETWEEN :startDate AND :endDate")
    LiveData<Double> getTotalIncomeByDateRange(long startDate, long endDate);

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense' AND date BETWEEN :startDate AND :endDate")
    LiveData<Double> getTotalExpenseByDateRange(long startDate, long endDate);

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense' AND category = :category AND date BETWEEN :startDate AND :endDate")
    LiveData<Double> getCategoryExpenseByDateRange(String category, long startDate, long endDate);

    @Query("SELECT * FROM transactions WHERE id = :id")
    LiveData<Transaction> getTransactionById(int id);

    @Insert
    void insert(Transaction transaction);

    @Update
    void update(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    @Query("DELETE FROM transactions WHERE id = :id")
    void deleteById(int id);

    // TODO: Add methods for pagination and search functionality
    @Query("SELECT * FROM transactions WHERE description LIKE :searchQuery OR category LIKE :searchQuery ORDER BY date DESC")
    LiveData<List<Transaction>> searchTransactions(String searchQuery);
    
    // Synchronous methods for background operations
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    List<Transaction> getAllTransactionsSync();
    
    @Query("DELETE FROM transactions")
    void deleteAllTransactions();
    
    @Insert
    void insertAllTransactions(List<Transaction> transactions);
    
    // Additional methods needed by repositories
    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY date DESC")
    LiveData<List<Transaction>> getTransactionsByUserId(long userId);
    
    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT :limit")
    LiveData<List<Transaction>> getRecentTransactions(int limit);
    
    @Query("SELECT (SELECT IFNULL(SUM(amount), 0) FROM transactions WHERE type = 'income') - (SELECT IFNULL(SUM(amount), 0) FROM transactions WHERE type = 'expense')")
    LiveData<Double> getTotalBalance();
    
    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'income' AND userId = :userId")
    LiveData<Double> getTotalIncomeByUserId(long userId);
    
    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense' AND userId = :userId")
    LiveData<Double> getTotalExpenseByUserId(long userId);
    
    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'income' AND userId = :userId AND date >= :todayStart AND date < :todayEnd")
    LiveData<Double> getTodayIncomeByUserId(long userId, long todayStart, long todayEnd);
    
    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense' AND userId = :userId AND date >= :todayStart AND date < :todayEnd")
    LiveData<Double> getTodayExpenseByUserId(long userId, long todayStart, long todayEnd);
    
    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'income' AND userId = :userId AND date >= :monthStart AND date < :monthEnd")
    LiveData<Double> getMonthlyIncomeByUserId(long userId, long monthStart, long monthEnd);
    
    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense' AND userId = :userId AND date >= :monthStart AND date < :monthEnd")
    LiveData<Double> getMonthlyExpenseByUserId(long userId, long monthStart, long monthEnd);
    
    @Query("DELETE FROM transactions")
    void deleteAll();
    
    @Insert
    long insertTransaction(Transaction transaction);
}