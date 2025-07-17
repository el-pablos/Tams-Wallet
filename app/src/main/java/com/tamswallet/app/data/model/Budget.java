package com.tamswallet.app.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
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
    private long createdAt;

    // Constructors
    public Budget() {}

    @Ignore
    public Budget(String category, double limit, String period) {
        this.category = category;
        this.limit = limit;
        this.period = period;
        this.spent = 0.0;
        this.createdAt = System.currentTimeMillis();
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

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    // Helper methods
    public double getRemainingBudget() {
        return limit - spent;
    }

    public double getPercentageUsed() {
        return limit > 0 ? (spent / limit) * 100 : 0;
    }
}