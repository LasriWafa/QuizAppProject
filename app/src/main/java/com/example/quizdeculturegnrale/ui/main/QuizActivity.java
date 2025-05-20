package com.example.quizdeculturegnrale.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.quizdeculturegnrale.R;
import com.example.quizdeculturegnrale.model.Question;
import com.example.quizdeculturegnrale.repository.QuizRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizActivity extends AppCompatActivity implements QuizRepository.OnQuestionsLoadedCallback {
    private static final String TAG = "QuizActivity";
    public static final String EXTRA_CATEGORY = "category";
    public static final String EXTRA_USER_ID = "userId";

    private TextView questionText;
    private RadioGroup answerGroup;
    private RadioButton answerButton1;
    private RadioButton answerButton2;
    private RadioButton answerButton3;
    private RadioButton answerButton4;
    private Button nextButton;
    private TextView scoreText;
    private TextView questionCountText;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private TextView progressText;

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private String category;
    private long userId;
    private QuizRepository quizRepository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "QuizActivity created");
        setContentView(R.layout.activity_quiz);

        // Get category and user ID from intent
        category = getIntent().getStringExtra(EXTRA_CATEGORY);
        userId = getIntent().getLongExtra(EXTRA_USER_ID, -1);
        Log.d(TAG, "Category: " + category + ", User ID: " + userId);

        // Validate inputs
        if (category == null || userId == -1) {
            Log.e(TAG, "Missing category or user ID");
            runOnUiThread(() -> {
                Toast.makeText(this, "Error: Missing category or user ID", Toast.LENGTH_SHORT).show();
                finish();
            });
            return;
        }

        initializeViews();
        setupToolbar();
        quizRepository = new QuizRepository(getApplication());
        loadQuestions();
    }

    private void initializeViews() {
        questionText = findViewById(R.id.questionText);
        answerGroup = findViewById(R.id.answerGroup);
        answerButton1 = findViewById(R.id.answerButton1);
        answerButton2 = findViewById(R.id.answerButton2);
        answerButton3 = findViewById(R.id.answerButton3);
        answerButton4 = findViewById(R.id.answerButton4);
        nextButton = findViewById(R.id.nextButton);
        scoreText = findViewById(R.id.scoreText);
        questionCountText = findViewById(R.id.questionCountText);
        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);

        nextButton.setOnClickListener(v -> checkAnswer());
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(category);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadQuestions() {
        quizRepository.getQuestionsByCategory(category, this);
    }

    @Override
    public void onQuestionsLoaded(List<Question> loadedQuestions) {
        questions = loadedQuestions;
        if (questions.isEmpty()) {
            runOnUiThread(() -> {
                Toast.makeText(this, "No questions available for this category", Toast.LENGTH_SHORT).show();
                finish();
            });
            return;
        }
        runOnUiThread(this::displayQuestion);
    }

    @Override
    public void onError(String error) {
        runOnUiThread(() -> {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void displayQuestion() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        questionText.setText(currentQuestion.getQuestionText());
        
        String[] options = currentQuestion.getOptions();
        answerButton1.setText(options[0]);
        answerButton2.setText(options[1]);
        answerButton3.setText(options[2]);
        answerButton4.setText(options[3]);

        // Reset answer buttons
        answerButton1.setEnabled(true);
        answerButton2.setEnabled(true);
        answerButton3.setEnabled(true);
        answerButton4.setEnabled(true);
        
        // Reset backgrounds
        answerButton1.setBackgroundResource(android.R.drawable.btn_default);
        answerButton2.setBackgroundResource(android.R.drawable.btn_default);
        answerButton3.setBackgroundResource(android.R.drawable.btn_default);
        answerButton4.setBackgroundResource(android.R.drawable.btn_default);

        answerGroup.clearCheck();
        nextButton.setText("Next");
        nextButton.setOnClickListener(v -> checkAnswer());
        updateProgress();
    }

    private void checkAnswer() {
        int selectedId = answerGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        Question currentQuestion = questions.get(currentQuestionIndex);
        int selectedAnswer = getSelectedAnswerIndex(selectedId);
        boolean isCorrect = selectedAnswer == currentQuestion.getCorrectOptionIndex();

        if (isCorrect) {
            score++;
            scoreText.setText("Score: " + score);
        }

        // Disable answer buttons
        answerButton1.setEnabled(false);
        answerButton2.setEnabled(false);
        answerButton3.setEnabled(false);
        answerButton4.setEnabled(false);

        // Show correct answer
        RadioButton correctButton = getAnswerButton(currentQuestion.getCorrectOptionIndex());
        correctButton.setBackgroundResource(R.drawable.correct_answer_background);

        if (!isCorrect) {
            RadioButton selectedButton = findViewById(selectedId);
            selectedButton.setBackgroundResource(R.drawable.incorrect_answer_background);
        }

        nextButton.setText(currentQuestionIndex < questions.size() - 1 ? "Next" : "Finish");
        nextButton.setOnClickListener(v -> {
            if (currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                displayQuestion();
            } else {
                finishQuiz();
            }
        });
    }

    private int getSelectedAnswerIndex(int selectedId) {
        if (selectedId == R.id.answerButton1) return 0;
        if (selectedId == R.id.answerButton2) return 1;
        if (selectedId == R.id.answerButton3) return 2;
        if (selectedId == R.id.answerButton4) return 3;
        return -1;
    }

    private RadioButton getAnswerButton(int index) {
        switch (index) {
            case 0: return answerButton1;
            case 1: return answerButton2;
            case 2: return answerButton3;
            case 3: return answerButton4;
            default: return null;
        }
    }

    private void updateProgress() {
        int progress = (currentQuestionIndex + 1) * 100 / questions.size();
        progressBar.setProgress(progress);
        progressText.setText(String.format("%d/%d", currentQuestionIndex + 1, questions.size()));
        questionCountText.setText(String.format("Question %d of %d", currentQuestionIndex + 1, questions.size()));
    }

    private void finishQuiz() {
        quizRepository.saveScore(userId, category, score, questions.size());
        Intent intent = new Intent(this, QuizResultsActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("totalQuestions", questions.size());
        intent.putExtra("category", category);
        intent.putExtra("userId", userId);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
