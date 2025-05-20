package com.example.quizdeculturegnrale.repository;

import android.app.Application;
import android.util.Log;
import com.example.quizdeculturegnrale.dao.QuestionDao;
import com.example.quizdeculturegnrale.dao.ScoreDao;
import com.example.quizdeculturegnrale.db.AppDatabase;
import com.example.quizdeculturegnrale.model.Question;
import com.example.quizdeculturegnrale.model.Score;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizRepository {
    private static final String TAG = "QuizRepository";
    private final QuestionDao questionDao;
    private final ScoreDao scoreDao;
    private final ExecutorService executorService;

    public interface OnQuestionsLoadedCallback {
        void onQuestionsLoaded(List<Question> questions);
        void onError(String error);
    }

    public QuizRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        questionDao = db.questionDao();
        scoreDao = db.scoreDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void getQuestionsByCategory(String category, OnQuestionsLoadedCallback callback) {
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Loading questions for category: " + category);
                List<Question> questions = questionDao.getQuestionsByCategory(category);
                Log.d(TAG, "Loaded " + questions.size() + " questions");
                callback.onQuestionsLoaded(questions);
            } catch (Exception e) {
                Log.e(TAG, "Error loading questions: " + e.getMessage());
                callback.onError("Error loading questions: " + e.getMessage());
            }
        });
    }

    public void saveScore(long userId, String category, int score, int totalQuestions) {
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Saving score for user " + userId + " in category " + category);
                Score scoreObj = new Score(userId, category, score, totalQuestions, LocalDate.now());
                scoreDao.insert(scoreObj);
                Log.d(TAG, "Score saved successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error saving score: " + e.getMessage());
                throw e;
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
} 