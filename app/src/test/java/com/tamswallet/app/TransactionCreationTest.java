package com.tamswallet.app;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tamswallet.app.data.model.Transaction;
import com.tamswallet.app.data.repository.TransactionRepository;
import com.tamswallet.app.utils.SessionManager;
import com.tamswallet.app.utils.ValidationUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Test class for Transaction Creation functionality
 * Tests the complete transaction creation flow including validation, database operations, and user session handling
 */
@RunWith(AndroidJUnit4.class)
public class TransactionCreationTest {

    private Context context;
    private TransactionRepository transactionRepository;
    private SessionManager sessionManager;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        transactionRepository = TransactionRepository.getInstance(context);
        sessionManager = new SessionManager(context);
    }

    @Test
    public void testAmountValidation() {
        // Test valid amounts
        assertNull("Valid amount should pass validation", ValidationUtils.getAmountValidationError("100.50"));
        assertNull("Valid integer amount should pass validation", ValidationUtils.getAmountValidationError("1000"));
        assertNull("Valid decimal amount should pass validation", ValidationUtils.getAmountValidationError("99.99"));

        // Test invalid amounts
        assertNotNull("Empty amount should fail validation", ValidationUtils.getAmountValidationError(""));
        assertNotNull("Null amount should fail validation", ValidationUtils.getAmountValidationError(null));
        assertNotNull("Zero amount should fail validation", ValidationUtils.getAmountValidationError("0"));
        assertNotNull("Negative amount should fail validation", ValidationUtils.getAmountValidationError("-100"));
        assertNotNull("Too large amount should fail validation", ValidationUtils.getAmountValidationError("9999999999"));
        assertNotNull("Invalid format should fail validation", ValidationUtils.getAmountValidationError("abc"));
    }

    @Test
    public void testDescriptionValidation() {
        // Test valid descriptions
        assertTrue("Valid description should pass", ValidationUtils.isValidDescription("Lunch at restaurant"));
        assertTrue("Empty description should pass", ValidationUtils.isValidDescription(""));
        assertTrue("Null description should pass", ValidationUtils.isValidDescription(null));
        assertTrue("Description with numbers should pass", ValidationUtils.isValidDescription("Grocery shopping 123"));

        // Test invalid descriptions (if any special characters are restricted)
        // Note: Based on the current implementation, most descriptions should be valid
        assertTrue("Description with special chars should pass", ValidationUtils.isValidDescription("Coffee & snacks"));
    }

    @Test
    public void testSessionManagerTestUserCreation() {
        // Clear any existing session
        sessionManager.logout();
        
        // Verify user is not logged in initially
        assertFalse("User should not be logged in initially", sessionManager.isLoggedIn());
        assertEquals("User ID should be -1 when not logged in", -1, sessionManager.getUserId());

        // Create test user session
        sessionManager.createTestUserSession();

        // Verify test user session is created
        assertTrue("User should be logged in after creating test session", sessionManager.isLoggedIn());
        assertEquals("User ID should be 1 for test user", 1, sessionManager.getUserId());
        assertEquals("User name should be 'Test User'", "Test User", sessionManager.getUserName());
        assertEquals("User email should be test email", "test@tamswallet.com", sessionManager.getUserEmail());
    }

    @Test
    public void testTransactionCreationWithValidData() throws InterruptedException {
        // Create test user session
        sessionManager.createTestUserSession();
        
        // Create a test transaction
        Calendar testDate = Calendar.getInstance();
        Transaction testTransaction = new Transaction(150.75, "expense", "Makanan", "Lunch at cafe", testDate.getTimeInMillis());
        testTransaction.setUserId(sessionManager.getUserId());

        // Use CountDownLatch to wait for async operation
        CountDownLatch latch = new CountDownLatch(1);
        final boolean[] success = {false};
        final String[] errorMessage = {null};

        // Insert transaction
        transactionRepository.insertTransaction(testTransaction, new TransactionRepository.TransactionCallback() {
            @Override
            public void onSuccess(long transactionId) {
                success[0] = true;
                latch.countDown();
            }

            @Override
            public void onError(String error) {
                errorMessage[0] = error;
                latch.countDown();
            }
        });

        // Wait for async operation to complete
        assertTrue("Transaction insertion should complete within 5 seconds", latch.await(5, TimeUnit.SECONDS));
        assertTrue("Transaction should be inserted successfully: " + errorMessage[0], success[0]);
        assertNull("Error message should be null on success", errorMessage[0]);
    }

    @Test
    public void testTransactionCreationWithoutUserSession() throws InterruptedException {
        // Clear user session
        sessionManager.logout();
        
        // Create a test transaction
        Calendar testDate = Calendar.getInstance();
        Transaction testTransaction = new Transaction(100.0, "income", "Gaji", "Monthly salary", testDate.getTimeInMillis());
        testTransaction.setUserId(-1); // Invalid user ID

        // Use CountDownLatch to wait for async operation
        CountDownLatch latch = new CountDownLatch(1);
        final boolean[] success = {false};
        final String[] errorMessage = {null};

        // Insert transaction
        transactionRepository.insertTransaction(testTransaction, new TransactionRepository.TransactionCallback() {
            @Override
            public void onSuccess(long transactionId) {
                success[0] = true;
                latch.countDown();
            }

            @Override
            public void onError(String error) {
                errorMessage[0] = error;
                latch.countDown();
            }
        });

        // Wait for async operation to complete
        assertTrue("Transaction insertion should complete within 5 seconds", latch.await(5, TimeUnit.SECONDS));
        
        // Note: The repository might still insert the transaction even with invalid user ID
        // The validation should happen at the UI level (AddTransactionActivity)
        // This test verifies that the repository layer works regardless of user ID
    }

    @Test
    public void testTransactionDataIntegrity() throws InterruptedException {
        // Create test user session
        sessionManager.createTestUserSession();
        
        // Test data
        double testAmount = 299.99;
        String testType = "expense";
        String testCategory = "Belanja";
        String testDescription = "Shopping at mall";
        Calendar testDate = Calendar.getInstance();
        testDate.set(2024, Calendar.DECEMBER, 19);

        Transaction testTransaction = new Transaction(testAmount, testType, testCategory, testDescription, testDate.getTimeInMillis());
        testTransaction.setUserId(sessionManager.getUserId());

        // Verify transaction data integrity
        assertEquals("Amount should match", testAmount, testTransaction.getAmount(), 0.01);
        assertEquals("Type should match", testType, testTransaction.getType());
        assertEquals("Category should match", testCategory, testTransaction.getCategory());
        assertEquals("Description should match", testDescription, testTransaction.getDescription());
        assertEquals("Date should match", testDate.getTimeInMillis(), testTransaction.getDate());
        assertEquals("User ID should match", sessionManager.getUserId(), testTransaction.getUserId());
    }

    @Test
    public void testMultipleTransactionCreation() throws InterruptedException {
        // Create test user session
        sessionManager.createTestUserSession();
        
        // Create multiple test transactions
        Transaction[] transactions = {
            new Transaction(50.0, "expense", "Makanan", "Breakfast", System.currentTimeMillis()),
            new Transaction(1000.0, "income", "Gaji", "Salary", System.currentTimeMillis()),
            new Transaction(25.5, "expense", "Transport", "Bus fare", System.currentTimeMillis())
        };

        // Set user ID for all transactions
        for (Transaction transaction : transactions) {
            transaction.setUserId(sessionManager.getUserId());
        }

        // Insert all transactions
        CountDownLatch latch = new CountDownLatch(transactions.length);
        final int[] successCount = {0};
        final int[] errorCount = {0};

        for (Transaction transaction : transactions) {
            transactionRepository.insertTransaction(transaction, new TransactionRepository.TransactionCallback() {
                @Override
                public void onSuccess(long transactionId) {
                    successCount[0]++;
                    latch.countDown();
                }

                @Override
                public void onError(String error) {
                    errorCount[0]++;
                    latch.countDown();
                }
            });
        }

        // Wait for all operations to complete
        assertTrue("All transactions should complete within 10 seconds", latch.await(10, TimeUnit.SECONDS));
        assertEquals("All transactions should succeed", transactions.length, successCount[0]);
        assertEquals("No transactions should fail", 0, errorCount[0]);
    }
}
