package com.tamswallet.app.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

/**
 * Unit tests for ValidationUtils
 */
@RunWith(JUnit4.class)
public class ValidationUtilsTest {

    @Test
    public void testIsValidEmail() {
        // Valid emails
        assertTrue("Valid email should pass", ValidationUtils.isValidEmail("test@example.com"));
        assertTrue("Valid email should pass", ValidationUtils.isValidEmail("user.name@domain.co.id"));
        assertTrue("Valid email should pass", ValidationUtils.isValidEmail("test123@gmail.com"));
        
        // Invalid emails
        assertFalse("Invalid email should fail", ValidationUtils.isValidEmail("invalid-email"));
        assertFalse("Invalid email should fail", ValidationUtils.isValidEmail("@domain.com"));
        assertFalse("Invalid email should fail", ValidationUtils.isValidEmail("test@"));
        assertFalse("Empty email should fail", ValidationUtils.isValidEmail(""));
        assertFalse("Null email should fail", ValidationUtils.isValidEmail(null));
    }

    @Test
    public void testIsValidName() {
        // Valid names
        assertTrue("Valid name should pass", ValidationUtils.isValidName("John Doe"));
        assertTrue("Valid name should pass", ValidationUtils.isValidName("Maria"));
        assertTrue("Valid name should pass", ValidationUtils.isValidName("Jean Pierre"));
        
        // Invalid names
        assertFalse("Name with numbers should fail", ValidationUtils.isValidName("John123"));
        assertFalse("Name with special chars should fail", ValidationUtils.isValidName("John@Doe"));
        assertFalse("Too short name should fail", ValidationUtils.isValidName("J"));
        assertFalse("Too long name should fail", ValidationUtils.isValidName("A".repeat(51)));
        assertFalse("Empty name should fail", ValidationUtils.isValidName(""));
        assertFalse("Null name should fail", ValidationUtils.isValidName(null));
    }

    @Test
    public void testIsValidPassword() {
        // Valid passwords
        assertTrue("Valid password should pass", ValidationUtils.isValidPassword("password123"));
        assertTrue("Valid password should pass", ValidationUtils.isValidPassword("myPass1"));
        assertTrue("Valid password should pass", ValidationUtils.isValidPassword("Test123"));
        
        // Invalid passwords
        assertFalse("Password without numbers should fail", ValidationUtils.isValidPassword("password"));
        assertFalse("Password without letters should fail", ValidationUtils.isValidPassword("123456"));
        assertFalse("Too short password should fail", ValidationUtils.isValidPassword("pass1"));
        assertFalse("Empty password should fail", ValidationUtils.isValidPassword(""));
        assertFalse("Null password should fail", ValidationUtils.isValidPassword(null));
    }

    @Test
    public void testIsValidAmount() {
        // Valid amounts
        assertTrue("Valid amount should pass", ValidationUtils.isValidAmount("100"));
        assertTrue("Valid amount should pass", ValidationUtils.isValidAmount("1000.50"));
        assertTrue("Valid amount should pass", ValidationUtils.isValidAmount("999999999.99"));
        
        // Invalid amounts
        assertFalse("Zero amount should fail", ValidationUtils.isValidAmount("0"));
        assertFalse("Negative amount should fail", ValidationUtils.isValidAmount("-100"));
        assertFalse("Too large amount should fail", ValidationUtils.isValidAmount("9999999999"));
        assertFalse("Invalid format should fail", ValidationUtils.isValidAmount("100.123"));
        assertFalse("Non-numeric should fail", ValidationUtils.isValidAmount("abc"));
        assertFalse("Empty amount should fail", ValidationUtils.isValidAmount(""));
        assertFalse("Null amount should fail", ValidationUtils.isValidAmount(null));
    }

    @Test
    public void testIsValidCategory() {
        // Valid categories
        assertTrue("Valid category should pass", ValidationUtils.isValidCategory("Food"));
        assertTrue("Valid category should pass", ValidationUtils.isValidCategory("Transport"));
        assertTrue("Valid category should pass", ValidationUtils.isValidCategory("Health Care"));
        
        // Invalid categories
        assertFalse("Category with special chars should fail", ValidationUtils.isValidCategory("Food@Home"));
        assertFalse("Too long category should fail", ValidationUtils.isValidCategory("A".repeat(31)));
        assertFalse("Empty category should fail", ValidationUtils.isValidCategory(""));
        assertFalse("Null category should fail", ValidationUtils.isValidCategory(null));
    }

