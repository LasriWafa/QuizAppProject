package com.example.quizdeculturegnrale.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizdeculturegnrale.R;
import com.example.quizdeculturegnrale.repository.UserRepository;
import com.example.quizdeculturegnrale.repository.ScoreRepository;
import com.example.quizdeculturegnrale.model.User;
import com.example.quizdeculturegnrale.model.Score;
import com.example.quizdeculturegnrale.ui.map.MapActivity;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final int MAP_REQUEST_CODE = 1001;

    private TextView usernameText;
    private TextView emailText;
    private TextView addressText;
    private TextView scoresText;
    private Button editProfileButton;
    private Button openMapButton;
    private long userId;
    private UserRepository userRepository;
    private ScoreRepository scoreRepository;

    private final ActivityResultLauncher<Intent> mapLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String selectedAddress = result.getData().getStringExtra("selected_address");
                    if (selectedAddress != null) {
                        addressText.setText("Adresse : " + selectedAddress);
                        userRepository.updateUserAddress(userId, selectedAddress, new UserRepository.OnUserOperationCallback() {
                            @Override
                            public void onSuccess(long userId) {
                                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, 
                                    "Adresse mise à jour avec succès", Toast.LENGTH_SHORT).show());
                            }

                            @Override
                            public void onError(String error) {
                                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, 
                                    "Erreur lors de la mise à jour de l'adresse: " + error, 
                                    Toast.LENGTH_SHORT).show());
                            }
                        });
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);
        addressText = findViewById(R.id.addressText);
        scoresText = findViewById(R.id.scoresText);
        editProfileButton = findViewById(R.id.btn_edit_profile);
        openMapButton = findViewById(R.id.btn_open_map);

        userId = getIntent().getLongExtra("userId", -1);
        if (userId == -1) {
            finish();
            return;
        }

        userRepository = new UserRepository(getApplication());
        scoreRepository = new ScoreRepository(getApplication());
        
        loadUserData();
        loadUserScores();

        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        openMapButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            mapLauncher.launch(intent);
        });
    }

    private void loadUserData() {
        userRepository.getUserById(userId, new UserRepository.OnUserDataCallback() {
            @Override
            public void onSuccess(User user) {
                usernameText.setText("Nom d'utilisateur : " + user.getUsername());
                emailText.setText("Email : " + user.getEmail());
                addressText.setText("Adresse : " + (user.getAddress() != null ? user.getAddress() : "Non définie"));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ProfileActivity.this, "Erreur chargement données : " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserScores() {
        scoreRepository.getUserScores(userId, new ScoreRepository.OnScoresCallback() {
            @Override
            public void onSuccess(List<Score> scores) {
                if (scores.isEmpty()) {
                    scoresText.setText("Aucun quiz complété");
                    return;
                }

                StringBuilder scoreHistory = new StringBuilder();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                
                for (Score score : scores) {
                    int percentage = (score.getScore() * 100) / score.getTotalQuestions();
                    scoreHistory.append(String.format("• %s - %s\n   Score: %d/%d (%d%%)\n\n",
                            score.getCategory(),
                            score.getDate().format(formatter),
                            score.getScore(),
                            score.getTotalQuestions(),
                            percentage));
                }
                
                scoresText.setText(scoreHistory.toString());
            }

            @Override
            public void onError(String error) {
                scoresText.setText("Erreur lors du chargement des scores");
                Toast.makeText(ProfileActivity.this, "Erreur : " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
        loadUserScores();
    }
}
