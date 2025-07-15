package com.tamswallet.app.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.tamswallet.app.data.model.Transaction;
import java.util.Date;
import java.util.List;

@Dao
public interface TransactionDao {
    
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<Transaction>> getTransactionsByDateRange(Date startDate, Date endDate);

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    LiveData<List<Transaction>> getTransactionsByType(String type);

    @Query("SELECT * FROM transactions WHERE category = :category ORDER BY date DESC")
    LiveData<List<Transaction>> getTransactionsByCategory(String category);

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'income' AND date BETWEEN :startDate AND :endDate")
    LiveData<Double> getTotalIncomeByDateRange(Date startDate, Date endDate);

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense' AND date BETWEEN :startDate AND :endDate")
    LiveData<Double> getTotalExpenseByDateRange(Date startDate, Date endDate);

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense' AND category = :category AND date BETWEEN :startDate AND :endDate")
    LiveData<Double> getCategoryExpenseByDateRange(String category, Date startDate, Date endDate);

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
}