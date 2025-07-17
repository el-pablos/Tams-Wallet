package com.tamswallet.app.data.repository;

import android.content.Context;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tamswallet.app.data.database.TamsWalletDatabase;
import com.tamswallet.app.data.model.Transaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Integration tests for TransactionRepository
 */
@RunWith(AndroidJUnit4.class)
public class TransactionRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private TamsWalletDatabase database;
    private TransactionRepository transactionRepository;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, TamsWalletDatabase.class)
                .allowMainThreadQueries()
                .build();
        transactionRepository = TransactionRepository.getInstance(context);
    }

    @After
    public void tearDown() {
        database.close();
        transactionRepository.shutdown();
    }

    @Test
    public void testInsertTransaction() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        final long[] transactionId = new long[1];
        final String[] errorMessage = new String[1];

        Transaction transaction = new Transaction(100.0, "income", "Salary", "Monthly salary", System.currentTimeMillis());

        transactionRepository.insertTransaction(transaction, new TransactionRepository.TransactionCallback() {
            @Override
            public void onSuccess(long id) {
                transactionId[0] = id;
                latch.countDown();
            }

            @Override
            public void onError(String error) {
                errorMessage[0] = error;
                latch.countDown();
            }
        });

        assertTrue("Transaction insertion should complete within timeout", latch.await(5, TimeUnit.SECONDS));
        assertTrue("Transaction ID should be positive", transactionId[0] > 0);
        assertNull("Error message should be null on success", errorMessage[0]);
    }

    @Test
    public void testGetAllTransactions() throws InterruptedException {
        // Insert test transactions
        Transaction transaction1 = new Transaction(100.0, "income", "Salary", "Monthly salary", System.currentTimeMillis());
        Transaction transaction2 = new Transaction(50.0, "expense", "Food", "Lunch", System.currentTimeMillis());

        CountDownLatch insertLatch = new CountDownLatch(2);

        transactionRepository.insertTransaction(transaction1, new TransactionRepository.TransactionCallback() {
            @Override
            public void onSuccess(long id) {
                insertLatch.countDown();
            }

            @Override
            public void onError(String error) {
                fail("Transaction insertion should succeed: " + error);
                insertLatch.countDown();
            }
        });

        transactionRepository.insertTransaction(transaction2, new TransactionRepository.TransactionCallback() {
            @Override
            public void onSuccess(long id) {
                insertLatch.countDown();
            }

            @Override
            public void onError(String error) {
                fail("Transaction insertion should succeed: " + error);
                insertLatch.countDown();
            }
        });

        assertTrue("Transaction insertions should complete", insertLatch.await(5, TimeUnit.SECONDS));

        // Test getting all transactions
        CountDownLatch getLatch = new CountDownLatch(1);
        final List<Transaction>[] transactions = new List[1];

        LiveData<List<Transaction>> transactionsLiveData = transactionRepository.getAllTransactions();
        transactionsLiveData.observeForever(new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactionList) {
                if (transactionList != null && transactionList.size() >= 2) {
                    transactions[0] = transactionList;
                    transactionsLiveData.removeObserver(this);
                    getLatch.countDown();
                }
            }
        });

        assertTrue("Getting transactions should complete within timeout", getLatch.await(5, TimeUnit.SECONDS));
        assertNotNull("Transactions list should not be null", transactions[0]);
        assertTrue("Should have at least 2 transactions", transactions[0].size() >= 2);
    }

    @Test
    public void testUpdateTransaction() throws InterruptedException {
        // Insert a transaction first
        CountDownLatch insertLatch = new CountDownLatch(1);
        final long[] transactionId = new long[1];

        Transaction originalTransaction = new Transaction(100.0, "income", "Salary", "Monthly salary", System.currentTimeMillis());

        transactionRepository.insertTransaction(originalTransaction, new TransactionRepository.TransactionCallback() {
            @Override
            public void onSuccess(long id) {
                transactionId[0] = id;
                insertLatch.countDown();
            }

            @Override
            public void onError(String error) {
                fail("Transaction insertion should succeed: " + error);
                insertLatch.countDown();
            }
        });

        assertTrue("Transaction insertion should complete", insertLatch.await(5, TimeUnit.SECONDS));

        // Update the transaction
        CountDownLatch updateLatch = new CountDownLatch(1);
        final boolean[] updateSuccess = new boolean[1];
        final String[] updateError = new String[1];

        Transaction updatedTransaction = new Transaction(150.0, "income", "Bonus", "Performance bonus", System.currentTimeMillis());
        updatedTransaction.setId(transactionId[0]);

        transactionRepository.updateTransaction(updatedTransaction, new TransactionRepository.UpdateCallback() {
            @Override
            public void onSuccess() {
                updateSuccess[0] = true;
                updateLatch.countDown();
            }

            @Override
            public void onError(String error) {
                updateError[0] = error;
                updateLatch.countDown();
            }
        });

        assertTrue("Transaction update should complete within timeout", updateLatch.await(5, TimeUnit.SECONDS));
        assertTrue("Update should succeed", updateSuccess[0]);
        assertNull("Update error should be null on success", updateError[0]);
    }

    @Test
    public void testDeleteTransaction() throws InterruptedException {
        // Insert a transaction first
        CountDownLatch insertLatch = new CountDownLatch(1);
        final long[] transactionId = new long[1];

        Transaction transaction = new Transaction(100.0, "expense", "Food", "Lunch", System.currentTimeMillis());

        transactionRepository.insertTransaction(transaction, new TransactionRepository.TransactionCallback() {
            @Override
            public void onSuccess(long id) {
                transactionId[0] = id;
                insertLatch.countDown();
            }

            @Override
            public void onError(String error) {
                fail("Transaction insertion should succeed: " + error);
                insertLatch.countDown();
            }
        });

        assertTrue("Transaction insertion should complete", insertLatch.await(5, TimeUnit.SECONDS));

        // Delete the transaction
        CountDownLatch deleteLatch = new CountDownLatch(1);
        final boolean[] deleteSuccess = new boolean[1];
        final String[] deleteError = new String[1];

        transactionRepository.deleteTransaction(transactionId[0], new TransactionRepository.DeleteCallback() {
            @Override
            public void onSuccess() {
                deleteSuccess[0] = true;
                deleteLatch.countDown();
            }

            @Override
            public void onError(String error) {
                deleteError[0] = error;
                deleteLatch.countDown();
            }
        });

        assertTrue("Transaction deletion should complete within timeout", deleteLatch.await(5, TimeUnit.SECONDS));
        assertTrue("Delete should succeed", deleteSuccess[0]);
        assertNull("Delete error should be null on success", deleteError[0]);
    }

    @Test
    public void testGetTransactionsByType() throws InterruptedException {
        // Insert transactions of different types
        Transaction incomeTransaction = new Transaction(1000.0, "income", "Salary", "Monthly salary", System.currentTimeMillis());
        Transaction expenseTransaction = new Transaction(50.0, "expense", "Food", "Lunch", System.currentTimeMillis());

        CountDownLatch insertLatch = new CountDownLatch(2);

        transactionRepository.insertTransaction(incomeTransaction, new TransactionRepository.TransactionCallback() {
            @Override
            public void onSuccess(long id) {
                insertLatch.countDown();
            }

            @Override
            public void onError(String error) {
                fail("Income transaction insertion should succeed: " + error);
                insertLatch.countDown();
            }
        });

        transactionRepository.insertTransaction(expenseTransaction, new TransactionRepository.TransactionCallback() {
            @Override
            public void onSuccess(long id) {
                insertLatch.countDown();
            }

            @Override
            public void onError(String error) {
                fail("Expense transaction insertion should succeed: " + error);
                insertLatch.countDown();
            }
        });

        assertTrue("Transaction insertions should complete", insertLatch.await(5, TimeUnit.SECONDS));

        // Test getting income transactions
        CountDownLatch getLatch = new CountDownLatch(1);
        final List<Transaction>[] incomeTransactions = new List[1];

        LiveData<List<Transaction>> incomeTransactionsLiveData = transactionRepository.getTransactionsByType("income");
        incomeTransactionsLiveData.observeForever(new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactionList) {
                if (transactionList != null && transactionList.size() >= 1) {
                    incomeTransactions[0] = transactionList;
                    incomeTransactionsLiveData.removeObserver(this);
                    getLatch.countDown();
                }
            }
        });

        assertTrue("Getting income transactions should complete within timeout", getLatch.await(5, TimeUnit.SECONDS));
        assertNotNull("Income transactions list should not be null", incomeTransactions[0]);
        assertTrue("Should have at least 1 income transaction", incomeTransactions[0].size() >= 1);
        assertEquals("Transaction should be income type", "income", incomeTransactions[0].get(0).getType());
    }
}
