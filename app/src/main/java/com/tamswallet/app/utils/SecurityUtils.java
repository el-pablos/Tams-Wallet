package com.tamswallet.app.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SecurityUtils {
    
    private static final int HASH_ITERATIONS = 10000;
    private static final int SALT_LENGTH = 16;

    /**
     * Generate cryptographically secure random salt
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hash password with provided salt using SHA-256
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Add salt to password
            String saltedPassword = password + salt;

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
     * Legacy method for backward compatibility - DO NOT USE FOR NEW CODE
     */
    @Deprecated
    public static String hashPassword(String password) {
        // For backward compatibility only - uses fixed salt
        String legacySalt = "TamsWallet2025SecureSalt";
        return hashPassword(password, legacySalt);
    }

    /**
     * Verify password against stored hash with salt
     */
    public static boolean verifyPassword(String password, String storedHash, String salt) {
        String hashToCheck = hashPassword(password, salt);
        return hashToCheck.equals(storedHash);
    }

    /**
     * Legacy verification method - for backward compatibility only
     */
    @Deprecated
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