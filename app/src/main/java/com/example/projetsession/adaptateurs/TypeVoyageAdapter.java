package com.example.projetsession.adaptateurs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetsession.R;

import java.util.List;

public class TypeVoyageAdapter extends RecyclerView.Adapter<TypeVoyageAdapter.TypeViewHolder> {

    public interface OnTypeClickListener {
        void onTypeClick(String type);
    }

    private List<String> typeList;
    private String selectedType;
    private OnTypeClickListener listener;

    public TypeVoyageAdapter(List<String> typeList, String selectedType, OnTypeClickListener listener) {
        this.typeList = typeList;
        this.selectedType = selectedType;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_type_voyage, parent, false);
        return new TypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, int position) {
        String type = typeList.get(position);
        holder.bind(type, selectedType, listener);
    }

    @Override
    public int getItemCount() {
        return typeList != null ? typeList.size() : 0;
    }

    // Ajout de la méthode setSelectedType pour mettre à jour la sélection
    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
        notifyDataSetChanged();
    }

    public static class TypeViewHolder extends RecyclerView.ViewHolder {
        TextView typeVoyageText;

        public TypeViewHolder(@NonNull View itemView) {
            super(itemView);
            typeVoyageText = itemView.findViewById(R.id.typeVoyageText);
        }

        public void bind(final String type, final String selectedType, final OnTypeClickListener listener) {
            typeVoyageText.setText(type);
            // Par exemple, si le type est sélectionné, on peut rendre la vue plus visible
            if (type.equals(selectedType)) {
                itemView.setAlpha(1.0f);
            } else {
                itemView.setAlpha(0.5f);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTypeClick(type);
                }
            });
        }
    }
}
