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
import com.example.biblioteca.Models.Genero;
import com.example.biblioteca.R;
import com.example.biblioteca.Utilities.UtilidadesGenero;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class ListViewGeneroActivity extends AppCompatActivity {

    FloatingActionButton addGeneroButton;

    SearchView searchView;

    RecyclerView GeneroRecyclerView;

    GeneroAdapter generoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_genero_view);

        addGeneroButton = (FloatingActionButton) findViewById(R.id.add_genero_btn);
        GeneroRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_genero);
        searchView = (SearchView) findViewById(R.id.searchViewGenero);

        addGeneroButton.setOnClickListener(view -> startActivity(new Intent(ListViewGeneroActivity.this, CategoryActivity.class)));
        setupRecyclerView();
        setupSearchView();
    }

    void setupRecyclerView() {
        Query query = UtilidadesGenero.getCollectionReferenceForGeneros();
        FirestoreRecyclerOptions<Genero> options = new FirestoreRecyclerOptions.Builder<Genero>().setQuery(query, Genero.class).build();
        GeneroRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        generoAdapter = new GeneroAdapter(options, this);
        GeneroRecyclerView.setAdapter(generoAdapter);
    }

    void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryText) {
                Toast.makeText(ListViewGeneroActivity.this, "Searching...", Toast.LENGTH_SHORT).show();
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
        Query query = UtilidadesGenero.getCollectionReferenceForGeneros().orderBy("title")
                .startAt(queryText).endAt(queryText+"\uf8ff");
        FirestoreRecyclerOptions<Genero> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Genero>()
                .setQuery(query, Genero.class).build();
        generoAdapter = new GeneroAdapter(firestoreRecyclerOptions, this);
        generoAdapter.startListening();
        GeneroRecyclerView.setAdapter(generoAdapter);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (value.isEmpty()) {
                    Toast.makeText(ListViewGeneroActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        generoAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        generoAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        generoAdapter.notifyDataSetChanged();
    }
}