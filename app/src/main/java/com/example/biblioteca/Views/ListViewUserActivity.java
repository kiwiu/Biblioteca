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
import com.example.biblioteca.Adapter.UsuarioAdapter;
import com.example.biblioteca.Models.Genero;
import com.example.biblioteca.Models.Usuario;
import com.example.biblioteca.R;
import com.example.biblioteca.Utilities.UtilidadesGenero;
import com.example.biblioteca.Utilities.UtilityUsuario;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class ListViewUserActivity extends AppCompatActivity {

    FloatingActionButton addUserButton;

    SearchView searchView;

    RecyclerView userRecyclerView;

    UsuarioAdapter UsuarioAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_user);


        addUserButton = (FloatingActionButton) findViewById(R.id.add_user_btn);
        userRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_User);
        searchView = (SearchView) findViewById(R.id.searchViewUser);

        addUserButton.setOnClickListener(view -> startActivity(new Intent(ListViewUserActivity.this, UserActivity.class)));
        setupRecyclerView();
        setupSearchView();
    }

    void setupRecyclerView() {
        Query query = UtilityUsuario.getCollectionReferenceForUsers();
        FirestoreRecyclerOptions<Usuario> options = new FirestoreRecyclerOptions.Builder<Usuario>().setQuery(query, Usuario.class).build();
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        UsuarioAdapter = new UsuarioAdapter(options, this);
        userRecyclerView.setAdapter(UsuarioAdapter);
    }

    void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryText) {
                Toast.makeText(ListViewUserActivity.this, "Searching...", Toast.LENGTH_SHORT).show();
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
        Query query = UtilityUsuario.getCollectionReferenceForUsers().orderBy("nombre")
                .startAt(queryText).endAt(queryText+"\uf8ff");
        FirestoreRecyclerOptions<Usuario> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Usuario>()
                .setQuery(query, Usuario.class).build();
        UsuarioAdapter = new UsuarioAdapter(firestoreRecyclerOptions, this);
        UsuarioAdapter.startListening();
        userRecyclerView.setAdapter(UsuarioAdapter);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (value.isEmpty()) {
                    Toast.makeText(ListViewUserActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        UsuarioAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        UsuarioAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UsuarioAdapter.notifyDataSetChanged();
    }
}