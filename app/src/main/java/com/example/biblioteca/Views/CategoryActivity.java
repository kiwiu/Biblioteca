package com.example.biblioteca.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.biblioteca.R;

public class CategoryActivity extends AppCompatActivity {

    private EditText editTextNombre;
    private EditText editTextDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //Conectar lo del diseño con el codigo
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);

        Button buttonGuardar = findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes obtener los valores de los campos y guardarlos en tu base de datos
                String nombre = editTextNombre.getText().toString();
                String descripcion = editTextDescripcion.getText().toString();


                // Verifica si los campos están vacíos
                if (nombre.isEmpty() || descripcion.isEmpty()) {
                    Toast.makeText( CategoryActivity.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
    }
}