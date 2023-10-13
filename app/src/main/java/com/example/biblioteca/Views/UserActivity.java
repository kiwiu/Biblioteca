package com.example.biblioteca.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.biblioteca.Models.Genero;
import com.example.biblioteca.Models.Usuario;
import com.example.biblioteca.R;
import com.example.biblioteca.Utilities.UtilityUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class UserActivity extends AppCompatActivity {
    private EditText editTextNombre;
    private EditText editTextNumBiblioteca;

    private  Button buttonGuardar;
    private EditText editTextDireccion;
    private EditText editTextTelefono;

    String usuarioId, nombre, numBiblio, direccion, telefono;

    boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Conectar lo del diseño con el codigo
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextNumBiblioteca = findViewById(R.id.editTextNumeroBiblioteca);
        editTextDireccion = findViewById(R.id.editTextDireccion);
        editTextTelefono = findViewById(R.id.editTextTelefono);

        buttonGuardar = findViewById(R.id.buttonGuardar);

        nombre = getIntent().getStringExtra("nombre");
        numBiblio = getIntent().getStringExtra("biblioteca");
        direccion = getIntent().getStringExtra("direccion");
        telefono = getIntent().getStringExtra("telefono");
        usuarioId = getIntent().getStringExtra("usuarioId");

        if (usuarioId != null && !usuarioId.isEmpty()) {
            isEdit = true;
        }

        editTextNombre.setText(nombre);
        editTextNumBiblioteca.setText(numBiblio);
        editTextDireccion.setText(direccion);
        editTextTelefono.setText(telefono);

        if (isEdit) {
            buttonGuardar.setText("Actualizar");
        }
        buttonGuardar.setOnClickListener( v -> saveUser() );
    }

    private void saveUser() {
        String nombre = editTextNombre.getText().toString();
        String numBiblio = editTextNumBiblioteca.getText().toString();
        String direccion = editTextDireccion.getText().toString();
        String telefono = editTextTelefono.getText().toString();

        if (nombre.isEmpty() || numBiblio.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

            // Validar el formato del número de teléfono "0000-0000"
        if (!telefono.matches("\\d{4}-\\d{4}")) {
            Toast.makeText(this, "Formato de teléfono inválido. Debe ser '0000-0000'", Toast.LENGTH_SHORT).show();
            return;
        }

            // Validar la longitud del número de teléfono
        if (telefono.length() != 9) {
            Toast.makeText(this, "Formato de teléfono inválido. Debe ser '0000-0000'", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setNumeroTelefono(telefono);
        usuario.setNumeroBiblioteca(numBiblio);
        usuario.setDireccion(direccion);

        savedUserToFirebase(usuario);
}

    private void savedUserToFirebase(Usuario usuario) {
        DocumentReference documentReference;
        if (isEdit) {
            documentReference = UtilityUsuario.getCollectionReferenceForUsers().document(usuarioId);
        } else {
            documentReference = UtilityUsuario.getCollectionReferenceForUsers().document();
        }

        documentReference.set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UserActivity.this, "Usuario guardado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UserActivity.this, "Error al guardar usuaro" , Toast.LENGTH_SHORT).show();
                }
            }});
    }

    }