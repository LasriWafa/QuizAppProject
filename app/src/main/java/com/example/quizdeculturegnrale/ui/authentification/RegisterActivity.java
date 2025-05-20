package com.example.quizdeculturegnrale.ui.authentification;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizdeculturegnrale.R;
import com.example.quizdeculturegnrale.model.User;
import com.example.quizdeculturegnrale.repository.UserRepository;

public class RegisterActivity extends AppCompatActivity implements UserRepository.OnUserOperationCallback {
    private EditText usernameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private Button registerButton;
    private Button loginButton;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        // Initialize repository
        userRepository = new UserRepository(getApplication());

        // Set up click listeners
        registerButton.setOnClickListener(v -> register());
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void register() {
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate input
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user object
        User user = new User(username, email, password);

        // Show loading state
        registerButton.setEnabled(false);
        registerButton.setText("Registering...");

        // Register user
        userRepository.register(user, this);
    }

    @Override
    public void onSuccess(long userId) {
        runOnUiThread(() -> {
            // Hide loading state
            registerButton.setEnabled(true);
            registerButton.setText("Register");

            // Show success message
            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

            // Navigate to login
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onError(String error) {
        runOnUiThread(() -> {
            // Hide loading state
            registerButton.setEnabled(true);
            registerButton.setText("Register");

            // Show error message
            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
        });
    }
} 