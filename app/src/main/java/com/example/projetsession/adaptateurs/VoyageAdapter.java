package com.example.projetsession.adaptateurs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projetsession.R;
import com.example.projetsession.modeles.Voyage;
import java.util.List;

public class VoyageAdapter extends RecyclerView.Adapter<VoyageAdapter.VoyageViewHolder> {

    private List<Voyage> listeVoyages;
    private OnItemClickListener listener;

    public VoyageAdapter(List<Voyage> listeVoyages, OnItemClickListener listener) {
        this.listeVoyages = listeVoyages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VoyageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Utiliser ici le layout existant pour l'item de voyage,
        // par exemple R.layout.item_destination ou R.layout.item_voyage_activity,
        // assurez-vous qu'il contient au moins destinationTextView et prixTextView.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_voyage_activity, parent, false);
        return new VoyageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoyageViewHolder holder, int position) {
        Voyage voyage = listeVoyages.get(position);
        holder.bind(voyage, listener);
    }

    @Override
    public int getItemCount() {
        return (listeVoyages != null) ? listeVoyages.size() : 0;
    }

    public void updateList(List<Voyage> newList) {
        this.listeVoyages = newList;
        notifyDataSetChanged();
    }

    public static class VoyageViewHolder extends RecyclerView.ViewHolder {
        TextView destinationTextView;
        TextView prixTextView;

        public VoyageViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationTextView = itemView.findViewById(R.id.destinationTextView);
            prixTextView = itemView.findViewById(R.id.prixTextView);
        }

        public void bind(final Voyage voyage, final OnItemClickListener listener) {
            destinationTextView.setText(voyage.getDestination());
            prixTextView.setText(String.valueOf(voyage.getPrix()) + " $");
            itemView.setOnClickListener(v -> listener.onItemClick(voyage));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Voyage voyage);
    }
}
