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
import com.example.biblioteca.Models.Prestamo;
import com.example.biblioteca.R;
import com.example.biblioteca.Views.CategoryActivity;
import com.example.biblioteca.Views.LoanActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.example.biblioteca.Utilities.UtilityPrestamo;

public class PrestamoAdapter extends FirestoreRecyclerAdapter<Prestamo, PrestamoAdapter.PrestamoViewHolder> {
    private Context context;

    public PrestamoAdapter(@NonNull FirestoreRecyclerOptions<Prestamo> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull PrestamoViewHolder holder, int position, @NonNull Prestamo prestamo) {
        holder.libroTextView.setText(prestamo.getLibro());
        holder.usuarioTextView.setText(prestamo.getUsuario());
        holder.fechaPrestamoTextView.setText(prestamo.getFechaPrestamo());
        holder.fechaDevolucionTextView.setText(prestamo.getFechaDevolucion());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, LoanActivity.class);
            intent.putExtra("libro", prestamo.getLibro());
            intent.putExtra("usuario", prestamo.getUsuario());
            intent.putExtra("fechaPrestamo", prestamo.getFechaPrestamo());
            intent.putExtra("fechaDevolucion", prestamo.getFechaDevolucion());
            String documentId = getSnapshots().getSnapshot(position).getId();
            intent.putExtra("documentId", documentId);
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(view -> {
            try {
                // Intenta eliminar el género
                String documentId = getSnapshots().getSnapshot(position).getId();
                DocumentReference documentReference = UtilityPrestamo.getCollectionReferenceForPrestamo().document(documentId);

                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Prestamo eliminado con éxito", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "Error al eliminar el prestamo", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Error al eliminar el prestamo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public PrestamoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prestamo, parent, false);
        return new PrestamoViewHolder(view);
    }

    class PrestamoViewHolder extends RecyclerView.ViewHolder {
        TextView libroTextView, usuarioTextView, fechaPrestamoTextView, fechaDevolucionTextView;

        ImageButton deleteButton;
        // Agrega cualquier otro elemento de UI que necesites aquí.

        public PrestamoViewHolder(@NonNull View itemView) {
            super(itemView);
            libroTextView = itemView.findViewById(R.id.titulo_text_view);
            usuarioTextView = itemView.findViewById(R.id.usuario_text_view);
            fechaPrestamoTextView = itemView.findViewById(R.id.prestamo_timestamp_text_view);
            fechaDevolucionTextView = itemView.findViewById(R.id.devolucion_timestamp_text_view);
            deleteButton = itemView.findViewById(R.id.delete_btn);
        }
    }
}
