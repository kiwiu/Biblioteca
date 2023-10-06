package com.example.biblioteca.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.biblioteca.R;

public class UserActivity extends AppCompatActivity {
    private EditText editTextNombre;
    private EditText editTextNumBiblioteca;
    private EditText editTextDireccion;
    private EditText editTextTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Conectar lo del diseño con el codigo
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextNumBiblioteca = findViewById(R.id.editTextNumeroBiblioteca);
        editTextDireccion = findViewById(R.id.editTextDireccion);
        editTextTelefono = findViewById(R.id.editTextTelefono);

        Button buttonGuardar = findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes obtener los valores de los campos y guardarlos en tu base de datos
                String nombre = editTextNombre.getText().toString();
                String numBiblio = editTextNumBiblioteca.getText().toString();
                String direccion = editTextDireccion.getText().toString();
                String telefono = editTextTelefono.getFontFeatureSettings();

                // Verifica si los campos están vacíos
                if (nombre.isEmpty() || numBiblio.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
                    Toast.makeText(UserActivity.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
    }
}