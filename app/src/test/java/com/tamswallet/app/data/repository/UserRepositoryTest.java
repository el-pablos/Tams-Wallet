package com.tamswallet.app.data.repository;

import android.content.Context;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tamswallet.app.data.database.TamsWalletDatabase;
import com.tamswallet.app.data.database.UserDao;
import com.tamswallet.app.data.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Integration tests for UserRepository
 */
@RunWith(AndroidJUnit4.class)
public class UserRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private TamsWalletDatabase database;
    private UserRepository userRepository;
    private UserDao userDao;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, TamsWalletDatabase.class)
                .allowMainThreadQueries()
                .build();
        userDao = database.userDao();
        userRepository = UserRepository.getInstance(context);
    }

    @After
    public void tearDown() {
        database.close();
        userRepository.shutdown();
    }

    @Test
    public void testUserRegistration() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        final long[] userId = new long[1];
        final String[] errorMessage = new String[1];

        userRepository.registerUser("Test User", "test@example.com", "password123",
                new UserRepository.RegisterCallback() {
                    @Override
                    public void onSuccess(long id) {
                        userId[0] = id;
                        latch.countDown();
                    }

                    @Override
                    public void onError(String error) {
                        errorMessage[0] = error;
                        latch.countDown();
                    }
                });

        assertTrue("Registration should complete within timeout", latch.await(5, TimeUnit.SECONDS));
        assertTrue("User ID should be positive", userId[0] > 0);
        assertNull("Error message should be null on success", errorMessage[0]);
    }

    @Test
    public void testUserLogin() throws InterruptedException {
        // First register a user
        CountDownLatch registerLatch = new CountDownLatch(1);
        userRepository.registerUser("Test User", "test@example.com", "password123",
                new UserRepository.RegisterCallback() {
                    @Override
                    public void onSuccess(long id) {
                        registerLatch.countDown();
                    }

                    @Override
                    public void onError(String error) {
                        fail("Registration should succeed: " + error);
                        registerLatch.countDown();
                    }
                });

        assertTrue("Registration should complete", registerLatch.await(5, TimeUnit.SECONDS));

        // Now test login
        CountDownLatch loginLatch = new CountDownLatch(1);
        final User[] loggedInUser = new User[1];
        final String[] loginError = new String[1];

        userRepository.loginUser("test@example.com", "password123",
                new UserRepository.LoginCallback() {
                    @Override
                    public void onSuccess(User user) {
                        loggedInUser[0] = user;
                        loginLatch.countDown();
                    }

                    @Override
                    public void onError(String error) {
                        loginError[0] = error;
                        loginLatch.countDown();
                    }
                });

        assertTrue("Login should complete within timeout", loginLatch.await(5, TimeUnit.SECONDS));
        assertNotNull("User should be returned on successful login", loggedInUser[0]);
        assertEquals("Email should match", "test@example.com", loggedInUser[0].getEmail());
        assertEquals("Name should match", "Test User", loggedInUser[0].getName());
        assertNull("Login error should be null on success", loginError[0]);
    }

    @Test
    public void testInvalidLogin() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        final User[] user = new User[1];
        final String[] errorMessage = new String[1];

        userRepository.loginUser("nonexistent@example.com", "wrongpassword",
                new UserRepository.LoginCallback() {
                    @Override
                    public void onSuccess(User u) {
                        user[0] = u;
                        latch.countDown();
                    }

                    @Override
                    public void onError(String error) {
                        errorMessage[0] = error;
                        latch.countDown();
                    }
                });

        assertTrue("Login should complete within timeout", latch.await(5, TimeUnit.SECONDS));
        assertNull("User should be null on failed login", user[0]);
        assertNotNull("Error message should be provided on failed login", errorMessage[0]);
    }

    @Test
    public void testDuplicateEmailRegistration() throws InterruptedException {
        // Register first user
        CountDownLatch firstLatch = new CountDownLatch(1);
        userRepository.registerUser("First User", "duplicate@example.com", "password123",
                new UserRepository.RegisterCallback() {
                    @Override
                    public void onSuccess(long id) {
                        firstLatch.countDown();
                    }

                    @Override
                    public void onError(String error) {
                        fail("First registration should succeed: " + error);
                        firstLatch.countDown();
                    }
                });

        assertTrue("First registration should complete", firstLatch.await(5, TimeUnit.SECONDS));

        // Try to register second user with same email
        CountDownLatch secondLatch = new CountDownLatch(1);
        final long[] secondUserId = new long[1];
        final String[] secondError = new String[1];

        userRepository.registerUser("Second User", "duplicate@example.com", "password456",
                new UserRepository.RegisterCallback() {
                    @Override
                    public void onSuccess(long id) {
                        secondUserId[0] = id;
                        secondLatch.countDown();
                    }

                    @Override
                    public void onError(String error) {
                        secondError[0] = error;
                        secondLatch.countDown();
                    }
                });

        assertTrue("Second registration should complete", secondLatch.await(5, TimeUnit.SECONDS));
        assertEquals("Second user ID should be -1 on failure", -1, secondUserId[0]);
        assertNotNull("Error message should be provided for duplicate email", secondError[0]);
        assertTrue("Error should mention email already exists", 
                secondError[0].toLowerCase().contains("email") || 
                secondError[0].toLowerCase().contains("sudah"));
    }

    @Test
    public void testUpdateUserProfile() throws InterruptedException {
        // First register a user
        CountDownLatch registerLatch = new CountDownLatch(1);
        final long[] userId = new long[1];

        userRepository.registerUser("Original Name", "update@example.com", "password123",
                new UserRepository.RegisterCallback() {
                    @Override
                    public void onSuccess(long id) {
                        userId[0] = id;
                        registerLatch.countDown();
                    }

                    @Override
                    public void onError(String error) {
                        fail("Registration should succeed: " + error);
                        registerLatch.countDown();
                    }
                });

        assertTrue("Registration should complete", registerLatch.await(5, TimeUnit.SECONDS));

        // Update user profile
        CountDownLatch updateLatch = new CountDownLatch(1);
        final boolean[] updateSuccess = new boolean[1];
        final String[] updateError = new String[1];

        User updatedUser = new User();
        updatedUser.setId(userId[0]);
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated@example.com");

        userRepository.updateUser(updatedUser, new UserRepository.UpdateCallback() {
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

        assertTrue("Update should complete within timeout", updateLatch.await(5, TimeUnit.SECONDS));
        assertTrue("Update should succeed", updateSuccess[0]);
        assertNull("Update error should be null on success", updateError[0]);
    }
}
