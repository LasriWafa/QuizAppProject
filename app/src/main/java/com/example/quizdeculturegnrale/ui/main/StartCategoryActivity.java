package com.example.quizdeculturegnrale.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizdeculturegnrale.R;
import com.example.quizdeculturegnrale.ui.main.QuizActivity;

public class StartCategoryActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "extra_category";
    public static final String EXTRA_USER_ID = "userId"; // Ajouté

    private String category;
    private long userId;  // Ajouté

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_category);

        category = getIntent().getStringExtra(EXTRA_CATEGORY);
        userId = getIntent().getLongExtra(EXTRA_USER_ID, -1); // Ajouté

        ImageView startIcon = findViewById(R.id.startIcon);
        startIcon.setOnClickListener(v -> {
            Intent intent = new Intent(StartCategoryActivity.this, QuizActivity.class);
            intent.putExtra(QuizActivity.EXTRA_CATEGORY, category);
            intent.putExtra(QuizActivity.EXTRA_USER_ID, userId);
            startActivity(intent);
        });
    }
}

