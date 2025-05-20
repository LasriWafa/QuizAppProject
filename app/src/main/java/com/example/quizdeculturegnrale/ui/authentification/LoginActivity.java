package com.example.quizdeculturegnrale.ui.authentification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizdeculturegnrale.MainActivity;
import com.example.quizdeculturegnrale.R;
import com.example.quizdeculturegnrale.repository.UserRepository;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity implements UserRepository.OnUserOperationCallback {
    private static final String TAG = "LoginActivity";
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button registerButton;
    private UserRepository userRepository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Starting LoginActivity");
        try {
            setContentView(R.layout.activity_login);
            Log.d(TAG, "onCreate: Layout set successfully");

            // Initialize views
            emailInput = findViewById(R.id.emailInput);
            passwordInput = findViewById(R.id.passwordInput);
            loginButton = findViewById(R.id.loginButton);
            registerButton = findViewById(R.id.registerButton);
            Log.d(TAG, "onCreate: Views initialized");

            // Initialize repository
            userRepository = new UserRepository(getApplication());
            Log.d(TAG, "onCreate: Repository initialized");

            // Set up click listeners
            loginButton.setOnClickListener(v -> login());
            registerButton.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            });
            Log.d(TAG, "onCreate: Click listeners set up");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Error initializing LoginActivity", e);
            Toast.makeText(this, "Error initializing app: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void login() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate input
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        // Attempt login in background
        executorService.execute(() -> {
            try {
                userRepository.login(email, password, this);
            } catch (Exception e) {
                Log.e(TAG, "Error during login: " + e.getMessage());
                runOnUiThread(() -> {
                    loginButton.setEnabled(true);
                    loginButton.setText("Login");
                    Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    public void onSuccess(long userId) {
        runOnUiThread(() -> {
            try {
                // Hide loading state
                loginButton.setEnabled(true);
                loginButton.setText("Login");

                // Show success message
                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                // Navigate to main activity with user ID
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Log.e(TAG, "Error navigating to MainActivity", e);
                Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onError(String error) {
        runOnUiThread(() -> {
            // Hide loading state
            loginButton.setEnabled(true);
            loginButton.setText("Login");

            // Show error message
            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: LoginActivity started");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: LoginActivity resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: LoginActivity paused");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: LoginActivity stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
        Log.d(TAG, "onDestroy: LoginActivity destroyed");
    }
}