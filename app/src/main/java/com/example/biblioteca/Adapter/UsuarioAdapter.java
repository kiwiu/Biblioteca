package com.example.biblioteca.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biblioteca.Models.Usuario;
import com.example.biblioteca.R;
import com.example.biblioteca.Utilities.UtilityUsuario;
import com.example.biblioteca.Views.UserActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class UsuarioAdapter extends FirestoreRecyclerAdapter<Usuario, UsuarioAdapter.UsuarioViewHolder> {
    Context context;

    public UsuarioAdapter(@NonNull FirestoreRecyclerOptions<Usuario> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position, @NonNull Usuario model) {

        holder.nombreTextView.setText(model.getNombre());
        holder.telefonoTextView.setText(model.getNumeroTelefono());



        holder.btnDelete.setOnClickListener(view -> {
            // Aquí puedes implementar la lógica para eliminar un género.
            try {
                // Intenta eliminar el género
                String documentId = getSnapshots().getSnapshot(position).getId();
                DocumentReference documentReference = UtilityUsuario.getCollectionReferenceForUsers().document(documentId);

                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Usuario eliminado con éxito", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "Error al eliminar el Usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Error al eliminar el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, UserActivity.class);
            intent.putExtra("nombre", model.getNombre()) ;
            intent.putExtra("telefono", model.getNumeroTelefono());
            intent.putExtra("direccion", model.getDireccion());
            intent.putExtra("biblioteca", model.getNumeroBiblioteca());

            String usuarioId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("usuarioId", usuarioId);
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    class UsuarioViewHolder extends RecyclerView.ViewHolder{
        TextView nombreTextView, telefonoTextView, direccionTextView, bibliotecaTextView;
        ImageView btnDelete;
        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.user_title_text_view);
            telefonoTextView = itemView.findViewById(R.id.telefono_content_text_view);
            direccionTextView = itemView.findViewById(R.id.editTextDireccion);
            bibliotecaTextView = itemView.findViewById(R.id.editTextNumeroBiblioteca);

            btnDelete = itemView.findViewById(R.id.delete_btn);
        }
    }
}
