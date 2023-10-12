package com.example.biblioteca.Utilities;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class UtilidadesGenero {
    public static CollectionReference getCollectionReferenceForGeneros() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("Generos").document(currentUser.getUid()).collection("my_generos");
    }
}
