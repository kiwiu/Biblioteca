package com.example.biblioteca.Utilities;

import android.icu.text.SimpleDateFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UtilityUsuario {
    public static CollectionReference getCollectionReferenceForUsers() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid()).collection("My_users");
    }

}
