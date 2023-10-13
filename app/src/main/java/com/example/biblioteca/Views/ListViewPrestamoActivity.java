package com.example.biblioteca.Views;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.biblioteca.Adapter.GeneroAdapter;
import com.example.biblioteca.Adapter.PrestamoAdapter;
import com.example.biblioteca.Models.Genero;
import com.example.biblioteca.Models.Prestamo;
import com.example.biblioteca.R;
import com.example.biblioteca.Utilities.UtilidadesGenero;
import com.example.biblioteca.Utilities.UtilityPrestamo;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class ListViewPrestamoActivity extends AppCompatActivity {

    FloatingActionButton addPrestamoButton;

    SearchView searchView;

    RecyclerView PrestamoRecyclerView;

    PrestamoAdapter prestamoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_prestamo);

        addPrestamoButton = (FloatingActionButton) findViewById(R.id.add_Prestamo_btn);
        PrestamoRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_Prestamo);
        searchView = (SearchView) findViewById(R.id.searchViewPrestamo);

        addPrestamoButton.setOnClickListener(view -> startActivity(new Intent(ListViewPrestamoActivity.this, LoanActivity.class)));
        setupRecyclerView();
        setupSearchView();
    }

    void setupRecyclerView() {
        Query query = UtilityPrestamo.getCollectionReferenceForPrestamo();
        FirestoreRecyclerOptions<Prestamo> options = new FirestoreRecyclerOptions.Builder<Prestamo>().setQuery(query, Prestamo.class).build();
        PrestamoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        prestamoAdapter = new PrestamoAdapter(options, this);
        PrestamoRecyclerView.setAdapter(prestamoAdapter);
    }

    void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryText) {
                Toast.makeText(ListViewPrestamoActivity.this, "Buscando...", Toast.LENGTH_SHORT).show();
                txtSearch(queryText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                txtSearch(newText);
                return false;
            }
        });
    }

    void txtSearch(String queryText){
        Query query = UtilityPrestamo.getCollectionReferenceForPrestamo().orderBy("libro")
                .startAt(queryText).endAt(queryText+"\uf8ff");
        FirestoreRecyclerOptions<Prestamo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Prestamo>()
                .setQuery(query, Prestamo.class).build();
        prestamoAdapter = new PrestamoAdapter(firestoreRecyclerOptions, this);
        prestamoAdapter.startListening();
        PrestamoRecyclerView.setAdapter(prestamoAdapter);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (value.isEmpty()) {
                    Toast.makeText(ListViewPrestamoActivity.this, "Sin conicidencias", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        prestamoAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        prestamoAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prestamoAdapter.notifyDataSetChanged();
    }
}