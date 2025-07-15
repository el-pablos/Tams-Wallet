package com.tamswallet.app.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.tamswallet.app.data.database.TamsWalletDatabase;
import com.tamswallet.app.data.model.CategoryReport;
import com.tamswallet.app.data.model.MonthlyTrend;
import com.tamswallet.app.data.model.Transaction;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReportsViewModel extends AndroidViewModel {
    
    private TamsWalletDatabase database;
    private ExecutorService executor;
    
    private MutableLiveData<List<CategoryReport>> categoryReports = new MutableLiveData<>();
    private MutableLiveData<List<MonthlyTrend>> monthlyTrends = new MutableLiveData<>();
    
    public ReportsViewModel(@NonNull Application application) {
        super(application);
        database = TamsWalletDatabase.getInstance(application);
        executor = Executors.newFixedThreadPool(2);
    }
    
    public LiveData<List<CategoryReport>> getCategoryReports() {
        return categoryReports;
    }
    
    public LiveData<List<MonthlyTrend>> getMonthlyTrends() {
        return monthlyTrends;
    }
    
    public void loadCategoryReports() {
        executor.execute(() -> {
            try {
                List<Transaction> transactions = database.transactionDao().getAllTransactionsSync();
                List<CategoryReport> reports = generateCategoryReports(transactions);
                categoryReports.postValue(reports);
            } catch (Exception e) {
                categoryReports.postValue(new ArrayList<>());
            }
        });
    }
    
    public void loadMonthlyTrends() {
        executor.execute(() -> {
            try {
                List<Transaction> transactions = database.transactionDao().getAllTransactionsSync();
                List<MonthlyTrend> trends = generateMonthlyTrends(transactions);
                monthlyTrends.postValue(trends);
            } catch (Exception e) {
                monthlyTrends.postValue(new ArrayList<>());
            }
        });
    }
    
    private List<CategoryReport> generateCategoryReports(List<Transaction> transactions) {
        Map<String, CategoryReport> categoryMap = new HashMap<>();
        double totalAmount = 0;
        
        for (Transaction transaction : transactions) {
            if ("expense".equals(transaction.getType())) {
                String category = transaction.getCategory();
                CategoryReport report = categoryMap.get(category);
                
                if (report == null) {
                    report = new CategoryReport(category, 0, 0);
                    categoryMap.put(category, report);
                }
                
                report.setTotalAmount(report.getTotalAmount() + transaction.getAmount());
                report.setTransactionCount(report.getTransactionCount() + 1);
                totalAmount += transaction.getAmount();
            }
        }
        
        // Calculate percentages
        for (CategoryReport report : categoryMap.values()) {
            double percentage = totalAmount > 0 ? (report.getTotalAmount() / totalAmount) * 100 : 0;
            report.setPercentage(percentage);
        }
        
        return new ArrayList<>(categoryMap.values());
    }
    
    private List<MonthlyTrend> generateMonthlyTrends(List<Transaction> transactions) {
        Map<String, MonthlyTrend> monthlyMap = new HashMap<>();
        
        for (Transaction transaction : transactions) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(transaction.getDate());
            
            String monthKey = getMonthName(cal.get(Calendar.MONTH)) + "_" + cal.get(Calendar.YEAR);
            String monthName = getMonthName(cal.get(Calendar.MONTH));
            int year = cal.get(Calendar.YEAR);
            
            MonthlyTrend trend = monthlyMap.get(monthKey);
            if (trend == null) {
                trend = new MonthlyTrend(monthName, year, 0, 0);
                monthlyMap.put(monthKey, trend);
            }
            
            if ("income".equals(transaction.getType())) {
                trend.setIncome(trend.getIncome() + transaction.getAmount());
            } else if ("expense".equals(transaction.getType())) {
                trend.setExpense(trend.getExpense() + transaction.getAmount());
            }
            
            trend.setTotalTransactions(trend.getTotalTransactions() + 1);
        }
        
        return new ArrayList<>(monthlyMap.values());
    }
    
    private String getMonthName(int month) {
        String[] months = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };
        return months[month];
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        if (executor != null) {
            executor.shutdown();
        }
    }
}