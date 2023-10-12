package com.example.biblioteca.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.biblioteca.Models.Genero;
import com.example.biblioteca.R;
import com.example.biblioteca.Views.CategoryActivity;
import com.example.biblioteca.Views.ListViewGeneroActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.example.biblioteca.Utilities.UtilidadesGenero;

public class GeneroAdapter extends FirestoreRecyclerAdapter<Genero, GeneroAdapter.GeneroViewHolder> {
    private Context context;

    public GeneroAdapter(@NonNull FirestoreRecyclerOptions<Genero> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull GeneroViewHolder holder, int position, @NonNull Genero genero) {
        holder.nombreTextView.setText(genero.getNombre());
        holder.descripcionTextView.setText(genero.getDescripcion());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, CategoryActivity.class);
            intent.putExtra("nombre", genero.getNombre());
            intent.putExtra("descripcion", genero.getDescripcion());
            String documentId = getSnapshots().getSnapshot(position).getId();
            intent.putExtra("documentId", documentId);
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(view -> {
            // Aquí puedes implementar la lógica para eliminar un género.
            try {
                // Intenta eliminar el género
                String documentId = getSnapshots().getSnapshot(position).getId();
                DocumentReference documentReference = UtilidadesGenero.getCollectionReferenceForGeneros().document(documentId);

                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Género eliminado con éxito", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "Error al eliminar el género", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Error al eliminar el género", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public GeneroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genero, parent, false);
        return new GeneroViewHolder(view);
    }

    class GeneroViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView, descripcionTextView;
        // Agrega cualquier otro elemento de UI que necesites aquí, como botones u otros.
        ImageButton deleteButton;
        public GeneroViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.note_genero_text_view);
            descripcionTextView = itemView.findViewById(R.id.note_descripcion_text_view);
            deleteButton = itemView.findViewById(R.id.delete_genero_btn);
        }
    }
}
