package com.example.quizdeculturegnrale.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.quizdeculturegnrale.dao.QuestionDao;
import com.example.quizdeculturegnrale.dao.ScoreDao;
import com.example.quizdeculturegnrale.dao.UserDao;
import com.example.quizdeculturegnrale.model.Question;
import com.example.quizdeculturegnrale.model.Score;
import com.example.quizdeculturegnrale.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Question.class, Score.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "quiz_db";
    private static volatile AppDatabase instance;

    public abstract UserDao userDao();
    public abstract QuestionDao questionDao();
    public abstract ScoreDao scoreDao();

    private static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create a temporary table with the new schema
            database.execSQL("CREATE TABLE IF NOT EXISTS users_temp (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, username TEXT NOT NULL, email TEXT NOT NULL, password TEXT NOT NULL, address TEXT)");
            
            // Copy data from the old table to the new table
            database.execSQL("INSERT INTO users_temp (id, username, email, password) SELECT id, username, email, password FROM users");
            
            // Drop the old table
            database.execSQL("DROP TABLE users");
            
            // Rename the temporary table to the original name
            database.execSQL("ALTER TABLE users_temp RENAME TO users");
        }
    };

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                DATABASE_NAME
            )
            .addMigrations(MIGRATION_1_2)
            .build();
        }
        return instance;
    }

    public static ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }
}
