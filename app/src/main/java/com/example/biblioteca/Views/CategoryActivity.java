package com.example.biblioteca.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.biblioteca.Models.Genero;
import com.example.biblioteca.R;
import com.example.biblioteca.Utilities.UtilidadesGenero;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class CategoryActivity extends AppCompatActivity {

    private EditText editTextNombre;
    private EditText editTextDescripcion;
    private Button guardarGenero;

    String nombre, descripcion, documentId;

    boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //Conectar lo del diseño con el codigo
        editTextNombre = (EditText) findViewById(R.id.editTextNombreGenero);
        editTextDescripcion = (EditText) findViewById(R.id.editTextDescripcion);
        guardarGenero = (Button) findViewById(R.id.buttonGuardar);

        nombre = getIntent().getStringExtra("nombre");
        descripcion = getIntent().getStringExtra("descripcion");
        documentId = getIntent().getStringExtra("documentId");

        if(documentId != null && !documentId.isEmpty()) {
            isEditing = true;
        }

        editTextNombre.setText(nombre);
        editTextDescripcion.setText(descripcion);

        if (isEditing) {
            guardarGenero.setText("Actualizar");
        }

        guardarGenero.setOnClickListener(v -> saveNote());
    }

    void saveNote () {
        String titleNote1 = editTextNombre.getText().toString();
        String contentNote1 = editTextDescripcion.getText().toString();

        if(titleNote1.isEmpty() || contentNote1.isEmpty()) {
            editTextNombre.setError("El nombre es requerido");
            editTextDescripcion.setError("La descripcion es requerida");
            return;
        }

        Genero genero = new Genero();
        genero.setNombre(titleNote1);
        genero.setDescripcion(contentNote1);

        saveNoteInFirebase(genero);
    }

    void saveNoteInFirebase(Genero genero) {
        // Aquí se guarda la nota en la base de datos de Firebase
        DocumentReference documentReference;
        if(isEditing) {
            // Aquí se actualiza la nota en la base de datos de Firebase
            documentReference = UtilidadesGenero.getCollectionReferenceForGeneros().document(documentId);
        }else {
            // Aquí se guarda la nota en la base de datos de Firebase
            documentReference = UtilidadesGenero.getCollectionReferenceForGeneros().document();
        }

        documentReference.set(genero).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Aquí se verifica si la nota se guardó correctamente en la base de datos de Firebase
                if(task.isSuccessful()) {
                    Toast.makeText(CategoryActivity.this, "Genero guardado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    // Aquí se muestra un mensaje de error si la nota no se guardó correctamente en la base de datos de Firebase
                    Toast.makeText(CategoryActivity.this, "Error al guardar nota", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}