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

        contentUser = findViewById(R.id.contentUsuer);
        contentBook = findViewById(R.id.contentBook);
        contentPrestamo = findViewById(R.id.contentPrestamo);
        contentCategory = findViewById(R.id.contentCategory);

        contentUser.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, UserActivity.class)));
        contentBook.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, BookActivity.class)));
        contentPrestamo.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LoanActivity.class)));
        contentCategory.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CategoryActivity.class)));
    }


}