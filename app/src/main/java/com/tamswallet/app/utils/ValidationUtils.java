package com.tamswallet.app.utils;

import android.text.TextUtils;
import android.util.Patterns;
import java.util.regex.Pattern;

/**
 * Utility class for input validation and sanitization
 */
public class ValidationUtils {
    
    // Regex patterns for validation
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{2,50}$");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("^[0-9]+(\\.[0-9]{1,2})?$");
    private static final Pattern CATEGORY_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s]{1,30}$");
    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s.,!?-]{0,200}$");
    
    /**
     * Validate email address
     */
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    /**
     * Validate user name
     */
    public static boolean isValidName(String name) {
        return !TextUtils.isEmpty(name) && NAME_PATTERN.matcher(name).matches();
    }
    
    /**
     * Validate password strength
     */
    public static boolean isValidPassword(String password) {
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            return false;
        }
        
        // Check for at least one number and one letter
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        
        return hasNumber && hasLetter;
    }
    
    /**
     * Validate transaction amount
     */
    public static boolean isValidAmount(String amountStr) {
        if (TextUtils.isEmpty(amountStr)) {
            return false;
        }
        
        try {
            double amount = Double.parseDouble(amountStr);
            return amount > 0 && amount <= 999999999.99 && AMOUNT_PATTERN.matcher(amountStr).matches();
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate category name
     */
    public static boolean isValidCategory(String category) {
        return !TextUtils.isEmpty(category) && CATEGORY_PATTERN.matcher(category).matches();
    }
    
    /**
     * Validate transaction description
     */
    public static boolean isValidDescription(String description) {
        // Description can be empty
        return description == null || DESCRIPTION_PATTERN.matcher(description).matches();
    }
    
    /**
     * Sanitize string input to prevent injection attacks
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        
        // Remove potentially dangerous characters
        return input.replaceAll("[<>\"'%;()&+]", "").trim();
    }
    
    /**
     * Sanitize search query for database operations
     */
    public static String sanitizeSearchQuery(String query) {
        if (TextUtils.isEmpty(query)) {
            return "";
        }
        
        // Escape special SQL characters and add wildcards
        String sanitized = query.replaceAll("[%_\\\\]", "\\\\$0");
        return "%" + sanitized + "%";
    }
    
    /**
     * Validate transaction type
     */
    public static boolean isValidTransactionType(String type) {
        return "income".equalsIgnoreCase(type) || "expense".equalsIgnoreCase(type);
    }
    
    /**
     * Get validation error message for name
     */
    public static String getNameValidationError(String name) {
        if (TextUtils.isEmpty(name)) {
            return "Nama tidak boleh kosong";
        }
        if (name.length() < 2) {
            return "Nama minimal 2 karakter";
        }
        if (name.length() > 50) {
            return "Nama maksimal 50 karakter";
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            return "Nama hanya boleh berisi huruf dan spasi";
        }
        return null;
    }
    
    /**
     * Get validation error message for email
     */
    public static String getEmailValidationError(String email) {
        if (TextUtils.isEmpty(email)) {
            return "Email tidak boleh kosong";
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Format email tidak valid";
        }
        return null;
    }
    
    /**
     * Get validation error message for password
     */
    public static String getPasswordValidationError(String password) {
        if (TextUtils.isEmpty(password)) {
            return "Password tidak boleh kosong";
        }
        if (password.length() < 6) {
            return "Password minimal 6 karakter";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password harus mengandung minimal 1 angka";
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            return "Password harus mengandung minimal 1 huruf";
        }
        return null;
    }
    
    /**
     * Get validation error message for amount
     */
    public static String getAmountValidationError(String amountStr) {
        if (TextUtils.isEmpty(amountStr)) {
            return "Jumlah tidak boleh kosong";
        }
        
        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                return "Jumlah harus lebih dari 0";
            }
            if (amount > 999999999.99) {
                return "Jumlah terlalu besar";
            }
        } catch (NumberFormatException e) {
            return "Format jumlah tidak valid";
        }
        
        return null;
    }
}
