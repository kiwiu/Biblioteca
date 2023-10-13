package com.example.biblioteca.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.example.biblioteca.R;
import com.example.biblioteca.Utilities.UtilidadesGenero;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;


public class BookActivity extends AppCompatActivity {

    //Declaracion de variables
    private EditText editTextTitulo;
    private EditText editTextAutor;
    private Spinner spinnerGenero;
    private EditText editTextAnio;

    private ImageView imagenLibro;
    private Button  seleccionarImagen;
    private static final int SELECT_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        //Conectar lo del diseño con el codigo
        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextAutor = findViewById(R.id.editTextAutor);
        spinnerGenero = findViewById(R.id.editTextGenero);
        editTextAnio = findViewById(R.id.editTextAnioPublicacion);
        imagenLibro = findViewById(R.id.imagenLibro);

        Button buttonGuardar = findViewById(R.id.buttonGuardar);

        // Configurar el Spinner para mostrar los géneros
        setupSpinner();

        buttonGuardar.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
    }

    private void setupSpinner() {
        // Obtener una referencia a la colección de "Generos"
        CollectionReference generoCollection = UtilidadesGenero.getCollectionReferenceForGeneros();

        // Recuperar los géneros de Firebase
        generoCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> generos = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String nombreGenero = document.getString("nombre");
                    generos.add(nombreGenero);
                }

                // Configurar el adaptador para el Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, generos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerGenero.setAdapter(adapter);
            } else {
                // Manejar errores en la recuperación de datos de Firebase
                Toast.makeText(BookActivity.this, "Error al obtener géneros", Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar un listener para el Spinner (si es necesario)
        spinnerGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Aquí puedes realizar acciones cuando se selecciona un género
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Manejar la selección de nada
            }
        });
    }

    //Para poder seleccionar una imagen de la galeria
    public void seleccionarImagen(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            try {
                // Obtener la imagen seleccionada como un Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                // Mostrar el Bitmap en el ImageView
                imagenLibro.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}