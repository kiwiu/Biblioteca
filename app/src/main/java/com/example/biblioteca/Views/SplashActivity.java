package com.example.biblioteca.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.biblioteca.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                // Aquí se verifica si currentUser es null, lo que significa que el usuario no ha iniciado sesión o no está autenticado en la aplicación
                if(currentUser == null){
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }else {// significa que el usuario ya ha iniciado sesión y está autenticado en la aplicación
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        }, SPLASH_DURATION);
    }
}

