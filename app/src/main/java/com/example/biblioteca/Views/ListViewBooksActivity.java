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

import com.example.biblioteca.Adapter.BookAdapter;
import com.example.biblioteca.Adapter.UsuarioAdapter;
import com.example.biblioteca.Models.Books;
import com.example.biblioteca.Models.Usuario;
import com.example.biblioteca.R;
import com.example.biblioteca.Utilities.UtilityBook;
import com.example.biblioteca.Utilities.UtilityUsuario;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class ListViewBooksActivity extends AppCompatActivity {

    FloatingActionButton addBookButton;

    SearchView searchView;

    RecyclerView bookRecyclerView;

    BookAdapter BookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_books);

        addBookButton = (FloatingActionButton) findViewById(R.id.add_Libros_btn);
        bookRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_Libros);
        searchView = (SearchView) findViewById(R.id.searchViewLibros);

        addBookButton.setOnClickListener(view -> startActivity(new Intent(ListViewBooksActivity.this, BookActivity.class)));
        setupRecyclerView();
        setupSearchView();
    }

    void setupRecyclerView() {
        Query query = UtilityBook.getCollectionReferenceForBooks();
        FirestoreRecyclerOptions<Books> options = new FirestoreRecyclerOptions.Builder<Books>().setQuery(query, Books.class).build();
        bookRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        BookAdapter = new BookAdapter(options, this);
        bookRecyclerView.setAdapter(BookAdapter);
    }

    void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryText) {
                Toast.makeText(ListViewBooksActivity.this, "Searching...", Toast.LENGTH_SHORT).show();
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
        Query query = UtilityBook.getCollectionReferenceForBooks().orderBy("titulo")
                .startAt(queryText).endAt(queryText+"\uf8ff");
        FirestoreRecyclerOptions<Books> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Books>()
                .setQuery(query, Books.class).build();
        BookAdapter = new BookAdapter(firestoreRecyclerOptions, this);
        BookAdapter.startListening();
        bookRecyclerView.setAdapter(BookAdapter);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (value.isEmpty()) {
                    Toast.makeText(ListViewBooksActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        BookAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        BookAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BookAdapter.notifyDataSetChanged();
    }

}