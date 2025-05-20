package com.example.quizdeculturegnrale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizdeculturegnrale.R;
import com.example.quizdeculturegnrale.ui.main.StartCategoryActivity;
import com.example.quizdeculturegnrale.ui.profile.ProfileActivity;
import com.example.quizdeculturegnrale.ui.authentification.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Log.d(TAG, "onCreate: Starting MainActivity");

        // Get user ID from intent
        userId = getIntent().getLongExtra("userId", -1);
        Log.d(TAG, "onCreate: Received userId: " + userId);

        if (userId == -1) {
            Log.e(TAG, "No user ID provided");
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize profile button
        ImageButton profileButton = findViewById(R.id.profileButton);
        if (profileButton != null) {
            profileButton.setOnClickListener(v -> showProfileMenu(v));
            Log.d(TAG, "Profile button initialized");
        } else {
            Log.e(TAG, "Profile button not found in layout");
        }

        // Initialize category buttons
        setupCategoryButtons();
    }

    private void showProfileMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.profile_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_profile) {
                openProfile();
                return true;
            } else if (itemId == R.id.menu_logout) {
                logout();
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void logout() {
        // Navigate to login activity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupCategoryButtons() {
        int[] buttonIds = {
                R.id.sportButton,
                R.id.scienceButton,
                R.id.historyButton,
                R.id.computerButton,
                R.id.inventionButton,
                R.id.generalButton
        };

        for (int buttonId : buttonIds) {
            LinearLayout button = findViewById(buttonId);
            if (button != null) {
                button.setOnClickListener(this::onCategoryClick);
                Log.d(TAG, "Initialized category button: " + buttonId);
            } else {
                Log.e(TAG, "Category button not found: " + buttonId);
            }
        }
    }

    public void onCategoryClick(View view) {
        String category = "";
        int viewId = view.getId();
        Log.d(TAG, "Category button clicked with ID: " + viewId);

        if (viewId == R.id.sportButton) {
            category = "sport";
        } else if (viewId == R.id.scienceButton) {
            category = "science";
        } else if (viewId == R.id.historyButton) {
            category = "history";
        } else if (viewId == R.id.computerButton) {
            category = "computer";
        } else if (viewId == R.id.inventionButton) {
            category = "invention";
        } else if (viewId == R.id.generalButton) {
            category = "general";
        }

        if (!category.isEmpty()) {
            Log.d(TAG, "Starting StartCategoryActivity for category: " + category + " with userId: " + userId);
            startCategoryActivity(category);
        } else {
            Log.e(TAG, "Unknown category clicked");
            Toast.makeText(this, "Category not implemented yet", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCategoryActivity(String category) {
        try {
            Intent intent = new Intent(this, StartCategoryActivity.class);
            intent.putExtra(StartCategoryActivity.EXTRA_CATEGORY, category);
            intent.putExtra(StartCategoryActivity.EXTRA_USER_ID, userId);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error starting StartCategoryActivity: " + e.getMessage());
            Toast.makeText(this, "Error starting quiz", Toast.LENGTH_SHORT).show();
        }
    }

    private void openProfile() {
        try {
            Log.d(TAG, "Opening profile for user: " + userId);
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error opening profile: " + e.getMessage());
            Toast.makeText(this, "Error opening profile", Toast.LENGTH_SHORT).show();
        }
    }
}
