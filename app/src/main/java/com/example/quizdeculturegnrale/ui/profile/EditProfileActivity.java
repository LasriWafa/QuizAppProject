package com.example.quizdeculturegnrale.ui.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizdeculturegnrale.R;
import com.example.quizdeculturegnrale.model.User;
import com.example.quizdeculturegnrale.repository.UserRepository;
import com.example.quizdeculturegnrale.ui.authentification.LoginActivity;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText editEmail;
    private Button btnSaveChanges;
    private UserRepository userRepository;
    private long userId;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editUsername = findViewById(R.id.edit_username);
        editEmail = findViewById(R.id.edit_email);
        btnSaveChanges = findViewById(R.id.btn_save_changes);

        userId = getIntent().getLongExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Erreur : ID utilisateur manquant", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userRepository = new UserRepository(getApplication());
        loadUserData();

        btnSaveChanges.setOnClickListener(v -> {
            updateProfile();
        });
    }

    private void loadUserData() {
        userRepository.getUserById(userId, new UserRepository.OnUserDataCallback() {
            @Override
            public void onSuccess(User user) {
                editUsername.setText(user.getUsername());
                editEmail.setText(user.getEmail());
            }

            @Override
            public void onError(String error) {
                Toast.makeText(EditProfileActivity.this, "Erreur chargement données : " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfile() {
        String newUsername = editUsername.getText().toString().trim();
        String newEmail = editEmail.getText().toString().trim();

        if (newUsername.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get existing user to preserve password and address
        userRepository.getUserById(userId, new UserRepository.OnUserDataCallback() {
            @Override
            public void onSuccess(User existingUser) {
                User updatedUser = new User(newUsername, newEmail, existingUser.getPassword());
                updatedUser.setId(existingUser.getId());
                updatedUser.setAddress(existingUser.getAddress()); // Preserve the existing address

                userRepository.updateUserProfile(updatedUser, new UserRepository.OnUserOperationCallback() {
                    @Override
                    public void onSuccess(long userId) {
                        runOnUiThread(() -> {
                            Toast.makeText(EditProfileActivity.this, "Profil mis à jour avec succès", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            Toast.makeText(EditProfileActivity.this, "Erreur: " + error, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(EditProfileActivity.this, "Erreur: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
