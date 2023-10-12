package com.example.biblioteca.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biblioteca.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText emailText, passwordText, confirmPasswordText;
    Button createAccountButton;
    ProgressBar progressBar;
    TextView loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailText = (EditText) findViewById(R.id.txtEmail);
        passwordText = (EditText) findViewById(R.id.txtPassword);
        confirmPasswordText = (EditText) findViewById(R.id.txtConfirmPassword);
        createAccountButton = (Button) findViewById(R.id.btnLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loginButton = (TextView) findViewById(R.id.txtLoginbtn);

        createAccountButton.setOnClickListener(v->createAccount());
        loginButton.setOnClickListener( v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    void createAccount(){
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Todos los campos son requeridos!", Toast.LENGTH_SHORT).show();
        }
        else {
            boolean isValidated = validateData(email, password, confirmPassword);
            if (!isValidated) {
                return;
            }
            createAccountInFirebase(email, password);
        }
    }

    boolean validateData(String email, String password, String confirmPassword){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Email invalido");
            return false;
        }
        if(password.length() < 6){
            passwordText.setError("Contraseña debe tener al menos 6 caracteres");
            return false;
        }
        if(!password.equals(confirmPassword)){
            confirmPasswordText.setError("Las contraseñas no coinciden");
            return false;
        }
        return true;
    }

    void createAccountInFirebase (String email, String password) {
        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Cuenta creada correctamente, verificar Email", Toast.LENGTH_SHORT).show();
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                }
                else{
                    Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void changeInProgress (boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            createAccountButton.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            createAccountButton.setVisibility(View.VISIBLE);
        }
    }
}