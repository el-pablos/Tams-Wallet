package com.tamswallet.app.data.model;

public class CategoryReport {
    private String category;
    private double totalAmount;
    private int transactionCount;
    private double percentage;
    
    public CategoryReport() {
    }
    
    public CategoryReport(String category, double totalAmount, int transactionCount) {
        this.category = category;
        this.totalAmount = totalAmount;
        this.transactionCount = transactionCount;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public int getTransactionCount() {
        return transactionCount;
    }
    
    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }
    
    public double getPercentage() {
        return percentage;
    }
    
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
    
    public double getAverageAmount() {
        return transactionCount > 0 ? totalAmount / transactionCount : 0;
    }
}