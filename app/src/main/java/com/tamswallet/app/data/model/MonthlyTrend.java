package com.tamswallet.app.data.model;

public class MonthlyTrend {
    private String month;
    private int year;
    private double income;
    private double expense;
    private double netIncome;
    private int totalTransactions;
    
    public MonthlyTrend() {
    }
    
    public MonthlyTrend(String month, int year, double income, double expense) {
        this.month = month;
        this.year = year;
        this.income = income;
        this.expense = expense;
        this.netIncome = income - expense;
    }
    
    public String getMonth() {
        return month;
    }
    
    public void setMonth(String month) {
        this.month = month;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public double getIncome() {
        return income;
    }
    
    public void setIncome(double income) {
        this.income = income;
        this.netIncome = income - expense;
    }
    
    public double getExpense() {
        return expense;
    }
    
    public void setExpense(double expense) {
        this.expense = expense;
        this.netIncome = income - expense;
    }
    
    public double getNetIncome() {
        return netIncome;
    }
    
    public void setNetIncome(double netIncome) {
        this.netIncome = netIncome;
    }
    
    public int getTotalTransactions() {
        return totalTransactions;
    }
    
    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }
    
    public String getMonthYear() {
        return month + " " + year;
    }
    
    public boolean isPositive() {
        return netIncome > 0;
    }
    
    public double getSavingsRate() {
        return income > 0 ? (netIncome / income) * 100 : 0;
    }
}