package com.example.biblioteca.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.biblioteca.R;

public class MainActivity extends AppCompatActivity {

    RelativeLayout contentUser, contentBook, contentPrestamo, contentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentUser = findViewById(R.id.contentUser);
        contentBook = findViewById(R.id.contentBook);
        contentPrestamo = findViewById(R.id.contentLoan);
        contentCategory = findViewById(R.id.contentCategory);

        contentUser.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ListViewUserActivity.class)));
        contentBook.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ListViewBooksActivity.class)));
        contentPrestamo.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ListViewPrestamoActivity.class)));
        contentCategory.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ListViewGeneroActivity.class)));
    }


}