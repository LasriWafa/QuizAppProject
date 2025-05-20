package com.example.quizdeculturegnrale.repository;

import android.app.Application;

import com.example.quizdeculturegnrale.dao.ScoreDao;
import com.example.quizdeculturegnrale.db.AppDatabase;
import com.example.quizdeculturegnrale.model.Score;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ScoreRepository {
    private final ScoreDao scoreDao;
    private final ExecutorService executorService;

    public ScoreRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        scoreDao = db.scoreDao();
        executorService = AppDatabase.getDatabaseWriteExecutor();
    }

    public void saveScore(Score score, OnScoreOperationCallback callback) {
        executorService.execute(() -> {
            try {
                scoreDao.insert(score);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public void getUserScores(long userId, OnScoresCallback callback) {
        executorService.execute(() -> {
            try {
                List<Score> scores = scoreDao.getUserScores(userId);
                callback.onSuccess(scores);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public void getUserScoresByCategory(long userId, String category, OnScoresCallback callback) {
        executorService.execute(() -> {
            try {
                List<Score> scores = scoreDao.getUserScoresByCategory(userId, category);
                callback.onSuccess(scores);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public void getTopScores(OnScoresCallback callback) {
        executorService.execute(() -> {
            try {
                List<Score> scores = scoreDao.getTopScores();
                callback.onSuccess(scores);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public interface OnScoreOperationCallback {
        void onSuccess();
        void onError(String error);
    }

    public interface OnScoresCallback {
        void onSuccess(List<Score> scores);
        void onError(String error);
    }
} 