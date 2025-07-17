package com.tamswallet.app.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private double amount;
    private String type; // "income" or "expense"
    private String category;
    private String description;
    private long date;
    private long userId; // TODO: Link to user when auth is implemented

    // Constructors
    public Transaction() {}

    @Ignore
    public Transaction(double amount, String type, String category, String description, long date) {
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
}