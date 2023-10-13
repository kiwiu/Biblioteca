package com.example.biblioteca.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biblioteca.Models.Genero;
import com.example.biblioteca.Models.Prestamo;
import com.example.biblioteca.R;
import com.example.biblioteca.Utilities.UtilidadesGenero;
import com.example.biblioteca.Utilities.UtilityBook;
import com.example.biblioteca.Utilities.UtilityPrestamo;
import com.example.biblioteca.Utilities.UtilityUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LoanActivity extends AppCompatActivity {

    private Spinner editTextLibroNombre;
    private EditText editTextFechaPrestamo;
    private EditText editTextFechaDevolucion;
    private Spinner editTextUsuario;

    private Button guardarPrestamo;

    Spinner spinnerLibro, spinnerUsuario;

    boolean isEditing = false;

    String nombreLibro, fechaPrestamo, fechaDevolucion, nombreUsuario, documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);

        editTextFechaPrestamo = (EditText) findViewById(R.id.editTextFechaPrestamo);
        editTextFechaDevolucion = (EditText) findViewById(R.id.editTextFechaDevolucion);
        guardarPrestamo = (Button) findViewById(R.id.buttonGuardarPrestamo);
        spinnerLibro = (Spinner) findViewById(R.id.spinerTitulo);
        spinnerUsuario = (Spinner) findViewById(R.id.spinerUsuario);


        nombreLibro = getIntent().getStringExtra("libro");
        fechaPrestamo = getIntent().getStringExtra("fechaPrestamo");
        fechaDevolucion = getIntent().getStringExtra("fechaDevolucion");
        nombreUsuario = getIntent().getStringExtra("usuario");
        documentId = getIntent().getStringExtra("documentId");

        if(documentId != null && !documentId.isEmpty()) {
            isEditing = true;
        }


        editTextFechaPrestamo.setText(fechaPrestamo);
        editTextFechaDevolucion.setText(fechaDevolucion);
        spinnerUsuario.setPrompt(nombreUsuario);
        spinnerLibro.setPrompt(nombreLibro);

        if (isEditing) {
            guardarPrestamo.setText("Actualizar");
        }

        setupSpinerLibro();
        setupSpinerUsuario();

        guardarPrestamo.setOnClickListener(v -> saveLoan());
    }



    void saveLoan () {
        String fechaDevolucion = editTextFechaDevolucion.getText().toString();
        String fechaPrestamo = editTextFechaPrestamo.getText().toString();
        String libro = spinnerLibro.getSelectedItem().toString();
        String usuario = spinnerUsuario.getSelectedItem().toString();

        if(fechaPrestamo.isEmpty() || fechaDevolucion.isEmpty() || libro.isEmpty() || usuario.isEmpty()) {
            editTextFechaPrestamo.setError("Campo requerido");
            editTextFechaDevolucion.setError("Campo requerido");
            return;
        }

        // Validar el formato de la fecha "DD/MM/YYYY"
        if (!fechaPrestamo.matches("\\d{2}/\\d{2}/\\d{4}")) {
            Toast.makeText(this, "Formato de fecha inválido. Debe ser 'DD/MM/YYYY'", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el formato de la fecha "DD/MM/YYYY"
        if (!fechaDevolucion.matches("\\d{2}/\\d{2}/\\d{4}")) {
            Toast.makeText(this, "Formato de fecha inválido. Debe ser 'DD/MM/YYYY'", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] fechaParts = fechaPrestamo.split("/");
        int day = Integer.parseInt(fechaParts[0]);
        int month = Integer.parseInt(fechaParts[1]);
        int year = Integer.parseInt(fechaParts[2]);

        // Obtener el año actual
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);

        // Realizar las validaciones
        if (day < 1 || day > 31 || month < 1 || month > 12 || year < 1800 || year > currentYear) {
            Toast.makeText(this, "Fecha inválida. Verifica el día, mes y año.", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] fechaParts1 = fechaDevolucion.split("/");
        int day1 = Integer.parseInt(fechaParts1[0]);
        int month1 = Integer.parseInt(fechaParts1[1]);
        int year1 = Integer.parseInt(fechaParts1[2]);

        // Obtener el año actual
        Calendar cal1 = Calendar.getInstance();
        int currentYear1 = cal1.get(Calendar.YEAR);

        // Realizar las validaciones
        if (day1 < 1 || day1 > 31 || month1 < 1 || month1 > 12 || year1 < 1800 || year1 > currentYear1) {
            Toast.makeText(this, "Fecha inválida. Verifica el día, mes y año.", Toast.LENGTH_SHORT).show();
            return;
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setFechaPrestamo(fechaPrestamo);
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setLibro(libro);
        prestamo.setUsuario(usuario);

        saveLoanInFirebase(prestamo);
    }

    void saveLoanInFirebase(Prestamo prestamo) {
        // Aquí se guarda la nota en la base de datos de Firebase
        DocumentReference documentReference;
        if(isEditing) {
            // Aquí se actualiza la nota en la base de datos de Firebase
            documentReference = UtilityPrestamo.getCollectionReferenceForPrestamo().document(documentId);
        }else {
            // Aquí se guarda la nota en la base de datos de Firebase
            documentReference = UtilityPrestamo.getCollectionReferenceForPrestamo().document();
        }

        documentReference.set(prestamo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Aquí se verifica si la nota se guardó correctamente en la base de datos de Firebase
                if(task.isSuccessful()) {
                    Toast.makeText(LoanActivity.this, "Prestamo guardado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    // Aquí se muestra un mensaje de error si la nota no se guardó correctamente en la base de datos de Firebase
                    Toast.makeText(LoanActivity.this, "Error al guardar el prestamo del libro", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupSpinerLibro() {
        // Obtener una referencia a la colección de "Generos"
        CollectionReference generoCollection = UtilityBook.getCollectionReferenceForBooks();

        // Recuperar los géneros de Firebase
        generoCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> libro = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String nombreLibro = document.getString("titulo");
                    libro.add(nombreLibro);
                }

                // Configurar el adaptador para el Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, libro);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerLibro.setAdapter(adapter);
            } else {
                // Manejar errores en la recuperación de datos de Firebase
                Toast.makeText(LoanActivity.this, "Error al obtener géneros", Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar un listener para el Spinner (si es necesario)
        spinnerLibro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


    private void setupSpinerUsuario() {
        // Obtener una referencia a la colección de "Generos"
        CollectionReference generoCollection = UtilityUsuario.getCollectionReferenceForUsers();

        // Recuperar los géneros de Firebase
        generoCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> user = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String nombreUsuario = document.getString("nombre");
                    user.add(nombreUsuario);
                }

                // Configurar el adaptador para el Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, user);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerUsuario.setAdapter(adapter);
            } else {
                // Manejar errores en la recuperación de datos de Firebase
                Toast.makeText(LoanActivity.this, "Error al obtener géneros", Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar un listener para el Spinner (si es necesario)
        spinnerUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
}