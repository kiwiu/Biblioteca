package com.example.biblioteca.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biblioteca.Models.Books;
import com.example.biblioteca.R;
import com.example.biblioteca.Utilities.UtilityBook;
import com.example.biblioteca.Views.BookActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;


public class BookAdapter extends FirestoreRecyclerAdapter<Books, BookAdapter.BookViewHolder> {
    Context context;

    public BookAdapter(@NonNull FirestoreRecyclerOptions<Books> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull BookViewHolder holder, int position, @NonNull Books model) {

        holder.tituloTextView.setText(model.getTitulo());
        holder.autorTextView.setText(model.getAutor());
        holder.generoTextView.setText(model.getGenero());
        holder.fechaTextView.setText(model.getFecha());

        holder.btnDelete.setOnClickListener(view -> {
            // Aquí puedes implementar la lógica para eliminar un género.
            try {
                // Intenta eliminar el género
                String documentId = getSnapshots().getSnapshot(position).getId();
                DocumentReference documentReference = UtilityBook.getCollectionReferenceForBooks().document(documentId);

                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "libro eliminado con éxito", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "Error al eliminar el libro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Error al eliminar el libro", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, BookActivity.class);
            intent.putExtra("titulo", model.getTitulo()) ;
            intent.putExtra("autor", model.getAutor());
            intent.putExtra("genero", model.getGenero());
            intent.putExtra("fecha", model.getFecha());
            intent.putExtra("disponible", model.isDisponibilidad());
            //intent.putExtra("imagen", model.getImagenURL());

            String bookId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("bookId", bookId);
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option, parent, false);
        return new BookViewHolder(view);
    }

    class BookViewHolder extends RecyclerView.ViewHolder{
        TextView tituloTextView, autorTextView, generoTextView, fechaTextView;

        ImageView btnDelete, imagen;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.note_title_text_view);
            autorTextView = itemView.findViewById(R.id.note_content_text_view);
            generoTextView = itemView.findViewById(R.id.note_category_text_view);
            fechaTextView = itemView.findViewById(R.id.note_timestamp_text_view);
            //imagen = itemView.findViewById(R.id.imagenLibro);
            btnDelete = itemView.findViewById(R.id.delete_btn);

        }
    }
}
