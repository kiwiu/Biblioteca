package com.example.biblioteca.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.biblioteca.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button registerButton;
    EditText emailEditText, passwordEditText;
    Button loginBtn;
    ProgressBar progress_Bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerButton = (Button) findViewById(R.id.btnRegisterLogin);
        emailEditText = findViewById(R.id.txtEmailLogin);
        passwordEditText = findViewById(R.id.txtPasswordLogin);
        progress_Bar = findViewById(R.id.progressBarLogin);
        loginBtn = findViewById(R.id.btnLogin);

        registerButton.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        loginBtn.setOnClickListener((v) -> loginUser());
    }

    void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        //se verifica si isValidated es false, lo que significa que los datos de inicio de sesión no son válidos según los criterios de validación
        boolean isValidated = validateData(email, password);
        if (!isValidated) {//Si los datos de inicio de sesión son válidos
            return;
        }
        //Se llama a una función llamada loginAccountInFirebase(email, password) con los valores de email y password como argumentos
        loginAccountInFirebase(email, password);
    }

    void loginAccountInFirebase(String email, String password) {
        // Se utiliza para llevar a cabo operaciones de autenticación, como el inicio de sesión
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        //cambiar el estado de la interfaz de usuario, indicando que se está llevando a cabo una operación en curso, como un inicio de sesión
        changeInProgress(true);
        //intenta iniciar sesión con la dirección de correo electrónico
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()) {//Se verifica si la operación de inicio de sesión fue exitosa
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        //si el correo esta verificado se inicia una nueva actividad
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }else{
                        // Si el correo electrónico no está verificado, se muestra un mensaje de Toast indicando que el usuario debe verificar su correo electrónico
                        Toast.makeText(LoginActivity.this, "Email no verificado!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void changeInProgress(boolean inProgress) {//Esta línea define la función, que toma un parámetro booleano llamado inprogress parámetro indica si la aplicación está en un estado de progreso o no

        if (inProgress) {
            progress_Bar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        } else {
            progress_Bar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password) {
// La función validateData probablemente contiene lógica para verificar si los datos son válidos según ciertos criterios, como si el correo electrónico tiene un formato válido o si la contraseña y su confirmación coinciden.
        //validacion de los dattos de cada input del usuario

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email invalido");
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Tamaño de contraseña muy pequeño");
            return false;
        }
        return true;
    }
}