package com.tamswallet.app.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SecurityUtils {
    
    private static final String SALT = "TamsWallet2025SecureSalt";
    private static final int HASH_ITERATIONS = 10000;
    
    /**
     * Hash password using SHA-256 with salt
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            // Add salt to password
            String saltedPassword = password + SALT;
            
            // Hash multiple times for better security
            byte[] hash = saltedPassword.getBytes(StandardCharsets.UTF_8);
            for (int i = 0; i < HASH_ITERATIONS; i++) {
                hash = md.digest(hash);
            }
            
            // Convert to base64 for storage
            return Base64.getEncoder().encodeToString(hash);
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    /**
     * Verify password against stored hash
     */
    public static boolean verifyPassword(String password, String storedHash) {
        String hashToCheck = hashPassword(password);
        return hashToCheck.equals(storedHash);
    }
    
    /**
     * Generate secure random string for tokens
     */
    public static String generateSecureToken(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return sb.toString();
    }
    
    /**
     * Validate password strength
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        
        // Check for at least one number and one letter
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        
        return hasNumber && hasLetter;
    }
    
    /**
     * Get password strength description
     */
    public static String getPasswordStrengthDescription(String password) {
        if (password == null || password.length() < 6) {
            return "Password minimal 6 karakter";
        }
        
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        
        if (password.length() >= 8 && hasNumber && hasLetter && hasSpecial) {
            return "Password kuat";
        } else if (password.length() >= 6 && hasNumber && hasLetter) {
            return "Password sedang";
        } else {
            return "Password lemah";
        }
    }
}