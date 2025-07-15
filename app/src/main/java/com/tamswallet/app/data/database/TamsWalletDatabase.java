package com.tamswallet.app.data.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import com.tamswallet.app.data.model.Transaction;
import com.tamswallet.app.data.model.Budget;
import com.tamswallet.app.data.model.User;
import com.tamswallet.app.utils.Converters;

@Database(
    entities = {Transaction.class, Budget.class, User.class},
    version = 1,
    exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class TamsWalletDatabase extends RoomDatabase {
    
    // DAO abstract methds
    public abstract TransactionDao transactionDao();
    public abstract BudgetDao budgetDao();
    public abstract UserDao userDao();

    // Singleton instance
    private static volatile TamsWalletDatabase INSTANCE;

    public static TamsWalletDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TamsWalletDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TamsWalletDatabase.class, "tamswallet_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}