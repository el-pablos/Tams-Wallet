package com.tamswallet.app.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "budgets")
public class Budget {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String category;
    private double limit;
    private double spent;
    private String period; // "monthly", "weekly", etc.
    private long userId; // TODO: Link to user when auth is implemented

    // Constructors
    public Budget() {}

    public Budget(String category, double limit, String period) {
        this.category = category;
        this.limit = limit;
        this.period = period;
        this.spent = 0.0;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getLimit() { return limit; }
    public void setLimit(double limit) { this.limit = limit; }

    public double getSpent() { return spent; }
    public void setSpent(double spent) { this.spent = spent; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    // Helper methods
    public double getRemainingBudget() {
        return limit - spent;
    }

    public double getPercentageUsed() {
        return limit > 0 ? (spent / limit) * 100 : 0;
    }
}