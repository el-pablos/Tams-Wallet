package com.tamswallet.app.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

/**
 * Unit tests for SecurityUtils
 */
@RunWith(JUnit4.class)
public class SecurityUtilsTest {

    @Test
    public void testGenerateSalt() {
        String salt1 = SecurityUtils.generateSalt();
        String salt2 = SecurityUtils.generateSalt();
        
        assertNotNull("Salt should not be null", salt1);
        assertNotNull("Salt should not be null", salt2);
        assertNotEquals("Salts should be different", salt1, salt2);
        assertTrue("Salt should not be empty", salt1.length() > 0);
        assertTrue("Salt should not be empty", salt2.length() > 0);
    }

    @Test
    public void testHashPasswordWithSalt() {
        String password = "testPassword123";
        String salt = SecurityUtils.generateSalt();
        
        String hash1 = SecurityUtils.hashPassword(password, salt);
        String hash2 = SecurityUtils.hashPassword(password, salt);
        
        assertNotNull("Hash should not be null", hash1);
        assertNotNull("Hash should not be null", hash2);
        assertEquals("Same password and salt should produce same hash", hash1, hash2);
        assertTrue("Hash should not be empty", hash1.length() > 0);
    }

    @Test
    public void testHashPasswordDifferentSalts() {
        String password = "testPassword123";
        String salt1 = SecurityUtils.generateSalt();
        String salt2 = SecurityUtils.generateSalt();
        
        String hash1 = SecurityUtils.hashPassword(password, salt1);
        String hash2 = SecurityUtils.hashPassword(password, salt2);
        
        assertNotEquals("Different salts should produce different hashes", hash1, hash2);
    }

    @Test
    public void testVerifyPassword() {
        String password = "testPassword123";
        String salt = SecurityUtils.generateSalt();
        String hash = SecurityUtils.hashPassword(password, salt);
        
        assertTrue("Correct password should verify", 
                SecurityUtils.verifyPassword(password, hash, salt));
        assertFalse("Incorrect password should not verify", 
                SecurityUtils.verifyPassword("wrongPassword", hash, salt));
    }

    @Test
    public void testGenerateSecureToken() {
        int length = 32;
        String token1 = SecurityUtils.generateSecureToken(length);
        String token2 = SecurityUtils.generateSecureToken(length);
        
        assertNotNull("Token should not be null", token1);
        assertNotNull("Token should not be null", token2);
        assertEquals("Token should have correct length", length, token1.length());
        assertEquals("Token should have correct length", length, token2.length());
        assertNotEquals("Tokens should be different", token1, token2);
    }

    @Test
    public void testIsPasswordStrong() {
        assertTrue("Password with letters and numbers should be strong", 
                SecurityUtils.isPasswordStrong("password123"));
        assertTrue("Password with mixed case and numbers should be strong", 
                SecurityUtils.isPasswordStrong("Password123"));
        
        assertFalse("Short password should not be strong", 
                SecurityUtils.isPasswordStrong("pass"));
        assertFalse("Password without numbers should not be strong", 
                SecurityUtils.isPasswordStrong("password"));
        assertFalse("Password without letters should not be strong", 
                SecurityUtils.isPasswordStrong("123456"));
        assertFalse("Null password should not be strong", 
                SecurityUtils.isPasswordStrong(null));
    }

    @Test
    public void testGetPasswordStrengthDescription() {
        assertEquals("Password minimal 6 karakter", 
                SecurityUtils.getPasswordStrengthDescription("pass"));
        assertEquals("Password minimal 6 karakter", 
                SecurityUtils.getPasswordStrengthDescription(null));
        assertEquals("Password sedang", 
                SecurityUtils.getPasswordStrengthDescription("password123"));
        assertEquals("Password kuat", 
                SecurityUtils.getPasswordStrengthDescription("Password123!"));
        assertEquals("Password lemah", 
                SecurityUtils.getPasswordStrengthDescription("password"));
    }
}
