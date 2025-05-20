package com.example.quizdeculturegnrale.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.example.quizdeculturegnrale.R;
import com.example.quizdeculturegnrale.ui.authentification.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 👇 Utilise le bon fichier layout ici
        setContentView(R.layout.activity_splash_quiz);

        LottieAnimationView animationView = findViewById(R.id.lottieView);
        animationView.setAnimation(R.raw.splash_quiz);
        animationView.playAnimation();

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 3000); // 3 secondes d'affichage
    }
}
