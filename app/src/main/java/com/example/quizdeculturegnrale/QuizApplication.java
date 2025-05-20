package com.example.quizdeculturegnrale;

import android.app.Application;
import android.util.Log;
import com.example.quizdeculturegnrale.db.DatabaseInitializer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizApplication extends Application {
    private static final String TAG = "QuizApplication";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Initializing application...");
        
        // Initialize database in background
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Initializing database...");
                DatabaseInitializer.initializeDatabase(this);
                Log.d(TAG, "Database initialization completed");
            } catch (Exception e) {
                Log.e(TAG, "Error initializing database: " + e.getMessage());
            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        executorService.shutdown();
    }
} 