    @Test
    public void testIsValidDescription() {
        // Valid descriptions
        assertTrue("Valid description should pass", ValidationUtils.isValidDescription("Lunch at restaurant"));
        assertTrue("Valid description should pass", ValidationUtils.isValidDescription("Monthly salary"));
        assertTrue("Empty description should pass", ValidationUtils.isValidDescription(""));
        assertTrue("Null description should pass", ValidationUtils.isValidDescription(null));
        
        // Invalid descriptions
        assertFalse("Too long description should fail", ValidationUtils.isValidDescription("A".repeat(201)));
        assertFalse("Description with invalid chars should fail", ValidationUtils.isValidDescription("Test<script>"));
    }

    @Test
    public void testSanitizeInput() {
        assertEquals("Clean input should remain unchanged", "Hello World", 
                ValidationUtils.sanitizeInput("Hello World"));
        assertEquals("Dangerous chars should be removed", "Hello World", 
                ValidationUtils.sanitizeInput("Hello<>World"));
        assertEquals("Multiple dangerous chars should be removed", "Test input", 
                ValidationUtils.sanitizeInput("Test\"'%;()&+input"));
        assertNull("Null input should return null", ValidationUtils.sanitizeInput(null));
    }

    @Test
    public void testSanitizeSearchQuery() {
        assertEquals("Normal query should get wildcards", "%test%", 
                ValidationUtils.sanitizeSearchQuery("test"));
        assertEquals("Empty query should return empty", "", 
                ValidationUtils.sanitizeSearchQuery(""));
        assertEquals("SQL chars should be escaped", "%test\\%query%", 
                ValidationUtils.sanitizeSearchQuery("test%query"));
    }

    @Test
    public void testIsValidTransactionType() {
        assertTrue("Income should be valid", ValidationUtils.isValidTransactionType("income"));
        assertTrue("Expense should be valid", ValidationUtils.isValidTransactionType("expense"));
        assertTrue("Case insensitive should work", ValidationUtils.isValidTransactionType("INCOME"));
        assertTrue("Case insensitive should work", ValidationUtils.isValidTransactionType("Expense"));
        
        assertFalse("Invalid type should fail", ValidationUtils.isValidTransactionType("invalid"));
        assertFalse("Empty type should fail", ValidationUtils.isValidTransactionType(""));
        assertFalse("Null type should fail", ValidationUtils.isValidTransactionType(null));
    }

    @Test
    public void testValidationErrorMessages() {
        // Test name validation errors
        assertEquals("Nama tidak boleh kosong", ValidationUtils.getNameValidationError(""));
        assertEquals("Nama minimal 2 karakter", ValidationUtils.getNameValidationError("J"));
        assertEquals("Nama maksimal 50 karakter", ValidationUtils.getNameValidationError("A".repeat(51)));
        assertEquals("Nama hanya boleh berisi huruf dan spasi", ValidationUtils.getNameValidationError("John123"));
        assertNull("Valid name should return null", ValidationUtils.getNameValidationError("John Doe"));

        // Test email validation errors
        assertEquals("Email tidak boleh kosong", ValidationUtils.getEmailValidationError(""));
        assertEquals("Format email tidak valid", ValidationUtils.getEmailValidationError("invalid-email"));
        assertNull("Valid email should return null", ValidationUtils.getEmailValidationError("test@example.com"));

        // Test password validation errors
        assertEquals("Password tidak boleh kosong", ValidationUtils.getPasswordValidationError(""));
        assertEquals("Password minimal 6 karakter", ValidationUtils.getPasswordValidationError("pass"));
        assertEquals("Password harus mengandung minimal 1 angka", ValidationUtils.getPasswordValidationError("password"));
        assertEquals("Password harus mengandung minimal 1 huruf", ValidationUtils.getPasswordValidationError("123456"));
        assertNull("Valid password should return null", ValidationUtils.getPasswordValidationError("password123"));

        // Test amount validation errors
        assertEquals("Jumlah tidak boleh kosong", ValidationUtils.getAmountValidationError(""));
        assertEquals("Jumlah harus lebih dari 0", ValidationUtils.getAmountValidationError("0"));
        assertEquals("Jumlah terlalu besar", ValidationUtils.getAmountValidationError("9999999999"));
        assertEquals("Format jumlah tidak valid", ValidationUtils.getAmountValidationError("abc"));
        assertNull("Valid amount should return null", ValidationUtils.getAmountValidationError("100"));
    }
}
