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
import android.widget.Toast;

import com.example.biblioteca.R;

public class BookActivity extends AppCompatActivity {

    //Declaracion de variables
    private EditText editTextTitulo;
    private EditText editTextAutor;
    private EditText editTextTGenero;
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
        editTextTGenero = findViewById(R.id.editTextGenero);
        editTextAnio = findViewById(R.id.editTextAnioPublicacion);

        Button buttonGuardar = findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes obtener los valores de los campos y guardarlos en tu base de datos
                String titulo = editTextTitulo.getText().toString();
                String autor = editTextAutor.getText().toString();

                // Verifica si los campos están vacíos
                if (titulo.isEmpty() || autor.isEmpty()) {
                    Toast.makeText(BookActivity.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                } else {

                }
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