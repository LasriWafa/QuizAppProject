package com.example.quizdeculturegnrale.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizdeculturegnrale.MainActivity;
import com.example.quizdeculturegnrale.R;

public class QuizResultsActivity extends AppCompatActivity {
    private TextView scoreText;
    private TextView feedbackText;
    private Button retryButton;
    private Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);

        // Initialize views
        scoreText = findViewById(R.id.scoreText);
        feedbackText = findViewById(R.id.feedbackText);
        retryButton = findViewById(R.id.retryButton);
        homeButton = findViewById(R.id.homeButton);

        // Get score from intent
        int score = getIntent().getIntExtra("score", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 10);
        String category = getIntent().getStringExtra("category");
        long userId = getIntent().getLongExtra("userId", -1);

        // Calculate percentage
        int percentage = (score * 100) / totalQuestions;

        // Display score
        scoreText.setText(percentage + "%");

        // Set feedback based on score
        String feedback;
        if (percentage >= 90) {
            feedback = "Excellent! You're a master!";
        } else if (percentage >= 70) {
            feedback = "Great job! Well done!";
        } else if (percentage >= 50) {
            feedback = "Good effort! Keep practicing!";
        } else {
            feedback = "Keep trying! You'll get better!";
        }
        feedbackText.setText(feedback);

        // Set up retry button
        retryButton.setOnClickListener(v -> {
            Intent quizIntent = new Intent(QuizResultsActivity.this, QuizActivity.class);
            quizIntent.putExtra("category", category);
            quizIntent.putExtra("userId", userId);
            startActivity(quizIntent);
            finish();
        });

        // Set up home button
        homeButton.setOnClickListener(v -> {
            Intent homeIntent = new Intent(QuizResultsActivity.this, MainActivity.class);
            homeIntent.putExtra("userId", userId);
            startActivity(homeIntent);
            finish();
        });
    }
} 