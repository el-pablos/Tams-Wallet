package com.tamswallet.app.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.tamswallet.app.data.database.TamsWalletDatabase;
import com.tamswallet.app.data.database.UserDao;
import com.tamswallet.app.data.model.User;
import com.tamswallet.app.utils.SecurityUtils;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private UserDao userDao;
    private ExecutorService executor;
    private static UserRepository instance;
    
    private UserRepository(Context context) {
        TamsWalletDatabase db = TamsWalletDatabase.getDatabase(context);
        userDao = db.userDao();
        executor = Executors.newFixedThreadPool(4);
    }
    
    public static synchronized UserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepository(context.getApplicationContext());
        }
        return instance;
    }
    
    public LiveData<User> getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }
    
    public LiveData<User> getUserById(long id) {
        return userDao.getUserById(id);
    }
    
    public interface AuthCallback {
        void onSuccess(User user);
        void onError(String error);
    }
    
    public interface RegisterCallback {
        void onSuccess(long userId);
        void onError(String error);
    }
    
    public void authenticateUser(String email, String password, AuthCallback callback) {
        executor.execute(() -> {
            try {
                String hashedPassword = SecurityUtils.hashPassword(password);
                User user = userDao.authenticateUserSync(email, hashedPassword);
                
                if (user != null) {
                    callback.onSuccess(user);
                } else {
                    callback.onError("Email atau password tidak valid");
                }
            } catch (Exception e) {
                callback.onError("Terjadi kesalahan saat login: " + e.getMessage());
            }
        });
    }
    
    public void registerUser(String name, String email, String password, RegisterCallback callback) {
        executor.execute(() -> {
            try {
                // Check if email already exists
                int existingCount = userDao.checkEmailExists(email);
                if (existingCount > 0) {
                    callback.onError("Email sudah terdaftar");
                    return;
                }
                
                // Hash password and create user
                String hashedPassword = SecurityUtils.hashPassword(password);
                User user = new User(email, name, hashedPassword);
                
                long userId = userDao.insert(user);
                callback.onSuccess(userId);
                
            } catch (Exception e) {
                callback.onError("Terjadi kesalahan saat registrasi: " + e.getMessage());
            }
        });
    }
    
    public void updateUser(User user) {
        executor.execute(() -> userDao.update(user));
    }
    
    public void deleteUser(User user) {
        executor.execute(() -> userDao.delete(user));
    }
    
    public void checkEmailExists(String email, EmailCheckCallback callback) {
        executor.execute(() -> {
            int count = userDao.checkEmailExists(email);
            callback.onResult(count > 0);
        });
    }
    
    public interface EmailCheckCallback {
        void onResult(boolean exists);
    }
}