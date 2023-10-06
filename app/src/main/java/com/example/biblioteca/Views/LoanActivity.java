package com.example.biblioteca.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.biblioteca.R;

public class LoanActivity extends AppCompatActivity {

    private EditText editTextLibroNombre;
    private EditText editTextFechaPrestamo;
    private EditText editTextFechaDevolucion;
    private EditText editTextUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);

        //Conectar lo del diseño con el codigo
        editTextLibroNombre = findViewById(R.id.editTextNombre);
        editTextFechaPrestamo = findViewById(R.id.editTextFechaPrestamo);
        editTextFechaDevolucion = findViewById(R.id.editTextFechaDevolucion);
        editTextUsuario = findViewById(R.id.editTextUsuario);

        Button buttonGuardar = findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes obtener los valores de los campos y guardarlos en tu base de datos
                String nombre = editTextLibroNombre.getText().toString();
                String fechaP = editTextFechaPrestamo.getText().toString();
                String fechaD = editTextFechaDevolucion.getText().toString();
                String usuario = editTextUsuario.getFontFeatureSettings();

                // Verifica si los campos están vacíos
                if (nombre.isEmpty() || fechaP.isEmpty() || fechaD.isEmpty() || usuario.isEmpty()) {
                    Toast.makeText(LoanActivity.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
    }
}