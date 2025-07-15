package com.tamswallet.app.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.tamswallet.app.data.model.User;

@Dao
public interface UserDao {
    
    @Query("SELECT * FROM users WHERE email = :email")
    LiveData<User> getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :id")
    LiveData<User> getUserById(long id);

    @Insert
    long insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    // Authentication methods
    @Query("SELECT * FROM users WHERE email = :email AND passwordHash = :passwordHash")
    LiveData<User> authenticateUser(String email, String passwordHash);
    
    @Query("SELECT * FROM users WHERE email = :email AND passwordHash = :passwordHash")
    User authenticateUserSync(String email, String passwordHash);

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    int checkEmailExists(String email);
}